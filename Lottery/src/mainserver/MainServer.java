package mainserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;


public class MainServer {
			private Map<Integer, List<Integer[]>> allcombinations;
			private List<Integer> allTransactions;
			private volatile int ticketID;
			public MainServer() {
				readCombinationsFromFile("allcombs.txt");
				readTransactonsFromFile("alltrans.txt");
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
			
			public synchronized void addCombination(int ticketID, List<Integer[]> comb) {
				allcombinations.put(ticketID, comb);
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
					allcombinations.forEach(new BiConsumer<Integer, List<Integer[]>>() {

						@Override
						public void accept(Integer t, List<Integer[]> u) {
							StringBuilder sb = new StringBuilder();
							sb.append(t+"!");
							for(Integer[] comb: u) {
								for(Integer num:comb) {
									sb.append(num+",");
								}
								sb.deleteCharAt(sb.length()-1);
								sb.append("#");
								
							}
							sb.deleteCharAt(sb.length()-1);
							sb.append("\n");
							
							
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
				allcombinations= new HashMap<Integer, List<Integer[]>>();
				
				LinkedList<Integer> combination = new LinkedList<Integer>();
				
				File file = new File(filepath);
				Scanner scanner;
				try {
					scanner = new Scanner(file);
					while(scanner.hasNextLine()) {
						LinkedList<Integer[]> allcombs = new LinkedList<Integer[]>();
					String line = scanner.nextLine();	
					String[]parts  = line.split("!");
					Integer id = Integer.parseInt(parts[0]);
					parts = parts[1].split("#");
					for(String comb : parts) {
						String[] nums = comb.split(",");
						for(String num:nums) {
							Integer n = Integer.parseInt(num);
							combination.add(n);
							
						}
						allcombs.add((Integer[])combination.toArray());
						combination.clear();
						
					}
						allcombinations.put(id, allcombs);
					}
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			 
			
}
