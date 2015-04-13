package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileHelper {
	
	public static void readTxtFile(String filePath){
        try {
                String encoding="utf-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        System.out.println(lineTxt);
                    }
                    read.close();
        }else{
            System.out.println("file not found");
        }
        } catch (Exception e) {
            System.out.println("failure to read");
            e.printStackTrace();
        }
     
    }
   
	 /** 
	  * delete dir and files within
	  *  
	  * @param dir 
	  *            
	  * @return 
	  */  
	 public static boolean deleteDirectory(String dir) {  
	   
	  if (!dir.endsWith(File.separator))  
	   dir = dir + File.separator;  
	  File dirFile = new File(dir);  
	  
	  if ((!dirFile.exists()) || (!dirFile.isDirectory())) {  
	   System.out.println("delete failure：" + dir + "not exist！");  
	   return false;  
	  }  
	  boolean flag = true;  
	  
	  File[] files = dirFile.listFiles();  
	  for (int i = 0; i < files.length; i++) {  
	   
	   if (files[i].isFile()) {  
	    flag = FileHelper.deleteFile(files[i].getAbsolutePath());  
	    if (!flag)  
	     break;  
	   }  
	    
	   else if (files[i].isDirectory()) {  
	    flag = FileHelper.deleteDirectory(files[i]  
	      .getAbsolutePath());  
	    if (!flag)  
	     break;  
	   }  
	  }  
	  if (!flag) {  
	   System.out.println("delete dir failure");  
	   return false;  
	  }  
	  
	  if (dirFile.delete()) {  
	   //System.out.println("delete" + dir + " succeed");  
	   return true;  
	  } else {  
	   return false;  
	  }  
	 }  
	
	 /** 
	  * delete single file
	  *  
	  * @param fileName 
	  *            
	  * @return 
	  */  
	 public static boolean deleteFile(String fileName) {  
	  File file = new File(fileName);  
	   
	  if (file.exists() && file.isFile()) {  
	   if (file.delete()) {  
	    //System.out.println("delete single file" + fileName + " succeed");  
	    return true;  
	   } else {  
	    System.out.println("delete single file" + fileName + " failure");  
	    return false;  
	   }  
	  } else {  
	   System.out.println("delete single file" + fileName + " not exist");  
	   return false;  
	  }  
	 }  
	 
	 public static void main(String argv[]){
	        String filePath = "integer.txt";
	        readTxtFile(filePath);
	    }
	
}
