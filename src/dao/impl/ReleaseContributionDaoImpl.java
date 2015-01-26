package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Release;
import usefuldata.ReleaseContribution;
import dao.DaoHelper;
import dao.ReleaseContributionDao;
import factory.DaoFactory;

public class ReleaseContributionDaoImpl implements ReleaseContributionDao{

	private static ReleaseContributionDaoImpl releaseContributionDao=new ReleaseContributionDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static ReleaseContributionDaoImpl getInstance(){
		return releaseContributionDao;
	}
	
	@Override
	public ArrayList<ReleaseContribution> getReleaseContribution(
			String projectName, String developer) {
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		con=daoHelper.getConnection();
		int developer_id = DaoFactory.getDeveloperDao().findDeveloper(developer).getId();
		
		con=daoHelper.getConnection();
		
		try{
			ps=con.prepareStatement("select * from release_contribution where project_id =? and developer_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, developer_id);
			rs=ps.executeQuery();
			ReleaseContribution rcb = null;
			ArrayList<ReleaseContribution> results = new ArrayList<ReleaseContribution>();
			
			while(rs.next()){
				rcb = new ReleaseContribution();
				int release_id = rs.getInt("release_id");
				String releaseName = DaoFactory.getReleaseDao().getRelease(release_id).getName();
				rcb.setReleaseName(releaseName);
				rcb.setContributions(rs.getInt("contributions"));
				
				results.add(rcb);
			}	
			
			List<Release> reles = DaoFactory.getReleaseDao().getAllRelease(projectName);;
			ArrayList<ReleaseContribution> res = new ArrayList<ReleaseContribution>();
			for(Release r:reles){
				res.add(new ReleaseContribution(r.getName(),0));
			}
			
			for(int i = 0;i<results.size();i++){
				for(int j =0;j<res.size();j++){
					if(res.get(j).getReleaseName().equals(results.get(i).getReleaseName())){
						res.get(j).setContributions(results.get(i).getContributions());
						break;
					}
					
				}
				
			}
			
			return res;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return null;
	}

}