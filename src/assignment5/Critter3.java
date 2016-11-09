/* CRITTERS Critter3.java
 * EE422C Project 5 submission by
 * Aaron Chang
 * AAC3434
 * 16475
 * Siva Manda
 * SM48525
 * 16480
 * Slip days used: <0>
 * Git URL: https://github.com/aaronachang/Project4
 * Fall 2016
 */
package assignment5;

import javafx.scene.paint.Color;

public class Critter3 extends Critter {

	@Override
	public String toString(){
		return "3";
	}
	
	@Override
	public CritterShape viewShape(){
		return CritterShape.STAR;
	}
	
	@Override
	public Color viewFillColor(){
		return Color.DARKORCHID;
	}
	
	@Override
	public void doTimeStep() {
		int direction = Critter.getRandomInt(7);
		boolean steps = false; //look 1 space
		
		for (int i = 0; i < 150; i++){
			if(look(direction, steps) == null || !look(direction, steps).equals("3")){
				break;
			}
			direction = Critter.getRandomInt(7);
		}
		
		// Critter3 walks around randomly
		walk(direction);
		Critter3 child = new Critter3();
		reproduce(child, Critter.getRandomInt(7));
	}
	
	@Override
	public boolean fight(String opponent) {
		// Critter3 doesn't fight odd critters
		char c = opponent.charAt(0); 
		if (c % 2 == 0) { return true; }
		return false;
	}
}
