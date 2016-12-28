package sim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.RepaintManager;
import javax.swing.Timer;

public class Simulation {
	private ArrayList<DynamicObject> dynObjList;
	private ArrayList<String[]> dynObjLabels; // array list of numbered labels
	private Dimension simulationSize;
	private boolean repaintable = false; // notify graphicsPanel

	private short state = 0; // paused or running state of simulation
	public final static int PAUSED = 0;
	public final static int RUNNING = 1;
	private int tickRate = 120; // recalculations per second
	private long lastLoop = System.currentTimeMillis();
	private long thisLoop;
	private int fps = 0; // frames per second based on deltaT

	private int edgeBehavior = 0; // what to do with edge collisions
	public final static int REMOVE = 0; // remove on collision
	public final static int BOUNCE = 1; // reverse V on collision
	public final static int WRAP = 2; // reset to opposite side on collision
	public final static int STOP = 3; // stop on collision

	private double globalGrav = 0.1; // global gravity in m/s
	private double globalGravAngle = 90; // angle of global gravity
	private double gravConst = 6.67408 * Math.pow(10, -11); // universal gravitational constant

	public Simulation(Dimension simulationSize) {

		// list of dynamic objects to simulate
		dynObjList = new ArrayList<>();
		
		// list of indexed names of dynamic objects
		dynObjLabels = new ArrayList<>();

		// size of this simulation
		this.simulationSize = simulationSize;

		// cue simulation updates
		Timer timer = new Timer(1000 / tickRate, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		timer.start();
	} // Simulation

	public void start() {
		state = 1;
		lastLoop = System.currentTimeMillis();
		System.out.println("Simulation Started");
	} // start

	public void pause() {
		state = 0;
		System.out.println("Simulation Paused");
	} // pause

	public void update() {
		if (state == RUNNING) {

			// calculate delta T and fps
			long deltaT = updateTimer();

			// add angular global gravity to X and Y velocities
			applyGlobalGrav(deltaT);

			// update the positions of each object independent of each other
			updatePositions(deltaT);

			// check for edge collision
			updateEdgeCollisions();

			// request repaint
			repaintable = true;

		} else if (state == PAUSED) {

		} // else if paused
	} // update

	private void updateEdgeCollisions() {
		ArrayList<DynamicObject> moveList = new ArrayList<>();

		// check for out of bounds objects, add to a list of objects to alter
		for (DynamicObject dynObj : dynObjList) {
			if (dynObj.getXpos() + dynObj.getSize() / 2 > simulationSize.getWidth()
					|| dynObj.getXpos() - dynObj.getSize() / 2 < 0
					|| dynObj.getYpos() + dynObj.getSize() / 2 > simulationSize.getHeight()
					|| dynObj.getYpos() - dynObj.getSize() / 2 < 0) {
				moveList.add(dynObj);
			}
		} // for

		switch (edgeBehavior) {
		case REMOVE:

			// remove offending objects
			for (DynamicObject moveObj : moveList) {
				System.out.println("removing '" + dynObjList.get(dynObjList.indexOf(moveObj)).getName() + "' - Out of bounds");
				dynObjList.remove(moveObj);
			}

			break;
		case BOUNCE:

			break;
		case WRAP:

			// move offending objects
			for (DynamicObject moveObj : moveList) {

				// store index of unaltered object so it may be replaced
				int originalIndex = dynObjList.indexOf((Object) moveObj);

				// set object's position to the remainder of
				// overflow/simDimension
				if (moveObj.getXpos() > simulationSize.getWidth()) {
					moveObj.setXpos((moveObj.getXpos() % simulationSize.getWidth()));
				} else if (moveObj.getYpos() > simulationSize.getHeight()) {
					moveObj.setYpos((moveObj.getYpos() % simulationSize.getHeight()));
				}

				// set original object to new, moved object
				dynObjList.set(originalIndex, (DynamicObject) moveObj);
			}

			break;
		case STOP:
			// stop offending objects
			for (DynamicObject moveObj : moveList) {

				// store index of unaltered object so it may be replaced
				int originalIndex = dynObjList.indexOf((Object) moveObj);

				// move object back to edge
				if (moveObj.getXpos() > simulationSize.getWidth()) {
					moveObj.setXpos(moveObj.getXpos() - (moveObj.getXpos() % simulationSize.getWidth()));
					moveObj.setvX(0);
				} else if (moveObj.getYpos() > simulationSize.getHeight()) {
					moveObj.setYpos(moveObj.getYpos() - (moveObj.getYpos() % simulationSize.getHeight()));
					moveObj.setvY(0);
				}

				// set original object to new, moved object
				dynObjList.set(originalIndex, (DynamicObject) moveObj);
			}
			break;

		default:
			break;
		}
	} // updateEdgeCollisions

	private void updateAccelerations(long deltaT) {

	} // updateAccelerations

	private void applyGlobalGrav(long deltaT) {
		double gravX = globalGrav * Math.cos(Math.toRadians(globalGravAngle));
		double gravY = globalGrav * Math.sin(Math.toRadians(globalGravAngle));

		// apply acceleration as long as it does not exceed max vMax
		for (DynamicObject dynObj : dynObjList) {
			dynObj.setvX(dynObj.getvX() + deltaT * gravX / 1000);
			dynObj.setvY(dynObj.getvY() + deltaT * gravY / 1000);
		}
	} // applyGlobalGrav

	private void updateVelocities(long deltaT) {

	} // updateVelocities

	private void updatePositions(long deltaT) {
		for (DynamicObject dynObj : dynObjList) {
			dynObj.setXpos(dynObj.getXpos() + dynObj.getvX());
			dynObj.setYpos(dynObj.getYpos() + dynObj.getvY());
		}
	} // updatePositions

	private long updateTimer() {
		// record time of update start
		thisLoop = System.currentTimeMillis();

		// time since start of last loop
		long deltaT = thisLoop - lastLoop;
		lastLoop = thisLoop;
		deltaT = (deltaT == 0) ? 1 : deltaT;
		fps = (int) (1000 / deltaT);
		return deltaT;
	} // updateTimer

	public void addDynamicObject(double X, double Y, double mass, double size, Color objectColor, String name) {

		// if name is Object #, increment label number
		if (name.charAt(0) == '~') {
			String baseName = name.substring(1);
			String[] baseNameArr = new String[2];

			// if baseName exists, add new occurrence, else add new
			int baseNameIndex = -1;
			if (dynObjLabels.size() > 0) {
				for (String[] strings : dynObjLabels) {
					if (strings[0].equals(baseName)) {
						baseNameIndex = dynObjLabels.indexOf(strings);
						break;
					} // if
				} // for
			} // if

			if (baseNameIndex != -1) {
				baseNameArr[0] = dynObjLabels.get(baseNameIndex)[0];
				baseNameArr[1] = dynObjLabels.get(baseNameIndex)[1];
				baseNameArr[1] = "" + (Integer.parseInt(baseNameArr[1]) + 1);
				dynObjLabels.set(baseNameIndex, baseNameArr);
			} else {
				baseNameArr[0] = baseName;
				baseNameArr[1] = "1";
				dynObjLabels.add(baseNameArr);
			} // else

			name = baseNameArr[0] + " #" + baseNameArr[1];
		} // if

		dynObjList.add(new DynamicObject(X, Y, mass, size, objectColor, name));
		System.out.println(
				"Added Dynamic object '" + name + "' (mass: " + mass + "kg, size: " + size + "m) at " + X + ", " + Y);
	} // addDynamicObject

	public void clearDynamicObjects() {
		dynObjList.clear();
		System.out.println("Removed all dynamic objects");
	} // clearDynamicObjects

	public void stopDynamicObjects() {
		for (int i = 0; i < dynObjList.size(); i++) {
			dynObjList.get(i).setvX(0);
			dynObjList.get(i).setvY(0);
		} // for
	} // stopDynamicObjects

	public ArrayList<DynamicObject> getObjects() {
		return dynObjList;
	} // getObjects
	
	public void selectNone() {
		for (DynamicObject dynObj : dynObjList) {
			dynObj.setSelected(false);			
		} // for
		System.out.println("Deselected all dynamic objects");
		
		// update selection graphic
		repaintable = true;
	} // selectNone
	
	public void selectAll() {
		for (DynamicObject dynObj : dynObjList) {
			dynObj.setSelected(true);	
		} // for
		System.out.println("Selected all dynamic objects");
		
		// update selection graphic
		repaintable = true;
	} // selectAll
	
	public void deleteSelected(){
		ArrayList<Object> removeList = new ArrayList<>();
		for (DynamicObject dynObj : dynObjList) {
			if (!dynObj.isSelected()) {
				removeList.add(dynObj);
			} // if
		} // for
		
		for (Object removeObj : removeList) {
			dynObjList.remove(removeObj);
		}
	} // deleteSelected

	public void reset() {
		System.out.println("Simulation Reset");
	} // reset

	public int getFps() {
		return fps;
	} // getFps

	public boolean getRepaintable() {
		return repaintable;
	} // getRepaintable

	public int getEdgeBehavior() {
		return edgeBehavior;
	} // get getEdgeBehavior

	public void setEdgeBehavior(int edgeBehavior) {
		this.edgeBehavior = edgeBehavior;
	} // set setEdgeBehavior
	
	public double getGlobalGrav() {
		return globalGrav;
	} // getGlobalGrav

	public void setGlobalGrav(double globalGrav) {
		this.globalGrav = globalGrav;
	} // setGlobalGrav

	public double getGlobalGravAngle() {
		return globalGravAngle;
	} // getGlobalGravAngle

	public void setGlobalGravAngle(double globalGravAngle) {
		this.globalGravAngle = globalGravAngle;
	} // setGlobalGravAngle
} // Simulation
