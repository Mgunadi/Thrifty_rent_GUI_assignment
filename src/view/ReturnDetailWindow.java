package view;

import model.*;
import exceptions.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import controller.DialogCancelController;
import controller.DialogOKController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReturnDetailWindow {

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	private DateTime returnDate_;
	private String returnDate;
	String message = "";
	final private String pattern = "dd/MM/yyyy"; 
	
	
	public ReturnDetailWindow(AbstractVehicle v) {
		dialogBox.setTitle("Return details registration");
		
		//Date Handling
		Label Datelabel = new Label("What is the return date?");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Locale.setDefault(Locale.ENGLISH);
		DatePicker datePicker = new DatePicker();
	    HBox datebox = new HBox(datePicker);
	    Label messagebox = new Label();
	    
	    //OK-Cancel
	    HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
		
		//VBx - main pane.
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().addAll(Datelabel, datebox, messagebox, dialogButtons);
		dialogVBox.setPadding(new Insets(10, 10, 10, 10));
	
		//Listener
		dialogCancelButton.setOnAction(new DialogCancelController(dialogBox));
		dialogOKButton.setOnAction(
				(e) -> {
					//handles empty datepicker.
				    try {
				    	returnDate = datePicker.getValue().format(formatter);
				    } catch(NullPointerException n) {
				    	try {
							throw new ReturnException("No value in datefield. Please select a date.");
						} catch (ReturnException e1) {
							e1.printStackTrace();
						}
				    }			
					
					if (datePicker.getValue() == null) {
						message = "Please select a date.";
						messagebox.setText(message);
						messagebox.setWrapText(true);

					} else {
	
						returnDate = datePicker.getValue().format(formatter);
						int day = Integer.parseInt(returnDate.substring(0,2));
						int month = Integer.parseInt(returnDate.substring(3,5));
						int yr = Integer.parseInt(returnDate.substring(6,10));
						returnDate_ = new DateTime(day, month, yr);
						
						//Polymorphically returns the vehicle based on the user input after error handling. 
						for (AbstractVehicle a : Model.getCurrentInstance().getValues()) {
							if (v.getId().equals(a.getId())) {
								if (v instanceof Car) {
									message = ((Car)v).returnVehicle(returnDate_) + datePicker.getValue().format(formatter);
									ConnectionSetup.UpdateVehicle(v.getId(), "Status", v.getStatus().toString());
									ConnectionSetup.UpdateRental(v.getCurrentRecord().getId(), "actualReturnDate", v.getCurrentRecord().getActualReturnDate().getEightDigitDate());
								} else if (v instanceof Van) {
									message = ((Van)v).returnVehicle(returnDate_) + datePicker.getValue().format(formatter);
									ConnectionSetup.UpdateVehicle(v.getId(), "Status", v.getStatus().toString());
									ConnectionSetup.UpdateRental(v.getCurrentRecord().getId(), "actualReturnDate", v.getCurrentRecord().getActualReturnDate().getEightDigitDate());
								}
								
								//Messagebox will display.
								messagebox.setText(message);
								messagebox.setWrapText(true);
								dialogOKButton.setDisable(true);
								dialogCancelButton.setDisable(true);
							}
						}
					}
				});
	
		Scene dialogScene = new Scene(dialogVBox, 200, 200);
		dialogBox.setScene(dialogScene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
