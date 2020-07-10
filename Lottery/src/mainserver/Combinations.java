package mainserver;

import java.util.LinkedList;
import java.util.List;

import constants.Constants;

public class Combinations {
	private String subeserverIp;
	private int transactionID;
	private int ticketID;
	private List<Integer[]> combinations;
	private String accountID;
	public String getSubeserverIp() {
		return subeserverIp;
	}
	public void setSubeserverIp(String subeserverIp) {
		this.subeserverIp = subeserverIp;
	}
	public int getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public int getTicketID() {
		return ticketID;
	}
	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
	}
	public List<Integer[]> getCombinations() {
		return combinations;
	}
	public void setCombinations(List<Integer[]> combinations) {
		this.combinations = combinations;
	}
	
	
	public static Combinations parseCombination(String line) {
		LinkedList<Integer[]> allcombs = new LinkedList<Integer[]>();
		Integer[] combination =new  Integer[Constants.NUMBER_OF_NUMS_IN_COMB];
		
		String[]parts  = line.split("!");
		Combinations combObj= new Combinations();
		Integer id = Integer.parseInt(parts[0]);
		combObj.setTicketID(id);
		String subserverip = parts[1];
		if(!subserverip.equals("*")) {
			combObj.setSubeserverIp(subserverip);
			combObj.setTransactionID(Integer.parseInt(parts[2]));
			combObj.setAccountID(parts[3]);
		}
		parts = parts[4].split("#");
		for(String comb : parts) {
			String[] nums = comb.split(",");
			int i=0;
			for(String num:nums) {
				Integer n = Integer.parseInt(num);
				combination[i++]=(n);
				
			}
			allcombs.add(combination);
			combObj.setCombinations(allcombs);
			combination = new Integer[Constants.NUMBER_OF_NUMS_IN_COMB];
			
		
		}
		return combObj;
	}
	public String toSting() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(getTicketID()+"!");
		if(getSubeserverIp()!=null) {
			sb.append(getSubeserverIp()+"!");
			sb.append(getTransactionID()+"!");
			sb.append(getAccountID()+"!");
			
		} else {
			sb.append("*!");
			sb.append("*!");
			sb.append("*!");
				
		}
		for(Integer[] comb: getCombinations()) {
			for(Integer num:comb) {
				sb.append(num+",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("#");
			
		}
		sb.deleteCharAt(sb.length()-1);
	     return sb.toString();
	}

}
