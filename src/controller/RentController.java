package controller;
import model.*;
import view.*;
import exceptions.*;
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



public class RentController<T extends AbstractVehicle> implements EventHandler<ActionEvent>{
	private Stage dialogBox;
	private boolean valid = false;
	private AbstractVehicle a;

	
	public RentController(Stage dialogBox, AbstractVehicle a) {
		this.dialogBox = dialogBox;
		this.a = a;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		try {
			RentDetailWindow w = new RentDetailWindow(a);
			w.getDialogBox().show();
		} catch(RentException e) {
			ErrorWindow e1 = new ErrorWindow(e.getDesc());
			e1.getDialogBox().show();
		}
	}	

}