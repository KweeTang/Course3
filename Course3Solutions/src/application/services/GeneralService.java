package application.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import application.DataSet;
import application.MapApp;

import java.util.Iterator;

import geography.GeographicPoint;
import geography.RoadSegment;
import gmapsfx.GoogleMapView;
import gmapsfx.javascript.event.UIEventType;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import gmapsfx.javascript.object.Marker;
import gmapsfx.javascript.object.MarkerOptions;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import mapmaker.MapMaker;
import netscape.javascript.JSObject;
import util.MapLoader;

// class for map and general application services (file IO, etc.)
public class GeneralService {

	GoogleMap map;
	GoogleMapView mapComponent;
    MapApp mainApp;

    // one for each instance, make sure application only has one instance of GeneralService
    private boolean currentlyFetching;

    static String dataFilePattern = "[\\w_]+.map|datafiles/[\\w_]+.map";
    roadgraph.MapGraph graph;
    HashMap<GeographicPoint,HashSet<RoadSegment>>  roads;
    List<String> filenames;

    public GeneralService(GoogleMapView mapComponent, MapApp mainApp) {
    	this.mapComponent = mapComponent;
    	this.map = mapComponent.getMap();
    	filenames = new ArrayList<String>();
        this.mainApp = mainApp;
        currentlyFetching = false;

    	// uncomment to click map and print coordinates
    	/*mapComponent.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
    		System.out.println(obj.getMember("latLng"));
    	});*/
    }


	// writes geographic data flat file
    public boolean writeDataToFile(String filename) {
    	float[] arr = getBoundsArray();
    	MapMaker mm = new MapMaker(arr);

    	// parse data and write to filename
    	if(mm.parseData(filename)) {
            return true;
    	}

        return false;
    }

    // gets current bounds of map view
    public float[] getBoundsArray() {
        LatLong sw, ne;
    	LatLongBounds bounds = map.getBounds();

    	sw = bounds.getSouthWest();
    	ne = bounds.getNorthEast();

    	// [S, W, N, E]
    	return new float[] {(float) sw.getLatitude(), (float) sw.getLongitude(),
    			            (float) ne.getLatitude(), (float) ne.getLongitude()};
    }

    public void addDataFile(String filename) {
    	filenames.add(filename);
    }

    public void displayIntersections(String filename) {
    	// initialize graph instance var and HashMap for intermediate RoadSegments
    	LatLongBounds bounds = new LatLongBounds();
    	graph = new roadgraph.MapGraph();
    	roads = new HashMap<GeographicPoint,HashSet<RoadSegment>>();

    	MapLoader.loadOneWayMap(filename, graph, roads);


    	Iterator<GeographicPoint>it = graph.getNodes().iterator();
        while(it.hasNext()) {
        	GeographicPoint point = it.next();
        	MarkerOptions markerOptions = new MarkerOptions();
        	LatLong coord = new LatLong(point.getX(), point.getY());
        	bounds.extend(coord);
        	markerOptions.animation(null)
        				 .icon(null)
        				 .position(coord)
                         .title(null)
                         .visible(true);
        	Marker marker = new Marker(markerOptions);
        	map.addMarker(marker);
            map.addUIEventHandler(marker, UIEventType.click, (JSObject o) -> {
            	System.out.println(coord);
            });

        }

        map.fitBounds(bounds);
        System.out.println("End of display Intersections");


    }

    public boolean goodDataFileName(String str) {
    	return Pattern.matches(dataFilePattern, str);
    }

    public void runFetchTask(String fName, ComboBox<DataSet> cb) {
        Task<String> task = new Task<String>() {
            @Override
        	public String call() {

                System.out.println("In call function for Fetch Task");
                System.out.println(currentlyFetching);
                if(currentlyFetching) {
                	MapApp.showErrorAlert("Fetch Error : ", "Data set already being fetched.\nTry again after task finishes.");
                	return "alreadyFetching";
                }

                System.out.println("Before if(writeDataToFile...");
        		if(writeDataToFile(fName)) {

                    return fName;
        		}

        		return "z" + fName;

            }
        };

        //final Stage loadStage = new Stage();

        task.setOnSucceeded( e -> {
           if(task.getValue().equals(fName)) {
               addDataFile(fName);
               cb.getItems().addAll(new DataSet(fName));
               MapApp.showInfoAlert("Fetch completed : ", "Data set : \"" + fName + "\" written to file!");
               System.out.println("Fetch Task Succeeded");

           }
           else {
               System.out.println("Task Succeeded : fName returned differently");

           }
           System.out.println("In onSucceeded");

           currentlyFetching = false;

        });

        task.setOnFailed( e -> {

        });

        task.setOnRunning(e -> {
            System.out.println("ON RUNNING");
            currentlyFetching = true;
            MapApp.showInfoAlert("Loading : ", "Fetching data for current map area...");

        });

        Thread fetchThread = new Thread(task);
        fetchThread.start();
    }

    public List<String> getDataFiles() {
    	return filenames;
    }

    public static String getFileRegex() {
    	return GeneralService.dataFilePattern;
    }

}
