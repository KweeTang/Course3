package application.controllers;

import application.MapApp;
import gmapsfx.javascript.event.UIEventType;
import gmapsfx.javascript.object.GoogleMap;
import javafx.application.Application;
import netscape.javascript.JSObject;

public class ClientMapController {
	// maybe less references to map
	private GoogleMap map;
	private MapApp app;

	// do i need application??
    public ClientMapController(GoogleMap map, MapApp app) {
    	this.map = map;
    	this.app = app;

    	map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
    		System.out.println(obj.getMember("latLng"));
    	});
    }

}
