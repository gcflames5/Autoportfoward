package net.njay.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

public class UPNPManager {

	private int port;
	private GatewayDevice d;
	
	public UPNPManager(int port){
		this.port = port;
	}
	
	public GatewayDevice discover() throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException{
		GatewayDiscover discover = new GatewayDiscover();
		System.out.println("Looking for Gateway Devices");
		discover.discover();
		this.d = discover.getValidGateway();
		return this.d;
	}
	
	public boolean map() throws IOException, SAXException{
		if (d != null) {
		    System.out.println("Gateway device found.\n + " + new Object[]{d.getModelName() + d.getModelDescription()});
		} else {
		    System.out.println("No valid gateway device found.");
		    return false;
		}

		InetAddress localAddress = d.getLocalAddress();
		System.out.println("Using local address: " + localAddress);
		String externalIPAddress = d.getExternalIPAddress();
		System.out.println("External address: " + externalIPAddress);
		PortMappingEntry portMapping = new PortMappingEntry();

		System.out.println("Attempting to map port {0} " + port);
		System.out.println("Querying device to see if mapping for port {0} already exists " + port);

		if (!d.getSpecificPortMappingEntry(port,"TCP",portMapping)) {
		    System.out.println("Sending port mapping request");

		    if (d.addPortMapping(port,port,localAddress.getHostAddress(),"TCP","test")) {
		        System.out.println("Mapping succesful!");
		        return true;
		 
		    } else {
		        System.out.println("Port mapping removal failed");
		        return false;
		    }
		    
		} else {
		    System.out.println("Port was already mapped. Deleting and trying again!");
		    d.deletePortMapping(port, "TCP");
		    return map();
		}
	}
	
	public void remove() throws IOException, SAXException{
	      d.deletePortMapping(port,"TCP");
	      System.out.println("Port mapping removed");

	}

}
