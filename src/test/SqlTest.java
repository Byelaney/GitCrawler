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
import usefuldata.Project;
import usefuldata.ProjectContribution;
import usefuldata.Release;
import usefuldata.ReleaseContribution;
import usefuldata.Vitality;
import factory.DaoFactory;

public class SqlTest {
	
	
	public static void main(String []args){
		
		//Developer a = DaoFactory.getDeveloperDao().findDeveloperForId("jason");
		//List<Developer> dp = DaoFactory.getReleaseDao().getAllDeveloper(1, "v1");
		//System.out.println(a.getId());
		
		/*
		for(Developer e:dp){
			System.out.println(e.getId());
			System.out.println(e.getName());
			
		}
		
		Developer dp = new Developer();
		dp.setId(1743086);
		dp.setLogin(null);
		dp.setEmail(null);
		dp.setUrl("https://github.com/bhong");
		
		DaoFactory.getDeveloperDao().addDeveloper(dp);
		
		
		
		Vitality v = new Vitality();
		v.setSubmits(900);
		v.setDate("2015-1-13");
		boolean flag = DaoFactory.getVitalityDao().deleteAllVitality(1);
		if(flag)
			System.out.println("succeed!");
		
		
		Project p = DaoFactory.getProjectDao().getProject("nasa", "mct");
		if(p!=null)
			System.out.println(p.getDescription());
		*/
		
		
		
		
		//DaoFactory.getReleaseDao().addRelease(release_id, developer_id, 4193864, contributions);
		
//		ArrayList<ReleaseContribution> ss = DaoFactory.getReleaseContribution().getReleaseContribution("mct", "kptran");
//		
//		for(int i = 0;i<ss.size();i++){
//			System.out.println(ss.get(i).getReleaseName() + " : " + ss.get(i).getContributions());
//			System.out.println("------------------------------");
//		}
		
		List<ProjectContribution> ss = DaoFactory.getProjectContribution().findProjectContribution("VWoeltjen");
		for(int i = 0;i<ss.size();i++){
			System.out.println(ss.get(i).getProjectName() +" : " + ss.get(i).getContributions());
		}
		
		
	}
}
