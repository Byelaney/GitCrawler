package test;


import java.util.ArrayList;

import analysis.CodeLinesCount;
import analysis.PackageDependency;
public class AnalysisTest {
	
	public static void main(String []args){
		//CodeLinesCount clc = new CodeLinesCount();
		
		//System.out.println(clc.StatisticCodeLines("", "v1.7b1.zip"));
		
		
		ArrayList<String> files=new ArrayList<String>();//所有版本文件的路径
		String destination=null;//.json文件要存入的文件夹位置
		
		files.add("v1.8b1.zip");
		destination="Downloads/";
		PackageDependency.readZipFile(files, destination);//将每个版本对应的包依赖json数据格式输出到目标文件夹
		
		
	}
}
