
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
 
 public class CMS1500 implements Printable {
 
    private static final double INCH = 72;
    private static final double margin = 25;
    private static final double pageWidth = (8.5 * INCH) - (2 * margin);
    private static final double pageHeight = (11.5 * INCH) - (2 * margin);

    private ArrayList clientClaims;
    private Clients clients = Clients.getInstance();
    private String clientID;
        
    public CMS1500 (String clntID, ArrayList claims) {
 
       // clone claims to the global clientClaims
        clientClaims = claims;
        clientID = clntID;
       //--- Create a printerJob object
       PrinterJob printJob = PrinterJob.getPrinterJob ();

        PageFormat pageFormat = printJob.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(margin,margin, pageWidth, pageHeight);
        pageFormat.setPaper(paper);
        
       
       //--- Show a print dialog to the user. If the user
       //--- clicks the print button, then print, otherwise
       //--- cancel the print job
       
       // can check the printDialog return stuff to see if 
       // we should say its been printed and need to set the claims
       // to been printed in the hash
       
       if (printJob.printDialog()) {
          try {
             printJob.setPrintable(this, pageFormat);
             printJob.print();
          } catch (Exception PrintException) {
             PrintException.printStackTrace();
          }
       }
 
    }
 
 
    /**
     *
     * This class is responsible for rendering a page using
     * the provided parameters. The result will be a grid
     * where each cell will be half an inch by half an inch.
     *
     * @param g a value of type Graphics
     * @param pageFormat a value of type PageFormat
     * @param page a value of type int
     * @return a value of type int
     */
     
     
    
    public int print (Graphics g, PageFormat pageFormat, int page) {
       
       int i;
       Graphics2D g2d;
       Line2D.Double line = new Line2D.Double ();
       
       Provider provider = Provider.getInstance();
       
       //--- Validate the page number, we only print the first page
       if (page == 0) {
 
          //--- Create a graphic2D object and set the default parameters
          g2d = (Graphics2D) g;
          g2d.setColor (Color.black);
 
          //--- Translate the origin to be (0,0)
          g2d.translate (pageFormat.getImageableX (), pageFormat.getImageableY ());
 
        // print all the damn form elements... somehow
        
        String field;
        
        // System.out.println("getting/placing form info");
        
        // box 1
        if(((String) getField("medicareButton")).equals("checked")){
         g2d.drawString("X",2,93);
        }
        if(((String) getField("medicaidButton")).equals("checked")){
         g2d.drawString("X",50,93);
        }
        if(((String) getField("champusButton")).equals("checked")){
         g2d.drawString("X",103,93);
        }
        if(((String) getField("champvaButton")).equals("checked")){
         g2d.drawString("X",168,93);
        }
        if(((String) getField("groupButton")).equals("checked")){
         g2d.drawString("X",217,93);
        }
        if(((String) getField("fecaButton")).equals("checked")){
         g2d.drawString("X",275,93);
        }
        if(((String) getField("otherInsButton")).equals("checked")){
         g2d.drawString("X",320,93);
        }
  
        // 1a  insureds ID
        
        g2d.drawString((String) getField("insuredsID"),360,93);
        
        // box 2 patients name
        
        field = new String((String) getField("Last Name")
                           + " " +
                           (String) getField("First Name")
                           + " " +
                           (String) getField("Middle Initial"));
                           
        g2d.drawString(field,10,115);
        
        // box 3 patients birthdate and sex
        
        g2d.drawString((String) getField("Bday MM"),216,115);
        g2d.drawString((String) getField("Bday DD"),240,115);
        g2d.drawString((String) getField("Bday YY"),260,115);
        if(((String) getField("maleButton")).equals("checked")){
         g2d.drawString("X",298,115);
        }
        if(((String) getField("femaleButton")).equals("checked")){
         g2d.drawString("X",333,115);
        }
        
        // box 4 insureds name
        
        g2d.drawString((String) getField("insuredsName"),360,115);
        
        // box 5 patients address
        
        g2d.drawString((String) getField("Street"),10,140);
        g2d.drawString((String) getField("City"),10,160);
        g2d.drawString((String) getField("State"),185,160);
        g2d.drawString((String) getField("Zip"),10,185);
        g2d.drawString((String) getField("Phone"),105,185);
        
        // box 6 patients relation to insured
        
        if(((String) getField("relationSelfButton")).equals("checked")){
         g2d.drawString("X",230,140);
        }
        if(((String) getField("relationSpouseButton")).equals("checked")){
         g2d.drawString("X",270,140);
        }
        if(((String) getField("relationChildButton")).equals("checked")){
         g2d.drawString("X",300,140);
        }
        if(((String) getField("relationOtherButton")).equals("checked")){
         g2d.drawString("X",335,140);
        }
        
        // box 7 insureds address
        
        // There is the option of selecting the same address 
        // as the client
        
        if(((String) getField("sameAddress")).equals("checked")){
          g2d.drawString((String) getField("Street"),360,140);
          g2d.drawString((String) getField("City"),360,160);
          g2d.drawString((String) getField("State"),530,160);
          g2d.drawString((String) getField("Zip"),360,190);
        }else{
          g2d.drawString((String) getField("insuredsAdd"),360,140);
          g2d.drawString((String) getField("insuredsCity"),360,160);
          g2d.drawString((String) getField("insuredsState"),530,160);
          g2d.drawString((String) getField("insuredsZipcode"),360,190);
        }
        
        g2d.drawString((String) getField("insuredsPhone"),465,190);
        
        // box 8 patients status
        
        if(((String) getField("singleButton")).equals("checked")){
         g2d.drawString("X",245,165);
        }
        if(((String) getField("marriedButton")).equals("checked")){
         g2d.drawString("X",290,165);
        }
        if(((String) getField("otherMaritalButton")).equals("checked")){
         g2d.drawString("X",332,165);
        }
        if(((String) getField("employedCheck")).equals("checked")){
         g2d.drawString("X",245,190);
        }
        if(((String) getField("fullStuCheck")).equals("checked")){
         g2d.drawString("X",290,190);
        }
        if(((String) getField("partStuCheck")).equals("checked")){
         g2d.drawString("X",332,190);
        }
        
        
        // box 9 
        
         // not implemented
         
        // box 10 patients condition related to
        
        if(((String) getField("employCheck")).equals("checked")){
         g2d.drawString("X",245,235);
        }else{
         g2d.drawString("X",290,235);
        }
        if(((String) getField("autoCheck")).equals("checked")){
         g2d.drawString("X",245,260);
         g2d.drawString(((String) getField("autoState")),324,260);
        }else{
         g2d.drawString("X",290,260);
        }
        if(((String) getField("accidentCheck")).equals("checked")){
         g2d.drawString("X",245,283);
        }else{
         g2d.drawString("X",290,283);
        }
        
        // box 11 insured policy group or feca number
        
        g2d.drawString(((String) getField("insuredsPolNum")),360,210);
        
         // box 11a insureds birthday and sex
          
         g2d.drawString((String) getField("insuredsBdayMM"),370,235);
         g2d.drawString((String) getField("insuredsBdayYY"),405,235);
         g2d.drawString((String) getField("insuredsBdayDD"),430,235);
       
         if(((String) getField("insuredsMaleButton")).equals("checked")){
          g2d.drawString("X",483,235);
         }
         if(((String) getField("insuredsFemaleButton")).equals("checked")){
          g2d.drawString("X",535,235);
         }
         
         // box 11b insureds eployers name or school name
         
         g2d.drawString(((String) getField("insuredsEmployer")),360,257);
         
         // box 11c insureds insurance plan or program name
         
         g2d.drawString(((String) getField("insuredsPlan")),360,285);
        
        // boxes 12 and 13
        
        g2d.drawString("Signature on file",40, 353);
        g2d.drawString(((String) getField("clientCreateDate")),255, 353);
        
        g2d.drawString("Signature on file",396, 353);
        
        // box 21 diagnosis
        
        
        
        
        // box 21, 24, 28, 30
        // this is the diagnosis codes, the claims themselves, and the 
        // charge totals, pile them all up in the same pass through the claims
        
        // clientClaims should be an ArrayList of claim keys
        
        Hashtable clms = (Hashtable) clients.getClaims(clientID);
        Hashtable diags = new Hashtable();
        float totCharges = 0;
        Hashtable clm;
        int diagPosition = 0;
        
        // System.out.println("claims...");
        
        // System.out.println("processing " + clientClaims.size() + " claims");
        
        for (int x = 0; x < clientClaims.size(); x++) {   
          
          // System.out.println("processing claim " + x);
          
          if(x > 6){
           break;
          }
          
          // piggyback building the diagnosis box and charge totals
          
          clm = (Hashtable) clms.get(clientClaims.get(x));
                      
          // update the running total
          
          if(clm == null){
           System.out.println("claims hash is null for: " + clientClaims.get(x));
          }
          
          // System.out.println("claim ammount: " + clm.get("claimAmt"));
          
          totCharges = totCharges + Float.parseFloat(((String) clm.get("claimAmt")));
          
          // place the diag in a hash, pull the keys later for unique list
          String diag = (String) clm.get("claimDiag");
          
          if(!diags.containsKey(diag)){
           diagPosition++;
           diags.put(diag,new Integer(diagPosition));
          }
          
          // date of claim
          g2d.drawString((String) ((String) clm.get("claimDate")).replaceAll("/", " "),10,(520 + (x * 25)));
          g2d.drawString((String) ((String) clm.get("claimDate")).replaceAll("/", " "),75,(520 + (x * 25)));
          
          // place of service
          if((String) clm.get("claimPlace") == null){
          	g2d.drawString("11",135,(520 + (x * 25)));
          }else{
            g2d.drawString((String) clm.get("claimPlace"),135,(520 + (x * 25)));
          }
          
          // procedure
          g2d.drawString((String) clm.get("claimProc"),180,(520 + (x * 25)));
          
          
          // diag ref
          g2d.drawString((String) ((Integer) diags.get((String) clm.get("claimDiag"))).toString(),300,(520 + (x * 25)));
          
          // charge
          g2d.drawString((String) clm.get("claimAmt"),360,(520 + (x * 25)));
        
          // units (always 1)
          g2d.drawString("1",425,(520 + (x * 25)));
          
        }
        
         // fill in the stupid diag boxes
         
         for( Enumeration e = diags.keys(); e.hasMoreElements();){
         
          String slot = (String) e.nextElement();
          
          int dPos = ((Integer) diags.get(slot)).intValue();
          
          if(dPos == 1){
           g2d.drawString(slot, 28,450);
          }
          
          if(dPos == 2){
           g2d.drawString(slot, 216,450);
          }
          
          if(dPos == 3){
           g2d.drawString(slot, 28,473);
          }
          
          if(dPos == 4){
           g2d.drawString(slot, 216,450);
          }
         }
        
        // box 25 fed tax id
        
        //g2d.drawString("512981594",10, 667);
        
        g2d.drawString((String) provider.getProperty("provTaxSSN"),10,667);
        
        g2d.drawString("X", 117,667);
        
        // box 27 accept assignment ( always checked )
        
        g2d.drawString("X",265, 667);
        
        // box 28 total charges
        
        // make sure we dont leave off a 0 from the totCharges String
        
         String totCString = new String((String) Float.toString(totCharges));
        
         //System.out.println(totCString.substring(totCString.indexOf(".")).length());
        
         if((int) ((String)totCString.substring(totCString.indexOf("."))).length() < 3){
          
          totCString = new StringBuffer().
                       append((String) Float.toString(totCharges)).
                       append("0").toString();
         }
         
        g2d.drawString(totCString,365, 667);
        
        // box 30 balance due
        
        g2d.drawString(totCString,514, 667);
        
        // box 33 physicians suppliers billing name,address,zip
        
        g2d.drawString((String) provider.getProperty("provName"),360,695);
        g2d.drawString((String) provider.getProperty("provAdd1"),360,710);
        g2d.drawString((String) provider.getProperty("provAdd2"),360,725);
        
         
          return (PAGE_EXISTS);
       }else{
          return (NO_SUCH_PAGE);
    }
}


  private String getField(String field){
  
   String ret = (String) clients.getClientField(clientID,field);
   
   // System.out.println("Got " + ret + " for field " + field);
   
   if(ret == null){
    return new String("");
   }else{
    return (String) ret;
   }
  
  }
  
 }                  
       

