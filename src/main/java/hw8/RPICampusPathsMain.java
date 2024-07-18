package hw8;

import hw6.*;
import hw7.*;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.image.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.shape.*;

public class RPICampusPathsMain extends Application{
	
	private CampusModel model;
	private int numClicks = 0;
	private ArrayList<MapNode> nodesClicked = new ArrayList<MapNode>();
	private HashMap<ImageView,MapNode> imageToNode = new HashMap<ImageView,MapNode>();
	private HashMap<MapNode,ImageView> nodeToImage = new HashMap<MapNode,ImageView>();
	private Pane root = new Pane();
	private double width;
	private double height;
	
	public void displayPath() {
		MapNode a = nodesClicked.get(0);
		MapNode b = nodesClicked.get(1);
		Iterator<QueueItem<MapNode>> itr = model.findPath(a.getName(), b.getName());
		QueueItem<MapNode> curr;
		if (itr.hasNext()) curr = itr.next();
		while (itr.hasNext()) {
			curr = itr.next();
			Line line = new Line();
			double xRatio = curr.parent.getX() / 2056.0;
	    	double yRatio = curr.parent.getY() / 1920.0;
			line.setStartX(xRatio*width-0.025*width);
			line.setStartY(yRatio*height-0.029*height);
			xRatio = curr.node.getX() / 2056.0;
	    	yRatio = curr.node.getY() / 1920.0;
			line.setEndX(xRatio*width-0.025*width);
			line.setEndY(yRatio*height-0.029*height);
			line.setStrokeWidth(0.005*width);
			root.getChildren().add(1,line);
		}
	}
	
	public void start(Stage stage) {
		// determines scale of image based on screen size
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		double min;
		if (bounds.getWidth() < bounds.getHeight()) {
			min = bounds.getWidth();
		}
		else min = bounds.getHeight();
		width = min;
		height = (min / 2056.0) * 1920.0;
		// opens image
		InputStream stream;
		Image mapPNG;
		try {
			stream = new FileInputStream("data/RPI_campus_map_2010_extra_nodes_edges.png");
			mapPNG = new Image(stream);
			stream.close();
		}
		catch (Exception e) {
			return;
		}
	    // inits the image view
	    ImageView map = new ImageView();
	    map.setImage(mapPNG);
	    map.setFitWidth(width);
	    map.setFitHeight(height);
	    map.setPreserveRatio(true);
	    // inits root with image
	    root.getChildren().add(map);
	    // gets button images
	    Image grayBtn;
	    Image blueBtn;
	    try {
	    	stream = new FileInputStream("data/gray_circle.png");
			grayBtn = new Image(stream);
			stream.close();
			stream = new FileInputStream("data/blue_circle.png");
			blueBtn = new Image(stream);
			stream.close();
	    }
	    catch (Exception e) {
			return;
		}
	    // gets node data from the model
	    try {
	    	model = new CampusModel();
	    }
	    catch (Exception e) {
	    	return;
	    }
	    Iterator<MapNode> itr = model.getBuildings();
	    
	    MapNode curr;
	    while (itr.hasNext()) {
	    	curr = itr.next();
	    	ImageView img = new ImageView();
	    	img.setImage(grayBtn);
	    	img.setFitWidth(0.015*width);
	    	img.setFitHeight(0.015*width);
	    	img.setPreserveRatio(true);
	    	double xRatio = curr.getX() / 2056.0;
	    	double yRatio = curr.getY() / 1920.0;
	    	img.setLayoutX(xRatio*width-0.031*width);
	    	img.setLayoutY(yRatio*height-0.037*height);
	    	img.setPickOnBounds(false);
	        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	if (!img.getImage().equals(blueBtn)) {
	            		if (numClicks < 2) {
	            			numClicks = numClicks + 1;
	            			img.setImage(blueBtn);
	            			nodesClicked.add(imageToNode.get(img));
	            		}
	            		if (numClicks == 2) {
	            			displayPath();
	            		}
	            	}
	            }

	        });
	        imageToNode.put(img,curr);
	        nodeToImage.put(curr,img);
	    	root.getChildren().add(img);
	    }
	    Button btn = new Button("Reset");
    	btn.setLayoutX(0.3*width);
    	btn.setLayoutY(0.15*height);
    	btn.setPickOnBounds(false);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	numClicks = 0;
            	nodesClicked.clear();
            	ObservableList<Node> children = root.getChildren();
            	while (children.size() > imageToNode.size()+2) {
            		children.remove(1);
            	}
            	for (ImageView image : imageToNode.keySet()) {
            		image.setImage(grayBtn);
            	}
            }

        });
    	root.getChildren().add(btn);
	    //Setting the Scene object
	    Scene scene = new Scene(root, width, height);      
	    stage.setTitle("Displaying Image");
	    stage.setScene(scene);
	    stage.show();
	 }
	
	 static void main(String[] args) {
		launch(args);
	}
}