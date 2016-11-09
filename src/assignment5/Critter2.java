/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
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

public class Critter2 extends Critter {
	
	@Override
	public String toString(){
		return "2";
	}
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.SQUARE;
	}
	
	@Override 
	public Color viewColor(){
		return Color.CYAN;
	}
	
	@Override
	public void doTimeStep(){
		//Critter 2 will run
		run(Critter.getRandomInt(7));
		Critter2 child = new Critter2();
		reproduce(child, getRandomInt(7));
	}
	
	@Override
	public boolean fight (String opponent){
		
		boolean steps1 = true;
		boolean steps2 = false;
		for (int i = 0; i<8; i++){
			if (look(i, steps1) != null && look(i, steps2) != null){  
				//Critter 2 will only fight with Critter 1 and Critter 3, else it doesn't
				if ((look(i, steps1).equals("1") || look(i, steps2).equals("1")) || 
				(look(i, steps1).equals("3") || look(i, steps2).equals("3"))){
				return true;
			}
		}
		}
		return false;
	}	
}
