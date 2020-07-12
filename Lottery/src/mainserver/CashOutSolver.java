package mainserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Consumer;

public class CashOutSolver {
	  private static final String WINNERS_FILE ="cashOutSolverWinners.txt";
		private LinkedList<CashOutClassAccount> winners;
		private MainServer server;
		public CashOutSolver(MainServer server) {
			this.server=server;
			readFromFile();
			startAll();
		}

		public MainServer getMainServer() {
			return server;
		}
		public synchronized void removeWinner(CashOutClassAccount ca) {
			winners.remove(ca);
			saveToFile();
		}
		
		public synchronized void addWinner(CashOutClassAccount ca) {
			winners.add(ca);
			ca.start();
			saveToFile();
		}
		
		private void startAll() {
			for(CashOutClassAccount ca : winners) {
				if(!ca.isAlive())ca.start();
			}
		}
		private void readFromFile() {
			File file = new File(WINNERS_FILE);
			winners = new LinkedList<CashOutClassAccount>();
			Scanner scanner;
			try {
				scanner = new Scanner(file);
				while(scanner.hasNextLine()) {
					String line= scanner.nextLine();
					String parts[] = line.split("#");
					  // host+"#" + port + "#" + amount + "#" + ticketID + (accountID!=null? accountID:'*'); 
					String host =parts[0];
					int port = Integer.parseInt(parts[1]);
					int amount= Integer.parseInt(parts[2]);
					
					int ticketId= Integer.parseInt(parts[3]);
					String accID= (parts[4]);
					CashOutClassAccount ca = new CashOutClassAccount(host, port, amount, ticketId, this);
					if(!accID.equals("*")) ca.setAccountID(accID);
				   	
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		
		public void saveToFile() {
			File file = new File(WINNERS_FILE);
			try {
				PrintWriter out = new PrintWriter(file);
				winners.forEach(new Consumer<CashOutClassAccount>() {

					@Override
					public void accept(CashOutClassAccount t) {
						// TODO Auto-generated method stub
						out.write(t.toString()+'\n');
						
					}
					
				});
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
}
