package test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timer.Server;
import usefuldata.CommitDate;
import usefuldata.Release;
import usefuldata.VersionDate;
import entity.Contributor;
import entity.Project;
import entity.UnPublishedRelease;
import factory.DaoFactory;
import factory.MetaDaoFactory;
import analysis.CodeLinesCount;
import analysis.CodeLinesCountImpl;
import analysis.DataHelperImpl;
import analysis.DevelopDigram;
import analysis.DevelopDigramImpl;
import analysis.EvolveAnalysis;
import analysis.PackageDependency;
import analysis.PackageDependencyImpl;
import analysis.Relation;
import analysis.RelationImpl;
public class AnalysisTest {
	
	final static Logger logger = LoggerFactory.getLogger(AnalysisTest.class);
	
	public static void main(String []args){

		
		AnalysisTest at = new AnalysisTest();
		at.logttest();
		
		//CodeLinesCount clc = new CodeLinesCount();
		
		//System.out.println(clc.StatisticCodeLines("", "v1.7b1.zip"));
		
		
//		ArrayList<String> files=new ArrayList<String>();//所有版本文件的路径
//		String destination=null;//.json文件要存入的文件夹位置
//		
//		files.add("v1.8b1.zip");
//		destination="Downloads/";
//		PackageDependency.readZipFile(files, destination);//将每个版本对应的包依赖json数据格式输出到目标文件夹
//		
//		
//		DevelopDigram dd = new DevelopDigramImpl();
//		ArrayList<String> ewq = new ArrayList<String>();
//		ewq.add("databasePersistence/src/main/java/gov/nasa/arc/mct/dbpersistence/service/PersistenceServiceImpl.java");
//		System.out.println(dd.getDevelopDigramByVersion(ewq));
//		
//		ArrayList<String> languages = new ArrayList<String>();
//		languages.add("java");
//		
//		ArrayList<String> paths = new ArrayList<String>();
//		
//		List<Release> rels = DaoFactory.getProjectDao().getAllReleases("mct");
//		
//		for(int i =0;i<rels.size();i++){
//			paths.add(rels.get(i).getName() + ".zip");
//		}
		
		//paths.add("v1.7.0.zip");
		//paths.add("v1.7b1.zip");
		
		
//		AnalysisTest at = new AnalysisTest();
//		at.evolveTest();
		
		//AnalysisTest a = new AnalysisTest();
		//a.release_echartTest();
		
		//Project p = MetaDaoFactory.getProjectDao().getProject("spinfo","java");
		//System.out.println(p == null);
		
//		DataHelperImpl dhi = new DataHelperImpl();
//		ArrayList<VersionDate> vd = dhi.getVersions("java","spinfo");
//		System.out.println(vd.size());
//		
//		System.out.println(MetaDaoFactory.getProjectDao().getProject("spinfo","java").getId());
	}
	
	public void logttest(){
		logger.info("hello");
	}
	
	public void DataHelperImplTest(){
		DataHelperImpl dhi = new DataHelperImpl();
		ArrayList<String> ss = dhi.getFiles("mct", "v1.8b2", "DanBerrios","");
		for(int i =0;i<ss.size();i++){
			System.out.println(ss.get(i));
		}
		
	}
	
	public void getCommitsTest(){
		DataHelperImpl dhi = new DataHelperImpl();
		ArrayList<CommitDate> cds = dhi.getCommits("mct","");
		for(CommitDate c:cds){
			System.out.println(c.getName());
			System.out.println(c.getDate());
			
		}
		
	}
	
	public void getFilesTest(){
		DataHelperImpl dhi = new DataHelperImpl();
		ArrayList<String> files = dhi.getFiles("mct", "VWoeltjen", "2012-06-11", "2013-08-12");
		for(int i =0;i<files.size();i++){
			System.out.println(files.get(i));
		}
		
	}
	
	public void evolveTest(){
		EvolveAnalysis ea = new EvolveAnalysis("mct","");
		List<UnPublishedRelease> upr = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(4193864);
		int project_id = MetaDaoFactory.getProjectDao().getProject("mct","").getId();
		for(UnPublishedRelease e:upr){
			String json = ea.getEvolveJson(e.getName());
			System.out.println(e.getName());
			System.out.println(json);
			System.out.println("*********");
			//DaoFactory.getEvolveEchartsDao().addEvolveEcharts(project_id, e.getId(), json);
		}
		
		
	}
	
	public void codecountTest(){
		CodeLinesCount ds = new CodeLinesCountImpl();
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("java");
		System.out.println(ds.getCodeLines("Downloads/nasa_mct/v1.7.0.zip", languages));
	}
	
	
	public void RelationTest(){
		Map<String, String> date_maps = new HashMap<String, String>();
		
		List<Contributor> ctr = MetaDaoFactory.getContributorDao().getAllContributors(4193864);
		List<UnPublishedRelease> upr= MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(4193864);
		ArrayList<String> developers = new ArrayList<String>();
		for(Contributor c:ctr){
			//System.out.println(c.getLogin());
			developers.add(c.getLogin());
		}
			
		
		for(UnPublishedRelease u:upr){
			//System.out.println(u.getName());
			date_maps.put(u.getName(), u.getDate());
		}
			
		
		Relation relation;
		String projectName = "mct";
				
		for(UnPublishedRelease u:upr){
			relation = new RelationImpl(projectName, u.getName(),"");
			System.out.println(u.getName());
			System.out.println(relation.getRelations());
			System.out.println(relation.getMainRelations());
			System.out.println("*************************");
		}
	}
	
	public void release_echartTest(){
		PackageDependency dd = new PackageDependencyImpl();
		ArrayList<String> as = new ArrayList<String>();
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("java");
		
		as.add("Downloads/java-s11.zip");
		ArrayList<String> s = dd.getPakageDependency(as, languages);
		for(int i = 0;i<s.size();i++){
			System.out.println(s.get(i));
		}
	}
}
