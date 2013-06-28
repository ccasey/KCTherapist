import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

/**
 * Sample application using Frame.
 *
 * @author 
 * @version 1.00 04/05/18
 */
public class KCTherapistFrame extends JFrame {
    
    private JTree clientTree; 
    private JSplitPane clientSplit;
    //private Hashtable fieldHash = new Hashtable();
    private Hashtable provFieldHash = new Hashtable();
    private JComboBox clientCombo;
    private JList claimList;
    private Fields fields = Fields.getInstance();
    Clients clients = Clients.getInstance();
    Hashtable claims;
    
    /**
     * The constructor.
     */  
     public KCTherapistFrame() {
     	
        this.setDefaultLookAndFeelDecorated(true);
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        
        JMenu menuEdit = new JMenu("Edit");
        menuBar.add(menuEdit);
        
        // *** create and add the new client MenuItem
        
        JMenuItem menuEditNew = new JMenuItem("New Client");
        menuEditNew.setAccelerator(KeyStroke.getKeyStroke(
        	 KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        	 
        menuEditNew.addActionListener(
        	new ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			// clear the fields for a new client to be entered
        			clearPanes();
        			newClient();
        		}
        	}
        ); 
        	 
        menuEdit.add(menuEditNew);
        
        
        // *** create and add the save MenuItem
        
        JMenuItem menuFileSave = new JMenuItem("Save");
        
        //Setting the accelerator:
		menuFileSave.setAccelerator(KeyStroke.getKeyStroke(
        	KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	    
	    // action listener
 		menuFileSave.addActionListener(
         new ActionListener() {
         	public void actionPerformed(ActionEvent e){
         		// put the current field values into the Clients Instance, 
         		// and then write the instance to disk.
         	    
                 saveEverything(true);
            }
         }
        );
          		
	 	menuFile.add(menuFileSave);
	 	
        // *** create and add the exit MenuItem
        
        JMenuItem menuFileExit = new JMenuItem("Quit");
        
        //Setting the accelerator:
		menuFileExit.setAccelerator(KeyStroke.getKeyStroke(
        	KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        
        // Add action listener.for the menu button
        menuFileExit.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveEverything(true);
                    KCTherapistFrame.this.windowClosed();
                }
            }
        ); 
        
        menuFile.add(menuFileExit);
        
        // *** create and add the rename clientID item
        
        JMenuItem menuEditReID = new JMenuItem("Rename ClientID");
        
        // set the accelerator
        
        menuEditReID.setAccelerator(KeyStroke.getKeyStroke(
        	KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        
        // Add action listener.for the menu button
        menuEditReID.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeClientID();
                }
            }
        );
        
        
        menuEdit.add(menuEditReID);
        
        // *** create and add the remove clientID item
        JMenuItem menuEditDelID = new JMenuItem("Delete ClientID");
        
        menuEditDelID.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        
        menuEditDelID.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    removeClientID((String) fields.getCurrentClientIDField() , true);
                    populateClientCombo();        
                }
            }
        );
        
        
        menuEdit.add(menuEditDelID);
        
        // *** create and add the provider info item
        
        JMenuItem menuEditProvInfo = new JMenuItem("Provider Info");
        
        menuEditProvInfo.addActionListener
        (
        
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
                configProvider();
                populateProviderConfig();
            }
         }
        
        );
        
        menuEdit.add(menuEditProvInfo);
        
        // set the title, menubar, size etc.
         
        setTitle("KCTherapist");
        setJMenuBar(menuBar);
        setSize(new Dimension(400, 400));
        
        // make toolbar  panel for client selection etc
        
        // combo box for client selection
        
        clientCombo = new JComboBox();
        
        populateClientCombo();
        
        clientCombo.addActionListener
        (
        
         new ActionListener(){
         	public void actionPerformed(ActionEvent e){
         		JComboBox cb = (JComboBox)e.getSource();
         		String selected = (String) cb.getSelectedItem();
         		if(selected != null){
         		 
         		 // first we need to save the values in the fields to 
         		 // the working hash.
         		 
         		 saveValues();
         		 
         		 // then repopulate the fields with the values for the selected 
         		 // client etc.
         		 
         		 populatePanes(selected.substring(0,(int) selected.indexOf(":")));
         		 populateClaimList();
         		}
         	}
         }
        	
        );
        
        
        menuBar.add(clientCombo);
        
        // create tabbed plane for all the stuff for the client
        
        JTabbedPane clientTabs = new JTabbedPane();
        
        JPanel clientTabPanel = new JPanel();
        JScrollPane clientTabPane = new JScrollPane(clientTabPanel);
        
        // ********************
        // ********************
        
        //CMS1500FieldPanel foo = new CMS1500FieldPanel();
        //clientTabPanel.add(foo);
        
        //Hashtable tmpHash = (Hashtable) foo.getFieldHash();
        
        JPanel contactPanel = new JPanel();
        
        
        TitledBorder cpTitle;
        cpTitle = BorderFactory.createTitledBorder("Patients Contact Info");
        contactPanel.setBorder(cpTitle);
        
        
        clientTabPanel.setPreferredSize(new Dimension(600, 1000));
        clientTabPane.setPreferredSize(new Dimension(600, 500));
		
        // String[] infoFields = FieldInfo.getInfoFields();

       
       JPanel clientInfoPanel = new JPanel();
       
        clientInfoPanel.setPreferredSize(new Dimension(900,150));
       
       TitledBorder cifTitle;
        cifTitle = BorderFactory.createTitledBorder("Clients Info");
        clientInfoPanel.setBorder(cifTitle);
          
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Client ID",15));
        
        ((JTextField)fields.get("Client ID")).setEditable(false);
        
        clientInfoPanel.add((JPanel) makeTextLabelPanel("clientCreateDate",9));
        
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Last Name",15));
        
        clientInfoPanel.add((JPanel) makeTextLabelPanel("First Name",15)); 
        
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Middle Initial",2));

        clientInfoPanel.add((JPanel) makeTextLabelPanel("Street",20));
         
        clientInfoPanel.add((JPanel) makeTextLabelPanel("City",15));
         
        clientInfoPanel.add((JPanel) makeTextLabelPanel("State",2));
            
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Zipcode",5,"Zip"));
        
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Phone",12,"Phone"));
        
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Bday MM",2));
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Bday DD",2));
        clientInfoPanel.add((JPanel) makeTextLabelPanel("Bday YY",2));
        
        clientTabPanel.add(clientInfoPanel);

        // ***
        
        //JPanel diagnosisPanel = new JPanel();
        
        //TitledBorder diagTitle;
        //diagTitle = BorderFactory.createTitledBorder("Patients Diagnosis");
        //diagnosisPanel.setBorder(diagTitle);
        
        //diagnosisPanel.add((JPanel) makeTextLabelPanel("Diagnosis",5));
        
        //clientTabPanel.add(diagnosisPanel);
        
        // ***
        
        
        JPanel relationPanel = new JPanel();
        TitledBorder rpTitle;
        rpTitle = BorderFactory.createTitledBorder("Patients Relation to Insured");
        relationPanel.setBorder(rpTitle);
         
        JRadioButton relationSelfButton = new JRadioButton("Self");
        JRadioButton relationSpouseButton = new JRadioButton("Spouse");
        JRadioButton relationChildButton = new JRadioButton("Child");
        JRadioButton relationOtherButton = new JRadioButton("Other");
        JRadioButton relationHiddenButton = new JRadioButton("Hidden");
                
        ButtonGroup relationGroup = new ButtonGroup();
        relationGroup.add(relationSelfButton);
        relationGroup.add(relationSpouseButton);
        relationGroup.add(relationChildButton);
        relationGroup.add(relationOtherButton);
        relationGroup.add(relationHiddenButton);
        
        fields.put("relationSelfButton", relationSelfButton);
        fields.put("relationSpouseButton", relationSpouseButton);
        fields.put("relationChildButton", relationChildButton);
        fields.put("relationOtherButton", relationOtherButton);
        fields.put("relationHiddenButton", relationHiddenButton);
        
        // set the hidden button to selected, and then dont add it to the panel
        // this will let us have a way to unselect all the options
        
        relationHiddenButton.setSelected(true);
        
        relationPanel.add(relationSelfButton);
        relationPanel.add(relationSpouseButton);
        relationPanel.add(relationChildButton);
        relationPanel.add(relationOtherButton);
        
        clientTabPanel.add(relationPanel);
        
        // ***
        
        JPanel patientStatusPanel = new JPanel();
        
        TitledBorder psTitle;
        psTitle = BorderFactory.createTitledBorder("Patient Status");
        patientStatusPanel.setBorder(psTitle);
        
        JRadioButton singleButton = new JRadioButton("Single");
        JRadioButton marriedButton = new JRadioButton("Married");
        JRadioButton otherMaritalButton = new JRadioButton("Other");
        JRadioButton maritalHiddenButton = new JRadioButton("Hidde");
        
        ButtonGroup maritalGroup = new ButtonGroup();
        maritalGroup.add(singleButton);
        maritalGroup.add(marriedButton);
        maritalGroup.add(otherMaritalButton);
        maritalGroup.add(maritalHiddenButton);
        
        fields.put("singleButton", singleButton);
        fields.put("marriedButton", marriedButton);
        fields.put("otherMaritalButton", otherMaritalButton);
        fields.put("maritalHiddenButton", maritalHiddenButton);
        
        
        patientStatusPanel.add(singleButton);
        patientStatusPanel.add(marriedButton);
        patientStatusPanel.add(otherMaritalButton);
        
        JCheckBox employedCheck = new JCheckBox("Employed");
        JCheckBox partStuCheck = new JCheckBox("Part Time Student");
        JCheckBox fullStuCheck = new JCheckBox("Full Time Student");
        
        fields.put("employedCheck", employedCheck);
        fields.put("partStuCheck", partStuCheck);
        fields.put("fullStuCheck", fullStuCheck);
        
        // no hidden checkbox, we can unselect them all we like
        
        patientStatusPanel.add(employedCheck);
        patientStatusPanel.add(partStuCheck);
        patientStatusPanel.add(fullStuCheck);
        
        clientTabPanel.add(patientStatusPanel);
        
        // ***
        
        JPanel clientSexPanel = new JPanel();
        
        TitledBorder csexTitle;
        csexTitle = BorderFactory.createTitledBorder("Sex");
        clientSexPanel.setBorder(csexTitle);
        
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        JRadioButton sexHiddenButton = new JRadioButton("Hidden");
        
        ButtonGroup sexGroup = new ButtonGroup();
        sexGroup.add(maleButton);
        sexGroup.add(femaleButton);
        sexGroup.add(sexHiddenButton);
        
        fields.put("maleButton", maleButton);
        fields.put("femaleButton", femaleButton);
        fields.put("sexHiddenButton", sexHiddenButton);
        
        clientSexPanel.add(maleButton);
        clientSexPanel.add(femaleButton);
        // leave hidden one out
        
        clientTabPanel.add(clientSexPanel);
        
        // ***
        
        JPanel clientInsPanel = new JPanel();
        
        TitledBorder ciTitle;
        ciTitle = BorderFactory.createTitledBorder("Insurance Type");
        clientInsPanel.setBorder(ciTitle);
        
        JRadioButton medicareButton = new JRadioButton("Medicare");
        JRadioButton medicaidButton = new JRadioButton("Medicaid");
        JRadioButton champusButton = new JRadioButton("CHAMPUS");
        JRadioButton champvaButton = new JRadioButton("CHAMPVA");
        JRadioButton groupButton = new JRadioButton("Group Health Plan");
        JRadioButton fecaButton = new JRadioButton("FECA BLK LUNG");
        JRadioButton otherInsButton = new JRadioButton("Other");
        JRadioButton insHiddenButton = new JRadioButton("Hidden");
        
        ButtonGroup insGroup = new ButtonGroup();
        insGroup.add(medicareButton);
        insGroup.add(medicaidButton);
        insGroup.add(champusButton);
        insGroup.add(champvaButton);
        insGroup.add(groupButton);
        insGroup.add(fecaButton);
        insGroup.add(otherInsButton);
        insGroup.add(insHiddenButton);
        
        fields.put("medicareButton", medicareButton);
        fields.put("medicaidButton", medicaidButton);
        fields.put("champusButton", champusButton);
        fields.put("champvaButton", champvaButton);
        fields.put("groupButton", groupButton);
        fields.put("fecaButton", fecaButton);
        fields.put("otherInsButton", otherInsButton);
        fields.put("insHiddenButton", insHiddenButton);
        
        clientInsPanel.add(medicareButton);
        clientInsPanel.add(medicaidButton);
        clientInsPanel.add(champusButton);
        clientInsPanel.add(champvaButton);
        clientInsPanel.add(groupButton);
        clientInsPanel.add(fecaButton);
        clientInsPanel.add(otherInsButton);
        
        clientTabPanel.add(clientInsPanel);
        
        // ***
        
        JPanel relatedPanel = new JPanel();
        
        TitledBorder rTitle;
        rTitle = BorderFactory.createTitledBorder("Patients Condition Related To");
        relatedPanel.setBorder(rTitle);
        
        JCheckBox employCheck = new JCheckBox("Employment (Current or Previous)");
        JCheckBox autoCheck = new JCheckBox("Auto Accident");
        JCheckBox accidentCheck = new JCheckBox("Other Accident");
        
        JTextField autoState = new JTextField(2);
        JLabel autoStateLabel = new JLabel("State:", JLabel.TRAILING);
        
        
        fields.put("employCheck", employCheck);
        fields.put("autoCheck", autoCheck);
        
        fields.put("autoState", autoState);
        fields.put("accidentCheck", accidentCheck);
        
        relatedPanel.add(employCheck);
        relatedPanel.add(autoCheck);
        relatedPanel.add(autoStateLabel);
        relatedPanel.add(autoState);
        relatedPanel.add(accidentCheck);
        
        clientTabPanel.add(relatedPanel);
        
        // ***
        
        JPanel insuredsInfoPanel = new JPanel();
         
         insuredsInfoPanel.setPreferredSize(new Dimension(900,275));
         insuredsInfoPanel.setLayout(new FlowLayout());
         
        TitledBorder iiTitle;
        iiTitle = BorderFactory.createTitledBorder("Insured's information");
        insuredsInfoPanel.setBorder(iiTitle);
         
         JPanel insuredsAddPanel = new JPanel();
          
          insuredsAddPanel.setPreferredSize(new Dimension(800,100));
          insuredsAddPanel.setLayout(new FlowLayout());
          TitledBorder iaTitle;
          iaTitle = BorderFactory.createTitledBorder("Insured's Address");
          insuredsAddPanel.setBorder(iaTitle);
          
          JCheckBox sameAddress = new JCheckBox("Same as Client");
          fields.put("sameAddress", sameAddress);
          
          sameAddress.addItemListener(
          
           new ItemListener() {
                public void itemStateChanged(ItemEvent e){
                
                 if(e.getStateChange() == ItemEvent.SELECTED){
                  ((JTextField)fields.get("insuredsAdd")).setEditable(false);
                  ((JTextField)fields.get("insuredsCity")).setEditable(false);
                  ((JTextField)fields.get("insuredsState")).setEditable(false);
                  ((JTextField)fields.get("insuredsZipcode")).setEditable(false);
                  
                 }else{
                 
                  ((JTextField)fields.get("insuredsAdd")).setEditable(true);
                  ((JTextField)fields.get("insuredsCity")).setEditable(true);
                  ((JTextField)fields.get("insuredsState")).setEditable(true);
                  ((JTextField)fields.get("insuredsZipcode")).setEditable(true);
                 
                 }
                }
           }
          );
          
          insuredsAddPanel.add(sameAddress);
          
          insuredsAddPanel.add((JPanel) makeTextLabelPanel("Insured's Address (No. Street)",20,"insuredsAdd")); 
         
          insuredsAddPanel.add((JPanel) makeTextLabelPanel("City",15,"insuredsCity"));
         
          insuredsAddPanel.add((JPanel) makeTextLabelPanel("State",2,"insuredsState"));
         
          insuredsAddPanel.add((JPanel) makeTextLabelPanel("Zipcode",5,"insuredsZipcode"));
         
         
         insuredsInfoPanel.add(insuredsAddPanel);
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insured's ID Number",10,"insuredsID"));
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insured's Name",20,"insuredsName")); 
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Phone",12,"insuredsPhone"));
          
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insureds Ploicy group or FECA #",20,
         "insuredsPolNum"));
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Employers Name or School Name",20,
         "insuredsEmployer"));
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insurance Plan or Program Name",20,
         "insuredsPlan"));
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insured's Birthday MM",2,
         "insuredsBdayMM"));
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insured's Birthday DD",2,
         "insuredsBdayDD"));
         
         insuredsInfoPanel.add((JPanel) makeTextLabelPanel("Insured's Birthday YY",2,
         "insuredsBdayYY"));
         
         
         JPanel insuredsSexPanel = new JPanel();
         
         JLabel insuredsSexLbl = new JLabel("Insured's Sex", JLabel.TRAILING);
         insuredsSexPanel.add(insuredsSexLbl);
         
         JRadioButton insuredsMaleButton = new JRadioButton("Male");
         JRadioButton insuredsFemaleButton = new JRadioButton("Female");
         JRadioButton insuredsSexHiddenButton = new JRadioButton("Hidden");
         
         
         ButtonGroup insuredsSexGroup = new ButtonGroup();
         
          insuredsSexGroup.add(insuredsMaleButton);
          insuredsSexGroup.add(insuredsFemaleButton);
          insuredsSexGroup.add(insuredsSexHiddenButton);
          
          fields.put("insuredsMaleButton", insuredsMaleButton);
          fields.put("insuredsFemaleButton", insuredsFemaleButton);
          fields.put("insuredsSexHiddenButton", insuredsSexHiddenButton);
        
         insuredsSexPanel.add(insuredsMaleButton);
         insuredsSexPanel.add(insuredsFemaleButton);
         
         insuredsInfoPanel.add(insuredsSexPanel);
          // ***
        
        clientTabPanel.add(insuredsInfoPanel);
        
       
        // ***
        
   clientTabs.addTab("ClientInfo",clientTabPane);
        
        
        // ***
        
        JPanel insTabPanel = new JPanel();
          
          
          JPanel newClaimPanel = new JPanel();
          
          TitledBorder newClaimTitle;
           newClaimTitle = BorderFactory.createTitledBorder("New Claim");
           newClaimPanel.setBorder(newClaimTitle);
   
           newClaimPanel.add((JPanel) makeTextLabelPanel("Date of service mm/dd/yy",8,"claimDate"));
           newClaimPanel.add((JPanel) makeTextLabelPanel("Billed Charge",15,"claimAmt"));
           newClaimPanel.add((JPanel) makeTextLabelPanel("Diagnosis",6,"claimDiag"));
           newClaimPanel.add((JPanel) makeTextLabelPanel("Procedure",6,"claimProc"));
           newClaimPanel.add((JPanel) makeTextLabelPanel("Place of Service", 2,"claimPlace"));
          
          JButton claimButton = new JButton("Add claim");
    
          claimButton.addActionListener
          (
            new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    Clients clients = new Clients();
                    clients.addClaim((String) fields.getCurrentClientIDField(), 
                                     (String) ((JTextField) fields.get("claimDate")).getText(), 
                                     (String) ((JTextField) fields.get("claimDiag")).getText(),
                                     (String) ((JTextField) fields.get("claimAmt")).getText(),
                                     (String) ((JTextField) fields.get("claimProc")).getText(),
									 (String) ((JTextField) fields.get("claimPlace")).getText());
                                     
                                     
                   //System.out.println("add claim");
                   saveEverything(false);
                   populateClaimList();
                }
            }
          );
          
          newClaimPanel.add(claimButton);
    
    
          insTabPanel.add(newClaimPanel);
          
        // ***
        
        JPanel betterClaimPanel = new JPanel();
          
          TitledBorder betterClaimTitle;
           betterClaimTitle = BorderFactory.createTitledBorder("Claims");
           betterClaimPanel.setBorder(betterClaimTitle);
        
        
          String columnNames[] = {"Service Date","Diagnosis","Billed Ammount",
                                    "Submitted","Paid","Serial"};
                                    
          
          
        
        insTabPanel.add(betterClaimPanel);
        
        // ***
        
          JPanel claimPanel = new JPanel();
          
          TitledBorder claimTitle;
           claimTitle = BorderFactory.createTitledBorder("Claims");
           claimPanel.setBorder(claimTitle);
           
            // create a scrolling list to show the billed and need to be billed 
            // sessions
            
            
            claimList = new JList();
            JScrollPane listScroller = new JScrollPane(claimList);
            listScroller.setPreferredSize(new Dimension(450, 200));
            
            claimPanel.add(listScroller);
          
          // ***
            
          JPanel claimButtonPanel = new JPanel();
           claimButtonPanel.setPreferredSize(new Dimension(200,250));
           
          JButton printButton = new JButton("Print Insurance Form");
    
          printButton.addActionListener
          (
            new ActionListener() {
                public void actionPerformed(ActionEvent e){
                   
                  int[] toPrint = claimList.getSelectedIndices();
                  
                  
                  ArrayList toBill = new ArrayList();
                  
                  for (int i = 0; i < toPrint.length; i++) { 
                   String clm = (String) claimList.getModel().getElementAt(toPrint[i]);
                   Long claimID = new Long(clm.substring((clm.length() - 13)));
                   //System.out.println("adding claimID to arraylist: " + claimID);
                   toBill.add(claimID);
                  }
                  
                  //System.out.println("printing form");
                  CMS1500 foo = new CMS1500(fields.getCurrentClientIDField(), toBill);
                  //System.out.println("printed form");
                  
                  // ask if we should set these as submitted
                  
                  setSubmitted(true);
                  
                  
                  
                  
                }
            }
          );

          claimButtonPanel.add(printButton);
           
          // ***
          
          JButton paidButton = new JButton("Mark as paid");
          
          paidButton.addActionListener
          (
           new ActionListener(){
            public void actionPerformed(ActionEvent e){
             
             int[] toPrint = claimList.getSelectedIndices();
                  
                  for (int i = 0; i < toPrint.length; i++) {
                   
                   String claimInfo = (String) (claimList.getModel().getElementAt(toPrint[i]));
                   Long claimID = new Long(claimInfo.substring((claimInfo.length() - 13)));
                   //System.out.println("claimID: " + claimID);
                   
                   clients.updateClaim((String) fields.getCurrentClientIDField(),
                                            claimID,
                                            "paid", "Paid");
                                            
                    //System.out.println("pay: " + claimInfo);
                   
                   populateClaimList();
                   
                   
                  }
             
            }
           }
          );
          
          claimButtonPanel.add(paidButton);
          
          // ***
          
          JButton unpaidButton = new JButton("Mark as unpaid");
          
          unpaidButton.addActionListener
          (
           new ActionListener(){
            public void actionPerformed(ActionEvent e){
             
             int[] toPrint = claimList.getSelectedIndices();
                  
                  for (int i = 0; i < toPrint.length; i++) { 
                   
                   String claimInfo = (String) (claimList.getModel().getElementAt(toPrint[i]));
                   Long claimID = new Long(claimInfo.substring((claimInfo.length() - 13)));
                   //System.out.println("claimID: " + claimID);
                   
                   clients.updateClaim((String) fields.getCurrentClientIDField(),
                                            claimID,
                                            "paid", "Not Paid");
                                            
                    //System.out.println("unpaid: " + claimInfo);
                   
                   populateClaimList();
                   
                   
                  }
             
            }
           }
          );
          
          claimButtonPanel.add(unpaidButton);
          
          // ***
          
          JButton subButton = new JButton("Mark as submitted");
          
          subButton.addActionListener
          (
           new ActionListener(){
            public void actionPerformed(ActionEvent e){
             
             int[] toPrint = claimList.getSelectedIndices();
                  
                  for (int i = 0; i < toPrint.length; i++) { 
                   
                   String claimInfo = (String) (claimList.getModel().getElementAt(toPrint[i]));
                   Long claimID = new Long(claimInfo.substring((claimInfo.length() - 13)));
                   //System.out.println("claimID: " + claimID);
                   
                   clients.updateClaim((String) fields.getCurrentClientIDField(),
                                            claimID,
                                            "submitted", "Submitted");
                                            
                    //System.out.println("submitted: " + claimInfo);
                   
                   populateClaimList();
                   
                   
                  }
             
            }
           }
          );
          
          claimButtonPanel.add(subButton);
          
          // ***
          
          JButton notSubButton = new JButton("Mark as not submitted");
          
          notSubButton.addActionListener
          (
           new ActionListener(){
            public void actionPerformed(ActionEvent e){
             
             int[] toPrint = claimList.getSelectedIndices();
                  
                  for (int i = 0; i < toPrint.length; i++) { 
                   
                   String claimInfo = (String) (claimList.getModel().getElementAt(toPrint[i]));
                   Long claimID = new Long(claimInfo.substring((claimInfo.length() - 13)));
                   //System.out.println("claimID: " + claimID);
                   
                   clients.updateClaim((String) fields.getCurrentClientIDField(),
                                            claimID,
                                            "submitted", "Not Submitted");
                                            
                    //System.out.println("not submitted: " + claimInfo);
                   
                   populateClaimList();
                   
                   
                  }
             
            }
           }
          );
          
          claimButtonPanel.add(notSubButton);
          // ***
          
          JButton delClaimButton = new JButton("Delete Claim");
          
          delClaimButton.addActionListener
          (
           new ActionListener(){
            public void actionPerformed(ActionEvent e){
             
             int[] toPrint = claimList.getSelectedIndices();
                  
                  for (int i = 0; i < toPrint.length; i++) { 
                   
                   String claimInfo = (String) (claimList.getModel().getElementAt(toPrint[i]));
                   Long claimID = new Long(claimInfo.substring((claimInfo.length() - 13)));
                   //System.out.println("claimID: " + claimID);
                   
                   removeClaim(claimID);
                   
                  }
             
            }
           }
          );
          
          claimButtonPanel.add(delClaimButton);
          
          // ***
          
          claimPanel.add(claimButtonPanel);
          
          // ***
          
          insTabPanel.add(claimPanel);
        
        // ********
       
   
        clientTabs.addTab("Insurance", insTabPanel);
        
        invoicePanel invoiceTabPanel = new invoicePanel();  
        clientTabs.addTab("Invoice", invoiceTabPanel);
        
        // ***
        // ***
        // ***   
        // ***
   
        this.getContentPane().add(clientTabs);
        
        this.pack();
        
        // Add window listener.
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    KCTherapistFrame.this.windowClosed();
                }
            }
        );  
    }
    
    
    /**
     * Shutdown procedure when run as an application.
     */
    protected void windowClosed() {
   	
    	int n = JOptionPane.showConfirmDialog(
        this,
        "Quit?",
        "Confirm Quit dialog",
        JOptionPane.YES_NO_OPTION);
    
      if(n == 0){
        //System.out.println("quit confirmed");
        System.exit(0);
      }
       
    }
    
    // ***
    
    void removeClaim(Long claimID){
    
     Clients clients = Clients.getInstance();
     
       int n = 1;
        n = JOptionPane.showConfirmDialog(
        this,
        "Remove " + claimID + "?",
        "Confirm Remove dialog",
        JOptionPane.YES_NO_OPTION);
        
        
        if(n == 0){
         clients.removeClaim((String) fields.getCurrentClientIDField(),claimID);
         //System.out.println("removed: " + claimID);
         populateClaimList();
        }
    
    
    }
    // ***
    
    void saveValues(){
     
     Clients clients = Clients.getInstance();
     //String[] infoFields = FieldInfo.getInfoFields();
     String clientID = fields.getCurrentClientIDField();
     
     if(clientID != null){
        
      for( Enumeration e = fields.keys(); e.hasMoreElements();){
      
        String objKey = (String) e.nextElement();
     	Object foo = (Object) fields.get(objKey);
      	
      	if(foo instanceof JTextField){
       	 clients.updateClient(clientID, objKey, (String) ((JTextField)foo).getText());
      	 // System.out.println(infoFields[i] + " : " + (String) foo.getText());
        }
        if(foo instanceof AbstractButton){
         if(((AbstractButton)foo).isSelected()){   
          clients.updateClient(clientID, objKey, "checked");  
         }else{
          clients.updateClient(clientID, objKey, "notchecked");
         }
        }
      }
     }
    }
    
    // ***
    
    void saveEverything(boolean confirm){
        Clients clients = Clients.getInstance();
        int n = 1;
        
        if(confirm){
         n = JOptionPane.showConfirmDialog(
         this,
         "Save?",
         "Confirm Save dialog",
         JOptionPane.YES_NO_OPTION);
        
        
        if(n == 0){
         saveValues();
         repopulateClientCombo();
         clients.saveClients();
        }else{
         return;
        }
       }else{
        clients.saveClients();
       }
    }
    
    // ***
    
    void addClientToCombo(String clientID){
    	
    	Clients clients = Clients.getInstance();
    	
    	clientCombo.addItem(clientID + ":  " +
             (String) clients.getClientField(clientID, "Last Name") + "," +
             (String) clients.getClientField(clientID, "First Name")) ;
    }
    
    // ***
    
    void populateClaimList(){
    
        Clients client = Clients.getInstance();
        
        DefaultListModel listModel = new DefaultListModel();
        
        claims = clients.getClaims((String) fields.getCurrentClientIDField());
        
        Long[] foo = clients.getSortedClaimsByDate((String) fields.getCurrentClientIDField());
        
        if(foo != null){
        
         for(int i = 0; i < foo.length; i++){
         
          Hashtable clms = ((Hashtable)claims.get(foo[i]));
          
          if(clms != null){
              
               String listElement = new String((String) clms.get("claimDate")
                                             + "  " +
                                             (String) clms.get("claimDiag")
                                             + "  " +
                                             (String) clms.get("claimAmt")
                                             + "  " +
                                             (String) clms.get("submitted")
                                             + "  " +
                                             (String) clms.get("paid")
                                             + "  " +
											 (String) clms.get("claimPlace")
											 + "  " +
                                             (Long) foo[i]
                                             );  
                 //System.out.println("adding " + listElement + " to claim list");   
               listModel.addElement(listElement);
              }
          
          
         
         }
        }
         
        claimList.setModel(listModel);
    }
    
    // ***
    
    void populateClientCombo(){
    	
    	Clients clients = Clients.getInstance();
    	
    	//System.out.println("populating the client combox box");
    	
    	clientCombo.removeAllItems();
    	
    	
    	if(clients.clientHash != null){ 
    	
         String[] byName = ((String[])clients.getSortedHashKeysByName());
         for (int i = 0; i < byName.length; i++) {
        
         //System.out.println(byName[i]);
         
         addClientToCombo(byName[i]);
        
         }
        }
    	
    	
    	if (clients.clientHash != null){
    	// for( Enumeration e = clients.clientHash.keys(); e.hasMoreElements();){
    		
    		// System.out.println("adding " + e + "to combo box");
        	
        //	String clientID = (String) e.nextElement();
        //    addClientToCombo(clientID);
        // }
        }
        
    }
    
    //***
    
    void repopulateClientCombo(){
    	
    	String clientID = fields.getCurrentClientIDField();
    	
    	//System.out.println("calling reselect with " + clientID);
    	
    	populateClientCombo();
        selectClientCombo(clientID);
        
    }
    
    //***
    
    void selectClientCombo(String clientID){
    	
    	Clients clients = Clients.getInstance();
    	
    	 //System.out.println("reselecting " + clientID + " in the combo box");
    	
    	clientCombo.setSelectedItem(clientID + ":  " +
             (String) clients.getClientField(clientID, "Last Name") + "," +
             (String) clients.getClientField(clientID, "First Name")) ;
    	
    }
    
    // ***
    
    void selectHiddenButtons(){
        
     ((JRadioButton)fields.get("relationHiddenButton")).setSelected(true);
     ((JRadioButton)fields.get("maritalHiddenButton")).setSelected(true);
     ((JRadioButton)fields.get("sexHiddenButton")).setSelected(true);
     ((JRadioButton)fields.get("insHiddenButton")).setSelected(true);
     ((JRadioButton)fields.get("insuredsSexHiddenButton")).setSelected(true);
    }
    
    // ***
    
    void populatePanes(String clientID){
    	
  	  Clients clients = Clients.getInstance();
      //String[] infoFields = FieldInfo.getInfoFields();
  
     
     // set the hidden radio buttons to selected, then let the visible
     // ones come out if they are there
     
     selectHiddenButtons();
     
      //for ( int i = 0; i < infoFields.length; i++){
      for( Enumeration e = fields.keys(); e.hasMoreElements();){
        
        String objKey = (String) e.nextElement();
        
        
      	String fieldText = (String) clients.getClientField(clientID,objKey);
      	// System.out.println("setting field " + infoFields[i] + " to " + fieldText);
     	Object foo  = ((Object) fields.get(objKey));
     	if (foo instanceof JTextField){
     	  ((JTextField)foo).setText(fieldText);
        }
        if (foo instanceof AbstractButton){
         if (fieldText != null){ 
          if (fieldText.equals("checked")){
           ((AbstractButton)foo).setSelected(true);
           //System.out.println("setting " + objKey + " to checked");
          }else{
           ((AbstractButton)foo).setSelected(false);
           //System.out.println("setting " + objKey +  " unchecked");
          }
         }else{
          ((AbstractButton)foo).setSelected(false);  
          //System.out.println("no fieldText defined setting to unselected");   
         } 
        }
      }  
    	
    }
    
    // ***
    
    void clearPanes(){
    	
     
      //System.out.println("clearing fields");
      
      selectHiddenButtons();
      
      for( Enumeration e = fields.keys(); e.hasMoreElements();){
        String objKey = (String) e.nextElement();
        
        Object foo = (Object) fields.get(objKey);
        
        if (foo instanceof JTextField){
          ((JTextField)foo).setText("");
        }else{
        if (foo instanceof AbstractButton){
            ((AbstractButton)foo).setSelected(false);
         }
        }
      }
      	
    }
    
    // ***
    
    void newClient(){
    	
        Date today;
        String dateOut;
        DateFormat dateFormatter;
        Locale curLocale = new Locale("en","US");

        dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
                       curLocale);
                       
        today = new Date();
        dateOut = dateFormatter.format(today);  
    	
    	String s = (String)JOptionPane.showInputDialog(
                    this,
                    "New Client ID",
                    "New Client ID Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
       
       if (s != null){
       	((JTextField) fields.get("Client ID")).setText(s);
       	((JTextField) fields.get("clientCreateDate")).setText(dateOut);
        saveValues();
        repopulateClientCombo();
        addClientToCombo(s);
        populatePanes(s);
       }
   
   }
   
   // ***

   void changeClientID(){
   	
   	 String clientID = fields.getCurrentClientIDField();
   	 
   	 String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Rename Client " + clientID + " to:",
                    "Rename Client ID Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
   	
   	if (s != null){
       	((JTextField) fields.get("Client ID")).setText(s);
        saveValues();
        removeClientID(clientID, false);
        repopulateClientCombo();
        populatePanes(s);
       }
   }
    
   /// ***
   
   void removeClientID(String clientID, boolean confirm){
   	
   	Clients clients = Clients.getInstance();
   	int n;
   	
   	if(confirm){
   	
   	   n = JOptionPane.showConfirmDialog(
     	this,
    	"Remove ClientID " + clientID + " and all associated info?",
    	"Confirm ClientID removal",
    	JOptionPane.YES_NO_OPTION);
    
      if(n == 0){
        //System.out.println("remove confirmed");
      }else{
        //System.out.println("removed cancled");
        return;  
      }
    }
    
    clearPanes();
    //System.out.println("removing " + clientID);
    clients.clientHash.remove(clientID);
   
   }

    // ***
    
   JPanel makeTextLabelPanel(String label, int textLen){
   
    return (JPanel) makeTextLabelPanel(label, textLen, null);
  
   }
  
    // ***
  
   JPanel makeTextLabelPanel(String label, int textLen, String hashKey){
    
     JPanel fooPanel = new JPanel();
     
     JTextField bar = new JTextField(textLen);
         JLabel barLbl = new JLabel(label,JLabel.TRAILING);
         barLbl.setLabelFor(bar);
     
     if(hashKey == null){
        fields.put(label, bar);
     }else{    
        fields.put(hashKey, bar);
     }
         
     fooPanel.add(barLbl);
     fooPanel.add(bar); 
     
     return (JPanel) fooPanel;
    
   } 
   
   //***
   
   JPanel makeTextLabelPanelProvider(String label, int textLen, String hashKey){
   
    JPanel fooPanel = new JPanel();
    
    JTextField bar = new JTextField(textLen);
         JLabel barLbl = new JLabel(label,JLabel.TRAILING);
         barLbl.setLabelFor(bar);
     
     if(hashKey == null){
        provFieldHash.put(label, bar);
     }else{    
        provFieldHash.put(hashKey, bar);
     }
         
     fooPanel.add(barLbl);
     fooPanel.add(bar); 
     
     return (JPanel) fooPanel; 
   
   }
   
   // ***
   void configProvider(){
               
       final JDialog piDialog = new JDialog(this, "Provider Info", false);
       
       // here we put stuff thats specific to the provider, address
       // ssn/tax number etc
       
       JPanel piPanel = new JPanel();
       piPanel.setPreferredSize(new Dimension(400, 300));
       
       piPanel.add(
        (JPanel) makeTextLabelPanelProvider("Provider Name",20, "provName")
       );
       
       piPanel.add(
        (JPanel) makeTextLabelPanelProvider("Provider Address 1",20, "provAdd1")
       );
       
       piPanel.add(
        (JPanel) makeTextLabelPanelProvider("Provider Address 2",20, "provAdd2")
       );
       
       //piPanel.add(
       // (JPanel) makeTextLabelPanelProvider("Provider Address 3",20, "provAdd3")
       //);
       
       piPanel.add(
        (JPanel) makeTextLabelPanelProvider("Tax # or SSN",20, "provTaxSSN")
       );
       
       
       JPanel stButtonPanel = new JPanel();
       
        JRadioButton einButton = new JRadioButton("EIN");
        JRadioButton ssnButton = new JRadioButton("SSN");
        
        ButtonGroup einSSNGroup = new ButtonGroup();
        einSSNGroup.add(einButton);
        einSSNGroup.add(ssnButton);
        
        provFieldHash.put("ssnButton",ssnButton);
        provFieldHash.put("einButton",einButton);
                
        stButtonPanel.add(einButton);
        stButtonPanel.add(ssnButton);
       
        piPanel.add(stButtonPanel);
       
       JPanel buttonPanel = new JPanel();
       
       JButton saveButton = new JButton("Save");
       
       saveButton.addActionListener
        (
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
             saveProviderConfig();
            }
         }
        );
       
       buttonPanel.add(saveButton);
       
       JButton closeButton = new JButton("Close");
       
       closeButton.addActionListener
        (
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
             piDialog.setVisible(false);
             piDialog.dispose();
            }
         }
        );
       
       buttonPanel.add(closeButton);
       
       piPanel.add(buttonPanel);
       
       piDialog.getContentPane().add(piPanel);
       
       piDialog.pack();
       piDialog.setVisible(true); 
   }

// **********

   void saveProviderConfig(){
   
   //System.out.println("saveProviderConfig");
   
    Provider provider = Provider.getInstance();
    
    for( Enumeration e = provFieldHash.keys(); e.hasMoreElements();){
    
     String objKey = (String) e.nextElement();
    
     Object foo = (Object) provFieldHash.get(objKey);
        if(foo instanceof JTextField){
         provider.updateProperty(objKey, (String) ((JTextField)foo).getText());
          //System.out.println( (String) ((JTextField)foo).getText());
        }
        if(foo instanceof AbstractButton){
         if(((AbstractButton)foo).isSelected()){   
          provider.updateProperty(objKey, "checked");  
         }else{
          provider.updateProperty(objKey, "notchecked");
         }
        }
    }
   
    provider.saveInstance();
   
   }
   
// **********

   void populateProviderConfig(){
    
    Provider provider = Provider.getInstance();
    
    for( Enumeration e = provFieldHash.keys(); e.hasMoreElements();){
   
     String objKey = (String) e.nextElement();
     
     String fieldText = (String) provider.getProperty(objKey);
     
     Object foo  = ((Object) provFieldHash.get(objKey));
     
        if (foo instanceof JTextField){
          ((JTextField)foo).setText(fieldText);
        }else{
         if (foo instanceof AbstractButton){
          if (fieldText != null){ 
           if (fieldText.equals("checked")){
            ((AbstractButton)foo).setSelected(true);
            //System.out.println("setting " + objKey + " to checked");
           }else{
            ((AbstractButton)foo).setSelected(false);
            //System.out.println("setting " + objKey +  " unchecked");
           }
          }else{
           ((AbstractButton)foo).setSelected(false);  
           //System.out.println("no fieldText defined setting to unselected");   
          } 
         }
        }
    
    }
   
   }
   
  
// **********

  void setSubmitted(){
  
   int[] toSub = claimList.getSelectedIndices();
                  
    for (int i = 0; i < toSub.length; i++) {
                   
     String claimInfo = (String) (claimList.getModel().getElementAt(toSub[i]));
     Long claimID = new Long(claimInfo.substring((claimInfo.length() - 13)));
     //System.out.println("claimID: " + claimID);
                    
     clients.updateClaim((String) fields.getCurrentClientIDField(),
                                            claimID,
                                            "submitted", "Submitted");
                                            
     //System.out.println("pay: " + claimInfo);
    
     populateClaimList();
                   
                   
    } 
   
  } 

// **********

 void setSubmitted(boolean confirm){
 
  if(confirm){
   int n = JOptionPane.showConfirmDialog(
                            this,
                            "Mark as submitted?",
                            "Confirm Submit dialog",
                            JOptionPane.YES_NO_OPTION);
    
                  if(n == 0){ 
                  
                   setSubmitted(); 
                    
                  }
  }
  
 }
   
}
