package database;

import java.util.LinkedList;
import java.util.List;

public class AccountInfo {
	private String accountId;
	private String pin;
	private volatile  int amount;
	private LinkedList<Integer> allTransactions;
	
	public AccountInfo(String accountId, String pin) {
		this.accountId= accountId;
		this.pin= pin;
		this.allTransactions=new LinkedList<Integer>();
		// TODO Auto-generated constructor stub
	}
	/**
	 * sets new account balance
	 * 
	 * @param amount  new Amount
	 * @return void
	 * @author CAR
	 */
	synchronized void setAmount(int amount) {
		 this.amount=amount;
	}
	/**gets account id
	 * 
	 * @return String accountID
	 */
	public String getAccountId () {
		return accountId;
	}
	/**gets accounts pin code
	 * 
	 * @return String pin
	 */
	
	public String getPIN() {
		return pin;
	}
	/**
	 * gets account balance
	 * 
	 * @return int amount
	 */
	synchronized public int getAmount() {
		return this.amount;
	}
	
	/**
	 * 
	 * 
	 * @param amount
	 * @return boolean- inidicates success  or failure
	 */
	
	synchronized public int cashOut(int amount) {
		
		if(this.amount<amount) return -1;
		this.amount-=amount;
		Integer newID=0;
		if(!allTransactions.isEmpty())
		 newID=allTransactions.getLast()+1;
		
		allTransactions.add(newID);
		Database.getInstance().saveToFile();
		Database.getInstance().saveAllTransactionsToFile();
		return newID;
	}
	
	/**
	 * 
	 * @param amount
	 * @return boolean indicates success or failure
	 */
	synchronized public boolean cashIn(int amount) {
		if(amount<0) return false;
		this.amount+=amount;
		Database.getInstance().saveToFile();
		return true;
		
	}
	public  List<Integer> getAllTransactions(){
		return allTransactions;
		
	}
	synchronized public String toString() {
		
		return accountId+"#"+pin+"#"+amount;
	}
	
}
