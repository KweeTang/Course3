package application.controllers;

import java.io.FileNotFoundException;

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
    }

    private void setupComboCells() {
    	//dataChoices.setVisibleRowCount(ROW_COUNT);
    	dataChoices.setCellFactory(new Callback<ListView<DataSet>, ListCell<DataSet>>() {
        	@Override public ListCell<DataSet> call(ListView<DataSet> p) {
        		return new ListCell<DataSet>() {
        			{
                        super.setPrefWidth(100);
                        //getItem().getFileName());

        			}

                    @Override
                    protected void updateItem(DataSet item, boolean empty) {
                        super.updateItem(item, empty);
                    	if(empty || item == null) {
                            super.setText(null);
                    	}
                    	else {
                        	super.setText(item.getFileName());

                    	}
                    }
        		};

        	}
    	});

        dataChoices.setButtonCell(new ListCell<DataSet>() {
        	@Override
        	protected void updateItem(DataSet t, boolean bln) {
        		super.updateItem(t,  bln);
        		if(t!=null) {
        			setText(t.getFileName());
        		}
        		else {
        			setText("Choose...");
        		}
        	}
        });
    }

    private void setupFetchButton() {
    	fetchButton.setOnAction(e -> {
    		String fName = writeFile.getText();
    		if(generalService.goodDataFileName(fName)) {
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
    		    Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("ZZZ");
    			alert.setHeaderText("IZZZZ" );
    			alert.setContentText("ZZZZZ");
    			alert.showAndWait();
            }
            else if(!dataSet.isDisplayed()) {
        		generalService.displayIntersections(dataSet.getFileName());
//        		generalService.displayIntersections("test.");
            }
            else {
    		    Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Display Info");
    			alert.setHeaderText("Intersections Already Displayed" );
    			alert.setContentText("Data set : " + dataSet.getFileName() + " has already been loaded.");
    			alert.showAndWait();
            }
    	});
    }




}
