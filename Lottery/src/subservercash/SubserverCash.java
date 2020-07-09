package subservercash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SubserverCash {
	private List<TicketInfo> winningTickets;
	
	SubserverCash(){
		readFromFile("cashsubserver.txt");
		
	}
	public synchronized int getAmount(int ticketID) {
		
		for(TicketInfo ti : winningTickets) {
			if(ti.getTickedID()==ticketID) return ti.getAmount();
		}
		
		return -1;
	}
	
	public synchronized void addWinningTicket(int ticketID, int amount) {
		TicketInfo ti = new TicketInfo(ticketID, amount);
		winningTickets.add(ti);
	}
	public synchronized boolean removeWinningTicket(int ticketID, int amount) {
		int num1 = winningTickets.size();
		winningTickets.removeIf(new Predicate<TicketInfo>() {

			@Override
			public boolean test(TicketInfo t) {
				return ticketID==t.getTickedID() && amount == t.getAmount();
			}
		});
		int num2= winningTickets.size();
		return num2<num1;
	}
	
	
	
	public void saveToFile(String filepath) {
		File file = new File(filepath);
		try {
			PrintWriter pout= new PrintWriter(file);
			winningTickets.forEach(new Consumer<TicketInfo>() {

				@Override
				public void accept(TicketInfo t) {
					pout.write(t.toString()+'\n');
					// TODO Auto-generated method stub
					
				}
			});
			pout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void readFromFile(String filepath) {
		winningTickets= new LinkedList<TicketInfo>();
		File file= new File(filepath);
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line= scanner.nextLine();
				String part[] = line.split(",");
				int ticketID = Integer.parseInt(part[0]);
				int amount= Integer.parseInt(part[1]);
			    winningTickets.add(new TicketInfo(ticketID, amount));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}


}
