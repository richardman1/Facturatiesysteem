package facturatieSysteem.main;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import Shared.KlantManagerIFrmi;
import facturatieSysteem.KlantenSubsysteem.BusinessLayer.KlantManagerImpl;

/**
 * The Class Server.
 */
public class Server {
	// Get a logger instance for the current class
	/** The logger. */
	static Logger logger = Logger.getLogger(Server.class);

	/**
	 * Instantiates a new server.
	 *
	 * @throws RemoteException the remote exception
	 */
	public Server() throws RemoteException {
		logger.debug("Constructor");
		logger.debug("Done");
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {

		String hostname = null;
		
		if(args.length != 1) {
			System.out.println("Usage: Server [hostname]");
			System.exit(1);
		} else {
			hostname = args[0];
		}
		
		// Configure logging. 
		PropertyConfigurator.configure("./resources/FacturatieServer.logconf");
	     
		logger.debug("Starting application ---------------------------------");
		
		System.setProperty("java.rmi.server.hostname", hostname);
		System.setProperty("java.rmi.server.codebase", "http://" + hostname + "/facturatie/bin/");
		System.setProperty("java.security.policy", "http://" + hostname + "/facturatie/resources/FacturatieServer.policy");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
            logger.debug("SecurityManager installed");
		}

		try {
			logger.debug("Creating stub");
			KlantManagerImpl obj = new KlantManagerImpl();
			KlantManagerIFrmi stub = (KlantManagerIFrmi) UnicastRemoteObject.exportObject(obj, 0);

			logger.debug("Locating registry on host '" + hostname + "'");
			Registry registry = LocateRegistry.getRegistry(hostname);
			logger.debug("Trying to register stub using name '" + KlantManagerIFrmi.servicename + "'");
			registry.rebind(KlantManagerIFrmi.servicename, stub);
			logger.debug("Stub registered");

			logger.info("Server ready");
		}
		catch (java.rmi.ConnectException e) {
			logger.error("Could not connect: " + e.getMessage());			
		}
		catch (java.security.AccessControlException e) {
			logger.error("Could not access registry: " + e.getMessage());			
		}
		catch (Exception e) {
			logger.error("Server exception: " + e.toString());
		}
	}	
}
