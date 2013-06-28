
/**\file**************************************************************
 *
 *  This layout manager is used with permission, and with original
 *  copyright notices attached.  The package name was changed to
 *  simplify building/usage.
 *
 ********************************************************************/

//=============================================================================
//  SSLayout.java
//
//-----------------------------------------------------------------------------
// Copyright (c) 1998 OpenConnect Systems (sm) Inc. All rights reserved.
//      This material contains trade secrets and confidential and
//      proprietary information of OpenConnect Systems.  Use of copyright
//      notice is precautionary only and does not imply publication.
//      This software may not be reproduced in part or whole by any means
//      without the prior written permission of OpenConnect Systems.
//=============================================================================


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;


/**
 * The SSLayout is a Spreadsheet style layout manager. It allows you to
 * specify a n x n grid of cells in which components may be placed.
 * <p>
 * Constraint types are assigned on a per row and a per column basis. This
 * matrix of constraints gives each grid cell a type of constraint management.
 * Constraint types derived from row constraints govern the upper and lower
 * bounds of the cell as well as all cells in that row. Constraint types
 * derived from column constraints govern the left and right bounds of the
 * cell as well as all cells in that column.
 * <p>
 * There are four constraint types available.
 * <pre>
 *   1. FIXED. The simplest of the constraints. It sets the governed bounds
 *      to be a specified number of pixels in size. When the SSLayout is
 *      constructed a default gap size can be set. Also each FIXED constraint
 *      can be supplied a specific amount of pixels to use. FIXED does not
 *      determine it size based on the component. Instead it controls the size
 *      of the component based on its size.
 *
 *   2. FIT. Just make the cell bounds shrink wrap/fit tight around the biggest
 *      component along its constraint axis. FIT looks at the preferred size
 *      of a component in determining its size. It is in effect just the
 *      opposite of the FIXED since the largest component along a constraint
 *      axis determines the cell's geometry. In addition, a FIT group value may
 *      be passed. This says that all components in a group will have the same size
 *      in dimension being configured. For example, for column constraints this mean
 *      all components in the same fit group will have the same width.
 * 
 *   3. FILL. Fill is the default constraint. It says to fill all available
 *      space in the parent. This is determined after FIT and FIXED constrained
 *      cells have had an opportunity to consume space. FILL is different in
 *      that it looks at the parent for calculating its size and not the
 *      components along its constraint axis. Multiple FILLs along a constraint
 *      axis divide the space evenly. A FILL causes the component it governs to
 *      take up all available space. In other words it resizes the component
 *      to its size.
 *
 *   4. FRACTION. Fraction is like fill except it specifies how much of the
 *      available parent space to consume. For example a fraction constraint
 *      of 25 says to use 25% of the available space. If 200 pixels are left
 *      over after FITs & FIXEDs get their share, the 50 pixels would get
 *      used.
 * </pre>
 *
 * An example will help to illustrate the constraint matrix. Lets say we have
 * a 2x2 grid and we want to the upper left corner to always resize 
 * horizontally with the parent, while the other 3 cells remain the same size. 
 * And lets say that the other 3 cells we want to completely 
 * display some buttons at their preferred size.
 * <p>
 * Here is the constraint matrix:
 * <pre>
 *         FIT   FIT   <---- Column Constraint Types
 *        +-----+-----+
 *   FILL |     | b1  |
 *        +-----+-----+
 *   FIT  | b2  | b3  |
 *     ^  +-----+-----+
 *     |
 *     +-------- Row Constraint Types
 *
 * Cell addresses:
 *        +-----+-----+
 *        | 0,0 | 1,0 |
 *        +-----+-----+
 *        | 0,1 | 1,1 |
 *        +-----+-----+
 * </pre>
 * All cells in column 0 have the FIT type. This means that the left and right
 * bounds of the cells 0,0 and 0,1 will be controlled by the width of the
 * largest component in that column. This happens to be b2 (button 2). If
 * cell 0,0 had a component that was larger than b2 its width would determine
 * the columns width.
 * <p>
 * Now take a look at the second column (index 1). It also is a FIT. The
 * widest of b1 & b3 determine the width of this column.
 * <p>
 * Looking at the row constraints we see a FILL and a FIT. The FILL affects
 * the top and bottom of all cells along the first column. The height of the
 * row will be the parent's height - the second row's height (the only other
 * row).
 * <p>
 * You can see from this that b1 will be grow tall as the window is resized
 * horizontally. However, its width will remain the same.
 * <p>
 * The steps to creating an SSLayout are illustrated below:
 * <pre>
        JButton b1 = new JButton("b1");
        JButton b2 = new JButton("b2");
        JButton b3 = new JButton("b3");
        JButton b4 = new JButton("b4");
        
        SSLayout ssl = new SSLayout(0, 4, 5);
        ssl.setColumnType(0, SSLayout.FIXED, 10);
        ssl.setColumnType(1, SSLayout.FILL);
        ssl.setColumnType(2, SSLayout.FIT);
        ssl.setColumnType(3, SSLayout.FIXED, 10);
        ssl.setRowType(0, SSLayout.FIXED, 10);
        ssl.setRowType(1, SSLayout.FIT);
        ssl.setRowType(2, SSLayout.FRACTION, 50);
        ssl.setRowType(3, SSLayout.FRACTION, 50);
        ssl.setRowType(4, SSLayout.FIXED, 10);

        JPanel p = new JPanel(ssl);
        p.add(b1, new Rectangle(1,1,1,1));
        p.add(b2, new Rectangle(2,1,1,1));
        p.add(b3, new Rectangle(1,2,1,1));
        p.add(b4, new Rectangle(2,2,1,1));
        p.add(b5, new Rectangle(2,3,2,1));

 * This creates a rather complex 4 column x 5 row grid. Lets look at the
 * matrix it specifies.
 *               FIXED             FIXED
 *                10   FILL   FIT   10   <---- Column Constraint Types
 *              +-----+-----+-----+-----+
 *     FIXED 10 |     |     |     |     |
 *              +-----+-----+-----+-----+
 *          FIT |     | b1  | b2  |     |
 *              +-----+-----+-----+-----+
 *  FRACTION 50 |     | b3  | b4  |     |
 *              +-----+-----+-----+-----+
 *  FRACTION 50 |     |     b5    |     |
 *              +-----+-----+-----+-----+
 *     FIXED 10 |     |     |     |     |
 *        ^     +-----+-----+-----+-----+
 *        |
 *        +-------- Row Constraint Types
 * </pre>
 * Note that there are three parts to setting up an SSLayout and
 * arranging components on the grid. First, grid dimensioning is performed in the
 * constructor by supplying the number of columns and the number of rows. Second,
 * the constraint matrix is set up by calling setColumnType and setRowType
 * methods. By default the constraint matrix is set up so that each row
 * and column are set to the FILL type. Last, buttons are placed in the grid
 * via the add() method.
 * <p>
 * In this example b1 and b2's height is at its preferred size (due to FIT).
 * b3 and b4's height however, is 50% of the available space. This is the
 * parent height minus the height of rows 0,1, and 4 divided by 2. The divided
 * by 2 is because rows 2,3 share the left over space 50/50.
 * <p>
 * Continuing in this example, the width of b2 and b4 are their preferred size.
 * However, the width of b1 & b3 is the available space after rows 0,1,3 have
 * claimed their space.
 * <p>
 * Finally note that b5 spans two columns. This information is specified
 * in the add for b5. The rectangle's width field specifies spanning. b5's
 * cell will span horizontally from b3's left edge to b4's grid's right edge.
 * <p>
 * Creation of the grid and initialization of the constraint matrix
 * can get a little tedious. It also is difficult to keep
 * the column, row ordering in your head. Additionally, if you change
 * the dimensions on the constructor you must be careful to also
 * keep the constraint matrix indices correct.
 * <p>
 * In order to alleviate these problems an
 * alternate method of initializing the grid and
 * specifying the constraint matrix is supplied.
 * <p>
 * The SSLayout has a couple of constructors that get a layoutSpec string. In
 * particular the constructor that gets a columnSpec and rowSpec string allows
 * a more terse expression of the grid and constraint matrix. The constraint
 * types FIXED, FIT, FILL, and FRACTION are represented by a single character
 * in a layout spec string.
 * <pre>
 * Spec strings may contain the following characters:
 *
 *   '-' | '-dd'  : FIT, where dd denotes a fit group. All components in a fit
 *                  group will have the same size.
 *   '~' | '@'    : FILL
 *   '#' or '#dd' : FIXED, where dd denotes an integer number.
 *                  Specifies the dimension of the grid in pixels.
 *   '%' or '%dd' : FRACTION, where dd denotes an integer number.
 *                  Specifies the percent of space left over this
 *                  grid will consume.
 * </pre>
 * <p>
 * Adding components to a grid via the add method with a Rectangle expressing
 * the constraints can be tedious.  If spanning is not needed you can pass
 * just a Point. It will assume no spanning and use the x,y for col,row placement.
 * <p>
 * Also you can add without any constraints. If no other adds have taken place
 * it starts at 0,0 and works its way through the grid left to right and top
 * to bottom. If an add occurs with a Point or Rectangle as a constraint the
 * next add without a constraint will begin to the right (if possible) or on
 * the start of the next row (if possible).
 * <p>
 * The example above may be rewritten as follows (less the b5 spanning):
 * <pre>
        SSLayout ssl = new SSLayout(0, 
                            "#10, @, -, #10", 
                            "#10, -, %50, %50, #10");
        
        JPanel p = new JPanel(ssl);
        p.add(b1, new Point(1,1));
        p.add(b2, new Point(2,1));
        p.add(b3, new Point(1,2));
        p.add(b4, new Point(2,2));
 * 
 * The adding could also have been done like this:
        p.add(b1, new Point(1,1));
        p.add(b2);
        p.add(b3, new Point(1,2));
        p.add(b4);
 * </pre>
 * <p>
 * See the description of the SSLayout(int, String, String) constructor for
 * more information.
 * <p>
 * One-dimensional grids.
 * <p>
 * One-dimensional grids of course are fairly straightforward to define and
 * populate. In fact if you pass a null to the constructor described above for
 * its column spec it will create 1 column x n row grid (VERTICAL orientation).
 * Or if you supply null to the second string, the rowSpec, it will create a
 * n column x 1 row grid (HORIZONTAL orientation). As a convenience you can
 * call the SSLayout(int, int, String) constructor and pass either a
 * HORIZONTAL or VERTICAL constant as the second argument, supplying a spec
 * string for along the major axis. For example, to create a button row
 * with ok/apply/cancel, right justified here is how you could accomplish it.
 * <pre>
        SSLayout ssl = new SSLayout(6, SSLayout.HORIZONTAL, "@,-,-,-");
        JPanel p = new JPanel(ssl);
        p.add(okB, new Point(1,0));
        p.add(applyB);
        p.add(cancelB);
 * </pre>
 * <p>
 * Note that I specify the okB position and let it auto place the next two.
 * <p>
 * One aside the default row spec created sets row 0 to be FILL. You could fix
 * this by adding the following line immediated after the SSLayout construction.
 * <pre>
        ssl.setRowType(0, SSLayout.FIT);
 * </pre>
 * For illustration purposes here is another way to do the same thing:
 * <pre>
        SSLayout ssl = new SSLayout(6, SSLayout.HORIZONTAL, "@,-,-,-");
        JPanel p = new JPanel(ssl);
        p.add(javax.swing.Box.createHorizontalGlue());
        p.add(okB);
        p.add(applyB);
        p.add(cancelB);
 * </pre>
 * The difference here is that instead of skipping column 0, we add
 * a lightweight component, Box glue. In this manner we can just add like
 * we would to a Box or BoxLayout.
 * <p>
 * By default any gap specified in a constructor also appears as a margin
 * around the grid inside the parent. You can turn this off by calling the
 * method, setOuterGap(false);
 * <p>
 * Notes on Spanning.
 * <p>
 * From the discussion above it is clear what effect constraint types have on
 * components within a particular grid cell. However, what happens when a compoent
 * spans several rows or columns. What effect do the constraint types have on the
 * spanning component?
 * <p>
 * If a component's anchor (where it begins a span-- its x,y) is in a FILL, FRACTION,
 * or FIXED constraint cell then spanning 
 * <h2>Revision History</h2><br>
 * ??/??/1998 epa Initial Version.<br>
 * 09/11/1998 epa Handle Insets of Container<br>
 * 09/11/1998 epa Value of FILL/FIT is now a grouping for size if value > 0<br>
 * <p>
 * <h2>Notes</h2><br>
 * Controller class for the Interpreter.
 */

public class SSLayout implements LayoutManager2
{
    public static final int FRACTION = 0;
    public static final int FILL = 1;
    public static final int FIXED = 2;
    public static final int FIT = 3;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final String FIXED_CHAR_SPEC    = "#";
    private static final String FRACTION_CHAR_SPEC = "%";
    private static final String FIT_CHAR_SPEC      = "-";
    private static final String FILL_CHAR_SPEC_1   = "~";
    private static final String FILL_CHAR_SPEC_2   = "@";
    
    private int cGap;
    private boolean oGap;
    private int nCols;
    private int nRows;

    private int cType[];
    private int rType[];
    private int cVal[];
    private int rVal[];
    private int cSize[];
    private int rSize[];
    private Hashtable cHash;
    private boolean debug;
    private int currColIndex = -1;
    private int currRowIndex = 0;

    /**
     * Creates a 1 x 1 grid with zero spacing.
     **/
    public SSLayout() {
        cGap = 0;
        nCols = 1;
        nRows = 1;
        oGap = true;
        initArrays();
    }

    /**
     * Creates a 1 x 1 grid with default spacing set to spacing.
     * @param spacing default gap to be used for FIXED constraints as well as
     * for an outer gap if OuterGap is true.
     **/
    public SSLayout(int spacing) {
        cGap = spacing;
        nCols = 1;
        nRows = 1;
        oGap = true;
        initArrays();
    }

    /**
     * Creates a cols x rows size grid with default spacing set to spacing.
     * @param spacing default gap to be used for FIXED constraints as well as
     * for an outer gap if OuterGap is true.
     * @param cols number of columns the grid will contain.
     * @param rows number of rows the grid will contain.
     **/
    public SSLayout(int spacing, int cols, int rows) {
        cGap = spacing;
        nCols = cols;
        nRows = rows;
        oGap = true;
        initArrays();
    }

    public void setColumnCount(int count) {
        nCols = count;
        reInitArrays();
    }

    public void setRowCount(int count) {
        nRows = count;
        reInitArrays();
    }

    /**
     * Sets the constraint type for a given column. The constraint value
     * defaults to 0.
     * @param col the column to set the constraint for.
     * @param type the constraint type: FIXED, FILL, FIT, or FRACTION.
     **/
    public void setColumnType(int col, int type) {
	cType[col] = type;
	cVal[col] = 0;
    }

    /**
     * Sets the constraint type for a given column and its value
     * @param col the column to set the constraint for.
     * @param type the constraint type: FIXED, FILL, FIT, or FRACTION.
     * @value qualifying information for the constraint type. If type is
     * FIXED, the value denotes the width in pixels. If FRACTION, the
     * value denotes a percent (value/100). value has no effect for FIT or
     * FILL constraint types.
     **/
    public void setColumnType(int col, int type, int value) {
        cType[col] = type;
        cVal[col] = value;
    }

    /**
     * Sets the constraint type for a given row. The constraint value
     * defaults to 0.
     * @param row the row to set the constraint for.
     * @param type the constraint type: FIXED, FILL, FIT, or FRACTION.
     **/
    public void setRowType(int row, int type) {
        rType[row] = type;
        rVal[row] = 0;
    }

    /**
     * Sets the constraint type for a given row and its value
     * @param row the row to set the constraint for.
     * @param type the constraint type: FIXED, FILL, FIT, or FRACTION.
     * @value qualifying information for the constraint type. If type is
     * FIXED, the value denotes the height in pixels. If FRACTION, the
     * value denotes a percent (value/100). value has no effect for FIT or
     * FILL constraint types.
     **/
    public void setRowType(int row, int type, int value) {
        rType[row] = type;
        rVal[row] = value;
    }

    /**
     * For the given row, returns the constraint type.
     * @return constraint type for row.
     **/
    public int getRowType(int row){
	return rType[row];
    }

    /**
     * Returns whether we use the spacing gap for extra margin around
     * our grid
     * @return boolean indicating if outer gap margin is turned on or off.
     **/
    public boolean getOuterGap(){
	return oGap;
    }

    /**
     * Turns on or off outer margin. Outer margin size is determined by the
     * value of the spacing size (specified in the constructor).
     * @param b outer gap flag.
     **/
    public void setOuterGap(boolean b) {
	oGap = b;
    }

    public void setDebug(boolean b)
    {
        debug = b;
    }

    private void initArrays() {
        initColArrays();
        initRowArrays();
        cHash = new Hashtable();
    }

    private void reInitArrays() {
        int rLength;
        int cLength;
        //
        //  Determining whether the new Layout
        //  has more or less elements than what is
        //  current and adjusting the length accordingly
        if( nRows >= rType.length)
            rLength = rType.length;
        else
            rLength = nRows;

        if( nCols >= rType.length)
            cLength = rType.length;
        else
            cLength = nCols;


        int na[] = new int[nCols];
        System.arraycopy(cType, 0, na, 0, cLength);
        na[nCols - 1] = FILL;
        cType = na;

        int nr[] = new int[nRows];
        System.arraycopy(rType, 0, nr, 0, rLength);
        nr[nRows - 1] = FILL;
        rType = nr;

        nr = new int[nCols];
        System.arraycopy(cVal, 0, nr, 0, cLength);
        cVal = nr;

        nr = new int[nRows];
        System.arraycopy(rVal, 0, nr, 0, rLength);
        rVal = nr;

        nr = new int[nCols];
        System.arraycopy(cSize, 0, nr, 0, cLength);
        cSize = nr;

        nr = new int[nRows];
        System.arraycopy(rSize, 0, nr, 0, rLength);
        rSize = nr;

        int i;
        for (i = 0; i < nCols; i++)
            cType[i] = FILL;
        for (i = 0; i < nRows; i++)
            rType[i] = FILL;


    }

    public void addLayoutComponent(String name, Component component) {
        // Do nothing.
    }

    public void removeLayoutComponent(Component component) {
        cHash.remove(component);
    }

    /**
     * Returns the position of the component in the current
     * layout in terms of a Rectangle
     *
     * @param   c   The desired component
     * @return  The position rectangle
     */
    public Rectangle getLayoutPos( Component c ){
        return (Rectangle)cHash.get(c);
    }

    /**
     * Get the component located at r.x, r.y
     **/
    public Component getComponent( Rectangle r){
	return (Component)cHash.get(r);
    }

    /**
     * Returns the row count in the layout
     */
    public int getRowCount(){
        return nRows;
    }

    /**
     * Returns the columm count in the layout
     */
    public int getColumnCount(){
        return nCols;
    }

    /**
     * Add a row to the layout
     */
    public void addRow(){
        nRows++;
        reInitArrays();
    }

    /**
     * Add multiple rows to the layout
     *
     * @param   n   The number of rows to add
     */
    public void addRows( int n ){
        nRows+=n;
        reInitArrays();
    }

    /**
     * Add a single column to the layout
     */
    public void addColumn(){
        nCols++;
        reInitArrays();
    }

    /**
     * Add multiple columns to the layout
     *
     * @param   n   The number of columns to add
     */
    public void addColumns( int n ){
        nCols+=n;
        reInitArrays();
    }

    public void removeRow( int row ){
        if (nRows == 1) {
        }
        else {
            int[] cacheVal = new int[nRows -1];
            int[] cacheType = new int[nRows -1];
            int[] cacheSize = new int[nRows -1];

            for (int i = 0; i<nRows; i++) {
                if(i < row) {
                    cacheVal[i] = rVal[i];
                    cacheType[i] = rType[i];
                    cacheSize[i] = rSize[i];
                }
                if (i > row) {
                    cacheVal[i-1] = rVal[i];
                    cacheType[i-1] = rType[i];
                    cacheSize[i-1] = rSize[i];
                }
            }
            rVal = cacheVal;
            rType = cacheType;
            rSize = cacheSize;
        }

        if (nRows == 1){
            cHash = new Hashtable();
        }
        else {
            nRows--;
            for (Enumeration e = cHash.keys(); e.hasMoreElements(); ){
                // Rectangle r = (Rectangle)e.nextElement();

                Component c = (Component)e.nextElement();
                Rectangle r = (Rectangle)cHash.get(c);

                int y = r.y;
                int w = r.width;

                if(r.y > row){
                    cHash.put(c, new Rectangle(r.x, r.y - 1, r.width,
                                               r.height));
                }
            }
        }
    }

    public Dimension preferredLayoutSize(Container target) {
        // Calculate the size this layout wants to be.
        return minimumLayoutSize(target);
    }

    public Dimension minimumLayoutSize(Container target) {
        // Calculate the size this layout wants to be.
        int mWidth = cGap * (nCols + ((oGap) ? 1 : -1));
        int mHeight = cGap * (nRows + ((oGap) ? 1 : -1));
        Rectangle r;
        int s;
        int i;
        Component c[] = target.getComponents();
        int fitCount[] = new int[cType.length];
        int fitSize[] = new int[cType.length];

        // Calculate Width
        for (i = 0; i < nCols; i++) {
            if (cType[i] == FIXED) {
                mWidth += cVal[i];
            }
            if (cType[i] == FRACTION) {
                mWidth += cGap * cVal[i];
            }
            if (cType[i] == FILL || cType[i] == FIT) {
                s = 0;
                for (int o = 0; o < c.length; o++) {
                    r = (Rectangle)cHash.get(c[o]);
                    if (r != null && i >= r.x && i < (r.x + r.width)) {
                        Dimension x = new Dimension(0,0);
                        try {
                            x = c[o].getPreferredSize();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        if (r.width > 1) {
                            int fitCnt = 0;
                            for (int rmIdx = r.x; rmIdx < (r.x + r.width);
                                 rmIdx++) {
                                if (cType[rmIdx] == FIXED)
                                    x.width -= cVal[rmIdx];
                                if (cType[rmIdx] == FRACTION)
                                    x.width -= (cVal[rmIdx] * cGap);
                                if (cType[rmIdx] == FIT)
                                    fitCnt++;
                                //if (cType[rmIdx] == FILL) {
                                //	x.width = cSize[i];
                                //	fitCnt=1;
                                //	break;
                                //}
                            }
                            if (cType[i] == FILL)
                                x.width = 0;
                            else
                                x.width /= fitCnt;
                        }
                        if (x != null)
                            s = Math.max(s, x.width);
                    }
                }
                if (cVal[i] > 0) {
                    if (s < fitSize[cVal[i]])
                        s = fitSize[cVal[i]];
                    if (s > fitSize[cVal[i]]) {
                        int t = fitSize[cVal[i]];
                        fitSize[cVal[i]] = s;
                        s += ((s - t) * fitCount[cVal[i]]);
                    }
                    fitCount[cVal[i]]++;
                }
                mWidth += s;
            }
        }
        // Calculate Height
        for (i = 0; i < nRows; i++) {
            if (rType[i] == FIXED) {
                mHeight += rVal[i];
            }
            if (rType[i] == FRACTION) {
                mHeight += cGap * rVal[i];
            }
            if (rType[i] == FILL || rType[i] == FIT) {
                s = 0;
                for (int o = 0; o < c.length; o++) {
                    r = (Rectangle)cHash.get(c[o]);
                    if (r != null && i >= r.y && i < (r.y + r.height)) {
                        Dimension x = new Dimension(0,0);
                        try {
                            x = c[o].getPreferredSize();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        if (r.height > 1) {
                            int fitCnt = 0;
                            for (int rmIdx = r.y; rmIdx < (r.y + r.height);
                                 rmIdx++) {
                                if (rType[rmIdx] == FIXED)
                                    x.height -= rVal[rmIdx];
                                if (rType[rmIdx] == FRACTION)
                                    x.height -= (rVal[rmIdx] * cGap);
                                if (rType[rmIdx] == FIT)
                                    fitCnt++;
                                //if (rType[rmIdx] == FILL) {
                                //	x.width = rSize[i];
                                //	fitCnt=1;
				//break;
                                //}
                            }
                            if (rType[i] == FILL)
                                x.height = 0;
                            else
                                x.height /= fitCnt;
                        }
                        if (x != null)
                            s = Math.max(s, x.height);
                    }
                }
                mHeight += s;
                if (rType[i] == FIT)
                    mHeight += rVal[i];
            }
        }
        Insets insets = target.getInsets();
        mWidth += insets.left + insets.right;
        mHeight += insets.top + insets.bottom;
        return new Dimension(mWidth, mHeight);
    }

    /**
     * Dumps the constraint matrix.
     **/
    public void getLayoutSpecification() {
        for (int i = 0; i < nCols; i++) {
            System.out.print("col " + i + ": v = " + cVal[i] + ",t=" + cType[i] + "\t");
        }
        System.out.println("\n");
        for (int i = 0; i < nRows; i++) {
            System.out.println("row " + i + ": v = " + rVal[i] + ",t=" + rType[i] + "\t");
        }
    }

    public void layoutContainer(Container target) {
        // Size all components in the target.
        Dimension td = target.getSize();
        Insets insets = target.getInsets();
        int aWidth = td.width - ((nCols + ((oGap) ? 1 : -1)) * cGap) -
	    insets.left - insets.right; // Remove Gap space.
        int aHeight = td.height - ((nRows + ((oGap) ? 1 : -1)) * cGap) -
	    insets.top - insets.bottom;
        int i;
        int fitSize[] = new int[cType.length];

        for (i = 0; i < nCols; i++)
            cSize[i] = -1; // Not set value;
        for (i = 0; i < nRows; i++)
            rSize[i] = -1; // Not set value;

        Component c[] = target.getComponents();
        Rectangle r;
        int s = 0;
        int fCnt = 0;
        // Calculate column sizes.  set all FIXED and FIT sizes first.
        for (i = 0; i < nCols; i++) {
            // If fixed, just take the pixel value as width
            if (cType[i] == FIXED) {
                cSize[i] = cVal[i];
            }
            // If fit, look at all components that intersect this
            // column. If the intersecting component spans multiple cols
            // iterate over its spanned cols to calculate a width for col.
            if (cType[i] == FIT) {
                s = 0;
                // Iterate over components
                for (int o = 0; o < c.length; o++) {
                    r = (Rectangle)cHash.get(c[o]);
                    // Does this component intersect the column?
                    if (r != null && i >= r.x && i < (r.x + r.width)) {
                        // Start with preferred width as assumed width
                        Dimension x = new Dimension(0,0);
                        try {
			    x = c[o].getPreferredSize();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        if (r.width > 1) {
                            int fitCnt = 0;
                            // Iterate over this component's spanned cols
                            // For each type found adjust our width to determine
                            // the needed width for the column.
                            for (int rmIdx = r.x; rmIdx < (r.x + r.width);
                                 rmIdx++) {
				// System.out.println(rmIdx + ":" + cType[rmIdx]);
                                // If we spanned a Fixed col, then subtract its fixed size from our
                                // total width.
                                if (cType[rmIdx] == FIXED)
                                    x.width -= cVal[rmIdx];
                                // If we spanned a Fraction col, then subtract the fraction cols value *
                                // gap size ?? is this right?
                                if (cType[rmIdx] == FRACTION)
                                    x.width -= (cVal[rmIdx] * cGap);
                                // If we spanned a Fit row then count how many of those we spanned,
                                // later we will divide the width by this one.
                                if (cType[rmIdx] == FIT)
                                    fitCnt++;
                                // If we span any FILL column then take the natural size of our fit column
                                // and just let it get consumed by the FILL column. Causes no other column
                                // to have effect during span.
                                if (cType[rmIdx] == FILL) {
                                    x.width = cSize[i];
                                    fitCnt=1;
                                    break;
                                }
                            }
                            x.width /= fitCnt;
                        }
                        if (x != null)
                            s = Math.max(s, x.width);
                    }
                }
                if (cVal[i] > 0) {
                    // FIT column is grouped.  If new size is larger than
                    // the groups size, resize all columns in group to be
                    // this size.
                    if (s > fitSize[cVal[i]]) {
                        fitSize[cVal[i]] = s;
                        for (int idx = 0; idx < i; idx++) {
                            if (cType[idx] == FIT && cVal[idx] == cVal[i]) {
                                aWidth -= (s - cSize[idx]);
                                cSize[idx] = s;
                            }
                        }
                    }
                    else
                        s = fitSize[cVal[i]];
                }
                cSize[i] = s;
            }
            aWidth -= cSize[i];
        }
        s = 0;
        // Fill in FRACTION columns with Val % of available space.
        // Count number of FILL columns
        for (i = 0; i < nCols; i++) {
            if (cType[i] == FRACTION) {
                cSize[i] = (aWidth * cVal[i]) / 100;
                s += cSize[i];
            }
            if (cType[i] == FILL) {
                fCnt++;
            }
        }
        aWidth -= s;
        // Fill in FILL columns with equal amounts of available space.
        if (fCnt > 0) {
            for (i = 0; i < nCols; i++) {
                if (cType[i] == FILL) {
                    cSize[i] = aWidth / fCnt;
                }
            }
        }

        // Calculate row sizes.  set all FIXED and FIT sizes first.
        for (i = 0; i < nRows; i++) {
            // If fixed just take the pixel value as height.
            if (rType[i] == FIXED) {
                rSize[i] = rVal[i];
            }
            // If fit, Look at all components that intersect this
            // row. If the intersecting component spans multiple rows
            // iterate over its spanned rows to calculate a height for row.
            if (rType[i] == FIT) {
                s = 0;
                for (int o = 0; o < c.length; o++) {
                    r = (Rectangle)cHash.get(c[o]);
                    // Does this component intersect the row?
                    if (r != null && i >= r.y && i < (r.y + r.height)) {
                        // Start with preferred height as assumed height
                        Dimension x = new Dimension(0,0);
                        try{
			    x = c[o].getPreferredSize();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Does this component have a multiple row span?
                        if (r.height > 1) {
                            int fitCnt = 0;
                            // Iterate over this component's spanned rows
                            // For each type found adjust our height to determine
                            // the needed height for the row.
                            for (int rmIdx = r.y; rmIdx < (r.y + r.height); rmIdx++) {
                                //System.out.println("ROW:"+rmIdx + ":" + rType[rmIdx]);
                                // If we spanned a Fixed row, then subtract its fixed size from our
                                // total height.
                                if (rType[rmIdx] == FIXED)
                                    x.height -= rVal[rmIdx];
                                // If we spanned a Fraction row, then subtract the fraction rows value *
                                // gap size ?? is this right?
                                if (rType[rmIdx] == FRACTION)
                                    x.height -= (rVal[rmIdx] * cGap);
                                // If we spanned a Fit row then count how many of those we spanned,
                                // later we will divide the height by this one.
                                if (rType[rmIdx] == FIT)
                                    fitCnt++;
                                // If we span any FILL row then take the natural size of our fit row
                                // and just let it get consumed by the FILL row.
                                if (rType[rmIdx] == FILL) {
                                    x.height = rSize[i];
                                    fitCnt=1;
                                    break;
                                }
                            }
                            x.height /= fitCnt;
                        }
                        if (x != null)
                            s = Math.max(s, x.height);
                    }
                }
                rSize[i] = s + rVal[i];
            }
            aHeight -= rSize[i];
        }
        s = 0;
        fCnt = 0;
        // Fill in FRACTION columns with Val % of available space.
        // Count number of FILL columns
        for (i = 0; i < nRows; i++) {
            if (rType[i] == FRACTION) {
                rSize[i] = (aHeight * rVal[i]) / 100;
                s += rSize[i];
            }
            if (rType[i] == FILL) {
                fCnt++;
            }
        }
        aHeight -= s;
        // Fill in FILL columns with equal amounts of available space.
        if (fCnt > 0) {
            for (i = 0; i < nRows; i++) {
                if (rType[i] == FILL) {
                    rSize[i] = aHeight / fCnt;
                }
            }
        }
        // Sanitize sizes....
        for (i = 0; i < nCols; i++) {
            if (cSize[i] < 0)
                cSize[i] = 0;
        }
        for (i = 0; i < nRows; i++) {
            if (rSize[i] < 0)
                rSize[i] = 0;
        }
        // Actually lay things out.
        int xP;
        int yP;
        int wD;
        int hT;
        for (i = 0; i < c.length; i++) {
            r = (Rectangle)cHash.get(c[i]);
            if (r == null) {
                c[i].setBounds(0, 0, 0, 0);
            }
            else {
                int o;
                xP = (oGap?cGap:0) + insets.left;
                yP = (oGap?cGap:0) + insets.top;
                wD = -cGap;
                hT = -cGap;
                for (o = 0; o < r.x; o++)
                    xP += cSize[o] + cGap;
                for (; o < r.x + r.width; o++)
                    wD += cSize[o] + cGap;
                for (o = 0; o < r.y; o++)
                    yP += rSize[o] + cGap;
                for (; o < r.y + r.height; o++)
                    hT += rSize[o] + cGap;
                if (wD < 0)
                    wD = 0;
                if (hT < 0)
                    hT = 0;
                c[i].setBounds(xP, yP, wD, hT);
            }
        }
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        Rectangle r = null;

        // if no constraints were passed then increment our internal indexing
        // to pick the right one to add to. This is especially useful for one-dimensional
        // grids.
        if (constraints == null) {
            currColIndex++;
            // if exceeded cols, bump up rows;reset cols;test rows
            if (currColIndex >= nCols) {
                currColIndex = 0;
                currRowIndex++;
                if (currRowIndex >= nRows) {
                    throw new RuntimeException("SSLayout: Attempt to more objects than available grid size");
                }
            }
            r = new Rectangle(currColIndex, currRowIndex, 1, 1);
        } else
            if (!(constraints instanceof Point || constraints instanceof Rectangle))
                throw new RuntimeException("SSLayout:  Constraints Object is not a java.awt.Rectangle.  type = " + constraints.getClass().getName());


	// If a point supply 1,1 spanning
        if (constraints instanceof Point) {
            Point point = (Point) constraints;
            r = new Rectangle(point, new Dimension(1, 1));
        } else {
            if (constraints instanceof Rectangle) {
                r = (Rectangle) constraints;
            }
        }

        // handle layout spec
        if (r.x < 0 || r.x >= nCols || // check x
            r.y < 0 || r.y >= nRows || // check y
            r.width < 0 || r.x + r.width > nCols || // check width
            r.height < 0 || r.y + r.height > nRows) { // check height
            throw new RuntimeException("SSLayout:  Object constraints out of bounds");
        }

        // always sync where we added with our indices (this allows you to skip a set of
        // cells by adding to a particular cell, then picking up from there.
        currColIndex = r.x;
        currRowIndex = r.y;
    
        cHash.put(comp, r);
    }

    public Dimension maximumLayoutSize(Container target) {
        // Calculate the maximum size?
        return target.getSize();
    }

    public float getLayoutAlignmentX(Container target) {
        return (float)0.5;
    }

    public float getLayoutAlignmentY(Container target) {
        return (float)0.5;
    }

    public void invalidateLayout(Container target) {
        // Do nothing, we recompute at every layoutContainer.
    }

    /**
     * Convenience constructor that allows the dimension and row/column
     * specifications to be given as strings.
     * <pre>
     * spec strings may contain the following characters:
     *
     *   '-' | '-dd'  : FIT, where dd denotes a fit group. All components in a fit
     *                  group will have the same size.
     *   '~' | '@'    : FILL
     *   '#' or '#dd' : FIXED, where dd denotes an integer number.
     *                  Specifies the dimension of the grid in pixels.
     *   '%' or '%dd' : FRACTION, where dd denotes an integer number.
     *                  Specifies the percent of space left over this
     *                  grid will consume.
     * </pre>
     * The following is an example showing the explicit style of creating
     * a layout and the alternate method using this convenience constructor.
     * <p>
     * <pre>
     First, the explict style:
    
     JButton b1 = new JButton("b1");
     JButton b2 = new JButton("b2");
     JButton b3 = new JButton("b3");
     JButton b4 = new JButton("b4");
        
     SSLayout ssl = new SSLayout(0, 4, 5);
     ssl.setColumnType(0, SSLayout.FIXED, 10);
     ssl.setColumnType(1, SSLayout.FILL);
     ssl.setColumnType(2, SSLayout.FIT);
     ssl.setColumnType(3, SSLayout.FIXED, 10);
     ssl.setRowType(0, SSLayout.FIXED, 10);
     ssl.setRowType(1, SSLayout.FIT);
     ssl.setRowType(2, SSLayout.FRACTION, 50);
     ssl.setRowType(3, SSLayout.FRACTION, 50);
     ssl.setRowType(4, SSLayout.FIXED, 10);

     JPanel p = new JPanel(ssl);
     p.add(b1, new Rectangle(1,1,1,1));
     p.add(b2, new Rectangle(2,1,1,1));
     p.add(b3, new Rectangle(1,2,1,1));
     p.add(b4, new Rectangle(2,2,1,1));

     Next, the alternate style using a string spec for same layout.
     JButton b1 = new JButton("b1");
     JButton b2 = new JButton("b2");
     JButton b3 = new JButton("b3");
     JButton b4 = new JButton("b4");
        
     SSLayout ssl = new SSLayout(0, 
     "#10, ~, -, #10", 
     "#10, -, %50, %50, #10");
        
     JPanel p = new JPanel(ssl);
     p.add(b1, new Point(1,1));
     p.add(b2, new Point(2,1));
     p.add(b3, new Point(1,2));
     p.add(b4, new Point(2,2));
     * </pre>
     * <p>
     * If colSpec is null then the SSLayout acts as a single column,
     * multi-row layout... VERTICAL in orientation.
     * <p>
     * If rowSpec is null then the SSLayout acts as a single row,
     * multi-column layout... HORIZONTAL in orientation.
     * <p>
     * @param spacing default gap to be used for FIXED constraints as well as
     * for an outer gap if OuterGap is true.
     * @param colSpec layout spec string that describes the constraints for
     * each column.
     * @param rowSpec layout spec string that describes the constraints for
     * each row.
     **/
    public SSLayout(int spacing, String colSpec, String rowSpec) {
        initFromSpec(spacing, colSpec, rowSpec);
    }

    private void initColArrays() {
        cType = new int[nCols];
        cVal = new int[nCols];
        cSize = new int[nCols];
        for (int i = 0; i < nCols; i++)
            cType[i] = FILL;
    }

    private void initRowArrays() {
        rType = new int[nRows];
        rVal = new int[nRows];
        rSize = new int[nRows];
        for (int i = 0; i < nRows; i++)
            rType[i] = FILL;
    }

    public static void main(String[] args) {
	/*
	  JButton b1 = new JButton("b1");
	  JButton b2 = new JButton("b2");
	  JButton b3 = new JButton("b3");
	  JButton b4 = new JButton("b4");
	*/
        
	/*
	  // explicit style
	  SSLayout ssl = new SSLayout(0, 4, 5);
	  ssl.setColumnType(0, SSLayout.FIXED, 10);
	  ssl.setColumnType(1, SSLayout.FILL);
	  ssl.setColumnType(2, SSLayout.FIT);
	  ssl.setColumnType(3, SSLayout.FIXED, 10);
	  ssl.setRowType(0, SSLayout.FIXED, 10);
	  ssl.setRowType(1, SSLayout.FIT);
	  ssl.setRowType(2, SSLayout.FRACTION, 50);
	  ssl.setRowType(3, SSLayout.FRACTION, 50);
	  ssl.setRowType(4, SSLayout.FIXED, 10);


	  JPanel p = new JPanel(ssl);
	  p.add(b1, new Rectangle(1,1,1,1));
	  p.add(b2, new Rectangle(2,1,1,1));
	  p.add(b3, new Rectangle(1,2,1,1));
	  p.add(b4, new Rectangle(2,2,1,1));
	*/
	/*
	  // alternate style
	  SSLayout ssl = new SSLayout(0, 
	  "#10, ~, -, #10", 
	  "#10, -, %50, %50, #10");
        
	  JPanel p = new JPanel(ssl);
	  p.add(b1, new Point(1,1));
	  p.add(b2, new Point(2,1));
	  p.add(b3, new Point(1,2));
	  p.add(b4, new Point(2,2));
	*/

	/*
	  // Simulation of ok/cancel button box
	  SSLayout ssl = new SSLayout(6, SSLayout.HORIZONTAL, "~,-1,-1");
	  ssl.setRowType(0, SSLayout.FIT);
	  JPanel p = new JPanel(ssl);
	  p.add(javax.swing.Box.createHorizontalGlue());
	  b1.setText("OK");
	  b2.setText("Cancel");
	  p.add(b1);
	  p.add(b2);
	*/
	/*
	  // Simulation of North, Center, South border layout
	  SSLayout ssl = new SSLayout(6, SSLayout.VERTICAL, "-,@,-");
	  JPanel p = new JPanel(ssl);
	  p.add(b1);
	  p.add(b2);
	  p.add(b3);
	*/
	/*
	  // Simulation of North, Center, South border layout
	  SSLayout ssl = new SSLayout(6, SSLayout.VERTICAL, "-,@,-");
	  JPanel p = new JPanel(ssl);
	  p.add(b1);
	  p.add(b2);
	  p.add(b3);

	*/
	/*
	  // Experiment
	  SSLayout ssl = new SSLayout(0, SSLayout.VERTICAL, "-,@,#,#,-");
	  JPanel p = new JPanel(ssl);
	  p.add(javax.swing.Box.createRigidArea(new Dimension(0,48)));
	  p.add(b2);
	  p.add(b3, new Point(0,4));
	*/

	/*
	  // Experiment
	  SSLayout ssl = new SSLayout(0, SSLayout.HORIZONTAL, "%25,-,%75");
	  ssl.setRowType(0,SSLayout.FIT);
	  JPanel p = new JPanel(ssl);
	  p.add(b1, new Point(1,0));
	*/

	/*
	  // Experiment
	  SSLayout ssl = new SSLayout(6, SSLayout.HORIZONTAL, "#,#,@,-");
	  ssl.setRowType(0,SSLayout.FIT);
	  JPanel p = new JPanel(ssl);
	  p.add(b1, new Point(2,0));
	  p.add(b2);
	*/
	/*
	  // Experiment
	  SSLayout ssl = new SSLayout(6, 
	  "-, -", // col = FIT, FIT
	  "-, -"); // row = FIT, FIT
        
	  JPanel p = new JPanel(ssl);
	  p.add(b1, new Point(1,0));
	  p.add(b2);//, new Point(0,1));
	  p.add(b3);//, new Point(1,1));
	*/
        
	/*
	  // Demonstrates how much easier SSLayout is...
	  JButton bt1 = new JButton("Button 1");
	  JButton bt2 = new JButton("2");
	  JButton bt3 = new JButton("Button 3");
	  JButton bt4 = new JButton("Long-Named Button 4");
	  JButton bt5 = new JButton("Button 5");
 
	  SSLayout ssl = new SSLayout(0,
	  "-1, -1, @",
	  "-, @, #10, -");
 
	  JPanel p = new JPanel(ssl);
	  p.add(bt1);
	  p.add(bt2);
	  p.add(bt3);
	  p.add(bt4, new Rectangle(0,1,3,1));
	  p.add(bt5, new Rectangle(1,3,2,1));
	*/
  
	/*
	  // The Example found in the GridBagLayout tutorial
	  // Demonstrates how much easier SSLayout is...
	  JButton bt1 = new JButton("Button 1");
	  JButton bt2 = new JButton("2");
	  JButton bt3 = new JButton("Button 3");
	  JButton bt4 = new JButton("Long-Named Button 4");
	  JButton bt5 = new JButton("Button 5");
         
	  SSLayout ssl = new SSLayout(0,
	  "-, -, -, @",
	  "-, @, #10, -");
         
	  JPanel p = new JPanel(ssl);
	  p.add(bt1);
	  p.add(bt2);
	  p.add(bt3, new Rectangle(2,0,2,1));
	  p.add(bt4, new Rectangle(0,1,4,1));
	  p.add(bt5, new Rectangle(1,3,2,1));
	*/

        //new com.oc.test.TestFrame(p);

        
    }

    private int parseSpec(String spec, int rcIdent) {
        StringTokenizer st = new StringTokenizer(spec.trim(), "|,:");
        int cnt = st.countTokens();

        // Got size of row or column, initialize array and set defaults to FILL.
        if (rcIdent == 0) {
            nCols = cnt;
            initColArrays();
        } else {
            nRows = cnt;
            initRowArrays();
        }
        int index = 0;
        while (st.hasMoreTokens()) {
            String gridSpec = st.nextToken().trim();

            // FIXED
            if (gridSpec.startsWith(FIXED_CHAR_SPEC)) {
                int val = getConstraintValue(spec, gridSpec);
                setType(rcIdent, index, FIXED, val);
                index++;
                continue;
            }

            // FRACTION
            if (gridSpec.startsWith(FRACTION_CHAR_SPEC)) {
                int val = getConstraintValue(spec, gridSpec);
                setType(rcIdent, index, FRACTION, val);
                index++;
                continue;
            }

            // FILL
            if (gridSpec.equals(FILL_CHAR_SPEC_1) || gridSpec.equals(FILL_CHAR_SPEC_2)) {
                setType(rcIdent, index, FILL, 0);
                index++;
                continue;
            }

            // FIT
            if (gridSpec.startsWith(FIT_CHAR_SPEC)) {
                int val = getConstraintValue(spec, gridSpec);
                setType(rcIdent, index, FIT, val);
                index++;
                continue;
            }

            // if here it was an invalid character
            throw new RuntimeException("SSLayout: Layout specification string had an invalid specification: '" + gridSpec + "' in the layout spec string: \"" + spec + "\"");
        }
        return cnt;
    }

    private void setType(int rcIdent, int idx, int type, int value) {
        if (rcIdent == 0) {
            cType[idx] = type;
            cVal[idx] = value;
        } else {
            rType[idx] = type;
            rVal[idx] = value;
        }
    }

    /**
     * Convenience constructor that allows the creation of one 
     * dimensional grids along a particular orientation.
     * <p>
     * This variant allows an easy way to create a one-dimensional grid
     * in a particular orientation. Valid values are HORIZONTAL & VERTICAL.
     * See the SSLayout(int, String, String) for full documentation on the
     * spec parameter.
     * <p>
     * Here is an example that lays out a row of ok/apply/cancel buttons that
     * are right justified:
     * <pre>
     SSLayout ssl = new SSLayout(6, SSLayout.HORIZONTAL, "@,-,-,-");
     JPanel p = new JPanel(ssl);
     p.add(okB, new Point(1,0));
     p.add(applyB);
     p.add(cancelB);
     * </pre>  
     * @param spacing default gap to be used for FIXED constraints as well as
     * for an outer gap if OuterGap is true.
     * @param orientation If HORIZONTAL denotes a 1 row x n column grid. The following
     * spec string defines the constraints for each column in the single row. If VERTICAL
     * it denotes a n rows by 1 column grid. The following spec string defines
     * the constraints for each row in the single column.
     * @param spec layout specification string for the orientation specified.
     **/
    public SSLayout(int spacing, int orientation, String spec) {

        if(orientation == HORIZONTAL)
            initFromSpec(spacing, spec, null);
        else
            initFromSpec(spacing, null, spec);
    }

    /**
     * Workhorse for specifying layouts from strings.
     **/
    private void initFromSpec(int spacing, String colSpec, String rowSpec) {
        cGap = spacing;
        oGap = true;

        // if no colspec make it 1 dimensional along the y-axis (VERTICAL)
        if(colSpec != null) {
            nCols = parseSpec(colSpec, 0);
        } else {
            nCols = 1;
            initColArrays();
        }

        // if no rowspec make it 1 dimensional along the x-axis (HORIZONTAL)
        if(rowSpec != null) {
            nRows = parseSpec(rowSpec, 1);
        } else {
            nRows = 1;
            initRowArrays();
        }
        
        cHash = new Hashtable();
    }

    private int getConstraintValue(String spec, String gridSpec) {
	// get trailing value if it exists.
	int val = 0;
	try {
	    String valStr = gridSpec.substring(1);
	    // substring is strange... if there is nothing it returns
	    // an empty string instead of exception.
	    if (valStr.length() > 0) {
		try {
		    val = Integer.valueOf(valStr).intValue();
		} catch (NumberFormatException fe) {
		    System.out.println("SSLayout string specification expected a number: " + "'" + gridSpec + " in " + spec);
		    fe.printStackTrace(System.out);
		    throw new RuntimeException("SSLayout: bad value for field.");
		}
	    }
	} catch (StringIndexOutOfBoundsException e) {
	    // silent. ok not to have a number
	}
        return val;
    }
}

