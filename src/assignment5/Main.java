/* CRITTERS GUI Main.java
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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
    public static void main(String[] args) {
        Critter.displayWorld();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("world.fxml"));
		primaryStage.setTitle("Critter World");
        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.show();
	}
}
