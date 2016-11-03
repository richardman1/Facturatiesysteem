package facturatieSysteem.VerzekeringSubsysteem.DataStoreLayer;

import java.util.ArrayList;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Class BehandelDAOImpl.
 */
public class BehandelDAOImpl implements BehandelDAO {
	
	/** The document. */
	private Document document;
	
	/** The dao factory. */
	private DAOFactoryVerzekering daoFactory = new DAOFactoryVerzekering("XML/behandelcodes.xml","XML/behandelcodes.xsd");
	
	/**
	 * Gets the behandelcodes.
	 *
	 * @return the behandelcodes
	 */
	@Override
	public ArrayList<String> getBehandelcodes(){
		ArrayList<String> behandelcodes = new ArrayList<>();
		document = daoFactory.getDocument();
		try {
			Element codesElement = (Element) document.getElementsByTagName("behandelcodes").item(0);
			NodeList codes = codesElement.getElementsByTagName("behandeling");
			for (int i = 0; i < codes.getLength(); i++) {
				
				Element behandelingElement = (Element) codes.item(i);
				String behandelcode = behandelingElement.getAttribute("behandelcode");
				behandelcodes.add(behandelcode);
			}
		} catch (DOMException e) {
			e.printStackTrace();
		}
		return behandelcodes;
	}
}
