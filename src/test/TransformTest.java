package test;

import java.util.List;

import transform.MetaTransformImpl;
import util.Dates;

public class TransformTest {
	public static void main(String []args){
		TransformTest tt = new TransformTest();
		tt.testRate();
//		long t = Dates.dayDiffer("2014-11-01", "2014-12-05");
//		System.out.println(t);
	}
	
	
	public void testCommit(){
		MetaTransformImpl mm = new MetaTransformImpl();
		List<entity.UnPublishedRelease> ubsrs = mm.getAllUnPublishedRelease(4193864);
		List<usefuldata.Release> rs = mm.getCommitNumber(ubsrs, 4193864);
		for(int i = 0;i<rs.size();i++){
			System.out.print(rs.get(i).getName() + "  " + rs.get(i).getRelease_commits());
			System.out.println();
		}
	}
	
	public void testRate(){
		MetaTransformImpl mm = new MetaTransformImpl();
		List<entity.UnPublishedRelease> ubsrs = mm.getAllUnPublishedRelease(4193864);
		List<usefuldata.Release> rs = mm.getCommitNumber(ubsrs, 4193864);
		mm.getCommitRate(rs);
		for(int i = 0;i<rs.size();i++){
			System.out.print(rs.get(i).getName() + "  " + rs.get(i).getCommit_rate());
			System.out.println();
		}
		
	}
	
	public void testIssue(){
		MetaTransformImpl mm = new MetaTransformImpl();
		List<entity.UnPublishedRelease> ubsrs = mm.getAllUnPublishedRelease(4193864);
		List<usefuldata.Release> rs = mm.getCommitNumber(ubsrs, 4193864);
		mm.getIssueNum(rs, 4193864);
		for(int i = 0;i<rs.size();i++){
			System.out.print(rs.get(i).getName() + "  " +rs.get(i).getIssue_number() + "  " + rs.get(i).getId());
			System.out.println();
		}
	}
}
