     
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class Clients {
 
 public static Hashtable clientHash = new Hashtable();	
 
 private static Clients instance = null;
 
  
 public static Clients getInstance(){
 	
 	 if (instance == null){
 	 	 instance = new Clients();
 	 	 //System.out.println("created new Clients object");
 	 }
 	 
  return (Clients) instance;
  
 }
     // ***
     
    public static String getClientField(String clientID, String field){
    	
    	// System.out.println("getting " + field + " for " + clientID);
    	
    	// System.out.println("returning " + (String) ((Hashtable) clientHash.get(clientID)).get(field));
    	
    	
    	return (String) ((Hashtable) clientHash.get(clientID)).get(field);
    	
    } 
  	 // ***
  	 
  	 public static void updateClaim(String clientID, Long claim,
  	                                String claimField, String claimValue){
  	                                
      Hashtable clH = (Hashtable) ((Hashtable)getClaims(clientID)).get(claim);
      
       
  	  //System.out.println("updating " + claimField + " to " + claimValue + " for " +
      //                    clientID);
      
      clH.put(claimField, claimValue);
  	                                
     }
  	 
  	 // ***
     
     public static void removeClaim(String clientID, Long claim){
                                    
      Hashtable clH = (Hashtable) ((Hashtable)getClaims(clientID)).remove(claim);
      
       
      //System.out.println("removing " + claim  + " for " +
      //                    clientID);
                                    
     }
  	 
  	 // ***
  	 
  	 public static Hashtable getClaims(String clientID){
  	     
  	     if(!clientHash.containsKey(clientID)){
  	         //System.out.println("returning null claim hash");
  	         return null;
  	     }
  	 
             Hashtable cH = ((Hashtable)clientHash.get(clientID));

  	         if(cH.containsKey("claims")){
  	           //System.out.println("returning existing claim hash");
  	           return (Hashtable) cH.get("claims");
  	         }
  	     
  	     return null;
  	 }
  	 
  	 // ***
  	 
  	public static void addClaim(String clientID, String claimDate, 
            String claimDiag, String claimAmt,
            String claimProc, String claimPlace){
  		
  		addClaim(clientID, claimDate, claimDiag, claimAmt, 
  				claimProc, claimPlace, "ins");
  		
  	}
  	 // ***
 
  	// claimType dict:
  	//					ins		insurance
  	//					inv		invoice
  	
     public static void addClaim(String clientID, String claimDate, 
                                 String claimDiag, String claimAmt,
                                 String claimProc, String claimPlace,
								 String claimType){

        Date today;
        Long seconds;
        String dateOut;
        DateFormat dateFormatter;
        Locale curLocale = new Locale("en","US");

        dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
                       curLocale);
                       
        today = new Date();
        dateOut = dateFormatter.format(today);     
        seconds = new Long(today.getTime());
     
     Hashtable claim = new Hashtable();
      claim.put("claimDate",claimDate);
      claim.put("claimDiag",claimDiag);
      claim.put("claimAmt",claimAmt);
      claim.put("claimProc",claimProc);
      claim.put("claimPlace",claimPlace);
      claim.put("claimType",claimType);
      claim.put("submitted","Not Submitted");
      claim.put("paid",(String) "Not Paid");
     
     
     Hashtable cH = ((Hashtable)clientHash.get(clientID));
     
     if(!cH.containsKey("claims")){
     
      //System.out.println("Creating new claim hash for " + clientID);
     
      Hashtable claimHash = new Hashtable();
      claimHash.put(seconds,claim);
      cH.put("claims",claimHash);
     
     }else{
     
        //System.out.println("Adding to existing claim hash for " + clientID);
        ((Hashtable)cH.get("claims")).put(seconds,claim);
        
     }
     
   }
  	 
  	 // ***
 
 public Long[] getSortedClaimsByDate(String clientID){
 
   Hashtable cHash = (Hashtable) clientHash.get(clientID);
   Hashtable claimHash = (Hashtable) cHash.get("claims");
   
   if(claimHash == null){
   	return null;
   }
    // 
    // put the things to sort in as keys, clientID as the value
    Hashtable keyHash = new Hashtable();
   
    // values are the things to sort
    String sortArray[] = new String[claimHash.size()];
   
    int cnt = 0;
   
    for( Enumeration e = claimHash.keys(); e.hasMoreElements();){
     Long objKey = (Long) e.nextElement();
     
     // what second level hash element to we want to sort
     String dateValue = (String) ((Hashtable) claimHash.get(objKey)).get("claimDate");
     dateValue = reverse(dateValue);
     
     keyHash.put(dateValue, objKey);
     sortArray[cnt] = dateValue;
    
     cnt++;
     
    }
   
    Arrays.sort(sortArray); 
    
    Long[] sortedKeys = new Long[sortArray.length];
    
    for (int i = 0; i < sortArray.length; i++) {
     //System.out.println((Long)keyHash.get((String) sortArray[i]) + sortArray[i]);
     sortedKeys[i] = (Long)keyHash.get((String) sortArray[i]); 
    }
   
  return (Long[]) sortedKeys; 
 
 }
 
     // *** 	 
  	public static void updateClient(String clientID, String field, String value){	

  	   //System.out.println("checking for existance of " +  clientID);
  	     
  	   if( clientHash.containsKey(clientID) ) {
  	   
  	     Hashtable foo = (Hashtable) clientHash.get(clientID);
  	     foo.put(field,value);
  	   
  	   }else{
  	   	
  	   	 Hashtable bar = new Hashtable();
  	   	 bar.put(field,value);
  	   	 clientHash.put(clientID, bar);
  	   	 
  	   	 //System.out.println("created new client" + clientID);
  	   	 
  	   }
  	   
  	   	
  	}
 	
     // ***
    
    static void loadClients() {
    	
    //System.out.println("load clients");
    	
    try {
      FileInputStream fileIn = new FileInputStream("c:/clientinfo.data");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      clientHash = (Hashtable)in.readObject();
      //System.out.println("loaded clients" + instance.toString());
     }
      catch (Exception e) {
      System.out.println(e);
     }	
    
   // XMLEncoder xmle = null;
//	try {
	//	xmle = new XMLEncoder(
		//        new BufferedOutputStream(            	
		  //           new FileOutputStream("c:/Test.xml")));
	//} catch (FileNotFoundException e1) {
		//e1.printStackTrace();
	//}
	//xmle.writeObject(clientHash);
    //xmle.close();
    }
    // ***
    
    static void saveClients (){
    	
    	//System.out.println("save clients");
    	
    	try{
    	 FileOutputStream out = new FileOutputStream("c:/clientinfo.data");
		 ObjectOutputStream s = new ObjectOutputStream(out);
		 s.writeObject(clientHash);
		 s.flush();
		 s.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
    }   
    
    // ***

    // **********

 // call this with a 2nd hashtable and a second level key name and
 // get back an ordered array of 1st level keys by 2nd level values

 public String[] getSortedHashKeysByName(){

  
  // put the things to sort in as keys, clientID as the value
  Hashtable keyHash = new Hashtable();
  
  // values are the things to sort
  String sortArray[] = new String[clientHash.size()];
  
  int cnt = 0;
  
  for( Enumeration e = clientHash.keys(); e.hasMoreElements();){
   String objKey = (String) e.nextElement();
   
   // what second level hash element to we want to sort
   String secLevValue = (String) ((Hashtable) clientHash.get(objKey)).get("Last Name") +
   						(String) ((Hashtable) clientHash.get(objKey)).get("First Name");
   
   keyHash.put(secLevValue, objKey);
   sortArray[cnt] = secLevValue;
  
   cnt++;
    
  }
  
   Arrays.sort(sortArray); 
   
   String[] sortedKeys = new String[sortArray.length];
   
   for (int i = 0; i < sortArray.length; i++) {
   // System.out.println((String)keyHash.get((String) sortArray[i]) + sortArray[i]);
    sortedKeys[i] = (String)keyHash.get((String) sortArray[i]); 
   }
  
 return (String[]) sortedKeys; 
}   
	
 // **********
 
 protected static String reverse(String string) {
    StringBuffer sb = new StringBuffer(string);

    return sb.reverse().toString();
}
}