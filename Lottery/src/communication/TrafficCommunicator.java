package communication;

import java.io.IOException;
import java.net.UnknownHostException;

public class TrafficCommunicator {
	
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
	public boolean payViaAccount(String accountId,String pin, int amount )  {
					String message = accountId + "#" + pin + "#" + amount;
					try {
						subserverCommunicator.sendString(message);
					
					int transactionID= subserverCommunicator.getInt();
					if(transactionID==-1)return false;
					subserverCommunicator.sendInt(transactionID);
					subserverCommunicator.getInt();
					
					mainserverCommunicator.sendInt(transactionID);
					mainserverCommunicator.getInt();
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
	
	

}
