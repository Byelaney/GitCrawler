package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.Dates;



public class JsonTest {
	
	public static void main(String []args){
		/*
		List<Vitality> ss = new ArrayList<Vitality>();
		Vitality a1 = new Vitality("1-Apr-12",12);
		//Vitality a2 = new Vitality("2-Apr-12",112);
		//Vitality a3 = new Vitality("3-Apr-12",123);
		//Vitality a4 = new Vitality("4-Apr-12",15212);
		//Vitality a5 = new Vitality("5-Apr-12",1233);
		ss.add(a1);
		//ss.add(a2);
		//ss.add(a3);
		//ss.add(a4);
		//ss.add(a5);
		
		VitalityDaoImpl a = new VitalityDaoImpl();
		String s = a.VitalityToJson(ss);
		System.out.println(s);
		
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		SearchGitHub searchGitHub = injector.getInstance(SearchGitHub.class);
		
		
		VitalityTransform a = new VitalityTransform();
		User user = new User("twbs");
		Project project = new Project(user,"bootstrap");
		
		User uss = new User("cvrebert");
		
		List<Commit> commits = searchGitHub.getAllProjectCommits(project);
		
		List<Commit> sseqw = new ArrayList<Commit>();
		
		for(Commit cmit:commits){
			if(cmit.getCommiter().getLogin().equals(uss.getLogin())){
				sseqw.add(cmit);
			}
		}
		
		//System.out.println(uss.getLogin());
		
		uss.setCommits(sseqw);
		List<Vitality> ss = a.transform(uss, project);
		for(Vitality e:ss){
			System.out.println(e.toString());
		}
		
		
		Map<String,String> st = new HashMap<String,String>();
		st.put("v1.7b1", "2012-06-30T20:18:25Z");
		st.put("v1.7b2", "2012-07-19T20:18:25Z");
		st.put("v1.7b3", "2012-08-07T20:18:25Z");
		
		
		Map<String,String> rs = Dates.dateFormat(st);
		Set<String> ss = rs.keySet();
		for(String name:ss){
			System.out.println(name + ": " + rs.get(name));
		}
		
		
		
		Map<String, Integer> map = new HashMap<String, Integer>();  
		  
		map.put("lisi", 5);   
		map.put("lisi1", 1);   
		map.put("lisi2", 3);   
		map.put("lisi3", 9);   
		*/
		
		
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("lisi", "2012-08-30"); 
		map.put("lisi1", "2012-06-30"); 
		map.put("lisi2", "2012-07-19"); 
		map.put("lisi3", "2012-08-07"); 
				
		ArrayList<String> aa = Dates.dateSort(map);
		for(int i = 0 ;i<aa.size();i++){
			System.out.println(aa.get(i));
		}
		
		
		
	}


}
