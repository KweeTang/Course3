package application.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gmapsfx.GoogleMapView;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import gmapsfx.javascript.object.MVCArray;
import gmapsfx.shapes.Polyline;

public class RouteService {
	private GoogleMap map;
	private GoogleMapView mapComponent;
    private Polyline routeLine;

	public RouteService(GoogleMapView mapComponent) {
		this.mapComponent = mapComponent;
		this.map = mapComponent.getMap();

	}
    // COULD SEPARATE INTO ROUTE SERVICES IF CONTROLLER
	// GETS BIG
	/**
	 *
	 * @param filename - path of route text file
	 * @return MVCArray of LatLongs (in order, 0 is start) representing the route on the map.
	 */
	private List<LatLong> parseRouteFromFile(String filename) {
		// string for reading line of file
		String line;
		String[] lineSplit;
		BufferedReader br = null;

		List<LatLong> route = new ArrayList<LatLong>();

		// open file with route data
		try {
			br = new BufferedReader(new FileReader(filename));

			// get first line of file then loop until EOF
			line = br.readLine();
			while(line != null) {
				// split String into latitude [0] and longitude [1]s
				lineSplit = line.split(",");

				// make sure line is of length 2 (lat and lon)
				// TODO MAYBE ADD MORE ROBUST CHECKS!!!!!
				if(lineSplit.length == 2) {
					route.add(new LatLong(Double.parseDouble(lineSplit[0]), Double.parseDouble(lineSplit[1])));
				}
				else {
					System.err.println("Incorrect Latitude, Longitude line in route file : \n" + line + "\n...skipping line");
				}

				// get new line
				line = br.readLine();
			}
		}
		catch (IOException e ) {
			// DEBUG
			e.printStackTrace();
		}




		return route;
	}
	// initialize??

	// add route polyline to map
	//DISPLAY ROUTE METHODS
	/**
	 * Displays route on Google Map
	 * @return returns false if route fails to display
	 */
	private boolean displayRoute(List<LatLong> route) {
		routeLine = new Polyline();
		MVCArray path = new MVCArray();
		LatLongBounds bounds = new LatLongBounds();
		for(LatLong point : route)  {
			path.push(point);
            bounds = bounds.extend(point);
		}
		routeLine.setPath(path);
		map.addMapShape(routeLine);

		System.out.println(bounds.getNorthEast());
		//EXCEPTION getBounds() messed up??
        //System.out.println(routeLine.getBounds());


		map.fitBounds(bounds);
		return true;
	}

	public boolean displayRoute(String filename) {
		List<LatLong> path = parseRouteFromFile(filename);
		return displayRoute(path);

	}

	private void hideRoute() {
		// hide routeLine
	}
}
