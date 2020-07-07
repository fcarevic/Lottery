package mainserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainServerRemoteInterface extends Remote {
public int sendCombination(String combination, int transactionID,String subserverIp) throws RemoteException;
public int sendCombination(String combination) throws RemoteException;
public int sendAck(int ticketID)  throws RemoteException;
	
  
}
