package application.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

import com.sun.javafx.geom.Rectangle;

import application.DataSet;
import application.MapApp;
import application.services.GeneralService;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import mapmaker.MapMaker;

public class FetchController {
    private static final int ROW_COUNT = 5;
    GeneralService generalService;
    private Node container;
    private Button fetchButton;
    private Button displayButton;
    private ComboBox<DataSet> dataChoices;
    // maybe choice map
    private TextField writeFile;
    private String filename = "data.map";

    // path for mapfiles to load when program starts
    private String persistPath = "data/mapfiles/mapfiles.list";

	// SHOULD I USE THIS????!!
    // LOOK AT TEXT EDIROT APP
	// TODO CENTER ON ROUTE, GET BOUND OF AREA, THINK OF UI

    // do i need reference to App?
    public FetchController(GeneralService generalService, TextField writeFile, Button fetchButton, ComboBox<DataSet> cb, Button displayButton) {
        this.generalService = generalService;
        this.fetchButton = fetchButton;
        this.displayButton = displayButton;
        this.writeFile = writeFile;
        dataChoices = cb;
        setupComboCells();
        setupFetchButton();
        setupDisplayButton();
        loadDataSets();
        for (DataSet d : dataChoices.getItems()) {
        	System.out.println(d);
        }

    }

    private void loadDataSets() {
    	try {
    		BufferedReader reader = null;
        	reader = new BufferedReader(new FileReader(persistPath));
            String line = reader.readLine();
            while(line != null) {
            	System.out.println("Getting " + line);;
            	dataChoices.getItems().add(new DataSet(GeneralService.getDataSetDirectory() + line));
                line = reader.readLine();
            }
            reader.close();
		} catch (IOException e) {
            e.printStackTrace();

		}
    }
    private void setupComboCells() {

    	//dataChoices.setVisibleRowCount(ROW_COUNT);
    	dataChoices.setCellFactory(new Callback<ListView<DataSet>, ListCell<DataSet>>() {
        	@Override public ListCell<DataSet> call(ListView<DataSet> p) {
        		System.out.println("Calling call with " + p);
        		return new ListCell<DataSet>() {
        			{
                        super.setPrefWidth(100);
                        //getItem().getFileName());

        			}

                    @Override
                    protected void updateItem(DataSet item, boolean empty) {
                    	System.out.println("Updating item: " + item);
                    	super.updateItem(item, empty);
                    	if(empty || item == null) {
                    		System.out.println("Setting text to null");
                            super.setText(null);
                    	}
                    	else {
                    		System.out.println("Setting text to " + item.getFilePath().substring(GeneralService.getDataSetDirectory().length()));

                    		super.setText(item.getFilePath().substring(GeneralService.getDataSetDirectory().length()));
                        	
                    	}
                    	
                    }
        		};

        	}
    	});

        dataChoices.setButtonCell(new ListCell<DataSet>() {
        	@Override
        	protected void updateItem(DataSet t, boolean bln) {
        		System.out.println("Calling setButtonCell");
        		super.updateItem(t,  bln);
        		if(t!=null) {
        			System.out.println("IN SBC Setting text to: " + t.getFilePath().substring(GeneralService.getDataSetDirectory().length()));
        			setText(t.getFilePath().substring(GeneralService.getDataSetDirectory().length()));
        		}
        		else {
        			System.out.println("IN SBC Setting text to: Choose..." );

        			setText("Choose...");
        		}
        	}
        });
    }

    private void setupFetchButton() {
    	fetchButton.setOnAction(e -> {
    		String fName = writeFile.getText();
    		if((fName = generalService.checkDataFileName(fName)) != null) {
                System.out.println("file name is good");
    			generalService.runFetchTask(fName, dataChoices, fetchButton);

    		}
    		else {
    		    Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("File Name Error");
    			alert.setHeaderText("Input Error");
    			alert.setContentText("Check filename input, \n Only characters in class" + GeneralService.getFileRegex()
    								 +"are allowed."
    					             + "\n\nFilename must fit one of the following formats : \n"
    								 + "[name].map or datasets/[name].map");

    			alert.showAndWait();
    		}
    	});
    }

    private void setupDisplayButton() {
    	displayButton.setOnAction( e -> {
            System.out.println("In setup display button");
            DataSet dataSet = dataChoices.getValue();

            // was any dataset selected?
            if(dataSet == null) {
    		    Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("Display Error");
    			alert.setHeaderText("Invalid Action :" );
    			alert.setContentText("No map file has been selected for display.");
    			alert.showAndWait();
            }
            else if(!dataSet.isDisplayed()) {
        		generalService.displayIntersections(dataSet);

            }
            else {
    		    Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Display Info");
    			alert.setHeaderText("Intersections Already Displayed" );
    			alert.setContentText("Data set : " + dataSet.getFilePath() + " has already been loaded.");
    			alert.showAndWait();
            }

            // TO TEST : only using test.map for intersections
        	//generalService.displayIntersections(new DataSet("my.map"));
    	});
    }




}
