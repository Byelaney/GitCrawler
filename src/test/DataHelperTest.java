package test;

import java.util.ArrayList;

import usefuldata.VersionDate;
import analysis.DataHelperImpl;

public class DataHelperTest {
	public static void main(String []args){
		DataHelperTest t1 = new DataHelperTest();
		t1.getVesionsTest();
	}
	
	
	public void getSizeTest1(){
		DataHelperImpl dhip = new DataHelperImpl();
		int size = dhip.getSize("DanBerrios", "mct");
		System.out.println(size);
	}
	
	public void getSizeTest2(){
		DataHelperImpl dhip = new DataHelperImpl();
		int size = dhip.getSize("DanBerrios", "mct", "v1.8b1");
		System.out.println(size);
	}
	
	public void getAllDeveloperNamesTest(){
		DataHelperImpl dhip = new DataHelperImpl();
		ArrayList<String> names = dhip.getAllDeveloperNames("mct");
		for(String e:names){
			System.out.println(e);
		}
	}
	
	public void getCodesTest(){
		DataHelperImpl dhip = new DataHelperImpl();
		int codes = dhip.getCodes("mct", "v1.8b1");
		System.out.println(codes);
	}
	
	public void getVesionsTest(){
		DataHelperImpl dhip = new DataHelperImpl();
		ArrayList<VersionDate> versiondates = dhip.getVersions("mct");
		
		for(int i =0;i<versiondates.size();i++){
			System.out.println(versiondates.get(i).getVersion());
			System.out.println(versiondates.get(i).getDate());
			System.out.println(versiondates.get(i).getOrder());
			
			System.out.println("--------------");
			
		}
		
	}
}
