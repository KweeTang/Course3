package application.controllers;

import application.MapApp;
import application.services.ClientService;
import application.services.GeneralService;
import gmapsfx.GoogleMapView;
import gmapsfx.javascript.event.UIEventType;
import gmapsfx.javascript.object.GoogleMap;
import javafx.application.Application;
import netscape.javascript.JSObject;

public class ClientMapController {
	// maybe less references to map
	// do i need to use this?
	private ClientService clientService;
	private GeneralService generalService;
	private MapApp app;

	// do i need application??
    public ClientMapController(GeneralService generalService, MapApp app) {
        this.generalService = generalService;
    	this.app = app;

    }



}
