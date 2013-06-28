import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/*
 * Created on Jan 15, 2005
 */

public class invoicePanel extends JPanel {

	Fields fields = Fields.getInstance();
	Clients clients = Clients.getInstance();
	
	JTable invoiceTable;
	
	public invoicePanel(){
		
		add((JPanel) fields.makeTextLabelPanel("Date",10));
		add((JPanel) fields.makeTextLabelPanel("Desc",30));
		add((JPanel) fields.makeTextLabelPanel("Ammount",10));
		
		add((JButton) new JButton("Add Line Item"));
		
		populate_inv();
	}
	
	void populate_inv(){
		
		String[] columnNames = {"Date", "Desc", "Ammount"};
		
		Object[][] data = {
				{"foo","bar","things"}
		};
		
		Hashtable stuff = (Hashtable) clients.getClaims((String) fields.getCurrentClientIDField());
		
		invoiceTable = new JTable(data,columnNames);
		
		invoiceTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		
		JScrollPane scrollPane = new JScrollPane(invoiceTable);
		
		add(scrollPane);
	}
}
