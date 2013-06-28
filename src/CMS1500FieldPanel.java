import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/*
 * Created on Dec 21, 2004
 */

public class CMS1500FieldPanel extends JPanel{
	
	SSLayout ssl = new SSLayout(0, 3, 14);
	 
	public static Hashtable fieldHash = new Hashtable();
	
	Hashtable getFieldHash(){
		return (Hashtable) fieldHash;
	}
	
	public CMS1500FieldPanel(){
	
		System.out.println("arg");
		
	  setLayout(ssl);
	
	  ssl.setColumnType(0, SSLayout.FIXED,300);
	  ssl.setColumnType(1, SSLayout.FIXED, 300);
	  ssl.setColumnType(2, SSLayout.FIXED, 300);
	 
	  
	  ssl.setRowType(0, SSLayout.FIXED, 60);
	  ssl.setRowType(1, SSLayout.FIXED, 60);
	  ssl.setRowType(2, SSLayout.FIXED, 60);
	  ssl.setRowType(3, SSLayout.FIXED, 60);
	  ssl.setRowType(4, SSLayout.FIXED, 60);
	  ssl.setRowType(5, SSLayout.FIXED, 60);
	  ssl.setRowType(6, SSLayout.FIXED, 60);
	  ssl.setRowType(7, SSLayout.FIXED, 60);
	  ssl.setRowType(8, SSLayout.FIXED, 60);
	  ssl.setRowType(9, SSLayout.FIXED, 60);
	  ssl.setRowType(10, SSLayout.FIXED, 60);
	  ssl.setRowType(11, SSLayout.FIXED, 60);
	  ssl.setRowType(12, SSLayout.FIXED, 60);
	  ssl.setRowType(13, SSLayout.FIXED, 60);
	  // so, we make all the damn fields, and put them into the SSLayout
	  // ug.
	  
	  JPanel relationPanel = new JPanel();
      TitledBorder rpTitle;
      rpTitle = BorderFactory.createTitledBorder("Client Info");
      setBorder(rpTitle);
	  // box 1
      
	  // this is the expected way the boxes will get handled
	  // build a panel to add to the ssl and fill it with either
	  // label/text or with button groups.
	  
	  JPanel b1P = makeBoxPanel("Insurance Type");
	  
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
      
      fieldHash.put("medicareButton", medicareButton);
      fieldHash.put("medicaidButton", medicaidButton);
      fieldHash.put("champusButton", champusButton);
      fieldHash.put("champvaButton", champvaButton);
      fieldHash.put("groupButton", groupButton);
      fieldHash.put("fecaButton", fecaButton);
      fieldHash.put("otherInsButton", otherInsButton);
      fieldHash.put("insHiddenButton", insHiddenButton);
      
      b1P.add(medicareButton);
      b1P.add(medicaidButton);
      b1P.add(champusButton);
      b1P.add(champvaButton);
      b1P.add(groupButton);
      b1P.add(fecaButton);
      b1P.add(otherInsButton);
      
      // now add the box panel to the ssl
      
      add(b1P, new Rectangle(0,0,2,1));
      
      // ***
	  // box 1a 
	  
      JPanel b1aP = makeBoxPanel("Insureds ID Number");
      b1aP.add((JPanel) makeTextLabelPanel(10,"insuredsID"));
      add(b1aP, new Rectangle(2,0,1,1));
      
      // ***
      // box 2
      
      JPanel b2P = makeBoxPanel("Patients Name (Last, First, MI)");
      b2P.add((JPanel) makeTextLabelPanel(25,"patientsName"));
      add(b2P,	new Rectangle(0,1,1,1));
      
      // ***
      // box3 patients birth date and sex
      
       JPanel b3P = makeBoxPanel("Patients Birth Date MM DD YY");
       
       JPanel foo = (JPanel) makeTextLabelPanel(2,"patientsBDAY");
       JTextField DD = new JTextField(2);
       JTextField YY = new JTextField(2);
       foo.add(DD);
       foo.add(YY);
       
       b3P.add(foo);
             
       JRadioButton maleButton = new JRadioButton("Male");
       JRadioButton femaleButton = new JRadioButton("Female");
       JRadioButton sexHiddenButton = new JRadioButton("Hidden");
      
       ButtonGroup sexGroup = new ButtonGroup();
       sexGroup.add(maleButton);
       sexGroup.add(femaleButton);
       sexGroup.add(sexHiddenButton);
      
       fieldHash.put("maleButton", maleButton);
       fieldHash.put("femaleButton", femaleButton);
       fieldHash.put("sexHiddenButton", sexHiddenButton);
       
       b3P.add(maleButton);
       b3P.add(femaleButton);
       // leave hidden one out
      
       add(b3P, new Rectangle(1,1,1,1));
       
      // ***
      // box 4 insured name
       
       JPanel b4P = makeBoxPanel("Insured's Name (Last, First, MI)");
       
       b4P.add((JPanel) makeTextLabelPanel(20,"insuredsName"));
       add(b4P,	new Rectangle(2,1,1,1));
       
      // ***
      // box 5 patients address
       
       JPanel b5Pa = makeBoxPanel("Patients Address (No. Street)");
       b5Pa.add((JPanel) makeTextLabelPanel(20,"Street")); 
       
       JPanel b5Pb = makeBoxPanel("Patients Address (City)(State)");
       b5Pb.add((JPanel) makeTextLabelPanel(15,"City"));
       b5Pb.add((JPanel) makeTextLabelPanel(2,"State"));
       
       JPanel b5Pc = makeBoxPanel("Patients Address (Zipcode)(Phone)");
       b5Pc.add((JPanel) makeTextLabelPanel(5,"Zip"));
       b5Pc.add((JPanel) makeTextLabelPanel(12,"Phone"));
       
       add(b5Pa, new Rectangle(0,2,1,1));
       add(b5Pb, new Rectangle(0,3,1,1));
       add(b5Pc, new Rectangle(0,4,1,1));
       
       // ***
       // box 6 patients relation to insured
       
       JPanel b6P = makeBoxPanel("Patients relation to insured");
       
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
       
       fieldHash.put("relationSelfButton", relationSelfButton);
       fieldHash.put("relationSpouseButton", relationSpouseButton);
       fieldHash.put("relationChildButton", relationChildButton);
       fieldHash.put("relationOtherButton", relationOtherButton);
       fieldHash.put("relationHiddenButton", relationHiddenButton);
       
       // set the hidden button to selected, and then dont add it to the panel
       // this will let us have a way to unselect all the options
       
       relationHiddenButton.setSelected(true);
       
       b6P.add(relationSelfButton);
       b6P.add(relationSpouseButton);
       b6P.add(relationChildButton);
       b6P.add(relationOtherButton);
         
       add(b6P, new Rectangle(1,2,1,1));
       
       
       // ***
       // box 7 insureds address
  
       JPanel b7Pa = makeBoxPanel("Insured's Address (No. Street)");
       b7Pa.add((JPanel) makeTextLabelPanel(20,"insuredsAdd")); 
       
       JPanel b7Pb = makeBoxPanel("Insureds Address (City) (State)");
       b7Pb.add((JPanel) makeTextLabelPanel(15,"insuredsCity"));
       b7Pb.add((JPanel) makeTextLabelPanel(2,"insuredsState"));
       
       JPanel b7Pc = makeBoxPanel("Insureds Address (Zipcode) (Phone)");
       b7Pc.add((JPanel) makeTextLabelPanel(5,"insuredsZipcode"));
       b7Pc.add((JPanel) makeTextLabelPanel(12,"insuredsPhone"));
       
       add(b7Pa, new Rectangle(2,2,1,1));
       add(b7Pb, new Rectangle(2,3,1,1));
       add(b7Pc, new Rectangle(2,4,1,1));
       
       
       
       
       // ***
       // box 8 patients status
       
       JPanel b8P = makeBoxPanel("Patient Status");
        
        JRadioButton singleButton = new JRadioButton("Single");
        JRadioButton marriedButton = new JRadioButton("Married");
        JRadioButton otherMaritalButton = new JRadioButton("Other");
        JRadioButton maritalHiddenButton = new JRadioButton("Hidde");
       
        ButtonGroup maritalGroup = new ButtonGroup();
        maritalGroup.add(singleButton);
        maritalGroup.add(marriedButton);
        maritalGroup.add(otherMaritalButton);
        maritalGroup.add(maritalHiddenButton);
       
        fieldHash.put("singleButton", singleButton);
        fieldHash.put("marriedButton", marriedButton);
        fieldHash.put("otherMaritalButton", otherMaritalButton);
        fieldHash.put("maritalHiddenButton", maritalHiddenButton);
       
        maritalHiddenButton.setSelected(true);
        
        b8P.add(singleButton);
        b8P.add(marriedButton);
        b8P.add(otherMaritalButton);
       
        add(b8P, new Rectangle(1,3,1,1));
        
        JPanel b8Pb = new JPanel();
       
        JCheckBox employedCheck = new JCheckBox("Employed");
        JCheckBox partStuCheck = new JCheckBox("PT Student");
        JCheckBox fullStuCheck = new JCheckBox("FT Student");
       
        fieldHash.put("employedCheck", employedCheck);
        fieldHash.put("partStuCheck", partStuCheck);
        fieldHash.put("fullStuCheck", fullStuCheck);
       
        // no hidden checkbox, we can unselect them all we like
       
        b8Pb.add(employedCheck);
        b8Pb.add(partStuCheck);
        b8Pb.add(fullStuCheck);
        
       add(b8Pb, new Rectangle(1,4,1,1));
       
       // ***
       // box 9 other insureds name
      
       JPanel b9P = makeBoxPanel("Other Insureds Name (Last, First, MI)");
       
       b9P.add((JPanel) makeTextLabelPanel(20,"otherName"));
       add(b9P,	new Rectangle(0,5,1,1));
       
       // ***
       // box 9a other insureds policy or group no
       
       JPanel b9aP = makeBoxPanel("Other insureds Policy or Group No.");
       b9aP.add((JPanel) makeTextLabelPanel(20,"otherPolNum"));
       
       add(b9aP, new Rectangle(0,6,1,1));
       
       // ***
       // box 9b other insureds date of birth
       
       JPanel b9bP = makeBoxPanel("Other Insured's Date of Birth");
       
       JPanel otherFoo = (JPanel) makeTextLabelPanel(2,"otherBdayMM");
       JTextField otherBdayDD = new JTextField(2);
       JTextField otherBdayYY = new JTextField(2);
       otherFoo.add(otherBdayDD);
       otherFoo.add(otherBdayYY);
       
       b9bP.add(otherFoo);
             
       JRadioButton otherMaleButton = new JRadioButton("Male");
       JRadioButton otherFemaleButton = new JRadioButton("Female");
       JRadioButton otherSexHiddenButton = new JRadioButton("Hidden");
      
       ButtonGroup otherSexGroup = new ButtonGroup();
       sexGroup.add(otherMaleButton);
       sexGroup.add(otherFemaleButton);
       sexGroup.add(otherSexHiddenButton);
      
       fieldHash.put("otherMaleButton", otherMaleButton);
       fieldHash.put("otherFemaleButton", otherFemaleButton);
       fieldHash.put("otherSexHiddenButton", otherSexHiddenButton);
       
       b9bP.add(otherMaleButton);
       b9bP.add(otherFemaleButton);
       // leave hidden one out
      
       add(b9bP, new Rectangle(0,7,1,1));
       
       // ***
       // box 9c Employer's name or School Name
       
       JPanel b9cP = makeBoxPanel("Employer's Name or School Name");
       
       b9cP.add((JPanel) makeTextLabelPanel(20,"otherEmploySchool"));
       
       add(b9cP, new Rectangle(0,8,1,1));
       
       // ***
       // box 9d Insurance plan name or program
       
       JPanel b9dP = makeBoxPanel("Insurance Plan Name or Program Name");
       
       b9dP.add((JPanel) makeTextLabelPanel(20,"otherPlan"));
       
       add(b9dP, new Rectangle(0,9,1,1));
       
       // ***
       // box 10 patients condition related to
       
       JLabel conditionLbl = new JLabel("Patients Condition Related To");
       add(conditionLbl, new Rectangle(1,5,1,1));
       
       JPanel b10aP = new JPanel();
       JCheckBox employCheck = new JCheckBox("Employment (Current or Previous)");
       b10aP.add(employCheck);
       add(b10aP, new Rectangle(1,6,1,1));
       
       JPanel b10bP = new JPanel();
        JCheckBox autoCheck = new JCheckBox("Auto Accident");
        JTextField autoState = new JTextField(2);
        JLabel autoStateLabel = new JLabel("State:", JLabel.TRAILING);
        b10bP.add(autoCheck);
        b10bP.add(autoStateLabel);
        b10bP.add(autoState);
        
       add(b10bP, new Rectangle(1,7,1,1));
       
       JPanel b10cP = new JPanel();
       JCheckBox accidentCheck = new JCheckBox("Other Accident");
       b10cP.add(accidentCheck);
       add(b10cP, new Rectangle(1,8,1,1));
       
       
       fieldHash.put("employCheck", employCheck);
       fieldHash.put("autoCheck", autoCheck);
       
       fieldHash.put("autoState", autoState);
       fieldHash.put("accidentCheck", accidentCheck);
       
       // ***
       // box 10d reserved for local use
       
       // ***
       // box 11 insureds policy group or feca number
       JPanel b11P = makeBoxPanel("Insureds Ploicy group or FECA #");
       
       b11P.add((JPanel) makeTextLabelPanel(20,"insuredsPolNum"));
       
       add(b11P, new Rectangle(2,5,1,1));
       
       // ***
       // box 11a insureds date of birth
       
       JPanel b11aP = makeBoxPanel("Other Insured's Date of Birth");
       
       JPanel insuredsFoo = (JPanel) makeTextLabelPanel(2,"insuredsBdayMM");
       JTextField insuredsBdayDD = new JTextField(2);
       JTextField insuredsBdayYY = new JTextField(2);
       insuredsFoo.add(insuredsBdayDD);
       insuredsFoo.add(insuredsBdayYY);
       
       b11aP.add(insuredsFoo);
             
       JRadioButton insuredsMaleButton = new JRadioButton("Male");
       JRadioButton insuredsFemaleButton = new JRadioButton("Female");
       JRadioButton insuredsSexHiddenButton = new JRadioButton("Hidden");
      
       ButtonGroup insuredsSexGroup = new ButtonGroup();
       sexGroup.add(insuredsMaleButton);
       sexGroup.add(insuredsFemaleButton);
       sexGroup.add(insuredsSexHiddenButton);
      
       fieldHash.put("insuredsMaleButton", otherMaleButton);
       fieldHash.put("insuredsFemaleButton", otherFemaleButton);
       fieldHash.put("insuredsSexHiddenButton", otherSexHiddenButton);
       
       b11aP.add(insuredsMaleButton);
       b11aP.add(insuredsFemaleButton);
       // leave hidden one out
      
       add(b11aP, new Rectangle(2,6,1,1));
      
       // ***
       // box 11b employers name or school name
       
       JPanel b11bP = makeBoxPanel("Employer's Name or School Name");
       
       b11bP.add((JPanel) makeTextLabelPanel(20,"insuredsEmployer"));
       
       add(b11bP, new Rectangle(2,7,1,1));
       
       // ***
       // box 11c insurance plan or program name
       
       JPanel b11cP = makeBoxPanel("Insurance Plan Name or Program Name");
       
       b11cP.add((JPanel) makeTextLabelPanel(20,"insuredsPlan"));
       
       add(b11cP, new Rectangle(2,8,1,1));
       
       // ***
       // box 11d Is there another health benefit plan
       
       JPanel b11dP = makeBoxPanel("Is There Another Health Benefit Plan");
       
       JCheckBox otherCheck = new JCheckBox("Yes");
       fieldHash.put("otherCheck", otherCheck);
       
       b11dP.add(otherCheck);
       
       JLabel fooLbl = new JLabel("If yes, return to and complete 9a-d",JLabel.TRAILING);
       
       b11dP.add(fooLbl);
       
       add(b11dP, new Rectangle(2,9,1,1));
       
       // ***
       // box 12 Patients or authorized persons signature
       
       JPanel b12P = makeBoxPanel("Patient's or Authorized Person's Signature and Date");
       
       b12P.add((JPanel) makeTextLabelPanel(30,"patientSig"));
       b12P.add((JPanel) makeTextLabelPanel(10,"patientSigDate"));
       
       add(b12P, new Rectangle(0,10,2,2));
       
       // ***
       // box 13
      
       JPanel b13P = makeBoxPanel("Insured's or Authorized Person's Signature and Date");
       
       b13P.add((JPanel) makeTextLabelPanel(15,"insuredsSig"));
       b13P.add((JPanel) makeTextLabelPanel(5,"insuredsSigDate"));
       
       add(b13P, new Rectangle(2,10,1,2));
       
       // ***
       //((JTextField)fieldHash.get("Client ID")).setEditable(false);
      
      //add((JPanel) makeTextLabelPanel("clientCreateDate",9));
      
      
      
      //add((JPanel) makeTextLabelPanel("First Name",15)); 
      
      //add((JPanel) makeTextLabelPanel("Middle Initial",2));

      
      // ***
        
        JCheckBox sameAddress = new JCheckBox("Same as Client");
        fieldHash.put("sameAddress", sameAddress);
        
        sameAddress.addItemListener(
        
         new ItemListener() {
              public void itemStateChanged(ItemEvent e){
              
               if(e.getStateChange() == ItemEvent.SELECTED){
                ((JTextField)fieldHash.get("insuredsAdd")).setEditable(false);
                ((JTextField)fieldHash.get("insuredsCity")).setEditable(false);
                ((JTextField)fieldHash.get("insuredsState")).setEditable(false);
                ((JTextField)fieldHash.get("insuredsZipcode")).setEditable(false);
                
               }else{
               
                ((JTextField)fieldHash.get("insuredsAdd")).setEditable(true);
                ((JTextField)fieldHash.get("insuredsCity")).setEditable(true);
                ((JTextField)fieldHash.get("insuredsState")).setEditable(true);
                ((JTextField)fieldHash.get("insuredsZipcode")).setEditable(true);
               
               }
              }
         }
        );
        
        //add(sameAddress);
        
        
       
       
       // ***  
       
             
       
       
       
      
       
        // ***
	  
	}

	// **********
	
	JPanel makeTextLabelPanel(int length, String key){
		return (JPanel) makeTextLabelPanel(null,length,key);
	}
	
	// **********
	
	JPanel makeTextLabelPanel(String label,int length){
		
		return (JPanel) makeTextLabelPanel(label,length,null);
	}
	
	// **********
	
	JPanel makeTextLabelPanel(String label,int length,String key){
		
		Hashtable fieldHash = new Hashtable();
		
		JPanel fooPanel = new JPanel();
	    
	    JTextField bar = new JTextField(length);
	    
	    if(label != null){
	         JLabel barLbl = new JLabel(label,JLabel.TRAILING);
	         barLbl.setLabelFor(bar);
	         fooPanel.add(barLbl);
	    }
	    
	     if(key == null){
	        fieldHash.put(label, bar);
	     }else{    
	        fieldHash.put(key, bar);
	     }
	         
	     
	     fooPanel.add(bar); 
	     
	     return (JPanel) fooPanel;
	}

	// **********
	
	JPanel makeBoxPanel(String label){
		
		JPanel bP = new JPanel();
	       
	       TitledBorder bT;
	       bT = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
	       		label);
	       bP.setBorder(bT);
	       
	       return (JPanel) bP;
	}
}
