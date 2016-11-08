/* CRITTERS GUI CanvasController.java
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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;


public class CanvasController implements Initializable {
	
	private static final int SQ_SIZE = 6;
	
    @FXML private Canvas critterCanvas;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // initialize your logic here: all @FXML variables will have been injected

    	critterCanvas.setWidth(Params.world_width * SQ_SIZE);
    	critterCanvas.setHeight(Params.world_height * SQ_SIZE);
        //centerNodeInScrollPane(scrollPane, (Node) critterCanvas);
    }
}
