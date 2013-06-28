import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Created on Jan 14, 2005
 *
 */

public class EDI837 {

	 FileWriter out;
	
	public EDI837(){
		
	 File outputFile = new File("c:/837test.edi");
	 
	 try{
	 	out = new FileWriter(outputFile);
	 }catch (IOException e){
	  System.err.println(e.getMessage());
	 }
	 
	 Date today;
	 Locale curLocale = new Locale("en","US");
     SimpleDateFormat formatter;
     today = new Date();
     
     formatter = new SimpleDateFormat("yyyyMMdd", curLocale);               
     String dateOut = formatter.format(today);  
     
     formatter = new SimpleDateFormat("Hmm");
     String timeOut = formatter.format(today);
     
     
     nextLine("Header");
	 
	 // ST transaction set header ST*ST01*ST02
	 // ST01 - transaction set identifier code (837) 
	 // ST02 - transaction set control number, unique w/in 
	 //			transaction set functional group
				
	 nextLine("ST*837*0001~");
	 			
	 			
	 // BHT01 - hierach struct code 
	 // BHT02 - trans set purpose code 
	 //		(00 Original, 01 Cancellation, 15 Re-Submission,  22 Information Copy)
	 // BHT03 - originators ref number 
	 // BHT04 - date of transaction creation
	 // BHT05 - time of transaction creation
	 // BHT06 - trans type code (optional)
	 //			(CH chargeable (if unsure), RP reporting )
	 
	 
	 			
	 nextLine("BHT*0019*00*refnum*dateOut*timeOut~");
	 
	 // Loop 1000A - Submitter Name
	 // NM101 - entity identifier code 
 	 //			(40 Reciever, 41 Submitter)
	 // NM102 - entity type qualifier
	 //			(1 Person, 2 Non-Person Entity)
	 // NM103 - name last or organization name
	 // NM104 - name first
	 // NM105 - name middle
	 // NM106 107 - not used
	 // NM108 - identification code qualifier
	 //      	46 electronic transmitter identification number (ETIN)
	 // NM109 - identification code
	 // NM110 111 - not used
	 				//     1 *2 *3*
	 
	 nextLine("Loop 1000A - Submitter Name");
	 nextLine("NM1*41*2*CASEY*KELLEY*JM***46*IDCODE~");
	 
	 // Loop 1000B - Receiver Name
	 
	 nextLine("Loop 1000B - Receiver Name");
	 nextLine("NM1*40*2*WPS*****46*IDCODE~");
	 
	 // Loop 2000A - Service Provider Hierarchical Level for Kelley Casey
	 
	 // HL01 - Hierarchical ID number
	 // HL02 - not used
	 // HL03 - hierarchical level code
	 // 		20 information source
	 // HL04 - hierarchical child code
	 //			1 additional subordinate HL data segment in this
	 //			  hierarchical structure
	 
	 nextLine("HL*1**20*1~");
	 
	 // 
	 
	 // close the file
	 try{
	 	out.close();
	 }catch (IOException e){
	 	System.err.println(e.getMessage());
	 }
	}
	
	void nextLine(String ln){
		try{
			out.write(ln + "\n");
		}catch(IOException e){
		    System.err.println(e.getMessage());
		}
	}
	
}
