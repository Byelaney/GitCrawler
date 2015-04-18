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

public class CodeLinesCountImpl implements CodeLinesCount {
	static private int codeLines = 0;

	
	private static void readZipFile(String file) throws Exception {
		codeLines=0;
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

				String[] languagetype = { "java", "1", "js", "sh", "rb", "cmp",
						"eps", "perl", "pl", "pm", "html", "php", "css", "bcp",
						"fmk", "asi", "bcp", "c++", "cc", "cls", "cpp", "crf",
						"cxx", "dbg", "dpr", "dsk", "h", "hpp", "hxx", "py",
						"pyc", "pyw", "pyo", "pyd", "r", "R", "c", "m", "mm",
						"o", "xslt", "xsl", "cjl", "asm", "m" ,"yml"};
				for (int i = 0; i < languagetype.length; i++) {
					if (ftype.equals(languagetype[i])) {
						findLanguageType = true;
				//		System.out.println(fnp);
						break;
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
		// zin.closeEntry();
	}

	public int getCodeLines(String file) {
		try {
			readZipFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return codeLines;
	}

}
