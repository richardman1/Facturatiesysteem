package facturatieSysteem.LoginSubSysteem.DataStoreLayer;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The Class DAOFactoryLogin.
 */
public class DAOFactoryLogin {
	
	/** The xml path. */
	private String xmlPath ;
	
	/** The xsd path. */
	private String xsdPath;
	
	/** The document. */
	private Document document;
	
	/**
	 * Instantiates a new DAO factory login.
	 *
	 * @param xml the xml
	 * @param xsd the xsd
	 */
	public DAOFactoryLogin(String xml, String xsd){
		this.xmlPath = xml;
		this.xsdPath = xsd;
		
	}
	
	/**
	 * Validate xml.
	 *
	 * @return the document
	 */
	public Document validateXML(){
		try{
			Schema schema = getValidationSchema();
			if (schema == null) {
				System.out.println("Schema file not found or contains errors, XML file not validated!");
				// Here we could decide to cancel initialization of the application.
				// For now, we do not.
				
			} else {
				validateDocument(schema);
			}
			
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
			document = dBuilder.parse(getXMLFile());
			 
		} catch (ParserConfigurationException e){
			e.printStackTrace();
		} catch (SAXException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	
		return document;
	}
	
	/**
	 * Gets the validation schema.
	 *
	 * @return the validation schema
	 */
	public Schema getValidationSchema() {
		Schema schema = null;

		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			System.out.println("getValidationSchema " + xsdPath);
			schema = factory.newSchema(new File(xsdPath));
		} catch (Exception e) {
			System.out.println("Error reading schema file: " + e.getMessage());
		}

		return schema;
	}

	/**
	 * Validate document.
	 *
	 * @param schema the schema
	 * @return true, if successful
	 */
	public boolean validateDocument(Schema schema) {

		System.out.println("validateDocument");

		boolean result = false;

		try {
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlPath));
			result = true;
			System.out.println("Valid XML: " + xmlPath);
		} catch (IOException e) {
			System.out.println("I/O error: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Parse exception: " + e.getMessage());
		}
		return result;

	}
	

	/**
	 * Write document.
	 *
	 * @return true, if successful
	 */
	public boolean writeDocument() {

		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(xmlPath));
			transformer.transform(source, result);
			return true;
		} catch (TransformerConfigurationException e) {
			
		} catch (TransformerException e) {
			
		}
		return false;
	}
	
	
	/**
	 * Gets the XML file.
	 *
	 * @return the XML file
	 */
	public String getXMLFile(){
		return xmlPath;
	}
	
	/**
	 * Gets the document.
	 *
	 * @return the document
	 */
	public Document getDocument(){
		try{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
			document = dBuilder.parse(xmlPath);
		} catch (ParserConfigurationException e){
			e.printStackTrace();
		} catch (SAXException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return document;
	}
}
