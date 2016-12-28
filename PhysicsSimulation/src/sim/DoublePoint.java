package sim;

public class DoublePoint {
	private double Xpos = 0;
	private double Ypos = 0;
	
	public DoublePoint(double X, double Y) {
		this.Xpos = X;
		this.Ypos = Y;
	} // doublePoint constructor
	
	public double getXPos(){
		return Xpos;
	} // getXPos
	
	public double getYPos(){
		return Ypos;
	} // getYPos
	
	public void setXPos(double newXPos){
		this.Xpos = newXPos;
	} // setXPos
	
	public void setYPos(double newYPos){
		this.Ypos = newYPos;
	} // setYPos
} // doublePoint
