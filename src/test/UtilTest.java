package test;

import java.util.List;

import factory.DaoFactory;
import usefuldata.Release;
import util.Dates;

public class UtilTest {
	public static void main(String []args){
//		List<Release> rls = DaoFactory.getProjectDao().getAllReleases("mct");
//		//List<Release> sorted = Dates.releaseSort(rls);
//		for(int i = 0;i<rls.size();i++){
//			System.out.println(rls.get(i).getName());
//			System.out.println(rls.get(i).getDate());
//			System.out.println("---------------------");
//		}
		
		String ss = Dates.metaDateFormat("Tue Sep 03 23:12:18 CST 2013");
		
		System.out.println(ss);
	}
}
