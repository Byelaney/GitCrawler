package search;

import http.HttpModule;
import http.Requests;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import search.UrlBuilder.GithubAPI;
import util.Dates;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import com.google.inject.Inject;

import entity.Comment;
import entity.Commit;
import entity.CommitFile;
import entity.Contributor;
import entity.Crawlindex;
import entity.GroundhogException;
import entity.Issue;
import entity.IssueLabel;
import entity.Project;
import entity.Release;
import entity.UnPublishedRelease;
import entity.User;

public class MetaSearchGitHub implements ForgeSearch{

	final static Logger logger = LoggerFactory.getLogger(MetaSearchGitHub.class);
	public static int INFINITY = -1;

	private final Gson gson;
	private final Requests requests;
	private final UrlBuilder builder;
	
	private Crawlindex crawlIndex;
		

	@Inject
	public MetaSearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
		this.builder = Guice.createInjector(new HttpModule()).getInstance(UrlBuilder.class);
		crawlIndex = new Crawlindex();
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

	/**
	 * Fetches all the Issues of the given {@link Project} from the GitHub API
	 * 
	 * @param project the @{link Project} of which the Issues are about
	 * @return a {@link List} of {@link Issues} objects
	 */
	
	public List<Issue> getAllProjectIssues(Project project) {

		logger.info(("Searching project all issues metadata"));
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/issues")
				  .withParam("?page=" +page +"&per_page=80")
				  .build();
				
		String  jsonString = getWithProtection(searchUrl);
				
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		List<Issue> issues = new ArrayList<Issue>();
		List<IssueLabel> labels = new ArrayList<IssueLabel>();
				
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				Issue issue = gson.fromJson(element, Issue.class);
				issue.setProject(project);
				
				for (JsonElement lab : element.getAsJsonObject().get("labels").getAsJsonArray()) {
					IssueLabel label = gson.fromJson(lab, IssueLabel.class);				
					labels.add(label);
				}
				
				issue.setLabels(labels);				
				issues.add(issue);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/issues")
					  .withParam("?page=" +page +"&per_page=80")
					  .build();
			
			jsonString = getWithProtection(searchUrl);
			jsonElement = gson.fromJson(jsonString, JsonElement.class);
			
			jsonArray = jsonElement.getAsJsonArray();
		}
		
		if(page == 1){
			this.crawlIndex.setIssue_page(page);
		}else
			this.crawlIndex.setIssue_page(page-1);
		
		return issues;
	}

	public List<Issue> getAllClosedProjectIssues(Project project){
		logger.info(("Searching project closed issues metadata"));		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/issues")
				  .withParam("?state=closed")
				  .withParam("&page=" +page +"&per_page=80")
				  .build();
				
		String  jsonString = getWithProtection(searchUrl);
				
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		List<Issue> issues = new ArrayList<Issue>();
		List<IssueLabel> labels = new ArrayList<IssueLabel>();
				
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				Issue issue = gson.fromJson(element, Issue.class);
				issue.setProject(project);
				
				for (JsonElement lab : element.getAsJsonObject().get("labels").getAsJsonArray()) {
					IssueLabel label = gson.fromJson(lab, IssueLabel.class);				
					labels.add(label);
				}
				
				issue.setLabels(labels);				
				issues.add(issue);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/issues")
					  .withParam("?state=closed")
					  .withParam("&page=" +page +"&per_page=80")
					  .build();
			
			jsonString = getWithProtection(searchUrl);
			jsonElement = gson.fromJson(jsonString, JsonElement.class);
			
			jsonArray = jsonElement.getAsJsonArray();
		}
		
		if(page == 1){
			this.crawlIndex.setClosed_issue_page(page);
		}else
			this.crawlIndex.setClosed_issue_page(page-1);
		
		return issues;
	}

	public List<Issue> getAllClosedProjectIssues(Project project,int page){
		logger.info(("Searching project closed issues metadata"));		
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/issues")
				  .withParam("?state=closed")
				  .withParam("&page=" +page +"&per_page=80")
				  .build();
				
		String  jsonString = getWithProtection(searchUrl);
				
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		List<Issue> issues = new ArrayList<Issue>();
		List<IssueLabel> labels = new ArrayList<IssueLabel>();
				
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				Issue issue = gson.fromJson(element, Issue.class);
				issue.setProject(project);
				
				for (JsonElement lab : element.getAsJsonObject().get("labels").getAsJsonArray()) {
					IssueLabel label = gson.fromJson(lab, IssueLabel.class);				
					labels.add(label);
				}
				
				issue.setLabels(labels);				
				issues.add(issue);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/issues")
					  .withParam("?state=closed")
					  .withParam("&page=" +page +"&per_page=80")
					  .build();
			
			jsonString = getWithProtection(searchUrl);
			jsonElement = gson.fromJson(jsonString, JsonElement.class);
			
			jsonArray = jsonElement.getAsJsonArray();
		}
		
		if(page == 1){
			this.crawlIndex.setClosed_issue_page(page);
		}else
			this.crawlIndex.setClosed_issue_page(page-1);
				
		return issues;
	}
	
	public List<Issue> getAllProjectIssues(Project project,int page){
		logger.info(("Searching project all issues metadata"));
		
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/issues")
				  .withParam("?page=" +page +"&per_page=80")
				  .build();
				
		
		String  jsonString = getWithProtection(searchUrl);
		
		//System.out.println(jsonString);
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		
		List<Issue> issues = new ArrayList<Issue>();
		List<IssueLabel> labels = new ArrayList<IssueLabel>();
		
		
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				Issue issue = gson.fromJson(element, Issue.class);
				issue.setProject(project);
				
				for (JsonElement lab : element.getAsJsonObject().get("labels").getAsJsonArray()) {
					IssueLabel label = gson.fromJson(lab, IssueLabel.class);				
					labels.add(label);
				}
				
				issue.setLabels(labels);
			
				issues.add(issue);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/issues")
					  .withParam("?page=" +page +"&per_page=80")
					  .build();
			
			jsonString = getWithProtection(searchUrl);
			jsonElement = gson.fromJson(jsonString, JsonElement.class);
			jsonArray = jsonElement.getAsJsonArray();
		}
		if(page == 1)
			this.crawlIndex.setIssue_page(page);
		else
			this.crawlIndex.setIssue_page(page-1);
		
		return issues;
	}
/**
 * get a contributor's all commits about a certain project
 * @param project
 * @param commiter
 * @return List<Commit>
 */
	public List<Commit> getProjectCommitsByCommiter(Project project, String commiter){
		//System.out.println("Searching " +commiter + "'s commits metadata");
				
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/commits?author=" + commiter + "&page=" +page +"&per_page=80")
				  .build();
		
		
		
		try{
			String jsonLegacy = getWithProtection(searchUrl);
			
			//could be requests.get(searchUrl)
			JsonElement jsonElement = gson.fromJson(jsonLegacy, JsonElement.class);
			
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
			
			
			
			jsonElement = gson.fromJson(requests.get(searchUrl), JsonElement.class);
			jsonArray = jsonElement.getAsJsonArray();
			}

			return commits;
			
			
		}catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
		
		
		
		
	}
	
	public List<Commit> getProjectCommits(Project project,int page){
		logger.info(("Searching " + "updated commits metadata"));
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/commits?"  + "&page=" +page +"&per_page=80")
				  .build();
		
		
		try{
			String jsonLegacy = getWithProtection(searchUrl);
			
			//could be requests.get(searchUrl)
			JsonElement jsonElement = gson.fromJson(jsonLegacy, JsonElement.class);
			
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
					  .withParam("/commits?"  + "&page=" +page +"&per_page=80")
					  .build();
			
			
			jsonElement = gson.fromJson(requests.get(searchUrl), JsonElement.class);
			jsonArray = jsonElement.getAsJsonArray();
			}

			if(page == 1)
				this.crawlIndex.setCommit_page(page);
			else
				this.crawlIndex.setCommit_page(page-1);
			
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
		
		logger.info(("trying to get " + ownerName +"'s " + projectName + " project"));
		//System.out.println(searchUrl);
		try{
			String json = getWithProtection(searchUrl);
			
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
		
		logger.info(("Searching project all contributors metadata"));
		
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
        if(page == 1)
        	this.crawlIndex.setContributor_page(page);
        else
        	this.crawlIndex.setContributor_page(page-1);
        return contributors;
	}
	
	public List<Contributor> getAllProjectContributors(Project project,int page){
		logger.info(("Searching project all contributors metadata"));
		
		List<Contributor> contributors = new ArrayList<>();
				
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
        if(page == 1)
        	this.crawlIndex.setContributor_page(page);
        else
        	this.crawlIndex.setContributor_page(page-1);
        return contributors;
	}
	
	
	/**
	 * get all UnPublished Releases 
	 * @param owner
	 * @param projectName
	 * @return List<UnPublishedRelease> or null
	 */
	public List<UnPublishedRelease> getAllUnPublishedRelease(String owner,String projectName){
		
		logger.info(("getting " + projectName + " project UnPublishedRelease metadata..."));
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withParam("/tags")
				  .withParam("?page=" + page + "&per_page=80")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
        JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
        
        if(jsonArray.size() == 0){
			return null;
		}
        
        List<UnPublishedRelease> results = null;
        
        while(jsonArray.size()!=0){
        	results = new ArrayList<UnPublishedRelease>();
        	for (JsonElement element: jsonArray) {
        		UnPublishedRelease upbRelease = gson.fromJson(element, UnPublishedRelease.class);
        		String sha = (element.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString());
        		
        		String commit_url = builder.uses(GithubAPI.ROOT)
      				  .withParam("repos")
    				  .withSimpleParam("/", owner)
    				  .withSimpleParam("/", projectName)
    				  .withParam("/commits")
    				  .withParam("/" + sha)
    				  .build();
        		
        		String date = getCommitDate(commit_url);
        		upbRelease.setCommit_url(commit_url);
        		upbRelease.setDate(date);
        		results.add(upbRelease);
        	}
        	
        	page++;
        	searchUrl = builder.uses(GithubAPI.ROOT)
  				  .withParam("repos")
  				  .withSimpleParam("/", owner)
  				  .withSimpleParam("/", projectName)
  				  .withParam("/tags")
  				  .withParam("?page=" + page + "&per_page=80")
  				  .build();
        	
        	jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

        }
		if(page == 1)
			this.crawlIndex.setUpbrelease_page(page);
		else
			this.crawlIndex.setUpbrelease_page(page-1);
		return results;
	}
	
	public List<UnPublishedRelease> getAllUnPublishedRelease(String owner,String projectName,int page){
		logger.info(("getting " + projectName + " project UnPublishedRelease metadata..."));
				
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withParam("/tags")
				  .withParam("?page=" + page + "&per_page=80")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
        JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
        
        if(jsonArray.size() == 0){
			return null;
		}
        
        List<UnPublishedRelease> results = null;
        
        while(jsonArray.size()!=0){
        	results = new ArrayList<UnPublishedRelease>();
        	for (JsonElement element: jsonArray) {
        		UnPublishedRelease upbRelease = gson.fromJson(element, UnPublishedRelease.class);
        		String sha = (element.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString());
        		
        		String commit_url = builder.uses(GithubAPI.ROOT)
      				  .withParam("repos")
    				  .withSimpleParam("/", owner)
    				  .withSimpleParam("/", projectName)
    				  .withParam("/commits")
    				  .withParam("/" + sha)
    				  .build();
        		
        		String date = getCommitDate(commit_url);
        		upbRelease.setCommit_url(commit_url);
        		upbRelease.setDate(date);
        		results.add(upbRelease);
        	}
        	
        	page++;
        	searchUrl = builder.uses(GithubAPI.ROOT)
  				  .withParam("repos")
  				  .withSimpleParam("/", owner)
  				  .withSimpleParam("/", projectName)
  				  .withParam("/tags")
  				  .withParam("?page=" + page + "&per_page=80")
  				  .build();
        	
        	jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

        }
		if(page == 1)
			this.crawlIndex.setUpbrelease_page(page);
		else
			this.crawlIndex.setUpbrelease_page(page-1);
		return results;
	}
	
	/**
	 * try to get a commit date using URL
	 * @param url
	 * @return String date
	 */
	private String getCommitDate(String url){
		//System.out.println("Getting date for a certain commit...");
		
		String searchUrl = url;
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonObject jsonobject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		
		String date = jsonobject.get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
		date = Dates.dateFormat(date);
		return date;
	}
	
	
	/**
	 * try to get all files of a certain commit
	 * @param owner
	 * @param projectName
	 * @param sha
	 * @return List<CommitFile>
	 */
	public List<CommitFile> getCommitFiles(String owner,String projectName,String sha){
		try{
			//System.out.println("Getting commit files for a certain commit...");
			
			String searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", owner)
					  .withSimpleParam("/", projectName)
					  .withSimpleParam("/", "commits")
					  .withSimpleParam("/",sha)
					  .build();
			
			String jsonLegacy = getWithProtection(searchUrl);
			JsonObject jsonobject = new JsonParser().parse(jsonLegacy).getAsJsonObject();
			JsonArray jsonArray = jsonobject.get("files").getAsJsonArray();
			
			
			
			
			if(jsonArray.size()==0){
				return null;
			}
			
			List<CommitFile> results = new ArrayList<CommitFile>();
			
			for (JsonElement element : jsonArray) {
				CommitFile cmf = new CommitFile();
				
				/**
				 * sometimes the sha is null
				 */
				JsonElement tmpElement= element.getAsJsonObject().get("sha");
				String file_sha = "";
				if(!tmpElement.isJsonNull())
				file_sha = element.getAsJsonObject().get("sha").getAsString();
				
				String filename = element.getAsJsonObject().get("filename").getAsString();
				String status = element.getAsJsonObject().get("status").getAsString();
				int additions = element.getAsJsonObject().get("additions").getAsInt();
				int deletions = element.getAsJsonObject().get("deletions").getAsInt();
				int changes = element.getAsJsonObject().get("changes").getAsInt();
				String contents_url = element.getAsJsonObject().get("contents_url").getAsString();
				
				cmf.setSha(file_sha);
				cmf.setFilename(filename);
				cmf.setStatus(status);
				cmf.setAdditions(additions);
				cmf.setDeletions(deletions);
				cmf.setChanges(changes);
				cmf.setContents_url(contents_url);
				cmf.setCommit_sha(sha);
				
				results.add(cmf);
			}
			
			return results;
			
			
		}catch (Throwable e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
		
	}
	
	public entity.User getUser(String login){
		//System.out.println("Searching " + login + "user's specific metadata");
		
		String searchUrl = builder.uses(GithubAPI.USERS)
				  .withParam(login)
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		entity.User user = gson.fromJson(jsonString, entity.User.class);
		user.setCreated_at(Dates.dateFormat(user.getCreated_at()));
		user.setUpdatedAt(Dates.dateFormat(user.getUpdatedAt()));
		return user;
	}
	
	public List<entity.Comment> getIssueComments(String projectName,String owner,entity.Issue issue){
		logger.info(("Searching " + projectName + " project's comment metadata"));
		
		List<entity.Comment> comments = new ArrayList<entity.Comment>();
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withParam("/issues/" + issue.getNumber())
				  .withParam("/comments")
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
        		Comment comment = new Comment();
        		
        		comment.setId(element.getAsJsonObject().get("id").getAsInt());
        		comment.setUrl(element.getAsJsonObject().get("url").getAsString());
        		
        		comment.setUser(element.getAsJsonObject().get("user").getAsJsonObject().get("login").getAsString()
        		);
        		comment.setUser_id(element.getAsJsonObject().get("user").getAsJsonObject().get("id").getAsInt()
                );
        		
        		if(element.getAsJsonObject().get("position").isJsonNull()){
        			comment.setPosition(0);
        		}
        		else{
        			comment.setPosition(element.getAsJsonObject().get("position").getAsInt());
        		}
        		
        		if(element.getAsJsonObject().get("line").isJsonNull()){
        			comment.setLine(0);
        		}
        		else{
        			comment.setLine(element.getAsJsonObject().get("line").getAsInt());
        		}
        		
        		if(element.getAsJsonObject().get("path").isJsonNull()){
        			comment.setPath("");
        		}
        		else{
        			comment.setPath(element.getAsJsonObject().get("path").getAsString());
        		}
     
        		comment.setCommit_id(element.getAsJsonObject().get("commit_id").getAsString());
        		comment.setCreated_at(Dates.dateFormat(element.getAsJsonObject().get("created_at").getAsString()));
        		comment.setUpdated_at(Dates.dateFormat(element.getAsJsonObject().get("updated_at").getAsString()));
        		comment.setBody(element.getAsJsonObject().get("body").getAsString());
        		
            	comments.add(comment);
            }
        	
        	page++;
        	
        	searchUrl = builder.uses(GithubAPI.ROOT)
  				  .withParam("repos")
  				  .withSimpleParam("/", owner)
  				  .withSimpleParam("/", projectName)
  				  .withParam("/issues/" + issue.getNumber())
  				  .withParam("/comments")
  				  .withParam("?page=" + page + "&per_page=80")
  				  .build();
        	
        	jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

        }
        if(page == 1)
        	this.crawlIndex.setComment_page(page);
        else
        	this.crawlIndex.setComment_page(page-1);
        return comments;
			
	}
	
	public List<entity.Comment> getComments(String projectName,String owner){
		logger.info(("Searching " + projectName + " project's comment metadata"));
		
		List<entity.Comment> comments = new ArrayList<entity.Comment>();
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withParam("/comments")
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
        		Comment comment = new Comment();
        		
        		comment.setId(element.getAsJsonObject().get("id").getAsInt());
        		comment.setUrl(element.getAsJsonObject().get("url").getAsString());
        		
        		comment.setUser(element.getAsJsonObject().get("user").getAsJsonObject().get("login").getAsString()
        		);
        		comment.setUser_id(element.getAsJsonObject().get("user").getAsJsonObject().get("id").getAsInt()
                );
        		
        		if(element.getAsJsonObject().get("position").isJsonNull()){
        			comment.setPosition(0);
        		}
        		else{
        			comment.setPosition(element.getAsJsonObject().get("position").getAsInt());
        		}
        		
        		if(element.getAsJsonObject().get("line").isJsonNull()){
        			comment.setLine(0);
        		}
        		else{
        			comment.setLine(element.getAsJsonObject().get("line").getAsInt());
        		}
        		
        		if(element.getAsJsonObject().get("path").isJsonNull()){
        			comment.setPath("");
        		}
        		else{
        			comment.setPath(element.getAsJsonObject().get("path").getAsString());
        		}
     
        		comment.setCommit_id(element.getAsJsonObject().get("commit_id").getAsString());
        		comment.setCreated_at(Dates.dateFormat(element.getAsJsonObject().get("created_at").getAsString()));
        		comment.setUpdated_at(Dates.dateFormat(element.getAsJsonObject().get("updated_at").getAsString()));
        		comment.setBody(element.getAsJsonObject().get("body").getAsString());
        		
            	comments.add(comment);
            }
        	
        	page++;
        	
        	searchUrl = builder.uses(GithubAPI.ROOT)
  				  .withParam("repos")
  				  .withSimpleParam("/", owner)
  				  .withSimpleParam("/", projectName)
  				  .withParam("/comments")
  				  .withParam("?page=" + page + "&per_page=80")
  				  .build();
        	
        	jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

        }
        if(page == 1)
        	this.crawlIndex.setComment_page(page);
        else
        	this.crawlIndex.setComment_page(page-1);
        return comments;
		
	}
	
	public List<entity.Comment> getComments(String projectName,String owner,int page){
		logger.info(("Searching " + projectName + " project's comment metadata"));
		
		List<entity.Comment> comments = new ArrayList<entity.Comment>();
				
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withParam("/comments")
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
        		Comment comment = new Comment();
        		
        		comment.setId(element.getAsJsonObject().get("id").getAsInt());
        		comment.setUrl(element.getAsJsonObject().get("url").getAsString());
        		
        		comment.setUser(element.getAsJsonObject().get("user").getAsJsonObject().get("login").getAsString()
        		);
        		comment.setUser_id(element.getAsJsonObject().get("user").getAsJsonObject().get("id").getAsInt()
                );
        		
        		if(element.getAsJsonObject().get("position").isJsonNull()){
        			comment.setPosition(0);
        		}
        		else{
        			comment.setPosition(element.getAsJsonObject().get("position").getAsInt());
        		}
        		
        		if(element.getAsJsonObject().get("line").isJsonNull()){
        			comment.setLine(0);
        		}
        		else{
        			comment.setLine(element.getAsJsonObject().get("line").getAsInt());
        		}
        		
        		if(element.getAsJsonObject().get("path").isJsonNull()){
        			comment.setPath("");
        		}
        		else{
        			comment.setPath(element.getAsJsonObject().get("path").getAsString());
        		}
     
        		comment.setCommit_id(element.getAsJsonObject().get("commit_id").getAsString());
        		comment.setCreated_at(Dates.dateFormat(element.getAsJsonObject().get("created_at").getAsString()));
        		comment.setUpdated_at(Dates.dateFormat(element.getAsJsonObject().get("updated_at").getAsString()));
        		comment.setBody(element.getAsJsonObject().get("body").getAsString());
        		
            	comments.add(comment);
            }
        	
        	page++;
        	
        	searchUrl = builder.uses(GithubAPI.ROOT)
  				  .withParam("repos")
  				  .withSimpleParam("/", owner)
  				  .withSimpleParam("/", projectName)
  				  .withParam("/comments")
  				  .withParam("?page=" + page + "&per_page=80")
  				  .build();
        	
        	jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

        }
        if(page == 1)
        	this.crawlIndex.setComment_page(page);
        else
        	this.crawlIndex.setComment_page(page-1);
		return comments;
		
		
	}
	
	/**
	 * Fetches all the Releases of the given {@link Project} from the GitHub API
	 * 
	 * @param project the @{link Project} of which the Releases are about
	 * @return a {@link List} of {@link Release} objects
	 */
	public List<Release> getAllProjectReleases(Project project) {

		logger.info(("Searching " + project.getName() +  " project releases metadata"));
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/releases")
				  .withParam("?page=" + page + "&per_page=80")
				  .build();
		
		//System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		if(jsonArray.size() == 0){
			return null;
		}
		
		List<Release> releases = new ArrayList<>();
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				Release release = gson.fromJson(element, Release.class);
				release.setProject(project);	
				releases.add(release);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getUser().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/releases")
					  .withParam("?page=" + page + "&per_page=80")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		}
		if(page == 1)
			this.crawlIndex.setRelease_page(page);
		else
			this.crawlIndex.setRelease_page(page-1);
		return releases;
	
	}
	
	/**
	 * with the page param
	 * @param project
	 * @param page
	 * @return
	 */
	public List<Release> getAllProjectReleases(Project project,int page) {
		logger.info(("Searching " + project.getName() +  " project releases metadata"));
				
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/releases")
				  .withParam("?page=" + page + "&per_page=80")
				  .build();
		
		//System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		if(jsonArray.size() == 0){
			return null;
		}
		
		List<Release> releases = new ArrayList<>();
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				Release release = gson.fromJson(element, Release.class);
				release.setProject(project);	
				releases.add(release);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getUser().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withParam("/releases")
					  .withParam("?page=" + page + "&per_page=80")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
            jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		}
		if(page == 1)
			this.crawlIndex.setRelease_page(page);
		else
			this.crawlIndex.setRelease_page(page-1);
		return releases;
	}
	
	
	private String getWithProtection(String url){
		String data = requests.get(url);

		if (data.contains("API rate limit exceeded for")) {
			try {
				Thread.sleep(1000 * 60 * 60);
				data = requests.get(url);

			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

		return data;
	}
	
	public static int getINFINITY() {
		return INFINITY;
	}


	public static void setINFINITY(int iNFINITY) {
		INFINITY = iNFINITY;
	}


	public Crawlindex getCrawlIndex() {
		return crawlIndex;
	}


	public void setCrawlIndex(Crawlindex crawlIndex) {
		this.crawlIndex = crawlIndex;
	}


	public Gson getGson() {
		return gson;
	}


	public Requests getRequests() {
		return requests;
	}


	public UrlBuilder getBuilder() {
		return builder;
	}
	
}
