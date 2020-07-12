package mainserver;

import java.awt.BorderLayout;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.Constants;

public class MainServerGUI extends JFrame {
	private JButton close, saveNumbers;
	private JTextField[] numbers;
	private MainServer server;
	
	
	
	public MainServerGUI() {
	  super("Main server");
	  setSize(600,300);
	   addButtons();
	   addNumbers();
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   setVisible(true);
	
	}
	public void  setMainServer(MainServer server) {
		this.server=server;
	}
	private void addButtons() {
		JPanel panel = new JPanel();
		close = new JButton("Open current");
		close.addActionListener(l->{
			if(server.getCurrentlyActive()) {
			close.setText("Open current");
			server.setCurrentlyActive(false);
			}
			else {
				close.setText("Close current");
				server.setCurrentlyActive(true);
			} 
			
		});
		saveNumbers = new JButton("Save winning numbers") ;
		saveNumbers.addActionListener(l->{
			try{
			Integer[] nums = new Integer[numbers.length];
			for(int i= 0 ; i < numbers.length;i++) {
				nums[i] = Integer.parseInt(numbers[i].getText());
				
			}
			server.setWinningCombiantion(nums);
			close.setText("Open current");
			server.setCurrentlyActive(false);
			server.declareWinners();
			}catch(NumberFormatException e) {}
			
		});
			
			panel.add(close);
			panel.add(saveNumbers);
			add(panel, BorderLayout.SOUTH);
		
		
	}
	private void addNumbers() {
		numbers= new JTextField[Constants.NUMBER_OF_NUMS_IN_COMB];
		JPanel numPanel = new JPanel();
		for(int i=0 ; i< 7; i++) {
			numbers[i]=new JTextField(4);
			
			numPanel.add(numbers[i]);
		}
		add(numPanel, BorderLayout.NORTH);
		
	}
	public static void main(String args[]) {
		
		MainServer server;
		try {
			server = new MainServer(Constants.SERVER_PORT_SUBSERVER);
	if(System.getSecurityManager()==null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			Registry reg = LocateRegistry.createRegistry(Constants.SERVER_PORT_TRAFFIC);
			MainServerRemoteImplementation remoteImpl = new MainServerRemoteImplementation(server);
			MainServerRemoteInterface remoteInter = (MainServerRemoteInterface) UnicastRemoteObject.exportObject(remoteImpl,Constants.SERVER_PORT_TRAFFIC);
			reg.rebind(Constants.MAIN_SERVER_RMI_NAME, remoteInter);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MainServerGUI gui = new MainServerGUI();
		gui.setMainServer(server);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	

}
