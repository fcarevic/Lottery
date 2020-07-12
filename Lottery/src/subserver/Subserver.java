package subserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import communication.SubserverCommunicator;
import constants.Constants;

public class Subserver{
	private ServerSocket serverSocketForClients, serverForMainServer;
	private String hostMainServer;
	private int portMainServer;
	
	public Subserver(int portClient,int subserverportMainServer, String hostMainServer, int portMainServer) throws IOException {
		serverSocketForClients = new ServerSocket(portClient);
		serverForMainServer= new ServerSocket(subserverportMainServer);
		this.hostMainServer=hostMainServer;
		this.portMainServer=portMainServer;
		new MainServer().start();
		new ClientServer().start();
		
		
		// TODO Auto-generated constructor stub
	}
	
private class MainServer extends Thread{
	@Override
	public void run() {
			while(!interrupted()) {
				try {
					Socket client = serverForMainServer.accept();
					new Thread() {
						@Override
						public void run() {
						 SubserverCommunicator comm = new SubserverCommunicator();
						 try {
							comm.setClientCommunicator(client);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 comm.cashIn();
						
						}
						
					}.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
			}
	
	}
	
	
	
	
}
	
	
	
private class ClientServer extends Thread{
@Override
public void run() {
	
	while(!interrupted()) {
		try {
			Socket client = serverSocketForClients.accept();
			System.out.println("Dobio zahtev");
			new Thread() {
				@Override
				public void run() {
						SubserverCommunicator comm;
						try {
							System.out.println("Obrada zahtev");
							comm = new SubserverCommunicator(client);
							System.out.println("napravio client");
							comm.setMainServerCommunicator(hostMainServer, portMainServer);
							System.out.println("napravio main");
							comm.processClientPaymentRequest();
							System.out.println("Obradio zahtev");
					       
						} catch (IOException e) {
							System.out.println("exception");
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!client.isClosed())
							try {
								client.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
			}.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
		

	}
}

public static void main(String[] args) {
	try {
		Subserver subserver = new Subserver(Constants.SUBSERVER_PORT_TRAFFIC,Constants.SUBSERVER_PORT_MAIN_SERVER, Constants.MAIN_SERVER_IP, Constants.SERVER_PORT_SUBSERVER);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
	
}
