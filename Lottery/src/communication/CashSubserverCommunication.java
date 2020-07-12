package communication;

import java.io.IOException;
import java.net.Socket;

import constants.Constants;
import subservercash.SubserverCash;
import subservercash.SubserverCashMonitor;

public class CashSubserverCommunication {
	private Communicator clientCommunicator;
	private SubserverCashMonitor server;
	
	public CashSubserverCommunication(Socket client, SubserverCashMonitor s) throws IOException {
		clientCommunicator= new Communicator(client);
		server=s;
		// TODO Auto-generated constructor stub
	}
	
	public void communicateWithTraffic() {
		boolean flag=false;
		int ticketID=Constants.STATUS_ERROR, amount= Constants.STATUS_ERROR;
		try {
			 ticketID = clientCommunicator.getInt();
			 amount = server.getAmount(ticketID);
			flag =server.removeWinningTicket(ticketID, amount); 
			clientCommunicator.sendInt(amount);
			clientCommunicator.getInt();
		} catch (IOException | ClassNotFoundException e) {
			if(flag)server.addWinningTicket(ticketID, amount);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void communicateWithMainServer() {
		int ticketID=Constants.STATUS_ERROR, amount=Constants.STATUS_ERROR;
		try {
			 ticketID = clientCommunicator.getInt();
			 amount = clientCommunicator.getInt();
			server.addWinningTicket(ticketID, amount);
			clientCommunicator.sendInt(Constants.STATUS_OK);
            clientCommunicator.close();
		} catch (IOException | ClassNotFoundException e) {
			server.removeWinningTicket(ticketID, amount);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
