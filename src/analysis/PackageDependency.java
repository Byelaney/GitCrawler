package analysis;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.json.JSONArray;
import usefuldata.PackageNode;


/**
 * this class tries to analyze package dependency...
 * @author 
 *
 */
public class PackageDependency {
	private static ArrayList<String> directories = new ArrayList<String>();
	private static List<PackageNode> architectures = new ArrayList<PackageNode>();
	private static ArrayList<String> srcpaths = new ArrayList<String>();
    private static String vison="";
	private static void readZipFile(String file) throws Exception {
		
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		
		ZipEntry ze;

		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				/*
				 * String dir = ze.getName(); String[] dirnames =
				 * dir.split("/"); int l = dirnames.length;
				 * 
				 * if (dirnames[l - 1].equals("src"))
				 * 
				 * { srcpaths.add(dir); }
				 */

			} else {
				String fnp = ze.getName();

				String[] fns = fnp.split("/");
				int l = fns.length;
				String fn = fns[l - 1];

				String[] ftypes = fn.split("\\.");
				String ftype = "";
				if (ftypes.length == 2) {
					ftype = ftypes[1];
				}

				String dir = "";
				if (ftype.equals("java") || ftype.equals("jsp")
						|| ftype.equals("html") || ftype.equals("js")
						|| ftype.equals("c") || ftype.equals("cpp")
						|| ftype.equals("h") || ftype.equals("py")
						|| ftype.equals("rb")) {
					dir = fnp.substring(0, fnp.length() - fn.length());
				}

				if (!srcpaths.contains(dir))

					srcpaths.add(dir);

				// System.out.println(ze.getName());
				// System.out.println("file - " + ze.getName() + " : " +
				// ze.getSize() + " bytes");
				long size = ze.getSize();
				if (size > 0) {

					/*
					 * BufferedReader br = new BufferedReader( new
					 * InputStreamReader(zf.getInputStream(ze))); String line;
					 * while ((line = br.readLine()) != null) {
					 * System.out.println(line); } br.close();
					 */
				}
				// s System.out.println();
			}
		}

		// directories.addAll(srcpaths);

		zin.closeEntry();
		zin.close();
	}

	private static void buildArchitectures() {
		for (String str : srcpaths) {
			String[] fns = str.split("/");
			String dir = "";
			for (int i = 0; i < fns.length - 1; i++) {
				dir += fns[i] + "/";
				if (!directories.contains(dir))
					directories.add(dir);
			}
		}

		for (String str : directories) {
			// System.out.println(str);
			String path = str;
			String[] fns = path.split("/");
			int l = fns.length;
			String fn = fns[l - 1];
			PackageNode pn = new PackageNode(path, fn);
			architectures.add(pn);
		}

		for (int i = 0; i < architectures.size(); i++) {
			PackageNode pn = architectures.get(i);
			String path = pn.getPath();
			String[] fns = path.split("/");

			for (int j = 0; j < architectures.size(); j++) {

				PackageNode pn2 = architectures.get(j);
				String path2 = pn2.getPath();
				if (path2.length() > path.length()) {
					if (findPakage(path2) != -1) {

						String[] fns2 = path2.split("/");

						if (path2.contains(path)
								&& (fns2.length - fns.length) == 1) {
							pn.addChild(j);
						}

						architectures.set(i, pn);
					}
				}
			}
		}
		
		traverse(0);

	}

	@SuppressWarnings("unused")
	private static void architecturesToString() {
		for (PackageNode pn : architectures) {
			System.out.print(pn.getPath());
			System.out.print("  children: ");
			for (Integer j : pn.takeCIndex())
				System.out.print(j+",");

			System.out.println();
		}
	}

	private static void traverse(int i) {
		PackageNode pnc = architectures.get(i);
		if (pnc.takeCIndex() == null) {

		} else {
			for (Integer j : pnc.takeCIndex()) {
				traverse(j);
				PackageNode pnc2 = architectures.get(j);
				pnc.addChild(pnc2);
				architectures.set(i, pnc);
			}
		}

	}

	private static int findPakage(String path) {
		int result = -1;

		for (int i = 0; i < architectures.size(); i++) {
			if (path.equals(architectures.get(i).getPath())) {
				result = i;
				break;
			}

		}

		return result;
	}

	private static String architecturesToJson(String destination) {
		

		JSONArray json = JSONArray.fromObject(architectures.get(0));
		String path="";
	   
		 FileWriter fw = null;
		 vison=directories.get(0).replace("/", "");
		try {
			fw = new FileWriter(destination+vison+".json");
		    path=destination+vison+".json";
		} catch (IOException e) {
			
			e.printStackTrace();
		}
         PrintWriter out = new PrintWriter(fw);
         out.write(json.toString());
         out.println();
         try {
			fw.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
         out.close();
         
         return path;
	}

	public static ArrayList<String> readZipFile(ArrayList<String> files,String destination)
	{
		ArrayList<String> paths=new ArrayList<String>();
		for(String file:files)
		{
			try {
				readZipFile(file);
			} catch (Exception e) {
				
				e.printStackTrace();
			}

			buildArchitectures();
			String path=architecturesToJson(destination);
			if(!path.equals(""))
				{
				paths.add(path);
				};
		}
		return paths;
		
	}
	

}
