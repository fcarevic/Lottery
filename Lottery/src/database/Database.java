package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class Database {
	private HashMap<String, AccountInfo> database;
	private static Database instance;
	private Database() {
		database = new HashMap<String, AccountInfo>();
		String filePath= "database.txt";
		readFromFile(filePath);
		
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
	private void readFromFile(String filePath) {
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
	
	private void saveToFile(String fileName) {
		File file =new File(fileName);
		try {
			PrintWriter writer = new PrintWriter(file);
			database.forEach(new BiConsumer<String, AccountInfo>() {

				@Override
				public void accept(String t, AccountInfo u) {
					writer.write(u.toString());
				}
			} );
			
			writer.close();
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

}
