package search;

import http.HttpModule;
import http.Requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import search.UrlBuilder.GithubAPI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import com.google.inject.Inject;

import entity.Commit;
import entity.Contributor;
import entity.GroundhogException;
import entity.Issue;
import entity.Project;
import entity.UnPublishedRelease;
import entity.User;

public class MetaSearchGitHub implements ForgeSearch{

	public static int INFINITY = -1;

	private final Gson gson;
	private final Requests requests;
	private final UrlBuilder builder;
	
	
	@Inject
	public MetaSearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
		this.builder = Guice.createInjector(new HttpModule()).getInstance(UrlBuilder.class);
	}
	
	
	@Override
	public List<Project> getProjects(String term, int page, int limit)
			throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> getProjects(String term, String username, int page)
			throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> getAllForgeProjects(int since, int limit)
			throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Issue> getAllProjectIssues(Project gr) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
/**
 * get a contributor's all commits about a certain project
 * @param project
 * @param commiter
 * @return List<Commit>
 */
	public List<Commit> getProjectCommitsByCommiter(Project project, String commiter){
		System.out.println("Searching " +commiter + "'s commits metadata");
				
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/commits?author=" + commiter + "&page=" +page +"&per_page=80")
				  .build();
		
		System.out.println(searchUrl);
		
		try{
			JsonElement jsonElement = gson.fromJson(requests.get(searchUrl), JsonElement.class);
			
			JsonArray jsonArray = jsonElement.getAsJsonArray();

			List<Commit> commits = new ArrayList<>();
			
			while(jsonArray.size()!=0){
			
			for (JsonElement element : jsonArray) {
				Commit commit = gson.fromJson(element, Commit.class);
				commit.setProject(project);
				
				User user = gson.fromJson(element.getAsJsonObject().get("committer"), User.class);
				commit.setCommiter(user);
				
				commit.setMessage(element.getAsJsonObject().get("commit").getAsJsonObject().get("message").getAsString());

				String date = element.getAsJsonObject().get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
				commit.setCommitDate(date);
				
				//System.out.println(commit.getCommitDate());
				
				
				String sha = element.getAsJsonObject().get("sha").getAsString();
				commit.setSha(sha);
				
				String anotherUrl = builder.uses(GithubAPI.ROOT)
						  .withParam("repos")
						  .withSimpleParam("/", project.getOwner().getLogin())
						  .withSimpleParam("/", project.getName())
						  .withParam("/commits")
						  .withParam("/" + sha)
						  .build();
				
				int add = 0;
				int del = 0;
				JsonElement ano_jsonElement = gson.fromJson(requests.get(anotherUrl), JsonElement.class);
				JsonArray ano_jsonArray = ano_jsonElement.getAsJsonObject().get("files").getAsJsonArray();
				for(JsonElement e:ano_jsonArray){
					int adi = e.getAsJsonObject().get("additions").getAsInt();
					int dei = e.getAsJsonObject().get("deletions").getAsInt();
					add += adi;
					del += dei;
				}
				commit.setAdditionsCount(add);
				commit.setDeletionsCount(del);
							
				commits.add(commit);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/commits?author=" + commiter + "&page=" +page +"&per_page=80")
					  .build();
			
			System.out.println(searchUrl);
			
			jsonElement = gson.fromJson(requests.get(searchUrl), JsonElement.class);
			jsonArray = jsonElement.getAsJsonArray();
			}

			return commits;
			
			
		}catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
		
		
		
		
	}
	
	/**
	 * try to find a certain project 
	 * if not return null
	 * @param projectName
	 * @param owner
	 * @return Project or null
	 */
	public Project getProject(String projectName,String ownerName){
		String searchUrl = builder.uses(GithubAPI.SEARCH)
				  .withParam(projectName + "+user:" + ownerName)
				  .withParam("sort", "stars")
				  .withParam("order", "desc")
				  .build();
		
		System.out.println("trying to get project...");
		try{
			String json = requests.get(searchUrl);
			
			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
			JsonArray jsonArray= jsonObject.get("items").getAsJsonArray();

			Project p = null;
			
			for (JsonElement element : jsonArray) {

				p = gson.fromJson(element, Project.class);

				String owner = element.getAsJsonObject().get("owner").getAsJsonObject().get("login").getAsString();
				p.setCheckoutURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));

				String userUrl = builder.uses(GithubAPI.USERS).withParam(owner).build();
				String userJson = requests.get(userUrl);
				User user = gson.fromJson(userJson, User.class);

				p.setOwner(user);
				
			}
			
			return p;
		}catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
		
		
	}
	
	/**
	 * 
	 * <p>Fetches all the contributors of the given {@link Project} from the GitHub API</p>
	 * <p>modified by guanjun</p>
	 * @see <p>We can map all {@link Contributor} into a {@link User} by {@link Contributor#getUrl} method that return is the same for a Contributor and his correspondent User ({@link User#getUrl})</p>
	 * @param project the @{link Project} to get the contributors from
	 * @return a {@link List} of {@link Contributor} objects
	 */
	public List<Contributor> getAllProjectContributors(Project project) {
		
		System.out.println("Searching project contributors metadata");
		
		List<Contributor> contributors = new ArrayList<>();
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/contributors")
				  .withParam("?page=" + page + "&per_page=80")
				  .build();
		
		//System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
        JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
        
        if(jsonArray.size() == 0){
			return null;
		}
        
        
        while(jsonArray.size()!=0){
        	
        	for (JsonElement element: jsonArray) {
            	Contributor contributor = gson.fromJson(element, Contributor.class);
            	contributors.add(contributor);
            }
        	
        	page++;
        	
        	searchUrl = builder.uses(GithubAPI.ROOT)
  				  .withParam("repos")
  				  .withSimpleParam("/", project.getUser().getLogin())
  				  .withSimpleParam("/", project.getName())
  				  .withParam("/contributors")
  				  .withParam("?page=" + page + "&per_page=80")
  				  .build();
        	
        	jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

        }
        
		return contributors;
	}
	
	public List<UnPublishedRelease> getAllUnPublishedRelease(String owner,String projectName){
		
		System.out.println("Searching UnPublishedRelease metadata...");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withParam("/releases")
				  .build();
		
		
		return null;
	}
	
	
}
