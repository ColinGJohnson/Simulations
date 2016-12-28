package sim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GraphicsJPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private int minDrawSize = 8; // smallest allowable draw size (px)
	private int gridWidth = 0; // width of simulation in meters
	private int gridHeight = 0; // height of simulation in meters
	private int gridScale = 0; // Pixels per grid square (zoom)
	private int gridSize = 0; // Meters per grid square
	private int paintFrequency = 120; // times to try to repaint per second
	private Point mousePosition; // Track mouse Position for previews
	Simulation sim; // simulation to be displayed on the panel
	
	public GraphicsJPanel(int gridWidth, int gridHeight, int gridScale, Simulation sim) {
		this.setDoubleBuffered(true);
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		this.gridScale = gridScale;
		this.sim = sim;
		mousePosition = new Point(0, 0);
		
		// set a preferred size for the custom panel.
		updatePreferredSize();
		
		// create repaint timer
		Timer paintTimer = new Timer(1000/paintFrequency, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sim.getRepaintable()) {
					repaint();
				}
			}
		});
		
		// start repaint timer
		paintTimer.start();
	} // GraphicsJPanel

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// white background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);

		// Cartesian plane
		drawCartPlane(gridWidth, gridHeight, gridScale, g);
		
		// Objects
		drawObjects(g);
	} // paintComponent

	private void drawObjects(Graphics g) {
		
		// convert to graphics2d for anti-aliasing
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// loop through objects to draw
		for (DynamicObject dynObj: sim.getObjects()) {
			double size = dynObj.getSize();
			int drawX = (int)(dynObj.getXpos()*gridScale - (size*gridScale)/2); 
			int drawY = (int)(dynObj.getYpos()*gridScale - (size*gridScale)/2);
			int diameter = (int)(size * gridScale);
			
			// object shape
			g.setColor(dynObj.getColor());
			if (dynObj.getShape() == DynamicObject.ROUND) {
				if (diameter >= minDrawSize) {
					g2.fillOval(drawX, drawY, diameter, diameter);
					
					// object label
					g.drawString(dynObj.getName(), drawX + (int)(size*gridScale), drawY);
				} else {
					g2.drawOval(drawX, drawY, minDrawSize, minDrawSize);
					
					// object label
					g.drawString(dynObj.getName(), drawX + 8, drawY);
				} // else
			} // if
			
			// selection box
			if (dynObj.isSelected()) {
				g2.setColor(Color.GREEN);
				if (diameter >= minDrawSize) {
					g2.drawRect(drawX - 2, drawY - 2 , (int)(size*gridScale + 3), (int)(size*gridScale + 3));
				} else {
					g2.drawRect(drawX - 2, drawY - 2 , minDrawSize + 3, minDrawSize + 3);
				} // else
			} // if
		} // for
	} // drawObjects

	private void drawCartPlane(int width, int height, int gridSize, Graphics g) {
		g.setColor(Color.lightGray);
		
		//vertical lines
		for (int i = 0; i < width; i++) {
			g.drawLine(i*gridSize, 0, i*gridSize, height*gridSize);
		}
		
		//horizontal lines
		for (int i = 0; i < height; i++) {
			g.drawLine(0, i*gridSize, width*gridSize, i*gridSize);
		}
		
		//top/bottom/left/right border
		g.drawLine(0, 0, gridWidth*gridScale, 0);
		g.drawLine(0, 0, 0, gridHeight*gridScale);
		g.drawLine(0, gridHeight*gridScale, gridWidth*gridScale, gridHeight*gridScale);
		g.drawLine(gridWidth*gridScale, 0, gridWidth*gridScale, gridHeight*gridScale);
		
		// X and Y axis
		g.setColor(Color.GRAY);
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
		g.drawLine(0, (gridHeight*gridScale)/2, gridWidth*gridScale, (gridHeight*gridScale)/2);
		g.drawLine((gridWidth*gridScale)/2, 0, (gridWidth*gridScale)/2, gridHeight*gridScale);
		
		// X and Y axis labels
		g.setColor(Color.BLACK);
		g.drawString("X", gridWidth*gridScale + 4, (gridHeight*gridScale)/2 + 4);
		g.drawString("Y", (gridWidth*gridScale)/2 - 2, gridHeight*gridScale + 12);
	} // drawCartPlane
	
	public void updatePreferredSize(){
		setPreferredSize(new Dimension(gridWidth*gridScale + 20, gridHeight*gridScale + 20));
	} // updatePreferredSize
	
	public int getGridWidth(){
		return gridWidth;
	} // getGridWidth
	
	public void setGridWidth(int newGridWidth){
		gridWidth = newGridWidth;
		this.setSize(this.getWidth(), gridWidth*gridScale);
		updatePreferredSize();
		this.repaint();
	} // setGridWidth
	
	public int getGridHeight(){
		return gridHeight;
	} // getGridHeight
	
	public void setGridHeight(int newGridHeight){
		gridHeight = newGridHeight;
		this.setSize(this.getWidth(), gridHeight*gridScale);
		updatePreferredSize();
		this.repaint();
	} // setGridHeight
	
	public int getGridScale(){
		return gridScale;
	} // getGridScale
	
	public void setGridScale(int newGridScale){
		if (newGridScale > 2) {
			gridScale = newGridScale;
			updatePreferredSize();
			this.repaint();
		}
	} // setGridScale
	
	public void updateMousePosition(Point newMousePosition){
		mousePosition = newMousePosition;
	} // updateMousePosition
} // GraphicsPanel
