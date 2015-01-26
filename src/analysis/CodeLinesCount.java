package analysis;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * this class tries to analyze source code
 * from a zip file
 * return lines of code
 */
public class CodeLinesCount {
	static private int codeLines = 0;

	private static void readZipFile(String file) throws Exception {
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;

		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				
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

				
				if (ftype.equals("java") || ftype.equals("jsp")
						|| ftype.equals("html") || ftype.equals("js")
						|| ftype.equals("c") || ftype.equals("cpp")
						|| ftype.equals("h") || ftype.equals("py")
						|| ftype.equals("rb")|| ftype.equals("php")) {
					

					BufferedReader br = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));
					
					while ((br.readLine()) != null) {
						codeLines++;
					}
					br.close();
				}

				
				long size = ze.getSize();
				if (size > 0) {

					
				}
				
			}
		}

		

		zin.closeEntry();
		zin.close();
		zf.close();
	}
	
	public static int getCodeLines(String file)
	{
		try {
			readZipFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 return codeLines;
	}
	
	public int StatisticCodeLines(String path,String fileName){
		int clcount=0;
		clcount=CodeLinesCount.getCodeLines(path + fileName);
		return clcount;
	}

}

