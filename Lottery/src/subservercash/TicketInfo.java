package subservercash;

public class TicketInfo {
   private int tickedID;
   private int amount;
public int getTickedID() {
	return tickedID;
}
public void setTickedID(int tickedID) {
	this.tickedID = tickedID;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
public TicketInfo(int tickedID, int amount) {
	super();
	this.tickedID = tickedID;
	this.amount = amount;
}
@Override
public String toString() {
	// TODO Auto-generated method stub
	return tickedID+ "," + amount;
   
}
}
