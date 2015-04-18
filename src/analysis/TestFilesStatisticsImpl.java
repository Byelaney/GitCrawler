package analysis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TestFilesStatisticsImpl implements TestFilesStatistics
{

	private static int count=0;
	@Override
	public int getTestFilesCount(String filePath) {

		try {
			readZipFile(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	private static void readZipFile(String file) throws Exception {
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
        count=0;

		while ((ze = zin.getNextEntry()) != null) {
			if (!ze.isDirectory()) {
				String fnp = ze.getName();
				if(fnp.indexOf("test")!=-1)
				{
					count++;
				}

			}

		}
		
		zin.close();
		zf.close();
	}

}
