package metadao;

import java.util.List;

import crawler.DataSource;
import entity.Comment;
import entity.Contributor;
import entity.Issue;
import entity.Project;
import entity.UnPublishedRelease;
import entity.User;
import factory.MetaDaoFactory;

public class MySQLController extends MetaDaoController{

	public MySQLController(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void IntoDataBase() {
		if(this.dataSource.getInIndex() == null){
			Project project = this.dataSource.getProject();
			project.getOwner().setLogin(this.dataSource.getOwner());
			MetaDaoFactory.getProjectDao().addProject(project);
			
			List<Comment> comments = this.dataSource.getComments();
			int project_id = project.getId();
			if(comments != null)
				MetaDaoFactory.getCommentDao().addComments(comments,project_id);
			
			List<Contributor> contributors = this.dataSource.getContributors();
			MetaDaoFactory.getContributorDao().addContributors(contributors, project_id);
			
			List<Issue> issues = this.dataSource.getIssues();
			if(issues != null)
				MetaDaoFactory.getIssueDao().addIssues(issues, project_id);
			
			//IssueLabel issuelabel = this.dataSource.getIssuelabel();
			//not finished yet
			
			//Language language = this.dataSource.getLanguage();
			//not finished yet
			
			//License license = this.dataSource.getLicense();
			//not finished yet
			
			//Milestone milestone = this.dataSource.getMilestone();
			//not finished yet
			
			//PullRequest pullrequest = this.dataSource.getPullrequest();
			//not finished yet
			
			//List<Release> releases = this.dataSource.getReleases();
			//not finished yet
			
			List<UnPublishedRelease> unpublish_releases = this.dataSource.getUnpublish_releases();
			if(unpublish_releases != null)
				MetaDaoFactory.getUnPublishedReleaseDao().addUnPublishedReleases(unpublish_releases, project_id);
			
			List<User> users = this.dataSource.getUsers();
			MetaDaoFactory.getUserDao().addUsers(users);
			
			MetaDaoFactory.getCrawlindexDao().addCrawlindex(this.dataSource.getOutIndex());
			
			
		}else{
			//if already exist then update it
			
			Project project = this.dataSource.getProject();
			project.getOwner().setLogin(this.dataSource.getOwner());
			MetaDaoFactory.getProjectDao().addProject(project);
			
			List<Comment> comments = this.dataSource.getComments();
			int project_id = project.getId();
			if(comments != null)
				MetaDaoFactory.getCommentDao().CheckaddComments(comments,project_id);
			
			List<Contributor> contributors = this.dataSource.getContributors();
			MetaDaoFactory.getContributorDao().CheckaddContributors(contributors, project_id);
			
			List<Issue> issues = this.dataSource.getIssues();
			if(issues != null)
				MetaDaoFactory.getIssueDao().CheckaddIssues(issues, project_id);
			
			List<UnPublishedRelease> unpublish_releases = this.dataSource.getUnpublish_releases();
			if(unpublish_releases != null)
				MetaDaoFactory.getUnPublishedReleaseDao().CheckaddUnPublishedReleases(unpublish_releases, project_id);
			
			List<User> users = this.dataSource.getUsers();
			MetaDaoFactory.getUserDao().CheckaddUsers(users);
			
			MetaDaoFactory.getCrawlindexDao().addCrawlindex(this.dataSource.getOutIndex());
		}
				
	}

}
