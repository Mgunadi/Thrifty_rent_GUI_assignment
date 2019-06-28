package view;
import model.*;
import controller.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
//import javax.security.auth.callback.Callback;
import controller.MenuItemListener;
import javafx.application.Application;
import javafx.stage.Stage;
// beans - for property management
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// Events
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// Controls
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
// Layouts
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
// Text
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
// Scenes
import javafx.scene.Scene;
//Observable List
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//image
import javafx.scene.image.*;

public class MainApp extends Application{

	 //private final Image HONDA_CIVIC  = new Image("Thrifty_GUI/images/CivicGood.png");
	 //private final Image COMMO_UTE  = new Image("Thrifty_GUI/images/commo-ute.jpg");
	 //private final Image TOYOTA_CAMRY  = new Image("Thrifty_GUI/images/toyota-camry.jpg");
	 //private final Image TOYOTA_VIOS = new Image("Thrifty_GUI/images/Toyota-vios.jpg");

	//Access the current instance of the rental system.
	private Model model = Model.getInstance();
	public Model getModel() {
		return model;
	}
	
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage)  throws FileNotFoundException {
		System.out.println(model.getValues().toString());
		
		Image HONDA = new Image(new FileInputStream("images/CivicGood.png"));
		ImageView imageView = new ImageView(HONDA);
		
		//Menubar
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		
		MenuItem importMenuItem = new MenuItem("Import");
		importMenuItem.setOnAction(new MenuItemListener(importMenuItem, primaryStage));
		
		MenuItem exportMenuItem = new MenuItem("Export");
		exportMenuItem.setOnAction(new MenuItemListener(exportMenuItem, primaryStage));
		
		fileMenu.getItems().addAll(importMenuItem, exportMenuItem);
		menuBar.getMenus().add(fileMenu);		
		
		
		VBox vBox = new VBox(); 
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		
		//Buttons
		Button quitButton = new Button("Quit");
		HBox hBox = new HBox(); // Hold two buttons in an vBox
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(quitButton);

		//Header and list sorter
		Label label = new Label("Thrifty Fleet");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		Button Sort0 = new Button("Unsorted list");
		Button Sort1 = new Button("Sort by Type");
		Button Sort2 = new Button("Sort by seats");
		Button Sort3 = new Button("Sort by make");
		
		HBox SortButtons = new HBox();
		SortButtons.setAlignment(Pos.CENTER);

		//SortButtons.getChildren().add(label);
		SortButtons.getChildren().add(Sort0);
		SortButtons.getChildren().add(Sort1);
		SortButtons.getChildren().add(Sort2);
		SortButtons.getChildren().add(Sort3);
		
		vBox.getChildren().add(label);
		vBox.getChildren().add(SortButtons);
	
		TextField newValueToAdd = new TextField();
		vBox.getChildren().add(newValueToAdd);

		//Create the listview of the cars in system.
		ListView<AbstractVehicle> listView = new ListView<AbstractVehicle>();
		listView.setItems(model.getValues());
		listView.setCellFactory(param -> new Cell3());
		vBox.getChildren().add(listView);                      
		vBox.getChildren().add(hBox);
	
		quitButton.setOnAction(
			(e) -> {
					System.out.println("Goodbye!");
					System.exit(0);
				}
		);
		
		//Create the Borderpane to contain all nodes then set stage and scene.
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setBottom(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		Scene scene = new Scene(borderPane, 400, 600);
		primaryStage.setTitle("Thrifty Rental System"); 
		primaryStage.setScene(scene); 
		
		// Display the stage
		primaryStage.show(); 
	}
	
	
	//Customised Cell contents with brief details about the vehicles.
	static class Cell3 extends ListCell<AbstractVehicle> {
		
		HBox hbox1 = new HBox();
		Button btn = new Button("Get Details");
		Label label = new Label("");
		Pane pane = new Pane();
		
		Image profile = new Image("https://upload.wikimedia.org/wikipedia/commons/f/f1/Ruby_logo_64x64.png");
		ImageView img = new ImageView(profile);

		Detail detailWindow;			// this contains the vehicle
		
		public Cell3() {
			super();
			hbox1.getChildren().addAll(img,label,pane,btn);
			hbox1.setHgrow(pane, Priority.ALWAYS);
			
		}
		
		//updates the cells as data on vehicles are changed in the system and database.
		@Override
		public void updateItem(AbstractVehicle v, boolean empty) {
			super.updateItem(v, empty);;
			setText(null);
			setGraphic(null);
			detailWindow = new Detail(btn, v);
			if(v !=null && !empty) {
				String name = v.getId();
				String make =v.getMake();
				String model = v.getModel();
				int seats = v.NumOfSeats();
				int year = v.getYear();
				Status status = v.getStatus();
				label.setText(name + "\n" + make + " " + model + " " + year + "\n" + "Number of seats: " + seats + "\n" + status);
				setGraphic(hbox1);
			}
		}
			
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
