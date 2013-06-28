import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Created on Dec 22, 2004
 */

public class Fields extends Hashtable {
	
	 public static Hashtable fieldHash = new Hashtable();	
	 
	 private static Fields instance = null;
	 
	 public static Fields getInstance(){
	 	
	 	 if (instance == null){
	 	 	 instance = new Fields();
	 	 	 //System.out.println("created new Clients object");
	 	 }
	 	 
	  return (Fields ) instance;
	  
	 }
	 
 // **********


	    
	   JPanel makeTextLabelPanel(String label, int textLen){
	   
	    return (JPanel) makeTextLabelPanel(label, textLen, null);
	  
	   }
	  
	    // ***

	 public JPanel makeTextLabelPanel(String label, int textLen, String hashKey){
	    
	     JPanel fooPanel = new JPanel();
	     
	     JTextField bar = new JTextField(textLen);
	         JLabel barLbl = new JLabel(label,JLabel.TRAILING);
	         barLbl.setLabelFor(bar);
	     
	     if(hashKey == null){
	        fieldHash.put(label, bar);
	     }else{    
	        fieldHash.put(hashKey, bar);
	     }
	         
	     fooPanel.add(barLbl);
	     fooPanel.add(bar); 
	     
	     return (JPanel) fooPanel;
	    
	 } 
	 
	 // ***
	 
	 String getCurrentClientIDField(){
	     	
	     	String clientID = ((JTextField) fieldHash.get("Client ID")).getText();
	     	
	     	return (String) clientID;
	 }
}
