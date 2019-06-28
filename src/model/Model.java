package model;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Model {
	
	private static Model instance;
	final static String Table1 = "VEHICLES";
	final static String Table2 = "RENTAL_RECORDS";

	private ObservableList<AbstractVehicle> vehicles =FXCollections.observableArrayList();
	
	public void test() {
		
		ConnectionSetup.start();
		ConnectionSetup.checkTable(Table1);
		ConnectionSetup.checkTable(Table2);
		
		//uncomment to refresh clear the database at the start.
		//ConnectionSetup.deleteTable(Table1);
		//ConnectionSetup.deleteTable(Table2);
		
		ConnectionSetup.createTableV();
		System.out.println();
		ConnectionSetup.createTableRental();
		System.out.println();
		
		//Enters pre-existing vehicles into database from the "22sampleimport.txt" saved in this project.
		try {
			ConnectionSetup.addtoDB();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//View snapshot of current DB entries.
		ConnectionSetup.Query();
		ConnectionSetup.Query2();

	}

	private Model() {
	}
	
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		instance.test();
		
		return instance;
	}
	
	public static Model getCurrentInstance() {
		return instance;
	}
	
	public ObservableList<AbstractVehicle> getValues() {
		return this.vehicles;
	}
	
}
