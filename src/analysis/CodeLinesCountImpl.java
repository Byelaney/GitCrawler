package analysis;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * this class tries to analyze source code
 * from a zip file
 * return lines of code
 */
public class CodeLinesCountImpl implements CodeLinesCount{
	static private int codeLines = 0;
	
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

				if (findLanguageType) {

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
				// s System.out.println();
			}
		}

		zf.close();
		zin.close();
		//zin.closeEntry();
	}

	public int getCodeLines(String file, ArrayList<String> languages) {
		try {
			readZipFile(file, languages);
		} catch (Exception e) {			
			e.printStackTrace();
		}

		return codeLines;
	}


}

