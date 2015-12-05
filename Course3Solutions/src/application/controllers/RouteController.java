package application.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.MapApp;
import application.CLabel;
import application.services.GeneralService;
import application.services.RouteService;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import gmapsfx.javascript.object.MVCArray;
import gmapsfx.shapes.Polyline;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;

public class RouteController {
	// Strings for slider labels
	private static final String DISABLE_STR = "Disable";
	private static final String START_STR = "Select Start";
	private static final String DEST_STR = "Select Destination";
	public static final int DISABLE = 0;
	public static final int START = 1;
	public static final int DESTINATION = 2;

    private int currentState = 0;

	private GoogleMap map;
    private GeneralService generalService;
    private RouteService routeService;
    private Button displayButton;
    private String filename = "myroute.route";
    private CLabel<geography.GeographicPoint> startLabel;
    private CLabel<geography.GeographicPoint> endLabel;
    private Slider optionsSlider;




	// display one route at a time
	private Polyline routeLine;

	public RouteController(RouteService routeService, Button displayButton,
			CLabel<geography.GeographicPoint> startLabel, CLabel<geography.GeographicPoint> endLabel) {
        this.routeService = routeService;
		this.displayButton = displayButton;
		this.startLabel = startLabel;
		this.endLabel = endLabel;

        setupDisplayButton();
        setupLabels();
        //routeService.displayRoute("data/sampleroute.map");
	}


	private void setupDisplayButton() {
		displayButton.setOnAction(e -> {
            if(startLabel.getItem() != null && endLabel.getItem() != null) {
    			routeService.displayRoute(startLabel.getItem(), endLabel.getItem());
            }
            else {
            	MapApp.showErrorAlert("Route Display Error", "Make sure to choose points for both start and destination.");
            }
		});
	}


    private void setupLabels() {

    }




}
