package test;


import java.util.ArrayList;
import java.util.List;

import usefuldata.Release;
import factory.DaoFactory;
import analysis.CodeLinesCountImpl;
import analysis.DevelopDigram;
import analysis.DevelopDigramImpl;
import analysis.PackageDependency;
import analysis.PackageDependencyImpl;
public class AnalysisTest {
	
	public static void main(String []args){
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
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("java");
		
		ArrayList<String> paths = new ArrayList<String>();
		
		List<Release> rels = DaoFactory.getProjectDao().getAllReleases("mct");
		
		for(int i =0;i<rels.size();i++){
			paths.add(rels.get(i).getName() + ".zip");
		}
		
		//paths.add("v1.7.0.zip");
		//paths.add("v1.7b1.zip");
		
		
		
		PackageDependency pd = new PackageDependencyImpl();
		ArrayList<String> ds;
		ds = pd.getPakageDependency(paths, languages);
		
		
//		for(int j =0;j<rels.size();j++){
//			DaoFactory.getReleaseEchartsDao().addReleaseEcharts(4193864, rels.get(j).getId(), ds.get(j));
//		}
		
		System.out.println(DaoFactory.getReleaseEchartsDao().getReleaseEcharts("mct", "v1.7.0"));
		
	}
}
