package mainserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import communication.MainServerCommunicator;
import constants.Constants;


public class MainServer {
			
		private static final String COMB_FILE="allcombsMainServer.txt";
		private static final String TRANS_FILE="alltransMainServer.txt";
		private static final String UNCASH_WINN_FILE="allUncashWinnersMainServer.txt";
	
			private ServerSocket serverForAccountSubserver;
			private Map<Integer,Combinations> allcombinations;
			private volatile boolean currentlyActive;
			private List<Integer> allTransactions;
			private volatile int ticketID;
			private List<Integer> unconfirmedTickets;
			private ConcurrentLinkedQueue<CashOutClassAccount> uncashedWinningCombinations;
			private Integer[] winningCombination;
			
			private CashOutSolver winnersSolver;
			private String cashSubserverIP;
			public MainServer(int portForAccountSubserver, String cashSubserverIp) throws IOException {
				this.winnersSolver=new CashOutSolver(this);
				this.cashSubserverIP=cashSubserverIp;
					serverForAccountSubserver= new ServerSocket(portForAccountSubserver);
				readCombinationsFromFile(COMB_FILE);
				readTransactonsFromFile(TRANS_FILE);
				if(!allcombinations.isEmpty()) {
					ticketID= allcombinations.entrySet().size()+1;
				}
				unconfirmedTickets= new LinkedList<Integer>();
				new ServerForMainAccountSubserver().start();
				
			}
			
			
			
			
			public void removeUncashedWinner(CashOutClassAccount comb) {
				uncashedWinningCombinations.remove(comb);
			}
			public void declareWinners() {
				allcombinations.forEach(new BiConsumer<Integer, Combinations>() {

					@Override
					public void accept(Integer t, Combinations u) {
						// TODO Auto-generated method stub
						int amount=0;
						for( Integer[] comb:u.getCombinations()) {
							int num_matched = countMatched(comb);
							amount+=Constants.PRICE_MATCHED[num_matched];
						}
					//TODO dodaj u slucaju greske koji nisu sve isplaceni!!!!!!!!!!!
						CashOutClassAccount ca = new CashOutClassAccount(u.getSubeserverIp(), Constants.SUBSERVER_PORT_MAIN_SERVER, amount,u.getTicketID() , winnersSolver);
						if(u.getAccountID()!=null)ca.setAccountID(u.getAccountID());
						 winnersSolver.addWinner(ca);
					}
					
				});
				
				
			}
			
			private int countMatched(Integer[] comb) {
				int sum=0;
				for(int i : winningCombination) {
					for(int j : comb)
						if(j==i)sum++;
				}
				return sum;
			}
			
			public void setWinningCombiantion(Integer[] comb) {
				this.winningCombination=comb;
			}
			
			public synchronized void addUnconfirmedTicket(int ticketID) {
				
				 unconfirmedTickets.add(ticketID);
			}
			public synchronized void removeUnconfirmedTicket(int ticketID) {
				
				 unconfirmedTickets.removeIf(new Predicate<Integer>() {

					@Override
					public boolean test(Integer t) {
						return t==ticketID;
					}
				});
			}
				public boolean getCurrentlyActive() {
					return currentlyActive;
					
				}
				
				public boolean setCurrentlyActive(boolean active) {
					return currentlyActive=active;
					
				}
				
			
			public synchronized boolean checkExistsTransactions(int transactionID) {
				return allTransactions.contains(transactionID);
				
			}
			public synchronized boolean addTransaction(int transactionID) {
				if(checkExistsTransactions(transactionID))
				return false;
				else allTransactions.add(transactionID);
				saveTransactionsToFile();
				return true;
			}
			
			public synchronized boolean removeTransaction(int transactionID) {
				if(!checkExistsTransactions(transactionID))
				return false;
				else allTransactions.remove(allTransactions.indexOf(transactionID));
				saveTransactionsToFile();
				return true;
			}
			
			public synchronized int getNextTicketID() {
				return ticketID++;
			}
			public synchronized void addCombination(int ticketID, List<Integer[]> comb, int transactionID, String subserverIp,String accountID) {
				Combinations combination = new Combinations();
				combination.setTicketID(ticketID);
				combination.setCombinations(comb);
				combination.setTransactionID(transactionID);
				combination.setSubeserverIp(subserverIp);
				combination.setAccountID(accountID);
				allcombinations.put(ticketID, combination);
				saveCombinationsToFile();
				
			}
			public synchronized void addCombination(int ticketID, List<Integer[]> comb) {
				Combinations combination= new Combinations();
				combination.setTicketID(ticketID);
				combination.setCombinations(comb);
				combination.setSubeserverIp(Constants.CASH_SUBSERVER_IP);
				allcombinations.put(ticketID, combination);
				saveCombinationsToFile();
				
			}
			public synchronized void removeCombination(int ticketID) {
				allcombinations.remove(ticketID);
				saveCombinationsToFile();
				
			}
			
			
			
			public synchronized void saveCombinationsToFile() {
				String filename= COMB_FILE;
				File file = new File(filename);
				try {
					PrintWriter out = new PrintWriter(file);
					allcombinations.forEach(new BiConsumer<Integer, Combinations>() {

						@Override
						public void accept(Integer t, Combinations u) {
							out.write(u.toSting()+"\n");
						
							
						}
						
					});
					out.close();
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			public synchronized void saveTransactionsToFile() {
				String filename=TRANS_FILE;
				File file = new File(filename);
				try {
					PrintWriter out = new PrintWriter(file);
					for(Integer id :allTransactions) {
						out.write(id + " ");
						
					}
				out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			private void readTransactonsFromFile(String filename) {
				allTransactions = new LinkedList<Integer>();
				File file = new File(filename);
				try {
					Scanner scanner = new Scanner(file);
					while(scanner.hasNextInt()) {
						Integer id = scanner.nextInt();
						allTransactions.add(id);
					}
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			}
			
			
			
			
			
			
			
			private void readCombinationsFromFile(String filepath) {
				allcombinations= new HashMap<Integer, Combinations>();
				
				
				File file = new File(filepath);
				Scanner scanner;
				try {
					scanner = new Scanner(file);
					while(scanner.hasNextLine()) {
						String line = scanner.nextLine();
						Combinations comb = Combinations.parseCombination(line);
						allcombinations.put(comb.getTicketID(), comb);
					}
						
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
		/*	private class WorkerServer extends Thread {
				public void run() {
					try {
						ServerSocket server  = new ServerSocket(Constants.SERVER_PORT_TRAFFIC);
				
							while(!interrupted()) {
								Socket client = server.accept();
								
					
					
					
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			}*/
			public   List<Integer[]>  getCombinations(String combs) {
				System.out.println(combs);
					LinkedList<Integer[]> list = new LinkedList<Integer[]>();
					Integer[] allnums= new Integer[Constants.NUMBER_OF_NUMS_IN_COMB];
					
					String[] allcombs = combs.split("#");
					for(String comb : allcombs) {
						String[] nums = comb.split(",");
						int i = 0;
						for(String num:nums) {
							allnums[i++]=(Integer.parseInt(num));
						}
						
						list.add(allnums);
						allnums= new Integer[Constants.NUMBER_OF_NUMS_IN_COMB];
					}
					
					return list;
					
					
				}
			
			private class ServerForMainAccountSubserver extends Thread{
				@Override
				public void run() {
					while(!interrupted()) { 
						
				 try {
					Socket client = serverForAccountSubserver.accept();
					new Thread()
					{
						@Override
						public void run() {
							MainServerCommunicator comm;
							try {
								comm = new MainServerCommunicator(client,MainServer.this);
								comm.paymentViaAccountSubserver();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(!client.isClosed())
								try {
									client.close();
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

			public String getCashSubserverIp() {
				// TODO Auto-generated method stub
				return cashSubserverIP;
			}
			
			 
}

