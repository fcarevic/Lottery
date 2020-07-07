package mainserver;

import java.rmi.RemoteException;
import java.util.List;

import constants.Constants;

public class MainServerRemoteImplementation implements MainServerRemoteInterface{
			MainServer server;
			public MainServerRemoteImplementation(MainServer server) {
				this.server=server;
				// TODO Auto-generated constructor stub
			}
	
			public int sendCombination(String combination, int transactionID, String subserverIp) throws RemoteException{
				if(server.checkExistsTransactions(transactionID)) {
				List<Integer[]> comb = server.getCombinations(combination);
				int ticketID= server.getNextTicketID();
				server.addCombination(ticketID, comb,transactionID,subserverIp);
				server.addUnconfirmedTicket(ticketID);
				return ticketID;
				} else return Constants.STATUS_ERROR;
			}
			public int sendCombination(String combination) throws RemoteException{
				List<Integer[]> comb = server.getCombinations(combination);
				int ticketID= server.getNextTicketID();
				server.addCombination(ticketID, comb);
				server.addUnconfirmedTicket(ticketID);
				return ticketID;
			}
			
			public int sendAck(int ticketID)throws RemoteException {
			  server.removeUnconfirmedTicket(ticketID);
			  return Constants.STATUS_OK;
			}
			
			
}
