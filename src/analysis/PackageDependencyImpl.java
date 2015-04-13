package analysis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import net.sf.json.JSONArray;
import usefuldata.PackageNode;

public class PackageDependencyImpl implements PackageDependency {
	private static ArrayList<String> directories = new ArrayList<String>();
	private static List<PackageNode> architectures = new ArrayList<PackageNode>();
	private static ArrayList<String> srcpaths = new ArrayList<String>();

	
	private static void readZipFile(String file, ArrayList<String> languages)
			throws Exception {

		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);

		ZipEntry ze;

		while ((ze = zin.getNextEntry()) != null) {
			if (!ze.isDirectory()) {

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

				boolean findLanguageType = false;

				for (String language : languages) {

					if (!language.equals("objective-c")) {
						String lan = "";
						for (int i = 0; i < language.length(); i++) {
							lan = lan
									+ language.substring(i, i + 1)
											.toLowerCase();
						}
						if (ftype.equals(lan))

							findLanguageType = true;
					} else {
						if (ftype.equals("c") || ftype.equals("cc")
								|| ftype.equals("ccp") || ftype.equals("h")
								|| ftype.equals("m") || ftype.equals("mm")
								|| ftype.equals("o")) {
							findLanguageType = true;
						}
					}

				}

				if (findLanguageType)

				{
					dir = fnp.substring(0, fnp.length() - fn.length());
				}

				if (!srcpaths.contains(dir))

					srcpaths.add(dir);

			}

		}

		// directories.addAll(srcpaths);
		zf.close();
		zin.close();
		//zin.closeEntry();
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
			String path = pn.takePath();
			String[] fns = path.split("/");

			for (int j = 0; j < architectures.size(); j++) {

				PackageNode pn2 = architectures.get(j);
				String path2 = pn2.takePath();
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

	
//	private static void architecturesToString() {
//		for (PackageNode pn : architectures) {
//			System.out.print(pn.takePath());
//			System.out.print("  children: ");
//			for (Integer j : pn.takeCIndex())
//				System.out.print(j + ",");
//
//			System.out.println();
//		}
//	}

	private static void traverse(int i) {
		if(i < architectures.size()){
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
		
	}

	private static int findPakage(String path) {
		int result = -1;

		for (int i = 0; i < architectures.size(); i++) {
			if (path.equals(architectures.get(i).takePath())) {
				result = i;
				break;
			}

		}

		return result;
	}

	private static String architecturesToJson() {
		// String json=JSONValue.toJSONString(architectures);

		if(architectures.size() !=0){
			JSONArray json = JSONArray.fromObject(architectures.get(0));
			String resultStr = json.toString();
			directories = new ArrayList<String>();
			architectures = new ArrayList<PackageNode>();
			srcpaths = new ArrayList<String>();
			return resultStr.substring(1, resultStr.length() - 1);
		}		
		
		 return "";
	}

	public ArrayList<String> getPakageDependency(ArrayList<String> files,
			ArrayList<String> languages) {

		ArrayList<String> jsonStrs = new ArrayList<String>();

		for (String file : files) {
			try {
				if (!file.equals(""))
					readZipFile(file, languages);
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!file.equals("")) {
				buildArchitectures();

				String jsonStr = architecturesToJson();

				jsonStrs.add(jsonStr);
			}

		}

		return jsonStrs;

	}

	@Override
	public ArrayList<String> getMainPakageDependency(ArrayList<String> files,
			ArrayList<String> languages) {
		// TODO Auto-generated method stub

		return null;
	}



}
