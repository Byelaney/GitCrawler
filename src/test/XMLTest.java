package test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.XMLHelper;

public class XMLTest {
	public static void main(String[] args)  
    {  
		XMLHelper xx = new XMLHelper();
		ArrayList<String> s = xx.DBInputXML("DBConfigure.xml");
		for(int i = 0;i<s.size();i++){
			System.out.println(s.get(i));
		}
		
		//System.out.println(a.size());
		
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
//        try  
//        {  
//            DocumentBuilder db = dbf.newDocumentBuilder();  
//            Document doc = db.parse("pet.xml");  
//  
//            NodeList dogList = doc.getElementsByTagName("dog");  
//            System.out.println("共有" + dogList.getLength() + "个dog节点");  
//            for (int i = 0; i < dogList.getLength(); i++)  
//            {  
//                Node dog = dogList.item(i);  
//                Element elem = (Element) dog;  
//                System.out.println("id:" + elem.getAttribute("id"));  
//                for (Node node = dog.getFirstChild(); node != null; node = node.getNextSibling())  
//                {  
//                    if (node.getNodeType() == Node.ELEMENT_NODE)  
//                    {  
//                        String name = node.getNodeName();  
//                        String value = node.getFirstChild().getNodeValue();  
//                        System.out.print(name + ":" + value + "\t");  
//                    }  
//                }  
//                System.out.println();  
//            }  
//        }  
//        catch (Exception e)  
//        {  
//            e.printStackTrace();  
//        }  
    }  
}
