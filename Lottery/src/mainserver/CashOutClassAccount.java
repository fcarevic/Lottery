package mainserver;

import java.io.IOException;
import java.net.Socket;

import communication.MainServerCommunicator;

public class CashOutClassAccount extends Thread {
  private String host, accountID;
  private int amount, port;
  private MainServer server;
  private Socket socket;
  public CashOutClassAccount(String hostIp, int port, int amount, String accountID, MainServer server) {
	  this.host=hostIp;
	  this.amount= amount;
	  this.server=server;
	  this.port=port;
	  this.accountID=accountID;
	  setDaemon(true);
	// TODO Auto-generated constructor stub
  }
  
  
  public void run() {
	 
	  while(true) {
	   try {
		socket = new Socket(host, port);
	   MainServerCommunicator comm = new MainServerCommunicator(socket, server);
	   if(comm.cashOutAccount(accountID, amount)) break;
	   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	  
		try {
			Thread.sleep((long) (Math.random()*2000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
	  }
	  
	  
  }
}
