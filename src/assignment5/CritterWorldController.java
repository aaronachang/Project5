package assignment5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import assignment5.Critter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class CritterWorldController implements Initializable {
	
	private static final int SQ_SIZE = 6;
	
	@FXML private TextArea critterStats;
	@FXML private Text totalSteps;
	@FXML private TextField seedField;
	@FXML private TextField critterAddCount;
	@FXML private TextField addSteps;
	@FXML private ChoiceBox<String> critterSelect;
    @FXML private Canvas critterCanvas;
    @FXML private AnchorPane critterPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Slider canvasSlider;
    @FXML private Slider animationSlider;
    
    @FXML private CanvasController canvasController;

    private GraphicsContext gc;
    
    List<String> javaFiles = new ArrayList<String>();
    
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
        });
        animationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        	editTime(10200 / (newValue.longValue() + 1));
        });
        
        File[] files = new File(System.getProperty("user.dir") + "/src/assignment5").listFiles();
        for (File file : files) {
            if (file.isFile()) {
            	if (file.getName().endsWith(".java")){
            		String fileName = file.getName();
            		String classes = fileName.substring(0, fileName.length() - 5);

					try {
						if (!file.getName().equals("AlgaephobicCritter.java")) {
							Class<?> myCritter = Class.forName("assignment5." + classes);
							Object instanceOfMyCritter = myCritter.getConstructor().newInstance();
							Critter me = (Critter) instanceOfMyCritter;
						}
						critterSelect.getItems().add(classes);
						javaFiles.add(classes);
					} catch (Exception e) {}
            	}
            }
        }
    }
    
    @FXML
    protected void clearWorld() {
    	Critter.clearWorld();
    	draw(critterCanvas, Params.world_width*SQ_SIZE, Params.world_height*SQ_SIZE);
    	totalSteps.setText("");
    	//System.out.println("Population reset.");
    }
    
    @FXML 
    protected void multipleSteps() {
    	for (int i = 0; i < Integer.parseInt(addSteps.getText()); i++){
    		doStep();
    	}
    }
    
    @FXML
    protected void doStep() { 
    	Critter.worldTimeStep();
        
        draw(critterCanvas, Params.world_width*SQ_SIZE, Params.world_height*SQ_SIZE);
        totalSteps.setText("Step " + Critter.timestep);
        if (checked) printStats();
    }
    
    @FXML
    protected void addCritters() throws InvalidCritterException {
    	while (running) {}
    	for (int i = 0; i < Integer.parseInt(critterAddCount.getText()); i++){
    		if (critterSelect.getValue() != null)
    			Critter.makeCritter("assignment5." + (String)critterSelect.getValue());
    	}
    }
    
    @FXML
    protected void setSeed() { 
    	Critter.setSeed(Long.parseLong(seedField.getText()));
    }
    
    @FXML
    protected void quit() { System.exit(0); }
    
    long time = 200;
    protected void editTime(long time) {
    	this.time = time;
    	animationChange = true;
    }
    
    boolean pause = false;
    boolean animationChange = false;
    
    @FXML
    protected void pauseSim() {
    	pause = true;
    }
    
    boolean running;
    
    @FXML
    protected void startSim() { 
    	pause = false;    	
    	new Timer().scheduleAtFixedRate(new TimerTask(){
    		@Override
    		public void run(){
    			running = true;
    			doStep();
    			running = false;
    			if (animationChange || pause) {
    				cancel();
    			}
    		}
    	}, 0, time);
    	animationChange = false;
    }
    
    boolean checked = false;
    
    protected void printStats() {
	    String statsString = "";
	    for (String c: javaFiles) {
		    try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    PrintStream ps = new PrintStream(baos);
			    PrintStream old = System.out;
		    	
		    	List<Critter> crts = Critter.getInstances(c);
		    	Class<?> classes = Class.forName("assignment5." + c);
				System.setOut(ps);
				classes.getMethod("runStats", java.util.List.class).invoke(null, crts);
				System.out.flush();
			    System.setOut(old);
			    statsString += c + ": " + baos.toString() + "\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    critterStats.setText(statsString);
    }
    
    @FXML
    protected void toggleStats() {
    	if (!checked) {
			printStats();
			checked = true;
		} else {
			critterStats.setText("");
			checked = false;
		}
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
        }

        for (int y = startY; y <= endY; y += SQ_SIZE) {
            gc.strokeLine(0, y, (double)endX, y);
        }
        
        
        for (Critter c : Critter.getPopulation()) {
        	gc.setStroke(c.viewOutlineColor());
        	gc.setFill(c.viewColor());
			int leftX = startX + c.getX_coord()*SQ_SIZE;
			int topY = startY + c.getY_coord()*SQ_SIZE;
        	switch(c.viewShape()) {
    			case CIRCLE:
    				gc.fillOval(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE);
    				gc.strokeOval(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE); 
    				break;
    		
    			case TRIANGLE:
    				double[] xPointsTriangle = {leftX + SQ_SIZE/2, leftX + SQ_SIZE, leftX};
    				double[] yPointsTriangle = {topY, topY + SQ_SIZE, topY + SQ_SIZE};
    				gc.fillPolygon(xPointsTriangle, yPointsTriangle, 3);
    				gc.strokePolygon(xPointsTriangle, yPointsTriangle, 3);
    				break;
				
    			case DIAMOND:
    				double[] xPointsDiamond = {leftX + SQ_SIZE/2, leftX + SQ_SIZE, leftX + SQ_SIZE/2, leftX};
    				double[] yPointsDiamond = {topY, topY + SQ_SIZE/2, topY + SQ_SIZE, topY + SQ_SIZE/2};
    				gc.fillPolygon(xPointsDiamond, yPointsDiamond, 4);
    				gc.strokePolygon(xPointsDiamond, yPointsDiamond, 4);
    				break;
    				
    			case STAR:
    				double[] xPointsStar = {leftX + SQ_SIZE/2, leftX + SQ_SIZE*9/14, leftX + SQ_SIZE, 
    						leftX + SQ_SIZE*10.5/14, leftX + SQ_SIZE*6/7, leftX + SQ_SIZE/2, leftX + SQ_SIZE/7, 
    						leftX + SQ_SIZE*3.5/14, leftX, leftX + SQ_SIZE*5/14};
    				double[] yPointsStar = {topY, topY + SQ_SIZE*5/14, topY + SQ_SIZE*5.5/14, topY + SQ_SIZE*9/14,
    						topY + SQ_SIZE, topY + SQ_SIZE*11.5/14, topY + SQ_SIZE, topY + SQ_SIZE*9/14,
    						topY + SQ_SIZE*5.5/14, topY + SQ_SIZE*5/14};
    				gc.fillPolygon(xPointsStar, yPointsStar, 10);
    				gc.strokePolygon(xPointsStar, yPointsStar, 10);
    				break;
				
    			default:
    				gc.fillRect(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE);
    				gc.strokeRect(startX + c.getX_coord()*SQ_SIZE, startY + c.getY_coord()*SQ_SIZE, SQ_SIZE, SQ_SIZE);
    				break;
        	}
        }
    }
}