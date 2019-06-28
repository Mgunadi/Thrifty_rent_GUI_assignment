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


public class PerformMaintController implements EventHandler<ActionEvent>{
	private Stage dialogBox;
	private boolean valid = false;
	private AbstractVehicle a;
	
	public PerformMaintController(Stage dialogBox, AbstractVehicle a) {
		this.dialogBox = dialogBox;
		this.a = a;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		
		
		PerformMtWindow w = new PerformMtWindow(a);
		w.getDialogBox().show();
		
	}	
	

}