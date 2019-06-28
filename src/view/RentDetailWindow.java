package view;
import controller.*;
import model.*;
import exceptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RentDetailWindow{

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	private AbstractVehicle vehicle;
	private String rentDate;
	private DateTime rentDate_;
	private int rentalDays;
	private boolean numeric = true;
	String message = "";
	final private String pattern = "dd/MM/yyyy"; 
	
	public RentDetailWindow(AbstractVehicle v) {
		vehicle = v;
		dialogBox.setTitle("Rental details registration");
		
		//rental date
		Label Datelabel = new Label("What is the rental date?");
		DatePicker datePicker = new DatePicker();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Locale.setDefault(Locale.ENGLISH);

	    HBox datebox = new HBox(datePicker);
		
	    //Customer
	    Label Customerlabel = new Label("Please enter a Customer Id:");
		TextField CustomerField = new TextField();
		CustomerField.setPromptText("Name");
		CustomerField.setId("CustomerId");
	    
	    
	    //rental days
		Label Dayslabel = new Label("How many days?");
		TextField DaysField = new TextField("0");
		try{
			int num = Integer.parseInt(DaysField.getText());
			numeric = true;
		} catch(NumberFormatException error) {
			numeric = false;
			throw new RentException("You must select a valid number of days.");
		}
			
		DaysField.setPromptText("Days");
		DaysField.setId("Days");
		
		//dialog boxes
		HBox dialogButtons = new HBox();
		Label messagebox = new Label("");
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
	
		
		// layout the dialog components
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().addAll(Customerlabel, CustomerField, Datelabel, datebox, Dayslabel, DaysField, messagebox, dialogButtons);
		dialogVBox.setPadding(new Insets(10, 10, 10, 10));
		
		dialogCancelButton.setOnAction(new DialogCancelController(dialogBox));
		dialogOKButton.setOnAction(
				(e) -> {
					try{
						int num = Integer.parseInt(DaysField.getText());
						numeric = true;
					} catch(NumberFormatException error) {
						numeric = false;
					}

					if ((numeric == false) || (DaysField.getText() == "0")) {
						message = "Please select a date and enter number of days to rent (must be at least 1 day).";
						messagebox.setText(message);
						messagebox.setWrapText(true);
					} else {
						rentalDays = Integer.parseInt(DaysField.getText());
						rentDate = datePicker.getValue().format(formatter);
						int day = Integer.parseInt(rentDate.substring(0,2));
						int month = Integer.parseInt(rentDate.substring(3,5));
						int yr = Integer.parseInt(rentDate.substring(6,10));
						rentDate_ = new DateTime(day, month, yr);
						
						//rents the vehicle if user input is valid and refreshes the database.
						if (Model.getCurrentInstance().getValues().contains(v)) {
							messagebox.setText(v.rent(CustomerField.getText(), rentDate_, rentalDays));
							messagebox.setWrapText(true);
							dialogOKButton.setDisable(true);
							dialogCancelButton.setDisable(true);
							System.out.println(v.getStatus());
							ConnectionSetup.UpdateVehicle(v.getId(), "Status", v.getStatus().toString());
							ConnectionSetup.refreshVehicles();
							ConnectionSetup.refreshRecords();
						}
					}
					
				}
			);
		
		Scene dialogScene = new Scene(dialogVBox, 200, 300);
		dialogBox.setScene(dialogScene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
