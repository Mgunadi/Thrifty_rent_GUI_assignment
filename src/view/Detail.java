package view;
import model.*;
import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.image.*;

public class Detail {
	private Stage dialogDetail = new Stage();
	private AbstractVehicle a;
	
	public Detail(Button btn, AbstractVehicle a) {
		this.a =a;
		dialogDetail.setTitle("Detail of vehicle");
		
		//Menubar items - Top of borderpane
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem importMenuItem = new MenuItem("Import");
		importMenuItem.setOnAction(new MenuItemListener(importMenuItem, dialogDetail));
		MenuItem exportMenuItem = new MenuItem("Export");
		exportMenuItem.setOnAction(new MenuItemListener(exportMenuItem,  dialogDetail));
		fileMenu.getItems().addAll(importMenuItem, exportMenuItem);
		menuBar.getMenus().add(fileMenu);		
		
		
		//Header and scrollable list with details of car and records - Middle of borderpane
		String strlabel = "";
		if (a instanceof Car) {
			strlabel = String.format("VEHICLE DETAILS: \n %s", ((Car)a).getDetail());
		} else if (a instanceof Van) {
			strlabel = String.format("VEHICLE DETAILS: \n %s", ((Van)a).getDetail());
		}
		Label label = new Label(strlabel);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(label);

		//images
		final ImageView selectedImage = new ImageView(); 
		Image image1 = new Image("https://upload.wikimedia.org/wikipedia/commons/f/f1/Ruby_logo_64x64.png");
		selectedImage.setImage(image1);
		
		//Buttons
		Button act0 = new Button("Rent Vehicle");
		Button act1 = new Button("Return Vehicle");
		Button act2 = new Button("Perform maintenance");
		Button act3 = new Button("Complete Maintenance");
		VBox buttons = new VBox();
		buttons.getChildren().addAll(act0, act1, act2, act3);
		VBox buttonBar = new VBox();
		buttonBar.getChildren().add(buttons);

		//Ok-cancel
		Button dialogOKButton = new Button("OK");
		Button dialogCancelButton = new Button("Cancel");
		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().add(dialogButtons);
		
		//Layout of window
		Insets insets = new Insets(10);
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(15, 12, 15, 12));
		borderPane.setTop(menuBar);
		borderPane.setLeft(selectedImage);
		borderPane.setCenter(scrollPane);
		borderPane.setRight(buttonBar);
		borderPane.setBottom(dialogVBox);
		BorderPane.setAlignment(dialogVBox, Pos.CENTER);
		

		BorderPane.setMargin(menuBar, insets);
		BorderPane.setMargin(selectedImage, insets);
		BorderPane.setMargin(scrollPane, insets);
		BorderPane.setMargin(buttonBar, insets);
		BorderPane.setMargin(dialogVBox, insets);
		
		//getDetails button
		Scene dialogScene = new Scene(borderPane, 550, 400);
		dialogDetail.setScene(dialogScene);
		btn.setOnAction(
				(e) -> {
					dialogDetail.show();
				}
			);
		
		dialogOKButton.setOnAction(new DialogOKController(dialogDetail));
		dialogCancelButton.setOnAction(new DialogCancelController(dialogDetail));
		
		//links to the button listeners.
		act0.setOnAction(new RentController<Car>(dialogDetail, a));
		act1.setOnAction(new ReturnController(dialogDetail, a));
		act2.setOnAction(new PerformMaintController(dialogDetail, a));
		act3.setOnAction(new CompleteMaintController(dialogDetail, a));
		
		}

	
	public Stage getDetail() {
		return dialogDetail;
	}
	
	public AbstractVehicle getVehicle() {
		return a;
	}
}
	
