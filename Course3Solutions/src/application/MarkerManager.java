package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import gmapsfx.javascript.event.UIEventType;
import gmapsfx.javascript.object.Animation;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.Marker;
import gmapsfx.javascript.object.MarkerOptions;
import javafx.scene.control.Button;
import gmapsfx.javascript.object.LatLongBounds;
import netscape.javascript.JSObject;

public class MarkerManager {

    private HashMap<geography.GeographicPoint, Marker> markerMap;
    private ArrayList<geography.GeographicPoint> markerPositions;
    private GoogleMap map;
    protected static String startURL = "http://maps.google.com/mapfiles/kml/pal3/icon40.png";
    protected static String destinationURL = "http://maps.google.com/mapfiles/kml/pal2/icon5.png";
    protected static String SELECTED_URL = "http://maps.google.com/mapfiles/kml/paddle/ltblu-circle.png";
    protected static String markerURL = "http://maps.google.com/mapfiles/kml/paddle/blu-diamond-lv.png";
	protected static String visURL = "http://maps.google.com/mapfiles/kml/paddle/red-diamond-lv.png";
    private Marker startMarker;
    private Marker destinationMarker;
    private Marker selectedMarker;
    private DataSet dataSet;
    private LatLongBounds bounds;
    private SelectManager selectManager;
    private RouteVisualization rv;
    private Button vButton;
    private boolean selectMode = true;

    public MarkerManager() {
    	markerMap = new HashMap<geography.GeographicPoint, Marker>();
    	this.map = null;
    	this.selectManager = null;
        this.rv = null;
        markerPositions = null;
    }
    public MarkerManager(GoogleMap map, SelectManager selectManager) {
    	// TODO -- parameters?
        dataSet = null;

    }

    /**
     * Used to set reference to visualization button. Manager will be responsible
     * for disabling button
     *
     * @param vButton
     */
    public void setVisButton(Button vButton) {
    	this.vButton = vButton;
    }

    public void setSelect(boolean value) {
    	selectMode = value;
    }
    public RouteVisualization getVisualization() { return rv; }



    public GoogleMap getMap() { return this.map; }
    public void setMap(GoogleMap map) { this.map = map; }
    public void setSelectManager(SelectManager selectManager) { this.selectManager = selectManager; }

    public void putMarker(geography.GeographicPoint key, Marker value) {
    	markerMap.put(key, value);

    }

    /** Used to initialize new RouteVisualization object
     *
     */
    public void initVisualization() {
    	rv = new RouteVisualization(this);
    }

    public void clearVisualization() {
    	rv = null;
    }

    // TODO -- protect against this being called without visualization built
    public void startVisualization() {
    	if(rv != null) {
	    	rv.startVisualization();
    	}
    }

    public void setStart(geography.GeographicPoint point) {
    	if(startMarker!= null) {
    		startMarker.setIcon(markerURL);
    	}
        startMarker = markerMap.get(point);
        startMarker.setIcon(startURL);
    }
    public void setDestination(geography.GeographicPoint point) {
    	if(destinationMarker != null) {
    		destinationMarker.setIcon(markerURL);
    	}
        destinationMarker = markerMap.get(point);
        destinationMarker.setIcon(destinationURL);
    }

    /**
     * TODO -- Might need to create all new markers and add them??
     */
    public void restoreMarkers() {
        if(startMarker != null) {
        	startMarker.setVisible(false);
        }
        if(destinationMarker != null) {
        	destinationMarker.setVisible(false);
        }
        for(geography.GeographicPoint point : markerPositions) {
        	MarkerOptions markerOptions = createDefaultOptions(point);
        	Marker marker = new Marker(markerOptions);
            registerEvents(marker, point);
        	map.addMarker(marker);
        	putMarker(point, marker);
        }
        selectManager.resetSelect();
        selectMode = true;
    }
    public MarkerOptions createDefaultOptions(geography.GeographicPoint point) {
        	MarkerOptions markerOptions = new MarkerOptions();
        	LatLong coord = new LatLong(point.getX(), point.getY());
        	bounds.extend(coord);
        	markerOptions.animation(Animation.DROP)
        				 .icon(markerURL)
        				 .position(coord)
                         .title(null)
                         .visible(true);
        	return markerOptions;
    }

    public void hideIntermediateMarkers() {
        Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
            Marker marker = markerMap.get(it.next());
            if(marker != startMarker && marker != destinationMarker) {
                marker.setVisible(false);
            }
//        	map.addMarker(marker);
        }
    }

    public void displayMarker(geography.GeographicPoint point) {
    	if(markerMap.containsKey(point)) {
        	Marker marker = markerMap.get(point);
            marker.setVisible(true);
            System.out.println("Marker : " + marker + "set to visible");
    	}
    	else {
    		System.out.println("no key found for MarkerManager::displayMarker");
    	}
    }
    public void displayDataSet() {
        markerPositions = new ArrayList<geography.GeographicPoint>();
        dataSet.initializeGraph();
    	Iterator<geography.GeographicPoint>it = dataSet.getGraph().getVertices().iterator();
        bounds = new LatLongBounds();
        while(it.hasNext()) {
        	geography.GeographicPoint point = it.next();
        	MarkerOptions markerOptions = createDefaultOptions(point);
        	Marker marker = new Marker(markerOptions);
            registerEvents(marker, point);
        	map.addMarker(marker);
        	putMarker(point, marker);
        	markerPositions.add(point);
        }
        map.fitBounds(bounds);
        System.out.println("End of display Intersections");

    }

    public Marker createMarker(geography.GeographicPoint point) {
    	return null;
    }

    private void registerEvents(Marker marker, geography.GeographicPoint point) {
        /*map.addUIEventHandler(marker, UIEventType.mouseover, (JSObject o) -> {
           marker.setVisible(true);
           //marker.setAnimation(Animation.BOUNCE);
        });

        map.addUIEventHandler(marker, UIEventType.mouseout, (JSObject o) -> {
        	marker.setAnimation(null);
        });*/

        map.addUIEventHandler(marker, UIEventType.click, (JSObject o) -> {
            //System.out.println("Clicked Marker : " + point.toString());
            if(selectMode) {
            	if(selectedMarker != null && selectedMarker != startMarker
            	   && selectedMarker != destinationMarker) {
            		selectedMarker.setIcon(markerURL);
            	}
            	selectManager.setPoint(point, marker);
                selectedMarker = marker;
                selectedMarker.setIcon(SELECTED_URL);
            }
        });
    }

    public void disableVisButton(boolean value) {
    	if(vButton != null) {
	    	vButton.setDisable(value);
    	}
    }
	public void setDataSet(DataSet dataSet) {
		this.dataSet= dataSet;
	}


    public DataSet getDataSet() { return this.dataSet; }
}
