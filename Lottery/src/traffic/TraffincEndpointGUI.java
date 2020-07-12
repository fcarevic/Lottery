package traffic;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import communication.TrafficCommunicator;
import constants.Constants;
import pdfcreator.PdfWriter;

public class TraffincEndpointGUI extends JFrame {
  
private JButton addNewCombination;
 private JButton next;
 private JTextArea combinations;
 private JTextField[] numbers;
 private LinkedList<Integer[]> combinationList;
 
	public TraffincEndpointGUI() {
		setSize(500, 400);
		combinationList= new LinkedList<Integer[]>();
		setLayout(new BorderLayout());
		combinations= new JTextArea();
		combinations.setText("Combinations");
		combinations.setEditable(false);
		add(combinations, BorderLayout.CENTER);
		addNumbers();
		addButtons();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		// TODO Auto-generated constructor stub
	}
	
	private void addNumbers() {
		numbers= new JTextField[Constants.NUMBER_OF_NUMS];
		JPanel numPanel = new JPanel();
		for(int i=0 ; i< 7; i++) {
			numbers[i]=new JTextField(4);
			
			numPanel.add(numbers[i]);
		}
		add(numPanel, BorderLayout.NORTH);
		
	}
	
	private void addButtons() {
		addNewCombination= new JButton("Add combination");
		addNewCombination.addActionListener(event->{
			Integer [] newComb = new Integer[Constants.NUMBER_OF_NUMS];
			try {
			for(int i=0 ; i < Constants.NUMBER_OF_NUMS; i++) {
				newComb[i] = Integer.parseInt(numbers[i].getText());
				
						}

			TraffincEndpointGUI.this.combinationList.add(newComb);
			TraffincEndpointGUI.this.refreshCombinationList();
			}
		
			catch(NumberFormatException e) {
				JDialog error =new JDialog(TraffincEndpointGUI.this, true);
				
				error.add(new JLabel("      Incorrect number format"));
				error.setSize(300,100);
				error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				error.setVisible(true);
				
				
			}
		});
		next = new JButton("Next");
		next.addActionListener(l->{
			new PaymentDialog();
		});
		
		
		
		JPanel south= new JPanel();
		south.add(addNewCombination);
		south.add(next);
		add(south,BorderLayout.SOUTH);
		
		
	}
	
	private void refreshCombinationList() {
		StringBuilder sb= new StringBuilder();
		for(Integer[] comb: combinationList) {
			for(Integer num:comb) sb.append(num+" ");
			 sb.append('\n');
				
		} 
		combinations.setText("");
		combinations.setText(sb.toString());
	}
	
	private class DialogCashClosed extends JDialog{
		private JButton refund;
		private JButton payForNextTIme;
		
		public DialogCashClosed(TraffincEndpointGUI parent) {
			super(parent,true);
			setSize(300, 300);
			addButtons();
			setVisible(true);
			
		}
		
		private void addButtons() {
			JPanel panel = new JPanel();
			
			refund = new JButton("Refund"); 
			refund.addActionListener(l->{
				
				
				
				
				
			});
			payForNextTIme = new JButton("Pay for next time");
			panel.add(refund);
			panel.add(payForNextTIme);
			add(panel);
			
		}
		
		
		
		
		
	}
	
	private class PaymentDialog extends JDialog{
		private JTextField id, pin ,cashAmount;
		public PaymentDialog() {
			
			super(TraffincEndpointGUI.this, "Payment", true);
			setSize(400,300);
			setLayout(new GridLayout(2,1));
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			addCashPayment();
			addPaymentViaAccount();
			setVisible(true);
			// TODO Auto-generated constructor stub
		}
		
		private void addCashPayment() {
			
			JPanel north= new JPanel();
			north.setLayout(new GridLayout(2, 1));
			cashAmount= new JTextField(10);
			JButton pay1 = new JButton("Pay cash");
			
			
			
			pay1.addActionListener(l->{
			 try {
				 Integer amount  = Integer.parseInt(cashAmount.getText());
				 if(amount<Constants.COST_PER_COMBINATION*combinationList.size()) {
					 printErrorDialog("Insufficient amount");
	 
					 return;
				 }
				 try {
					TrafficCommunicator communicator = new TrafficCommunicator();
					 communicator.setMainServer(Constants.MAIN_SERVER_IP, Constants.SERVER_PORT_TRAFFIC);
				int response= communicator.payViaCash(combinationList);
				if(Constants.STATUS_CLOSED==response ) {
					 printErrorDialog("Currenlty closed");
					
				} else resolveStatus(response);
		
				 } catch(Exception e) {
					 e.printStackTrace();
					 System.out.println("ovde");
					 printErrorDialog("Error while communicatoing with server");
	 
				 }
				 
				
			} catch (Exception e) {
				printErrorDialog("Incorrect number format");

				// TODO: handle exception
			}
			
			});
			
			north.add(cashAmount);
			north.add(pay1);
			add(north);
			
			
		
					
			
			
		}
		private void addPaymentViaAccount() {
			
			JTextField id = new JTextField("ID", 10);
			JTextField pin= new JPasswordField(10);
			JPanel west = new JPanel();
			JButton pay2 = new JButton("Pay via account");
			int amount = combinationList.size()*Constants.COST_PER_COMBINATION;
		
			pay2.addActionListener(l->{
				String accountId = id.getText();
				String pinCode= pin.getText();
				TrafficCommunicator comm  =new TrafficCommunicator();
				try {
					comm.setSubserver(Constants.SUBSERVER_ACCOUNT_IP, Constants.SUBSERVER_PORT_TRAFFIC);
					comm.setMainServer(Constants.MAIN_SERVER_IP, Constants.SERVER_PORT_TRAFFIC);
					int status =comm.payViaAccount(accountId, pinCode, amount, combinationList);
					resolveStatus(status);
					
				} catch (IOException e) {
					printErrorDialog("Error while communicating with servers");
					e.printStackTrace();
				} catch (NotBoundException e) {
					printErrorDialog("Error while communicating with main server");
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			});
			
			west.setLayout(new GridLayout(3,1));
			west.add(id);
			west.add(pin);
			west.add(pay2);
			add(west);
			
		}
		
	}
  private void resolveStatus(int status) {
	  switch (status) {
		case Constants.STATUS_CLOSED:
			printErrorDialog("Currently closed");
			
			break;
		case Constants.STATUS_ERROR:
			printErrorDialog("Error while communicating with servers");
			
			
			break;
		case Constants.STATUS_OK:
			printErrorDialog("Pdf printed");
			
			break;
		
		}
  }
	public void printErrorDialog(String errorS) {
		JDialog error =new JDialog(TraffincEndpointGUI.this, true);
		
		error.add(new JLabel("      " + errorS));
		error.setSize(300,100);
		error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		error.setVisible(true);

		
		
	}
	public static void main(String[] args) {
		if(System.getSecurityManager()==null) 
		{
			System.setSecurityManager(new SecurityManager());
		}
		new TraffincEndpointGUI();
		
	}

}
