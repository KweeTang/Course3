package application.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.MapApp;
import application.services.GeneralService;
import application.services.RouteService;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import gmapsfx.javascript.object.MVCArray;
import gmapsfx.shapes.Polyline;
import javafx.scene.control.Button;

public class RouteController {
	private GoogleMap map;
	private MapApp mainApp;
    private GeneralService generalService;
    private RouteService routeService;
    private Button displayButton;
    private String filename = "myroute.route";

	// display one route at a time
	private Polyline routeLine;

	public RouteController(RouteService routeService, MapApp mainApp, Button displayButton) {
        this.routeService = routeService;
		this.mainApp = mainApp;
		this.displayButton = displayButton;

        setupDisplayButton();
        //routeService.displayRoute("data/sampleroute.map");
	}


	private void setupDisplayButton() {
		displayButton.setOnAction(e -> {
			routeService.displayRoute(filename);
		});
	}







}
