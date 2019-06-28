package view;
import controller.*;
import exceptions.ErrorWindow;
import exceptions.MaintenanceException;
import model.*;

import java.util.InputMismatchException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CompleteMtWindow {

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	private AbstractVehicle vehicle;
	
	
	public CompleteMtWindow(AbstractVehicle v) {
		vehicle = v;
		dialogBox.setTitle("Maintenance");
		
		BorderPane BorderPane = new BorderPane(); 
		BorderPane.setPadding(new Insets(5, 5, 5, 5));
			
		String message ="The vehicle has now completed Maintenance. It is now available to rent.";

		System.out.println(v.getStatus().toString());
		ConnectionSetup.UpdateVehicle(v.getId(), "Status", v.getStatus().toString());
		ConnectionSetup.refreshVehicles();
		ConnectionSetup.refreshRecords();
			
			
		Text text1 = new Text(20, 20, message);
		text1.setFont(Font.font("Courier", FontWeight.BOLD, 15));

		//Ok
		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
	
		BorderPane.setTop(text1);
		BorderPane.setBottom(dialogButtons);
			
			
		dialogOKButton.setOnAction(new DialogOKController(dialogBox));
			
		Scene scene = new Scene(BorderPane);
		dialogBox.setScene(scene);

	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
