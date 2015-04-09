package analysis;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import usefuldata.PackageNode;

public class DevelopDigramImpl implements DevelopDigram {

	private ArrayList<PackageNode> architectures;
	private boolean toJson;
	
	public  DevelopDigramImpl()
	{
		architectures= new ArrayList<PackageNode>();
		toJson=false;
	}

	private String architecturesToJson(boolean toJson) {
		
		if(toJson)
		{
		JSONArray json = JSONArray.fromObject(architectures.get(0));
		String jsonStr=json.toString();
		String resultStr=jsonStr.substring(1,jsonStr.length()-1);
	
		//String resultStr=json.toString().substring(1,json.toString().length()-1);
		architectures = new ArrayList<PackageNode>();
		return resultStr;
		}
		else return null;
	}

	private int findPakage(String path) {
		int result = -1;

		for (int i = 0; i < architectures.size(); i++) {
			if (path.equals(architectures.get(i).takePath())) {
				result = i;
				break;
			}

		}

		return result;
	}

	private void traverse(int i) {
		//System.out.println(i);
		PackageNode pnc = architectures.get(i);
		if (pnc.takeCIndex() != null) {
			for (Integer j : pnc.takeCIndex()) {
				traverse(j);
				PackageNode pnc2 = architectures.get(j);
				pnc.addChild(pnc2);
				//System.out.println(pnc.getPath()+"child:");
				//System.out.println(pnc2.getPath());
				architectures.set(i, pnc);
			}
		}

	}

	@Override
	public String getDevelopDigramByVersion(ArrayList<String> filenames) {
		// TODO Auto-generated method stub

		for (String file : filenames) {
			file="./"+file;
			String pathName = "";
			String[] pakageNames = file.split("/");
			for (int i = 0; i < pakageNames.length; i++) {
				if (i == (pakageNames.length - 1))
					pathName += pakageNames[i];
				else
					pathName += pakageNames[i] + "/";
			
				if (findPakage(pathName) == -1) 
				{
					
					PackageNode pakagenode = new PackageNode(pathName,
							pakageNames[i]);
					architectures.add(pakagenode);
				}
			}

		}

		
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+architectures.size());
		for (int i = 0; i < architectures.size(); i++) {
			PackageNode pn = architectures.get(i);
			String pnPath = pn.takePath();
			String[] fns = pnPath.split("/");

			for (int j = 0; j < architectures.size(); j++) {
				PackageNode pn2 = architectures.get(j);
				String pn2Path = pn2.takePath();

				if (pn2Path.length() > pnPath.length()) {
					if (findPakage(pn2Path) != -1) {

						String[] fns2 = pn2Path.split("/");

						if (pn2Path.contains(pnPath)
								&& (fns2.length - fns.length) == 1) {
							pn.addChild(j);
						}

						architectures.set(i, pn);
					}
				}
				
				
			}
			
		}

		if(filenames.size()!=0)
		{
		traverse(0);
	    toJson=true;
		}
		
		return architecturesToJson(toJson);
	}

}



