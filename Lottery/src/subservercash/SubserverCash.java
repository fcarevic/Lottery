package subservercash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

import communication.CashSubserverCommunication;
import constants.Constants;

public class SubserverCash {
	private  ServerSocket clientServer;
	private ServerSocket mainServer;
	private SubserverCashMonitor monitor;
	
	SubserverCash(int portClientServer, int portMainServer) throws IOException{
		monitor= new SubserverCashMonitor();
		clientServer= new ServerSocket(portClientServer);
		mainServer = new ServerSocket(portMainServer);
		new MainServer().start();
		System.out.println("Main server started");
		 
		new ClientServer().start();
		System.out.println("Client subserver fully started");
		 
	
	}
	
	private class MainServer extends Thread{
		
		@Override
		public void run() {
			while(!interrupted()) {
				try {
					Socket client = mainServer.accept();
					new Thread() {
						@Override
						public void run() {
						  try {
							CashSubserverCommunication comm = new CashSubserverCommunication(client, monitor);
							comm.communicateWithMainServer();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  
						
						}
						
						
					}.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		}
	}
	
private class ClientServer extends Thread{
		
		@Override
		public void run() {
			while(!interrupted()) {
				try {
					Socket client = clientServer.accept();
					new Thread() {
						@Override
						public void run() {
						  try {
							CashSubserverCommunication comm = new CashSubserverCommunication(client, monitor);
							comm.communicateWithTraffic();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  
						
						}
						
						
					}.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		}
	}
	
	
  public static void main(String[] args) {
	  
	  try {
		SubserverCash subserverCash = new SubserverCash(Constants.CASH_SUBSERVER_TRAFFIC_PORT, Constants.CASH_SUBSERVER_SERVER_PORT);
		System.out.println("Cash subserver fully started");
	  
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
  }

}
