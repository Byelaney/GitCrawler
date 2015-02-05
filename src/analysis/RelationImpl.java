package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import usefuldata.Node;
import usefuldata.Link;;



public class RelationImpl implements Relation {

	private DataHelper dh = new DataHelperImpl();
	private List<Node> nodes = new ArrayList<Node>();
	private List<Link> links = new ArrayList<Link>();
	private List<ArrayList<String>> files = new ArrayList<ArrayList<String>>();
	private ArrayList<String> developers = new ArrayList<String>();
	private String projectName = null;
	private String releaseName = null;

	// private ArrayList<Integer> size=new ArrayList<Integer>();
	public RelationImpl(ArrayList<String> developers,
			Map<String, String> dateMap, String owner, String projectName,
			String releaseName) {
		this.developers = developers;
		this.projectName = projectName;
		this.releaseName = releaseName;
		setFiles(developers, dateMap, owner, projectName, releaseName);
	
	}

	private void setFiles(ArrayList<String> developers,
			Map<String, String> dateMap, String owner, String projectName,
			String releaseName) 
	{
		for (int i = 0; i < developers.size(); i++) {
			ArrayList<String> filenames = new ArrayList<String>();
			filenames = dh.getFiles(dateMap, developers.get(i), owner,
					projectName, releaseName);
			files.add(filenames);

		}

	}

	private void sort(int low, int high) {
		int l = low;
		int h = high;
		int povit = nodes.get(low).getSize();

		while (l < h) {
			while (l < h && nodes.get(h).getSize() <= povit)
				h--;
			if (l < h) {
				Node temp = nodes.get(h);
				nodes.set(h, nodes.get(l));
				nodes.set(l, temp);
				l++;

			}
			while (l < h && nodes.get(l).getSize() >= povit)
				l++;
			if (l < h) {
				Node temp = nodes.get(h);
				nodes.set(h, nodes.get(l));
				nodes.set(l, temp);
				h--;
			}

		}

		if (l > low)
			sort(low, h - 1);
		if (h < high)
			sort(l + 1, high);
	}

	private int getNodeIndex(String name) {
		int result = -1;
		for (int i = 0; i < developers.size(); i++) {

			if (developers.get(i).equals(name)) {
				result = i;
			}

		}
		return result;
	}

	@Override
	public String getRelations() {
		// TODO Auto-generated method stub

		for (int i = 0; i < developers.size(); i++) {
			int dsize = (int) Math.log(dh.getSize(developers.get(i), projectName, releaseName));
			int ddsize=dsize/10+1;
			/*if(dsize>0)
				ddsize= (int) Math.log(dh.getSize(developers.get(i), projectName, releaseName));
*/
			// size.add(dsize);

			Node node = new Node(developers.get(i), ddsize);
			nodes.add(node);

			ArrayList<String> filenames = new ArrayList<String>();
			filenames = files.get(i);

			/*
			 * filenames = dh.getFiles(dateMap, developers.get(i), owner,
			 * projectName, releaseName);
			 */

			for (int j = 0; j < developers.size(); j++) {
				if (i != j) {
					ArrayList<String> filenamesToCompare = new ArrayList<String>();
					filenamesToCompare = files.get(j);
					if (isLinked(filenames, filenamesToCompare)) {
						Link link = new Link(i, j);
						addLink(link);
					}
				}

			}

		}

		JSONArray jsonNode = JSONArray.fromObject(nodes);
		JSONArray jsonLink = JSONArray.fromObject(links);
		String resultStr = "{ 'nodes': " + jsonNode.toString() + ", 'links':"
				+ jsonLink.toString() + "}";

		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();

		// FileWriter fw = null;
		// String destination = path + projectName + "_" + releaseName + "_"
		// + "relations.json";
		//
		// boolean written = false;
		// try {
		// fw = new FileWriter(destination);
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// PrintWriter out = new PrintWriter(fw);
		// out.write(resultStr);
		// out.println();
		// written = true;
		// try {
		// fw.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// out.close();
		//
		// if (written)
		//
		// return destination;
		// else
		return resultStr;
	}

	private void addLink(Link link) {
		if (links.size() == 0) 
		{
			links.add(link);
		} else {

			boolean add = true;
			for (int i = 0; i < links.size(); i++) {
				Link l = links.get(i);
				int s = l.getSource();
				int t = l.getTarget();

				if (s == link.getTarget() && t == link.getSource())
					add = false;
				if (t == link.getTarget() && s == link.getSource())
					add = false;
			}
			if (add) {
				links.add(link);
			}
		}
	}

	private boolean isLinked(ArrayList<String> filenames,
			ArrayList<String> filenamesToCompare) {
		boolean result = false;

		for (String file : filenames) {
			int l = file.split("/").length;
			String filename = file.split("/")[l - 1];
			String filePath = file.substring(0,
					file.length() - filename.length());

			for (String fileToComepare : filenamesToCompare) {
				int l2 = fileToComepare.split("/").length;
				String filename2 = fileToComepare.split("/")[l2 - 1];
				String filePath2 = fileToComepare.substring(0,
						fileToComepare.length() - filename2.length());

				if (filePath.equals(filePath2)) {
					result = true;
					break;
				}
			}
			if (result)
				break;
		}

		return result;
	}

	@Override
	public String getMainRelations() {

		for (int i = 0; i < developers.size(); i++) {
			int dsize = dh.getSize(developers.get(i), projectName, releaseName);
			int ddsize=dsize/10+1;
			 /*
			if(dsize>0)
				ddsize= (int) Math.log(dh.getSize(developers.get(i), projectName, releaseName));*/
			// size.add(dsize);

			Node node = new Node(developers.get(i), ddsize);

			nodes.add(node);
		}

		sort(0, nodes.size() - 1);

		int length = nodes.size();
		if (length > 10)
			length = 10;
		
		for (int i = 0; i < length; i++) {
			ArrayList<String> filenames = new ArrayList<String>();
			filenames = files.get(getNodeIndex(nodes.get(i).getName()));

			for (int j = 0; j < length; j++) {
				if (i != j) {

					ArrayList<String> filenamesToCompare = new ArrayList<String>();

					filenamesToCompare = files.get(getNodeIndex(nodes.get(j).getName()));

					if (isLinked(filenames, filenamesToCompare)) {
						Link link = new Link(getNodeIndex(nodes.get(i).getName()), getNodeIndex(nodes.get(j).getName()));
						addLink(link);
					}
				}

			}
		}

		JSONArray jsonNode = JSONArray.fromObject(nodes);
		JSONArray jsonLink = JSONArray.fromObject(links);

		String resultStr = "{ 'nodes': " + jsonNode.toString() + ", 'links':"
				+ jsonLink.toString() + "}";
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();

		// FileWriter fw = null;
		// String destination = path + projectName + "_" + releaseName + "_"
		// + "relations.json";
		//
		// boolean written = false;
		// try {
		// fw = new FileWriter(destination);
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// PrintWriter out = new PrintWriter(fw);
		// out.write(resultStr);
		// out.println();
		// written = true;
		// try {
		// fw.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// out.close();
		//
		// if (written)
		//
		// return destination;
		// else
		return resultStr;

	}

}