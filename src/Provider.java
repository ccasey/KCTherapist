import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Provider {
 
 public static Properties provProps = new Properties();  
 
 
 private static Provider instance = null;
 
 public static Provider getInstance(){
    
     if (instance == null){
         instance = new Provider();
         try{
          FileInputStream in = new FileInputStream("c:/providerinfo.prop");
           provProps.load(in);
          in.close();
         }
          catch (Exception e) {
          System.out.println(e);
         }
         
         
         //System.out.println("created new Provider object");
     }
     
  return (Provider) instance;
  
 }

// **********

 public static void saveInstance(){
 
  System.out.println("saveInstance");
  try{
   FileOutputStream out = new FileOutputStream("c:/providerinfo.prop");
    provProps.store(out,"--foo--");
   out.close();
  }
   catch (Exception e) {
   System.out.println(e);
  } 
 }

// **********
 
 public static void updateProperty(String prop, String value){
 
  provProps.put(prop, value);
 
 }

// **********
 
 public static String getProperty(String prop){
 
  return (String) provProps.getProperty(prop);
 
 } 

}