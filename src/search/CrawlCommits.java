package search;

import http.HttpModule;
import http.Requests;

import java.util.ArrayList;
import java.util.List;

import search.UrlBuilder.GithubAPI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.inject.Guice;
import com.google.inject.Inject;

import entity.Commit;
import entity.GroundhogException;
import entity.Project;
import entity.User;

public class CrawlCommits implements Runnable{

	public static int INFINITY = -1;

	private final Gson gson;
	private final Requests requests;
	private final UrlBuilder builder;
	
	
	@Inject
	public CrawlCommits(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
		this.builder = Guice.createInjector(new HttpModule()).getInstance(UrlBuilder.class);
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


		@Override
		public void run() {
			
			
			
			
		}
}
