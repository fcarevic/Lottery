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
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import constants.Constants;


public class MainServer {
			private Map<Integer,Combinations> allcombinations;
			private volatile boolean currentlyActive;
			private List<Integer> allTransactions;
			private volatile int ticketID;
			private List<Integer> unconfirmedTickets;
			private Integer[] winningCombination;
			public MainServer() {
				readCombinationsFromFile("allcombs.txt");
				readTransactonsFromFile("alltrans.txt");
				unconfirmedTickets= new LinkedList<Integer>();
				
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
						new CashOutClassAccount(u.getSubeserverIp(), Constants.SERVER_PORT_SUBSERVER, amount, u.getAccountID(), MainServer.this).start();
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
			public synchronized void addCombination(int ticketID, List<Integer[]> comb, int transactionID, String subserverIp) {
				Combinations combination = new Combinations();
				combination.setTicketID(ticketID);
				combination.setCombinations(comb);
				combination.setTransactionID(transactionID);
				combination.setSubeserverIp(subserverIp);
				allcombinations.put(ticketID, combination);
				saveCombinationsToFile();
				
			}
			public synchronized void addCombination(int ticketID, List<Integer[]> comb) {
				Combinations combination= new Combinations();
				combination.setTicketID(ticketID);
				combination.setCombinations(comb);
				allcombinations.put(ticketID, combination);
				saveCombinationsToFile();
				
			}
			public synchronized void removeCombination(int ticketID) {
				allcombinations.remove(ticketID);
				saveCombinationsToFile();
				
			}
			
			
			
			public synchronized void saveCombinationsToFile() {
				String filename= "alcombs.txt";
				File file = new File(filename);
				try {
					PrintWriter out = new PrintWriter(file);
					allcombinations.forEach(new BiConsumer<Integer, Combinations>() {

						@Override
						public void accept(Integer t, Combinations u) {
							out.append(u.toSting()+"\n");
							
							
						}
						
					});
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			public synchronized void saveTransactionsToFile() {
				String filename="alltrans.txt";
				File file = new File(filename);
				try {
					PrintWriter out = new PrintWriter(file);
					for(Integer id :allTransactions) {
						out.append(id + " ");
					}
				
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
			
			
			private class WorkerServer extends Thread {
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
			}
			public   List<Integer[]>  getCombinations(String combs) {
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

