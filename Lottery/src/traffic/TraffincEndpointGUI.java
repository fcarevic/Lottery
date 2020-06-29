package traffic;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

public class TraffincEndpointGUI extends JFrame {
  private static final int NUMBER_OF_NUMS=7; 
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
		numbers= new JTextField[NUMBER_OF_NUMS];
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
			Integer [] newComb = new Integer[NUMBER_OF_NUMS];
			try {
			for(int i=0 ; i < NUMBER_OF_NUMS; i++) {
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
	
	
	
	private class PaymentDialog extends JDialog{
		private JTextField id, pin ,cashAmount;
		public PaymentDialog() {
			
			super(TraffincEndpointGUI.this, "Payment", true);
			setSize(400,300);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setVisible(true);
			// TODO Auto-generated constructor stub
		}
		
		private void addEast() {
			
			JPanel east= new JPanel();
			east.setLayout(new GridLayout(2, 1));
			cashAmount= new JTextField(10);
			JButton pay1 = new JButton("Pay cash");
			east.add(cashAmount);
			east.add(pay1);
			add(east,BorderLayout.EAST);
					
			
			
		}
		private void addWest() {
			
			JTextField id = new JTextField("ID", 10);
			JTextField pin= new JPasswordField(10);
			JPanel west = new JPanel();
			JButton pay2 = new JButton("Pay via account");
			
			west.setLayout(new GridLayout(3,1));
			west.add(id);
			west.add(pin);
			west.add(pay2);
			add(west,BorderLayout.WEST);
			
		}
		
	}
	
	public static void main(String[] args) {
		new TraffincEndpointGUI();
		
	}

}
