package communication;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import pdfcreator.PdfWriter;

public class TrafficCommunicator {
	private static final int STATUS_OK=0;
	
	private Communicator subserverCommunicator, mainserverCommunicator;
	
	public TrafficCommunicator(String hostMainServer, int portMainServer,String hostSubserver, int portSubserver) throws UnknownHostException, IOException {
		subserverCommunicator= new Communicator(hostSubserver, portSubserver);
		mainserverCommunicator= new Communicator(hostMainServer, portMainServer);
		// TODO Auto-generated constructor stub
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
					
					
					mainserverCommunicator.sendInt(transactionID);
					if(STATUS_OK!=mainserverCommunicator.getInt()) { System.out.println("Status not ok"); return false;}
					
					sendCombinationToMainServer(combinationList);
					int ticketID= mainserverCommunicator.getInt();
					
					PdfWriter.createPDF("Ticket"+ticketID+".pdf", combinationList, ticketID);
				    mainserverCommunicator.sendInt(STATUS_OK);
					return true;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						subserverCommunicator.close();
					}
					
					return false;
	}

	
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
		
	}
	public boolean payViaCash(LinkedList<Integer[]> combinations) {
		  try {
			sendCombinationToMainServer(combinations);
			int ticketID = mainserverCommunicator.getInt();
			if(ticketID!=-1) PdfWriter.createPDF("Ticket" + ticketID, combinations, ticketID);
			mainserverCommunicator.sendInt(STATUS_OK); // sta ako ovde pukne??
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return false;
	}
	
	

}
