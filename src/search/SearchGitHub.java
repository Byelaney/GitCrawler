package search;


import http.HttpModule;
import http.Requests;
import http.URLsDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import search.UrlBuilder.GithubAPI;
import usefuldata.Developer;
import usefuldata.ProjectContribution;
import usefuldata.Vitality;
import util.Dates;

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
import entity.IssueLabel;
import entity.Language;
import entity.Milestone;
import entity.Project;
import entity.Release;
import entity.User;

/**
 * Performs the project search on GitHub, via its official JSON API
 * 
 * 
 */
public class SearchGitHub implements ForgeSearch {
	
	public static int INFINITY = -1;

	private final Gson gson;
	private final Requests requests;
	private final UrlBuilder builder;

	@Inject
	public SearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
		this.builder = Guice.createInjector(new HttpModule()).getInstance(UrlBuilder.class);
	}

	public List<Project> getProjects(String term, int page, int limit) throws SearchException {
		System.out.println("Searching project metadata");
		try {
			if (term == null) {
				return getAllForgeProjects(page, limit);
			}

			String searchUrl = builder.uses(GithubAPI.LEGACY_V2)
					  .withParam(URLsDecoder.encodeURL(term)
)
					  .withParam("start_page", page)
					  .withParam("language", "java")
					  .build();

			//System.out.println(searchUrl);
			
			String json = requests.get(searchUrl);
			JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();			
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();

			List<Project> projects = new ArrayList<>();
			for (int i = 0; i < jsonArray.size() && (i < limit || limit < 0); i++) {
				String element = jsonArray.get(i).toString();

				Project p = gson.fromJson(element, Project.class);

				String owner = jsonArray.get(i).getAsJsonObject().get("owner").getAsString();
				p.setCheckoutURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));

				String userUrl = builder.uses(GithubAPI.USERS).withParam(owner).build();
				String userJson = requests.get(userUrl);
				User user = gson.fromJson(userJson, User.class);

				p.setOwner(user);
				projects.add(p);
			}

			return projects;

		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	
	
	/**
	 * Obtains from the GitHub API the set of projects with more than one language
	 * 
	 * @param Start indicates the desired page
	 * @param limit the total of projects that will be returned
	 * @throws SearchException
	 */
	
	public List<Project> getProjectsWithMoreThanOneLanguage(int limit) throws SearchException {
		
		try {
			
			System.out.println("Searching project with more than one language metadata");
			
			List<Project> rawData = getAllProjects(0, limit);

			List<Project> projects = new ArrayList<>();
			for (Project project : rawData) {
				List<Language> languages = getProjectLanguages(project);

				if (languages.size() > 1) {
					projects.add(project);
				}
			}

			return projects;

		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}
	
	
	
	/**
	 * Obtains from the GitHub API the set of projects within a specific
	 * language
	 * 
	 * @param lang the specific language
	 */
	
	public List<Project> getAllProjectsByLanguage(String lang) throws SearchException {

		
		System.out.println("Searching all project by language metadata");
		
		String searchUrl = builder.uses(GithubAPI.LEGACY_V2).withSimpleParam("language=", lang).build();
		String json = requests.get(searchUrl);

		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		JsonArray jsonArray= jsonObject.get("repositories").getAsJsonArray();

		List<Project> projects = new ArrayList<>();
		for (JsonElement element : jsonArray) {
			Project p = gson.fromJson(element, Project.class);
			String owner = element.getAsJsonObject().get("owner").getAsString();
			p.setUser(new User(owner));
			projects.add(p);
		}

		return projects;
	}
	
	
	/**
	 * Obtains from the GitHub API the set of projects
	 * 
	 * @param Start indicates the desired page
	 * @param limit is the total of projects that are going to me returned 
	 * @throws SearchException
	 */
	public List<Project> getAllProjects(int start, int limit) throws SearchException {

		try {

			int since = start;
			int totalRepositories = 0;

			List<Project> projects = new ArrayList<>();
			JsonParser parser = new JsonParser();
			while(totalRepositories < limit || limit < 0){

				String searchUrl = builder.uses(GithubAPI.REPOSITORIES)
										  .withParam("language", "java")
										  .withParam("since", since)
										  .build();
				
				String response = getWithProtection(searchUrl);
				JsonArray jsonArray = parser.parse(response).getAsJsonArray();

				int counter = 0;

				for (Iterator<JsonElement> iterator = jsonArray.iterator(); (iterator
						.hasNext() && (totalRepositories + counter < limit || limit < 0));) {

					JsonElement element = (JsonElement) iterator.next();

					String repoName = element.getAsJsonObject().get("name").getAsString();	
					String searchUrlLegacy = builder.uses(GithubAPI.LEGACY_V2).withParam(repoName).build();
					
					String jsonLegacy = getWithProtection(searchUrlLegacy);
					JsonElement jsonElement = parser.parse(jsonLegacy);
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					JsonArray jsonArrayLegacy = jsonObject.get("repositories").getAsJsonArray();

					if(jsonArrayLegacy.size() > 0) {
						JsonObject rawJsonObject = jsonArrayLegacy.get(0).getAsJsonObject();

						String stringElement = rawJsonObject.toString();
						Project p = gson.fromJson(stringElement, Project.class);

						String owner = rawJsonObject.getAsJsonObject().get("owner").getAsString();
						p.setCheckoutURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));
						p.setOwner(new User(owner));

						projects.add(p);

						counter++;
						totalRepositories++;
					}
				}

				JsonElement lastPagesRepository = jsonArray.get(jsonArray.size() -1);
				since = lastPagesRepository.getAsJsonObject().get("id").getAsInt();
			}
			return projects;

		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	/**
	 * get a certain project
	 * @param projectName
	 * @param username
	 * @return
	 * @throws SearchException
	 */
	public usefuldata.Project getProjects(String projectName, String username) throws SearchException{
		try {
			System.out.println("Searching project metadata by term");
			
			
			String searchUrl = builder.uses(GithubAPI.SEARCH)
					  .withParam(projectName + "+user:" + username)
					  .withParam("sort", "stars")
					  .withParam("order", "desc")
					  .build();

			//System.out.println(searchUrl);
			
			String json = requests.get(searchUrl);
			
			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
			JsonArray jsonArray= jsonObject.get("items").getAsJsonArray();

			usefuldata.Project p = null;
			for (JsonElement element : jsonArray) {
				usefuldata.Project tmp = gson.fromJson(element, usefuldata.Project.class);
				if(tmp!=null){
					tmp.setOwner(element.getAsJsonObject().get("owner").getAsJsonObject().get("login").getAsString());
					
					if(tmp.getName()!=null && tmp.getOwner()!=null){
						p = tmp;
						break;
					}
				}
			}
			return p;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}
	
	
	
	@Override
	public List<Project> getProjects(String term, String username, int page) throws SearchException {
		try {
			
			System.out.println("Searching project metadata by term");
			
			List<Project> projects = new ArrayList<>();
			
			String searchUrl = builder.uses(GithubAPI.LEGACY_V2)
					  .withParam(URLsDecoder.encodeURL(term))
					  .withParam("start_page", page)
					  .withParam("language", "java")
					  .build();

			//System.out.println(searchUrl);
			
			String jsonLegacy = getWithProtection(searchUrl);
			JsonElement jsonElement = new JsonParser().parse(jsonLegacy);
			
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();
			
			for (JsonElement j: jsonArray) {
				Project p = gson.fromJson(j, Project.class);
				JsonObject jsonObj = j.getAsJsonObject();	
				
				p.setCheckoutURL(String.format("git@github.com:%s/%s.git", username, p.getName()));
				User u = new User(jsonObj.get("owner").getAsString());
				p.setUser(u);
				
				projects.add(p);
			}

			return projects;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}
	

	/**
	 * Obtains from the GitHub API the set of languages that compose a
	 * {@link Project}
	 * 
	 * @param project
	 *            a {@link Project} object to have its languages fetched
	 */
	
	public List<Language> getProjectLanguages(Project project) {

	  String searchUrl = builder.uses(GithubAPI.ROOT)
			  .withParam("repos")
			  .withSimpleParam("/", project.getUser().getLogin())
			  .withSimpleParam("/", project.getName())
			  .withParam("/languages")
			  .build();

		String json = requests.get(searchUrl).replace("{", "").replace("}", "");

		List<Language> languages = new ArrayList<>();
		if(!json.equalsIgnoreCase("{}")){
			for (String str: json.split(",")) {
				String[] hash = str.split(":");
				String key = hash[0].trim().replaceAll("\"", "");
				Integer value = Integer.parseInt(hash[1].trim());
				Language lang = new Language(key, value);
				languages.add(lang);
			}
		}

		return languages;
	}
	

	/**
	 * Fetches all the Issues of the given {@link Project} from the GitHub API
	 * 
	 * @param project the @{link Project} of which the Issues are about
	 * @return a {@link List} of {@link Issues} objects
	 */
	
	public List<Issue> getAllProjectIssues(Project project) {

		System.out.println("Searching project issues metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner())
				  .withSimpleParam("/", project.getName())
				  .withParam("/issues")
				  .build();
				
		String jsonString = requests.get(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		List<Issue> issues = new ArrayList<Issue>();
		List<IssueLabel> labels = new ArrayList<IssueLabel>();
		
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

		return issues;
	}
	
	
	/**
	 * Fetches all the Milestones of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} of which the Milestones are about
	 * @return a {@link List} of {@link Milestone} objects
	 */
	
	public List<Milestone> getAllProjectMilestones(Project project) {
		
		System.out.println("Searching project milestones metadata");
		
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/milestones")
				  .build();
		
		String jsonString = requests.get(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		List<Milestone> milestones = new ArrayList<>();
		
		for (JsonElement element : jsonArray) {
			Milestone milestone = gson.fromJson(element, Milestone.class);
			milestone.setProject(project);
			
			milestones.add(milestone);
		}

		return milestones;
	}
	
	
	/**
	 * Fetches all the Tags of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} of which the Milestones are about
	 * @return a the size of the {@link List} of {@link Tags} objects
	 */
	public int getNumberProjectTags(Project project) {

		System.out.println("Searching project tags metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/git/refs/tags")
				  .build();

		String jsonString = getWithProtection(searchUrl);

		int retorno = 0;
		
		if(!jsonString.contains("Not Found") && !jsonString.contains("Git Repository is empty.")){

			try {
				JsonElement element =gson.fromJson(jsonString, JsonElement.class);
				
				if(element.isJsonArray()){
					
					JsonArray jsonArray = element.getAsJsonArray();
					
					retorno = jsonArray.size();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GroundhogException(jsonString);
			}	
		}
		
		return retorno;
	}

	/**
	 * Fetches all the Commits of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} to which the commits belong
	 * @return a {@link List} of {@link Commit} objects
	 */
	public List<Commit> getAllProjectCommits(Project project) {
		System.out.println("Searching project commits metadata");
		
		
		/*
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/commits")
				  .build();
		*/
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/commits?page=" +page +"&per_page=30")
				  .build();
		
		System.out.println(searchUrl);
			
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
				  .withParam("/commits?page=" +page +"&per_page=30")
				  .build();
		
		System.out.println(searchUrl);
		
		jsonElement = gson.fromJson(requests.get(searchUrl), JsonElement.class);
		jsonArray = jsonElement.getAsJsonArray();
		
		}

		return commits;
	}

	
	
	/**
	 * Fetches all the Commits of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} to which the commits belong
	 * @return a {@link List} of {@link Commit} objects
	 */
	public List<Commit> getAllProjectCommitsByDate(Project project, String start, String end) {

		System.out.println("Searching all project commits metadata by date");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withSimpleParam("/", "commits")
				  .withParam("since", start)
				  .withParam("until", end)
				  .build();
		
		String response = getWithProtection(searchUrl);

		JsonElement jsonElement = gson.fromJson(response, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();

		List<Commit> commits = new ArrayList<>();
		for (JsonElement element : jsonArray) {
			Commit commit = gson.fromJson(element, Commit.class);
			commit.setProject(project);

			String date = element.getAsJsonObject().get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
			commit.setCommitDate(date);
			commits.add(commit);
		}

		return commits;
	}

	/**
	 * <p>Fetches all the contributors of the given {@link Project} from the GitHub API</p>
	 * @see <p>We can map all {@link Contributor} into a {@link User} by {@link Contributor#getUrl} method that return is the same for a Contributor and his correspondent User ({@link User#getUrl})</p>
	 * @param project the @{link Project} to get the contributors from
	 * @return a {@link List} of {@link Contributor} objects
	 */
	public List<Contributor> getAllProjectContributors(Project project) {
		
		System.out.println("Searching project contributors metadata");
		
		List<Contributor> contributors = new ArrayList<>();
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/contributors")
				  .build();
		
		//System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
        JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
                
		for (JsonElement element: jsonArray) {
        	Contributor contributor = gson.fromJson(element, Contributor.class);
        	contributors.add(contributor);
        }

		return contributors;
	}

	public List<Project> getAllForgeProjects(int start, int limit) throws SearchException{
		try{
			
			System.out.println("Searching all projects metadata");
			
			int since = start;
			int totalRepositories = 0;
			List<Project> projects = new ArrayList<>();
			
			while (totalRepositories < limit || limit < 0) {

				String searchUrl = builder.uses(GithubAPI.REPOSITORIES)
										  .withParam("since", since)
										  .withParam("language", "java")
										  .build();
				
				String jsonString = requests.get(searchUrl);
				JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
				for (int i = 0; i < jsonArray.size() && 
						(totalRepositories + i < limit || limit < 0); i++) {

					String repoName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();					
					String searchUrlLegacy = builder.uses(GithubAPI.LEGACY_V2)
													  .withParam(repoName)
													  .withParam("language", "java")
													  .build();
					
					String jsonLegacy = requests.get(searchUrlLegacy);

					JsonObject jsonObject = gson.fromJson(jsonLegacy, JsonElement.class).getAsJsonObject();			
					JsonArray jsonArrayLegacy = jsonObject.get("repositories").getAsJsonArray();

					if (jsonArrayLegacy.size() > 0) {
						JsonObject rawJsonObject = jsonArrayLegacy.get(0).getAsJsonObject();
						String stringElement = rawJsonObject.toString(); // verify here
						
						Project p = gson.fromJson(stringElement, Project.class);

						String owner = rawJsonObject.getAsJsonObject().get("owner").getAsString();
						p.setCheckoutURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));

						projects.add(p);
						totalRepositories++;
					}
				}
				
				JsonElement lastPagesRepository = jsonArray.get(jsonArray.size() -1);
				since = lastPagesRepository.getAsJsonObject().get("id").getAsInt();
			}
			return projects;
		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}
	
	/**
	 * Gets the top most Used languages among the projects considering the most used language
	 * in each project. 
	 * @param projects List of projects into consideration
	 * @param limit Limits the size of the returning list
	 * @return sorted list with the top most used languages
	 */
	public List<Language> getTopMostUsedLanguages(List<Project> projects, int limit){
		List<Language> topLanguages = new ArrayList<>();
		Map<String, Integer> languages = new HashMap<>(); 
		for (Project project: projects) {			
			String language = project.getLanguage();
			Integer count = 1;
			
			if (languages.containsKey(language)){ 
				count += languages.get(language);
			}						   
			
			languages.put(language, count);

		}
		for (Entry<String, Integer> language : languages.entrySet()) {
			topLanguages.add(new Language(language.getKey(), language.getValue()));
		}

		Collections.sort(topLanguages);
		if(limit < 0) limit = 0;
		
		topLanguages = topLanguages.subList(0, Math.min(limit, topLanguages.size()));
		return topLanguages;		
	}


	/**
	 * Gets the top most used languages among the projects according to the number 
	 * of LOC (lines of code) that they appear. 
	 * @param projects {@link List} of projects into consideration
	 * @param limit for limiting (upper bound) the size of the returning list
	 * @return sorted list with the top most used languages
	 */
	public List<Language> getTopMostUsedLanguagesLoc(List<Project> projects, int limit){
		List<Language> topLanguages = new ArrayList<>();
		Map<String, Integer> languages = new HashMap<>(); 
		
		for (Project project: projects){
			if (project.getLanguages() == null) {
				throw new GroundhogException("languages information required");
			}
			
			for (Language language : project.getLanguages()) {
				Integer newLoc = language.getByteCount();
				
				if (languages.containsKey(language.getName())) {
					newLoc += languages.get(language.getName());
				}
				
				languages.put(language.getName(), newLoc);
			}
		}
		
		for (Entry<String, Integer> language : languages.entrySet()) {
			topLanguages.add(new Language(language.getKey(), language.getValue()));
		}

		Collections.sort(topLanguages);
		if (limit < 0 ) limit = 0;
		
		topLanguages = topLanguages.subList(0, Math.min(limit, topLanguages.size()));
		return topLanguages;	
	}
	
	/**
	 * Fetches all the Releases of the given {@link Project} from the GitHub API
	 * 
	 * @param project the @{link Project} of which the Releases are about
	 * @return a {@link List} of {@link Release} objects
	 */
	public List<Release> getAllProjectReleases(Project project) {

		System.out.println("Searching project releases metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/releases")
				  .build();
		
		//System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		List<Release> releases = new ArrayList<>();
		for (JsonElement element : jsonArray) {
			Release release = gson.fromJson(element, Release.class);
			release.setProject(project);
			
			releases.add(release);
		}

		return releases;
	
	}

	/**
	 * get a ceratin developer
	 * @param login
	 * @return
	 */
	public usefuldata.Developer getDeveloper(String login){
		System.out.println("Searching developer metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("users")
				  .withSimpleParam("/", login)
				  .build();
		
		//System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonObject jsonobject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		usefuldata.Developer dp = gson.fromJson(jsonobject, usefuldata.Developer.class);

		return dp;
	}
	
	/**
	 * get developers involved in a certain project
	 * @param project
	 * @return
	 */
	public List<Developer> getDevelopers(Project project){
		System.out.println("Searching all developers metadata");
		
		List<Contributor> list = getAllProjectContributors(project);
		
		List<Developer> result = new ArrayList<Developer>();
		
		for(Contributor a:list){
			Developer dp = getDeveloper(a.getLogin());
			result.add(dp);
		}
		
		return result;
	}
	
		
	
	/**
	 * get all releases url of a certain project 
	 * @param owner
	 * @param projectName
	 * @return
	 */
	public Map<String,String> getReleaseUrls(String owner,String projectName){
		System.out.println("Getting prepared for release downloading...");
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withSimpleParam("/", "tags")
				  .withParam("?page=" + page + "&per_page=30")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		if(jsonArray.size() == 0){
			return null;
		}
		
		
		Map<String,String> urls = new HashMap<>();
		String name =null;
		String url = null;
		
		while(jsonArray.size()!=0){
			for (JsonElement element : jsonArray) {
				name = element.getAsJsonObject().get("name").getAsString();
				url = element.getAsJsonObject().get("zipball_url").getAsString();
				if(name!=null && url!=null)
				urls.put(name,url);
			}
			
			page++;			
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", owner)
					  .withSimpleParam("/", projectName)
					  .withSimpleParam("/", "tags")
					  .withParam("?page=" + page + "&per_page=30")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
			jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
			
		}
		
		

		System.out.println("Urls fetching completed....");
		return urls;
	}
	
	/**
	 * get a developer's contribution to a certain project
	 * @param owner
	 * @param projectName
	 * @param developer
	 * @param project_id
	 * @return
	 */
	public ProjectContribution getAllProjectContribution(String owner,String projectName,String developer
			,int project_id){
		
		System.out.println("Now fetching all shas for commits...");
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withSimpleParam("/", "commits")
				  .withParam("?author=" + developer + "&page=" + page + "&per_page=30")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
		if(jsonArray.size() == 0){
			return null;
		}
		
		JsonElement j1 = jsonArray.get(0);
		
		int developer_id = j1.getAsJsonObject().get("author").getAsJsonObject().get("id").getAsInt();
		int contributions = 0;
		
		while(jsonArray.size()!=0){
	
			for (JsonElement element : jsonArray) {
				String sha = element.getAsJsonObject().get("sha").getAsString();
				contributions += getContribution(owner,projectName,sha);
				
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", owner)
					  .withSimpleParam("/", projectName)
					  .withSimpleParam("/", "commits")
					  .withParam("?author=" + developer + "&page=" + page + "&per_page=30")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
			jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
			
			
		}
		
		ProjectContribution pct = new ProjectContribution();
		pct.setProject_id(project_id);
		pct.setDeveloper_id(developer_id);
		pct.setContributions(contributions);
		
		return pct;
	}
	
	
	
	/**
	 * this method tries to fetch map(release,date)
	 * @return
	 */
	public Map<String,String> getReleaseDate(String owner,String projectName){
		System.out.println("trying to get release date...");
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withSimpleParam("/", "tags")
				  .withParam("?page=" + page + "&per_page=30")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
		
		if(jsonArray.size() == 0){
			return null;
		}
		
		Map<String,String> results = new HashMap<String,String>();
		
		while(jsonArray.size()!=0){
			
			for(JsonElement element : jsonArray){
				String tag_name = element.getAsJsonObject().get("name").getAsString();
				String sha = element.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString();
				
				String url = builder.uses(GithubAPI.ROOT)
						  .withParam("repos")
						  .withSimpleParam("/", owner)
						  .withSimpleParam("/", projectName)
						  .withSimpleParam("/", "commits")
						  .withSimpleParam("/",sha)
						  .build();
				
				String date = getDateWithUrl(url);
				results.put(tag_name, date);
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", owner)
					  .withSimpleParam("/", projectName)
					  .withSimpleParam("/", "tags")
					  .withParam("?page=" + page + "&per_page=30")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
			jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
			
		}
		
		return results;
	}
	
	
	/**
	 * get a developer's contribution to a certain project
	 * separated by different releases
	 * @param dateMap
	 * @param project
	 * @param developer
	 * @return Map<releaseName,contributions>
	 */
	public Map<String,Integer> getReleaseContribution(Map<String,String> dateMap,Project project,String developer){
		System.out.println("trying to get release contributions...");
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withSimpleParam("/", "commits")
				  .withParam("?author=" + developer + "&page=" + page + "&per_page=30")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
		if(jsonArray.size() == 0){
			return null;
		}
				
		/**
		 * initialize result map,with <release_name,0>
		 */
		
		Map<String,Integer> results = new HashMap<String,Integer>();
		Set<String> tmp_key = dateMap.keySet();
		for(String tmp_name:tmp_key){
			results.put(tmp_name, 0);
		}
		
		
		while(jsonArray.size()!=0){
	
			for (JsonElement element : jsonArray) {
				
				String sha = element.getAsJsonObject().get("sha").getAsString();
				//<date,contributions>
				Map<String,Integer> date_cont = getReleaseContribution(project,sha);
				Set<String> date_set = date_cont.keySet();
				
				int position = 0;
				ArrayList<String> sorted_release = Dates.dateSort(dateMap);
				
				for(String specific_date:date_set){
					 position = Dates.getDateIndex(specific_date,dateMap);
					 
					 if(position >= sorted_release.size())
						 position = sorted_release.size() - 1;
					 
					 results.replace(sorted_release.get(position), date_cont.get(specific_date) +results.get(sorted_release.get(position)));
				}
				
				
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withSimpleParam("/", "commits")
					  .withParam("?author=" + developer + "&page=" + page + "&per_page=30")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
			jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
			
			
		}
		
		return results;
	}
	
	
	public List<Vitality> getVitality(Project project,String developer){	
		System.out.println("trying to get " +developer +  "'s vitalities...");
		
		int page = 1;
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withSimpleParam("/", "commits")
				  .withParam("?author=" + developer + "&page=" + page + "&per_page=30")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
		if(jsonArray.size() == 0){
			return null;
		}
		
		
		/**
		 * initialize result map,with <release_name,0> (0 should be vitality)
		 */
		
		List<Vitality> results = new ArrayList<Vitality>();
		
		while(jsonArray.size()!=0){
			
			for (JsonElement element : jsonArray) {
				String sha = element.getAsJsonObject().get("sha").getAsString();
				String date = getCommitDate(project.getOwner().getLogin(),project.getName(),sha);
				
				boolean isEnd = false;
				for(int i = 0;i<results.size();i++){
					if(results.get(i).getDate().equals(date)){
						results.get(i).setVitality(results.get(i).getVitality() + 1);
						isEnd = true;
						break;
					}
				}
				if(isEnd == false){
					Vitality tmp_vi = new Vitality();
					tmp_vi.setVitality(1);
					tmp_vi.setDate(date);
					results.add(tmp_vi);
				}
				
			}
			
			page++;
			searchUrl = builder.uses(GithubAPI.ROOT)
					  .withParam("repos")
					  .withSimpleParam("/", project.getOwner().getLogin())
					  .withSimpleParam("/", project.getName())
					  .withSimpleParam("/", "commits")
					  .withParam("?author=" + developer + "&page=" + page + "&per_page=30")
					  .build();
			
			jsonString = requests.getWithPreviewHeader(searchUrl);
			jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
			
			
		}
		
		return results;
	}
	
	
	
	private Map<String,Integer> getReleaseContribution(Project project,String sha){
		System.out.println("Getting contributions for a certain commit...");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getOwner().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withSimpleParam("/", "commits")
				  .withSimpleParam("/",sha)
				  .build();
		
		System.out.println(searchUrl);
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonObject jsonobject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		
		String date = jsonobject.get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
		int contributions = jsonobject.get("stats").getAsJsonObject().get("total").getAsInt();
		
		Map<String,Integer> result = new HashMap<String,Integer>();
		result.put(date, contributions);
		
		
		return result;
	}
	
	
	
	/**
	 * this method is used by getReleaseDate(String owner,String projectName)
	 * @param url
	 * @return
	 */
	private String getDateWithUrl(String url){
		System.out.println("trying to fetch the url...");
		
		String jsonString = requests.getWithPreviewHeader(url);
		JsonObject jsonobject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		
		
		//System.out.println(url);
		String date = jsonobject.get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
		
		return date;
	}
	
	/**
	 * get contribution using specific sha
	 * @param owner
	 * @param projectName
	 * @param sha
	 * @return
	 */
	private int getContribution(String owner,String projectName,String sha){
		
		System.out.println("Getting contributions for a certain commit...");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withSimpleParam("/", "commits")
				  .withSimpleParam("/",sha)
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonObject jsonobject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		
		int contributions = jsonobject.get("stats").getAsJsonObject().get("total").getAsInt();
		
		return contributions;
	}
	
	private String getCommitDate(String owner,String projectName,String sha){
		System.out.println("Getting date for a certain commit...");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", owner)
				  .withSimpleParam("/", projectName)
				  .withSimpleParam("/", "commits")
				  .withSimpleParam("/",sha)
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonObject jsonobject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		
		String date = jsonobject.get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
		date = Dates.dateFormat(date);
		return date;
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
	
	
	
	
}
