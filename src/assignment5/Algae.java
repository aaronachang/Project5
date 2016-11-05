/* CRITTERS GUI Algae.java
 * EE422C Project 5 submission by
 * Aaron Chang
 * AAC3434
 * 16475
 * Siva Manda
 * SM48525
 * 16480
 * Slip days used: <0>
 * Git URL: https://github.com/aaronachang/Project5
 * Fall 2016
 */
package assignment5;

import assignment5.Critter.TestCritter;

public class Algae extends TestCritter {

	public String toString() { return "@"; }
	
	public boolean fight(String opponent) {
		if (toString().equals(opponent)) { // same species as me!
			/* try to move away */
			walk(Critter.getRandomInt(8));
		}
		return false; 
	}
	
	public void doTimeStep() {
		setEnergy(getEnergy() + Params.photosynthesis_energy_amount);
	}
	
	public CritterShape viewShape() { return CritterShape.CIRCLE; }
	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.GREEN; }
}
