package application.services;

import gmapsfx.javascript.object.GoogleMap;

public class MapService {
	private GoogleMap map;

	public MapService(GoogleMap map) {
		this.map = map;
	}

    public static MapService getInstance(GoogleMap map) {
    	return new MapService(map);
    }

    public void displayIntersections(String filename) {

    }
}
