package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import application.controllers.ClientMapController;
import application.controllers.RouteController;
import gmapsfx.GoogleMapView;
import gmapsfx.MapComponentInitializedListener;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.MapOptions;
import gmapsfx.javascript.object.MapTypeIdEnum;

public class MapApp extends Application
					implements MapComponentInitializedListener {

	protected GoogleMapView mapComponent;
	protected GoogleMap map;


	@Override
	public void start(Stage primaryStage) throws Exception {

		mapComponent = new GoogleMapView();
		mapComponent.addMapInitializedListener(this);

		// border pane for main layout
		BorderPane bp = new BorderPane();

		// HBox for top panel with options
		HBox rightPanel = new HBox();
		VBox panelV = new VBox();
		TabPane tp = new TabPane(new Tab("Routing"), new Tab("Data Fetching"));
		Button button = new Button("Display Route");
//		rightPanel.getChildren().add(panelV);
//		panelV.getChildren().add(button);

		// add components to border pane

		bp.setRight(tp);
		bp.setCenter(mapComponent);

		Scene scene = new Scene(bp);
		primaryStage.setScene(scene);
		primaryStage.show();

	}


	@Override
	public void mapInitialized() {
		// TODO Auto-generated djdlkjmet`hod stub
		LatLong center = new LatLong(34.0219, -118.4814);
        mapComponent.addMapReadyListener(() -> {

        });

		// set map options
		MapOptions options = new MapOptions();
		options.center(center)
		       .mapMarker(false)
		       .mapType(MapTypeIdEnum.ROADMAP)
		       //maybe set false
		       .mapTypeControl(true)
		       .overviewMapControl(false)
		       .panControl(true)
		       .rotateControl(false)
		       .scaleControl(false)
		       .streetViewControl(false)
		       .zoom(8)
		       .zoomControl(true);

		// create map;
		map = mapComponent.createMap(options);
		RouteController routeController = new RouteController(map, this);
        ClientMapController userController = new ClientMapController(map, this);



	}

}
