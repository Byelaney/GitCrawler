package util;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper {
	
	public ArrayList<Integer> PeriodInputXML(String filepath){
		ArrayList<Integer> task_period = new ArrayList<Integer>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        try  
        {  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse(filepath);  
  
            NodeList rateList = doc.getElementsByTagName("rate");  
            //System.out.println("共有" + rateList.getLength() + "个dbRate节点");  
            for (int i = 0; i < rateList.getLength(); i++)  
            {  
                Node rate = rateList.item(i);  
                //Element elem = (Element) rate;  
                //System.out.println("id:" + elem.getAttribute("id"));  
                for (Node node = rate.getFirstChild(); node != null; node = node.getNextSibling())  
                {  
                    if (node.getNodeType() == Node.ELEMENT_NODE)  
                    {  
                        //String name = node.getNodeName();  
                        String value = node.getFirstChild().getNodeValue();  
                        task_period.add(Integer.parseInt(value));
                        //System.out.print(name + ":" + value + "\t");  
                    }  
                }  
                //System.out.println();  
            }  
            
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
		
        return task_period;  
		
	}
	
	
	public ArrayList<String> DBInputXML(String filepath){
		ArrayList<String> configures = new ArrayList<String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        try  
        {  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse(filepath);  
  
            NodeList dbList = doc.getElementsByTagName("DataBase");  
            //System.out.println("共有" + rateList.getLength() + "个dbRate节点");  
            for (int i = 0; i < dbList.getLength(); i++)  
            {  
                Node dataNode = dbList.item(i);  
                //Element elem = (Element) rate;  
                //System.out.println("id:" + elem.getAttribute("id"));  
                for (Node node = dataNode.getFirstChild(); node != null; node = node.getNextSibling())  
                {  
                    if (node.getNodeType() == Node.ELEMENT_NODE)  
                    {  
                        //String name = node.getNodeName();  
                        String value = node.getFirstChild().getNodeValue();  
                        if(value.equals("null"))
                        	value = "";
                        configures.add(value);
                        //System.out.print(name + ":" + value + "\t");  
                    }  
                }  
                //System.out.println();  
            }  
            
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
		
        return configures;  
		
	}
	
}
