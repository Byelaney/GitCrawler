package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import usefuldata.Developer;
import usefuldata.DeveloperEcharts;
import usefuldata.Project;
import usefuldata.ProjectContribution;
import usefuldata.Release;
import usefuldata.ReleaseContribution;
import usefuldata.Vitality;
import entity.Crawlindex;
import factory.DaoFactory;
import factory.MetaDaoFactory;

public class SqlTest {
	
	
	public static void main(String []args){
//		DeveloperEcharts dpe = new DeveloperEcharts();
//		dpe.setDeveloper_id(1);
//		dpe.setJson_string("hello world");
//		dpe.setProject_id(2);
//		dpe.setRelease_id(3);
//		DaoFactory.getDeveloperEchartsDao().updateEcharts(dpe);
//		
//		Crawlindex ci = new Crawlindex();
//		ci.setComment_page(1);
//		ci.setCommit_page(1);
//		ci.setContributor_page(1);
//		ci.setIssue_page(1);
//		ci.setMilestone_page(1);
//		ci.setProject_id(1);
//		ci.setPullrequest_page(1);
//		ci.setRelease_idx(1);
//		ci.setRelease_page(1);
//		ci.setUpbrelease_page(1);
//		ci.setUser_page(1);
//		
//		
//		MetaDaoFactory.getCrawlindexDao().addCrawlindex(ci);
		
		entity.Project p = MetaDaoFactory.getProjectDao().getProject("spinfo", "java");
		
		System.out.println(p == null);
	}
}
