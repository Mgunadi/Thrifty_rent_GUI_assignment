package controller;
import model.*;
import view.*;
import java.util.InputMismatchException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AbstractVehicle;
import view.RentDetailWindow;


public class ReturnController implements EventHandler<ActionEvent>{
	private Stage dialogBox;
	private boolean valid = false;
	private AbstractVehicle a;
	
	public ReturnController(Stage dialogBox, AbstractVehicle a) {
		this.dialogBox = dialogBox;
		this.a = a;
	}

	@Override
	public void handle(ActionEvent arg0) {
		ReturnDetailWindow w = new ReturnDetailWindow(a);
		w.getDialogBox().show();
		System.out.println("Open up ReturnDetail Window prompt");
		
	}	

}