package communication;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import constants.Constants;
import mainserver.MainServer;

public class MainServerCommunicator {
	private Communicator clientCommunicator;
	private MainServer server;
	public MainServerCommunicator(Socket client, MainServer server) throws IOException {
		clientCommunicator = new Communicator(client);
		this.server=server;
		// TODO Auto-generated constructor stub
	}
	
	public boolean paymentViaAccountSubserver() {
		int transactionID=-1; 
		try {
			transactionID= clientCommunicator.getInt();
			if(server.getCurrentlyActive()) {
			server.addTransaction(transactionID);
			clientCommunicator.sendInt(Constants.STATUS_OK);
			clientCommunicator.getInt();
			} else clientCommunicator.sendInt(Constants.STATUS_ERROR);
			return true;
		} catch (IOException e) {
			server.removeTransaction(transactionID);
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	/*public boolean paymentViaAccountTraffic() {
		List<Integer[]> combinations=null;
		int id=-1;
		
		try {
			int transactionID = clientCommunicator.getInt();
			//checkExisting
			if(server.checkExistsTransactions(transactionID)) {
				
				clientCommunicator.sendInt(0);
				String combinationString = clientCommunicator.getString();
				combinations = getCombinations(combinationString);
				id = server.getNextTicketID();
				server.addCombination(id, combinations);
				clientCommunicator.sendInt(id);
				clientCommunicator.getInt();
				return true;
			} else clientCommunicator.sendInt(-1);
			
		} catch (IOException | ClassNotFoundException e) {
			if(id!=-1) server.removeCombination(id);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
			
			
		
		
		
	}
	
	public boolean paymentViaCashTraffic() {
		int id=-1;
		String combinationString;
		try {
			combinationString = clientCommunicator.getString();
		List<Integer[]> combinations = getCombinations(combinationString);
		id= server.getNextTicketID();
		server.addCombination(id, combinations);
		clientCommunicator.sendInt(id);
		clientCommunicator.getInt();
		return true;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			if(id!=-1)server.removeCombination(id);
			e.printStackTrace();
		}
	  return false;
	}
	
	*/
	
	public boolean cashOutAccount(String account, int amount) throws IOException {
		clientCommunicator.sendString(account);
		clientCommunicator.sendInt(amount);
		return true;
		
		
	}
	public boolean cashOutCah(int ticketID, int amount) throws IOException {
		clientCommunicator.sendInt(ticketID);
		clientCommunicator.sendInt(amount);
		return true;
		
		
	}
	
	
	private List<Integer[]>  getCombinations(String combs) {
		LinkedList<Integer[]> list = new LinkedList<Integer[]>();
		LinkedList<Integer> allnums= new LinkedList<Integer>();
		
		String[] allcombs = combs.split("#");
		for(String comb : allcombs) {
			String[] nums = comb.split(",");
			for(String num:nums) {
				allnums.add(Integer.parseInt(num));
			}
			list.add((Integer[])allnums.toArray());
			allnums.clear();
		}
		
		return list;
		
		
	}
	

}
