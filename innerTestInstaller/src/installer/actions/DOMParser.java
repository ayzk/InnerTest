package installer.actions;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class DOMParser { 
	   DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
	   //Load and parse XML file into DOM 
	   public Document parse(String filePath) { 
	      Document document = null; 
	      try { 
	         //DOM parser instance 
	         DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
	         //parse an XML file into a DOM tree 
	         document = builder.parse(new File(filePath)); 
	      } catch (ParserConfigurationException e) { 
	         e.printStackTrace();  
	      } catch (SAXException e) { 
	         e.printStackTrace(); 
	      } catch (IOException e) { 
	         e.printStackTrace(); 
	      } 
	      return document; 
	   } 
}