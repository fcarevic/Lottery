package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class Database {
	private HashMap<String, AccountInfo> database;
	private static Database instance;
	private Database() {
		database = new HashMap<String, AccountInfo>();
		readDatabaseFromFile();
		
	}
	

	/**
	 * 
	 * @return Database instances
	 */
	public static Database getInstance() {
		if(instance==null) instance=new Database();
		return instance;
	}
	
	/**
	 * reads database from file
	 * @param filePath
	 * 
	 * 
	 */
	private void readDatabaseFromFile() {
		String filePath= "databaseSubserver.txt";
		
		File file = new File(filePath);
		try {
			Scanner scanner= new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String [] parts = line.split("#");
				String accountID =parts[0];
				String pin = parts[1];
				int amount = Integer.parseInt(parts[2]);
				AccountInfo account = new AccountInfo(accountID, pin);
				account.setAmount(amount);
				database.put(accountID, account);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * exports database to file
	 * 
	 * 
	 * @param fileName
	 */
	
	public void saveToFile() {
		String filePath= "databaseSubserver.txt";
		
		File file =new File(filePath);
		try {
			PrintWriter writer = new PrintWriter(file);
			database.forEach(new BiConsumer<String, AccountInfo>() {

				@Override
				public void accept(String t, AccountInfo u) {
					writer.write(u.toString()+"\n");
				}
			} );
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void saveAllTransactionsToFile( ) {
		String filePath="subserverAccountTransactions.txt";
		
		File file =new File(filePath);
		try {
			PrintWriter writer = new PrintWriter(file);
			database.forEach(new BiConsumer<String, AccountInfo>() {

				@Override
				public void accept(String t, AccountInfo u) {
				   if(!u.getAllTransactions().isEmpty()) {
					StringBuilder sb = new StringBuilder();
					sb.append(u);
					for(Integer idTrans: u.getAllTransactions()) {
						sb.append("#"+idTrans);
					}
					sb.append('\n');
					writer.write(sb.toString());
				}}
			} );
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	private void readAllTransactionsFromFile() {
		String filePath="subserverAccountTransactions.txt";
			
		File file = new File(filePath);
		
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String parts[] = line.split("#");
			String accountID=parts[0];
			LinkedList<Integer> list = new LinkedList<Integer>();
			for(int i=1 ; i < parts.length;i++) {
			    Integer idTrans = Integer.parseInt(parts[i]);
			   list.add(idTrans);
			   
			}
			database.get(accountID).getAllTransactions().addAll(list);
		}
		scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public AccountInfo getAccountInfo(String accountID, String pin) {
		AccountInfo account = database.get(accountID);
		if(account==null) return account;
		if(account.getPIN().equals(pin)) return account;
		return null;
		
		
	}


	synchronized public AccountInfo getAccountInfo(String accountID) {
		return database.get(accountID);
		// TODO Auto-generated method stub
		
	}

}
