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

    public void startVisualization() {

    	List<LatLong> latLongs = new ArrayList<LatLong>();
    	JavascriptArray jsArray = new JavascriptArray();
    	manager.hideIntermediateMarkers();

    	// create JavascriptArray of points
    	for(geography.GeographicPoint point : points) {
    		LatLong ll = new LatLong(point.getX(), point.getY());
            jsArray.push(ll);
            latLongs.add(ll);
    	}

        // get javascript runtime and execute animation
    	IJavascriptRuntime runtime = JavascriptRuntime.getInstance();
    	String command = runtime.getFunction("visualizeSearch", manager.getMap(), jsArray);
    	runtime.execute(command);


    }


}
