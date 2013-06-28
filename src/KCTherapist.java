/**
 * AWT Sample application
 *
 * @author 
 * @version 1.00 04/05/18
 */

public class KCTherapist {
    
        //public Hashtable fieldHash = new Hashtable();
    
    public static void main(String[] args) {
        
        Clients clients = Clients.getInstance();
        clients.loadClients();
        
        Provider provider = Provider.getInstance();

        KCTherapistFrame frame = new KCTherapistFrame();
        
        //CMS1500 foo = new CMS1500();
        
        //EDI837 foo = new EDI837();
        //System.exit(0);
        
        
        //PageGrid foo = new PageGrid();
        
        // Show frame
        frame.setVisible(true);
        
    
    }
    
    
   
    
}
