package communication;

import java.io.IOException;
import java.net.Socket;

import database.AccountInfo;
import database.Database;

public class SubserverCommunicator {
private Communicator clientCommunicator, mainServerCommunicator;

public SubserverCommunicator(Socket client, String hostMainSever, int portMainServer) throws IOException {
	this.clientCommunicator= new Communicator(client);
	this.mainServerCommunicator=new Communicator(hostMainSever,portMainServer);
	}

/**
 * @return int - id of transaction or -1 if failure
 * @author CAR
 */

	public void processClientPaymentRequest() {
			String request;
			int transactionID=-1;
			int amount=0;
			String accountID="";
			String pin="" ;
			try {
				request = clientCommunicator.getString();
				String [] parts = request.split("#");
				accountID = parts[0];
				pin= parts[1];
			 amount = Integer.parseInt(parts[2]); 
			
			
			
			AccountInfo account =Database.getInstance().getAccountInfo(accountID, pin);
			if(account==null) { clientCommunicator.sendInt(-1); return;}
			transactionID = account.cashOut(amount);
			if(-1==transactionID){ clientCommunicator.sendInt(-1); return;}
			clientCommunicator.sendInt(transactionID);
			mainServerCommunicator.sendInt(transactionID); //send transaction id
		
			clientCommunicator.getInt();  //await for ack
			mainServerCommunicator.getInt();
			
			clientCommunicator.sendInt(transactionID); //send ack2
			mainServerCommunicator.sendInt(transactionID);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				if(transactionID!=-1 ) {
					Database.getInstance().getAccountInfo(accountID, pin).cashIn(amount);
				}
				e.printStackTrace();
				
			}
			
			clientCommunicator.close();
			mainServerCommunicator.close();
		
			
			//razmisli kako da obezbedis da li je sve stiglo
			
			
			
		
	}




}
