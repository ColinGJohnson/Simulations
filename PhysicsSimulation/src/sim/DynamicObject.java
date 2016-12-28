package sim;

import java.awt.Color;
import java.util.ArrayList;

public class DynamicObject {
	private DynamicObject original;
	private String name = "";
	private double mass = 0;
	private double X = 0;
	private double Y = 0;
	public ArrayList<DoublePoint> posHistory;
	private double vX = 0;
	private double vY = 0;
	private double vMax = Double.MAX_VALUE;
	private Color objectColor;
	private short shape = 0;
	private double size = 0;
	public final static short ROUND = 0;
	public final static short SQUARE = 1;
	private boolean selected = false;
	
	public DynamicObject(double X, double Y, double mass, double size, Color objectColor, String name) {
		DynamicObject original = this;
		posHistory = new ArrayList<>();
		this.X = X;
		this.Y = Y;
		this.mass = mass;
		this.objectColor = objectColor;
		this.name = name;
		this.size = size;
	} // DynamicObject Constructor
	
	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public ArrayList<DoublePoint> getPosHistory() {
		return posHistory;
	}

	public void setPosHistory(ArrayList<DoublePoint> posHistory) {
		this.posHistory = posHistory;
	}

	public double getvMax() {
		return vMax;
	}

	public void setvMax(double vMax) {
		this.vMax = vMax;
	}

	public Color getObjectColor() {
		return objectColor;
	}

	public void setObjectColor(Color objectColor) {
		this.objectColor = objectColor;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setShape(short shape) {
		this.shape = shape;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getXpos() {
		return X;
	}
	
	public void setXpos(double newXpos){
		X = newXpos;
	}
	
	public double getYpos() {
		return Y;
	}
	
	public void setYpos(double newYpos){
		Y = newYpos;
	}
	
	public double getvX() {
		return vX;
	}
	
	public void setvX(double newvX){
		vX = newvX;
	}
	
	public double getvY() {
		return vY;
	}
	
	public void setvY(double newvY){
		vY = newvY;
	}
	
	public double getSize(){
		return size;
	}
	
	public short getShape(){
		return shape;
	}
	
	public Color getColor(){
		 return objectColor;
	}
	
	public String getName(){
		return name;
	}
	
	public void setVMax(double newVMax){
		this.vMax = newVMax;
	}
	
	public double getVMax(){
		return vMax;
	}
} // DynamicObject
