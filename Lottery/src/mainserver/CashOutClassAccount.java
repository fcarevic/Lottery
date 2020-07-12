package mainserver;

import java.io.IOException;
import java.net.Socket;

import communication.MainServerCommunicator;

public class CashOutClassAccount extends Thread {
  private String host, accountID;
  private int amount, port, ticketID;
  
  private CashOutSolver solver;
  private Socket socket;
  public CashOutClassAccount(String hostIp, int port, int amount, int ticketID, CashOutSolver server) {
	  this.host=hostIp;
	  this.amount= amount;
	  this.solver=server;
	  this.ticketID=ticketID;
	  this.port=port;
	  this.accountID=accountID;
	  setDaemon(true);
	// TODO Auto-generated constructor stub
  }
  
  public void setAccountID(String accId) {
	  this.accountID=accId;
  }
  
  
  public void run() {
	 
	  while(true) {
	   try {
		   System.out.println("Pokusava da uveca");
		socket = new Socket(host, port);
	   MainServerCommunicator comm = new MainServerCommunicator(socket, solver.getMainServer());
	   if(accountID!=null) {
	   if(comm.cashOutAccount(accountID, amount)) { System.out.println("uvecao " + accountID + " za " + amount ); break;}
	   }
	   /*else
	   if(comm.cashOutCah(ticketID, amount))break;*/
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
	  solver.removeWinner(this);
	  
	  
  }
  
  public String toString() {
	  return host+"#" + port + "#" + amount + "#" + ticketID +"#"+ (accountID!=null? accountID:'*'); 
	  
  }
}
