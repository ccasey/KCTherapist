
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;

 
 public class PageGrid implements Printable {
 
    private static final double INCH = 72;
    private static final double margin = 25;
    private static final double pageWidth = (8.5 * INCH) - (2 * margin);
    private static final double pageHeight = (11.5 * INCH) - (2 * margin);
    
    public PageGrid () {
 
       //--- Create a printerJob object
       PrinterJob printJob = PrinterJob.getPrinterJob ();

        PageFormat pageFormat = printJob.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(margin,margin, pageWidth, pageHeight);
        pageFormat.setPaper(paper);
        
       
       //--- Show a print dialog to the user. If the user
       //--- clicks the print button, then print, otherwise
       //--- cancel the print job
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
 
       //--- Validate the page number, we only print the first page
       if (page == 0) {
 
          //--- Create a graphic2D object and set the default parameters
          g2d = (Graphics2D) g;
          g2d.setColor (Color.black);
 
          //--- Translate the origin to be (0,0)
          g2d.translate (pageFormat.getImageableX (), pageFormat.getImageableY ());
 
          //--- Print the vertical lines
          for (i = 0; i < pageFormat.getWidth (); i += INCH / 2) {
             line.setLine (i, 0, i, pageFormat.getHeight ());
             g2d.draw (line);
          }
 
          //--- Print the horizontal lines
          for (i = 0; i < pageFormat.getHeight (); i += INCH / 2) {
             line.setLine (0, i, pageFormat.getWidth (), i);
             g2d.draw (line);
          }
 
          return (PAGE_EXISTS);
       }
       else
          return (NO_SUCH_PAGE);
    }
 
 } //Example1
                 
       

