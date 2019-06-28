package view;
import controller.*;
import model.*;
import exceptions.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class PerformMtWindow {

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	private AbstractVehicle vehicle;
	final private String pattern = "dd/MM/yyyy"; 
	
	public PerformMtWindow(AbstractVehicle v) {
		vehicle = v;
		dialogBox.setTitle("Maintenance");
		
		//date handling
		Label Datelabel = new Label("Enter date of maintenance:");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Locale.setDefault(Locale.ENGLISH);
		DatePicker datePicker = new DatePicker();
			
		//messagebox
		Label messagebox = new Label();
			
		//ok-cancel
		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
		
			
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().addAll(Datelabel, datePicker, messagebox, dialogButtons);
		dialogVBox.setPadding(new Insets(10, 10, 10, 10));
		
			dialogOKButton.setOnAction(
					(e) -> {
						String ID = vehicle.getId();
						String maintDate = datePicker.getValue().format(formatter);
						System.out.println(ID);

						messagebox.setText(maintDate + "\n" + v.performMaintenance());
						messagebox.setWrapText(true);
						dialogOKButton.setDisable(true);
						dialogCancelButton.setDisable(true);
						
						//After changing vehicle status, updates system and refreshes the database
						ConnectionSetup.UpdateVehicle(v.getId(), "Status", v.getStatus().toString());
						ConnectionSetup.refreshVehicles();
						ConnectionSetup.refreshRecords();
					}
				);
			
		dialogCancelButton.setOnAction(new DialogCancelController(dialogBox));
	
		Scene dialogScene = new Scene(dialogVBox, 200, 200);
		dialogBox.setScene(dialogScene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
