package application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import application.DataSet;
import application.MapApp;
import application.MarkerManager;
import application.SelectManager;
import gmapsfx.GoogleMapView;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import mapmaker.MapMaker;

// class for map and general application services (file IO, etc.)
public class GeneralService {
//	private static boolean singleton = false;
	private int currentState;
	private SelectManager selectManager;
	private GoogleMap map;
    private MarkerManager markerManager;



    private static final String DATA_FILE_PATTERN = "[\\w_]+.map";
    private static final String DATA_FILE_DIR_STR = "data/mapfiles/";

    private List<String> filenames;
    DataSet dataSet;

    public GeneralService(GoogleMapView mapComponent, SelectManager selectManager, MarkerManager markerManager) {
        // get map from GoogleMapView
    	this.map = mapComponent.getMap();
    	this.selectManager = selectManager;
        this.markerManager = markerManager;
        this.markerManager.setMap(map);
    	filenames = new ArrayList<String>();

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

    public static String getDataSetDirectory() { return GeneralService.DATA_FILE_DIR_STR; }

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

    public void displayIntersections(DataSet dataset) {
    	// initialize graph instance var and HashMap for intermediate RoadSegments
    	selectManager.setAndDisplayData(dataset);

    }

    /**
     * Check if file name matches pattern [filename].map
     *
     * @param str - path to check
     * @return string to use as path
     */
    public String checkDataFileName(String str) {
    	if(Pattern.matches(DATA_FILE_PATTERN, str)) {
            return DATA_FILE_DIR_STR + str;
    	}
    	return null;
    }

    public void runFetchTask(String fName, ComboBox<DataSet> cb, Button button) {
        Task<String> task = new Task<String>() {
            @Override
        	public String call() {

//                System.out.println("In call function for Fetch Task");
                /*if(currentlyFetching) {
                	MapApp.showErrorAlert("Fetch Error : ", "Data set already being fetched.\nTry again after task finishes.");
                	return "alreadyFetching";
                }*/

                System.out.println("Before if(writeDataToFile...");
        		if(writeDataToFile(fName)) {
                    return fName;
        		}

        		return "z" + fName;

            }
        };

        //final Stage loadStage = new Stage();

        Alert fetchingAlert = MapApp.getInfoAlert("Loading : ", "Fetching data for current map area...");
        task.setOnSucceeded( e -> {
           if(task.getValue().equals(fName)) {
               addDataFile(fName);

               cb.getItems().add(new DataSet(fName));
               if(fetchingAlert.isShowing()) {
            	   fetchingAlert.close();
               }
               MapApp.showInfoAlert("Fetch completed : ", "Data set : \"" + fName + "\" written to file!");
               System.out.println("Fetch Task Succeeded");

           }
           else {
               System.out.println("Task Succeeded : fName returned differently");

           }
           System.out.println("In onSucceeded");

           button.setDisable(false);

        });


        task.setOnFailed( e -> {

        });

        task.setOnRunning(e -> {
            button.setDisable(true);
            System.out.println("ON RUNNING");
            fetchingAlert.showAndWait();
        });

        Thread fetchThread = new Thread(task);
        fetchThread.start();
    }

    public List<String> getDataFiles() {
    	return filenames;
    }

    public static String getFileRegex() {
    	return GeneralService.DATA_FILE_PATTERN;
    }


    public void setState(int state) {
    	currentState = state;
    }


    public double getState() { return currentState; }


}

