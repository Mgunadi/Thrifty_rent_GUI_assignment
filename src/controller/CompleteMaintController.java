package controller;
import model.*;
import view.*;
import java.util.InputMismatchException;

import exceptions.ErrorWindow;
import exceptions.MaintenanceException;
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


public class CompleteMaintController implements EventHandler<ActionEvent>{
	private AbstractVehicle a;
	
	public CompleteMaintController(Stage dialogBox, AbstractVehicle a) {
		this.a = a;
	}

	@Override
	public void handle(ActionEvent arg0) {
		
		try {
			a.completeMaintenance(a);
			CompleteMtWindow w = new CompleteMtWindow(a);
			w.getDialogBox().show();
		} catch (MaintenanceException e) {
			ErrorWindow error = new ErrorWindow(e.getErrorMsg());
			error.getDialogBox().show();
		}
	}	
}