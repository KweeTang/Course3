package application;

import java.util.ArrayList;
import java.util.List;

import gmapsfx.javascript.IJavascriptRuntime;
import gmapsfx.javascript.JavascriptArray;
import gmapsfx.javascript.JavascriptRuntime;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import javafx.concurrent.Task;
import javafx.scene.control.Button;

public class RouteVisualization {
	List<geography.GeographicPoint> points;
	MarkerManager manager;
    private Button vButton;




	public RouteVisualization(MarkerManager manager) {
        points = new ArrayList<geography.GeographicPoint>();
		this.manager = manager;
	}

    public void acceptPoint(geography.GeographicPoint point) {
    	points.add(point);

        System.out.println("accepted point : " + point);
    }

    public void disableButton(boolean value) { vButton.setDisable(value); }
    /*public void startVisualization() {
        Task<Boolean> task = new Task<Boolean>() {
        	@Override
        	public Boolean call() {
        		manager.hideIntermediateMarkers();
        		GoogleMap map = manager.getMap();
            	for(geography.GeographicPoint point : points) {
            		manager.displayMarker(point);
            		try {
        				Thread.sleep(500);
        			} catch (InterruptedException e) {
                        System.out.println("InterruptedException when trying to sleep");
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
            	}
        		return true;

        	}
        };

        task.setOnSucceeded( e -> {
        	System.out.println("\nTask Succeeded");
        });

        task.setOnRunning( e -> {
        	System.out.println("\nTask setOnRunning");
        });

        Thread vThread = new Thread(task);
        vThread.start();
//        manager.hideIntermediateMarkers();
    	GoogleMap map = manager.getMap();
    	for(geography.GeographicPoint point : points) {
    		manager.displayMarker(point);
    		try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
                System.out.println("InterruptedException when trying to sleep");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }*/

    public void startVisualization() {
    	List<LatLong> latLongs = new ArrayList<LatLong>();
    	JavascriptArray jsArray = new JavascriptArray();
    	manager.hideIntermediateMarkers();

    	// create JavascriptArray of points
    	for(geography.GeographicPoint point : points) {
            //try {
    		LatLong ll = new LatLong(point.getX(), point.getY());
            jsArray.push(ll);
            latLongs.add(ll);
//        		manager.displayMarker(point);
            	//Thread.sleep(1000);
            /*} catch(InterruptedException ex) {
            	System.out.println("caught InterrupedException");
            }*/
    	}

    	IJavascriptRuntime runtime = JavascriptRuntime.getInstance();
    	String command = runtime.getFunction("visualizeSearch", jsArray);
    	runtime.execute(command);


    }


}
