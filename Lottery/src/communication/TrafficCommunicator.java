package communication;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import constants.Constants;
import mainserver.MainServerRemoteInterface;
import pdfcreator.PdfWriter;

public class TrafficCommunicator {
	private static final int STATUS_OK=0;
	
	private static Communicator subserverCommunicator;
	private static MainServerRemoteInterface mainserverCommunicator;
	private static Registry register;
	private String hostSubserver;
	
	public TrafficCommunicator(String hostMainServer, int portMainServer,String hostSubserver, int portSubserver) throws UnknownHostException, IOException, NotBoundException {
		setSubserver(hostSubserver, portSubserver);
		setMainServer(hostMainServer, portMainServer);
			// TODO Auto-generated constructor stub
	}
	
	
	public TrafficCommunicator() {}
	public void setSubserver(String hostSubserver, int portSubserver ) throws UnknownHostException, IOException {
		subserverCommunicator= new Communicator(hostSubserver, portSubserver);
		this.hostSubserver=hostSubserver;
	
	}
	
	public void setMainServer(String hostMainServer, int portMainServer) throws RemoteException, NotBoundException {
		
		 register= LocateRegistry.getRegistry(hostMainServer,portMainServer);
		mainserverCommunicator= (MainServerRemoteInterface) register.lookup(Constants.MAIN_SERVER_RMI_NAME);
		
		System.out.println("Dodao remte inter" + mainserverCommunicator);
	
		
	}
	
	
	/**
	 * @param 
	 * @return id of transaction, or -1 if failure
	 * @author CAR
	 * @throws IOException 
	 */
	public boolean payViaAccount(String accountId,String pin, int amount, LinkedList<Integer[]> combinationList )  {
					String message = accountId + "#" + pin + "#" + amount;
					try {
						subserverCommunicator.sendString(message);
					
					int transactionID= subserverCommunicator.getInt();
					if(transactionID==-1)return false;
					subserverCommunicator.sendInt(transactionID);
					subserverCommunicator.getInt();
					
					
					int ticketID=mainserverCommunicator.sendCombination(generateStringFromCombination(combinationList), transactionID, hostSubserver);
					if(ticketID!=-1) {
						PdfWriter.createPDF("Ticket"+ticketID+".pdf", combinationList, ticketID);
						mainserverCommunicator.sendAck(ticketID);
					}
					
					
				/*	mainserverCommunicator.sendInt(transactionID);
					if(STATUS_OK!=mainserverCommunicator.getInt()) { System.out.println("Status not ok"); return false;}
					
					sendCombinationToMainServer(combinationList);
					int ticketID= mainserverCommunicator.getInt();
					
					PdfWriter.createPDF("Ticket"+ticketID+".pdf", combinationList, ticketID);
				    mainserverCommunicator.sendInt(STATUS_OK);
					return true;
*/
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						subserverCommunicator.close();
					}
					
					return false;
	}

	private String generateStringFromCombination(LinkedList<Integer[]> combinations ) {
		StringBuilder sb = new StringBuilder();
		for(Integer[] comb : combinations) {
			for(int i=0; i< comb.length;i++) {
				sb.append(comb[i]);
				if(i!=comb.length-1)sb.append(',');
				
			}
			sb.append('#');
		}
		sb.deleteCharAt(sb.lastIndexOf("#"));
		
		return sb.toString();
		
		
	}
	/*
	private boolean sendCombinationToMainServer(LinkedList<Integer[]> combinations ) throws IOException {

		StringBuilder sb = new StringBuilder();
		for(Integer[] comb : combinations) {
			for(int i=0; i< comb.length;i++) {
				sb.append(comb[i]);
				if(i!=comb.length-1)sb.append(',');
				
			}
			sb.append('#');
			sb.deleteCharAt(sb.lastIndexOf("#"));
		}
		
		mainserverCommunicator.sendString(sb.toString());
		return true;
		
	}*/
	public int payViaCash(LinkedList<Integer[]> combinationList) {
		  try {
			  String combs = generateStringFromCombination(combinationList);
			  System.out.println(combs);
				int ticketID=mainserverCommunicator.sendCombination(combs);
				if(ticketID!=Constants.STATUS_ERROR && ticketID!=Constants.STATUS_CLOSED) {
					PdfWriter.createPDF("Ticket"+ticketID+".pdf", combinationList, ticketID);
					mainserverCommunicator.sendAck(ticketID);
				}
				} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return Constants.STATUS_ERROR;
	}
	
	
	public int cashSubserverCommunication(int ticketID) {
		
		try {
			subserverCommunicator.sendInt(ticketID);
			int amount = subserverCommunicator.getInt();
			subserverCommunicator.sendInt(Constants.STATUS_OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
		
		
	}
	

}
