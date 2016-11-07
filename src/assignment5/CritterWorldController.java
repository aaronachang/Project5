package assignment5;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import assignment5.Critter;
import assignment5.Critter.CritterShape;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class CritterWorldController implements Initializable {
	
	private static final int SQ_SIZE = 6;
	
	@FXML private Text totalSteps;
	@FXML private TextField seedField;
	@FXML private TextField critterAddCount;
	@FXML private ChoiceBox<String> critterSelect;
    @FXML private Canvas critterCanvas;
    @FXML private AnchorPane critterPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Slider canvasSlider;
    
    @FXML private CanvasController canvasController;

    private GraphicsContext gc;
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // initialize your logic here: all @FXML variables will have been injected
    	
        gc = critterCanvas.getGraphicsContext2D();
        
        totalSteps.setText("");
        critterCanvas.setWidth(Params.world_width * SQ_SIZE);
        critterCanvas.setHeight(Params.world_height * SQ_SIZE);
        centerNodeInScrollPane(scrollPane, (Node) critterCanvas);
        canvasSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        critterCanvas.setScaleX(2.0 * (newValue.intValue()/100.0));
        critterCanvas.setScaleY(2.0 * (newValue.intValue()/100.0));
        centerNodeInScrollPane(scrollPane, (Node) critterCanvas);
            //scrollPane.setS
            //node.setTranslateX(node.getScene().getWidth()/4);     
            //node.setTranslateY(node.getScene().getHeight()/4);
        });
        critterSelect.getItems().add("Algae");
        critterSelect.getItems().add("Craig");
        critterSelect.getItems().add("Critter1");
        critterSelect.getItems().add("Critter2");
        critterSelect.getItems().add("Critter3");
        critterSelect.getItems().add("Critter4");
    }
    
    @FXML
    protected void clearWorld() {
    	Critter.clearWorld();
    	//canvasController.cool(); 
    	draw(critterCanvas, Params.world_width*SQ_SIZE, Params.world_height*SQ_SIZE);
    	totalSteps.setText("");
    	System.out.println("Population reset.");
    }
    
    @FXML
    protected void doStep() { 
    	Critter.worldTimeStep();
        
        draw(critterCanvas, Params.world_width*SQ_SIZE, Params.world_height*SQ_SIZE);
        totalSteps.setText("Step " + Critter.timestep);
    }
    
    @FXML
    protected void addCritters() throws InvalidCritterException { 
    	for (int i = 0; i < Integer.parseInt(critterAddCount.getText()); i++){
    		Critter.makeCritter("assignment5." + (String)critterSelect.getValue());
    		//System.out.println(critterSelect.getValue());
    	}
    	return; 
    }
    
    @FXML
    protected void setSeed() { 
    	Critter.setSeed(Long.parseLong(seedField.getText()));
    }
    
    @FXML
    protected void quit() { System.exit(0); }
    
    @FXML
    protected void startSim() { 
    	new Timer().scheduleAtFixedRate(new TimerTask(){
    	    @Override
    	    public void run(){
    	       doStep();
    	    }
    	},0,200);
    }
    
    @FXML
    protected void toggleStats() throws InvalidCritterException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		List<Critter> crts = Critter.getInstances("Craig");
		Class<?> classes = Class.forName("assignment5." + "Craig");
		classes.getMethod("runStats", java.util.List.class).invoke(null, crts);
    }

    public void centerNodeInScrollPane(ScrollPane scrollPane, Node node) {
        double h = scrollPane.getContent().getBoundsInLocal().getHeight();
        double y = (node.getBoundsInParent().getMaxY() + 
                    node.getBoundsInParent().getMinY()) / 2.0;
        double v = scrollPane.getViewportBounds().getHeight();
        scrollPane.setVvalue(scrollPane.getVmax() * ((y - 0.5 * v) / (h - v)));
    }
    
    private void draw(Canvas canvas, int width, int height) {
        
        int startX = (int)(canvas.getWidth() / 2 - width / 2.0);
        int startY = (int)(canvas.getHeight() / 2 - height / 2.0);
        int endX = (int)(canvas.getWidth() / 2 + width / 2.0);
        int endY = (int)(canvas.getHeight() / 2 + height / 2.0);
        
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(startX, startY, endX, endY);
        gc.setLineWidth(1.0);

        for (int x = startX; x <= endX; x += SQ_SIZE) {
            gc.strokeLine(x, 0, x, (double)endY);
            //gc.stroke();
        }

        for (int y = startY; y <= endY; y += SQ_SIZE) {
            gc.strokeLine(0, y, (double)endX, y);
            //gc.stroke();
        }
        
        
        for (Critter c : Critter.getPopulation()) {
        	//if (c.getX_coord() < 0 || c.getY_coord() < 0) System.out.println(c.getX_coord() + " - " + c.getY_coord());
        	if (c.viewColor() == c.viewOutlineColor()) {
        		gc.setFill(c.viewColor());
        		switch(c.viewShape()) {
        			case CIRCLE: gc.fillOval(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE); break;
        			default:
        				if (c.viewShape() == CritterShape.TRIANGLE) {
        					int leftX = startX + c.getX_coord()*SQ_SIZE;
        					int topY = startY + c.getY_coord()*SQ_SIZE;
        					double[] xPoints = {leftX + SQ_SIZE/2, leftX + SQ_SIZE, leftX};
        					double[] yPoints = {topY, topY + SQ_SIZE, topY + SQ_SIZE};
        					gc.fillPolygon(xPoints, yPoints, 3);
        				} else {
        					gc.fillRect(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE);
        				}
        				break;
        		}
        	} else {
        		gc.setStroke(c.viewOutlineColor());
        		gc.setFill(c.viewColor());
        		switch(c.viewShape()) {
    			case CIRCLE: gc.strokeOval(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE); break;
    			default:
    				if (c.viewShape() == CritterShape.TRIANGLE) {
    					int leftX = startX + c.getX_coord()*SQ_SIZE;
    					int topY = startY + c.getY_coord()*SQ_SIZE;
    					double[] xPoints = {leftX + SQ_SIZE/2, leftX + SQ_SIZE, leftX};
    					double[] yPoints = {topY, topY + SQ_SIZE, topY + SQ_SIZE};
    					gc.fillPolygon(xPoints, yPoints, 3);
    					gc.strokePolygon(xPoints, yPoints, 3);
    				} else {
    					gc.fillRect(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE);
    					gc.strokeRect(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE);
    				}
    				break;
        		}
        	}
        }
    }
}