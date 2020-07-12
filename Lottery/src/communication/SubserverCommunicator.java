package communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import constants.Constants;
import database.AccountInfo;
import database.Database;

public class SubserverCommunicator {
private Communicator clientCommunicator, mainServerCommunicator;

public SubserverCommunicator(Socket client, String hostMainSever, int portMainServer) throws IOException {
	setClientCommunicator(client);
	}
public SubserverCommunicator(Socket client) throws IOException {
	setClientCommunicator(client);
}
public SubserverCommunicator(){}

public void setClientCommunicator(Socket client) throws IOException {
	this.clientCommunicator= new Communicator(client);
	
}
public void setMainServerCommunicator(String hostMainSever, int portMainServer) throws UnknownHostException, IOException {
	this.mainServerCommunicator=new Communicator(hostMainSever,portMainServer);
	
}

/**
 * @return int - id of transaction or -1 if failure
 * @author CAR
 */

	public void processClientPaymentRequest() {
			String request;
			int transactionID=Constants.STATUS_ERROR;
			int amount=0;
			String accountID="";
			String pin="" ;
			try {
				request = clientCommunicator.getString();
				String [] parts = request.split("#");
				accountID = parts[0];
				pin= parts[1];
			 amount = Integer.parseInt(parts[2]); 
			
			
			 System.out.println(request +" je req");
			AccountInfo account =Database.getInstance().getAccountInfo(accountID, pin);
			System.out.println(account);
			if(account==null) { clientCommunicator.sendInt(Constants.STATUS_ERROR); return;}
			transactionID = account.cashOut(amount);
			System.out.println("ovde 1");
			
			if(Constants.STATUS_ERROR==transactionID){ clientCommunicator.sendInt(Constants.STATUS_ERROR); return;}
			mainServerCommunicator.sendInt(transactionID); 
			System.out.println("ovde 2");
			
			//send transaction id
			int status = mainServerCommunicator.getInt();
			System.out.println("ovde proSAO");
			if(Constants.STATUS_OK!=status) {
				Database.getInstance().getAccountInfo(accountID, pin).cashIn(amount);
				clientCommunicator.sendInt(status);
			}
			else {
				System.out.println("ovde");
			clientCommunicator.sendInt(transactionID);
			System.out.println("ovde2");
			clientCommunicator.getInt();  //await for ack
			System.out.println("ovde3");
			clientCommunicator.sendInt(transactionID); //send ack2
			mainServerCommunicator.sendInt(transactionID);
			}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				if(transactionID!=Constants.STATUS_ERROR ) {
					Database.getInstance().getAccountInfo(accountID, pin).cashIn(amount);
				}
				e.printStackTrace();
				
			}
			
			clientCommunicator.close();
			mainServerCommunicator.close();
		
			
			//razmisli kako da obezbedis da li je sve stiglo
			
			
			
		
	}
	
	
	public void cashIn() {
		try {
			String accountID =clientCommunicator.getString();
			int amount = clientCommunicator.getInt();
			AccountInfo ai = Database.getInstance().getAccountInfo(accountID);
			if(ai!=null) {
				ai.cashIn(amount);
				System.out.println("Uvecao " + accountID+ " za " + amount);
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}




}
