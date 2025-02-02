package constants;

public class Constants {
	public static final int SERVER_PORT_TRAFFIC=4000; //RMI port
	public static final int SERVER_PORT_SUBSERVER=4001;  //main server
	public static final int SUBSERVER_PORT_TRAFFIC=4002; //account subserver
	public static final int SUBSERVER_PORT_MAIN_SERVER = 4003; // account subserver
	public static final int CASH_SUBSERVER_TRAFFIC_PORT = 4004;   
	public static final int CASH_SUBSERVER_SERVER_PORT=4005;

	public static final int STATUS_OK=0, STATUS_ERROR=-1, STATUS_CLOSED=-2;
	public static final String MAIN_SERVER_RMI_NAME = "/reg", MAIN_SERVER_IP="localhost";
	public static final int [] PRICE_MATCHED= {0,0,0,0,10,100,100,1000};
	public static final int NUMBER_OF_NUMS_IN_COMB = 7;
	public static final String SUBSERVER_ACCOUNT_IP = "localhost";
	public static final int NUMBER_OF_NUMS=7; 
	public static final int COST_PER_COMBINATION=10;
	public static final String CASH_SUBSERVER_IP = "localhost";
}