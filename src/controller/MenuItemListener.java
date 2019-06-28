package controller;

import model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MenuItemListener implements EventHandler<ActionEvent> {

	private MenuItem menuItem;
	private Stage stage;
	final static String DB_NAME = "THRIFTY_DB";
	final static String TABLE_NAME = "VEHICLES";
	final static String TABLE_NAME2 = "RENTAL_RECORDS";
	
	public MenuItemListener(MenuItem menuItem, Stage stage) {
		this.menuItem = menuItem;
		this.stage = stage;
	}
	
	@Override
	public void handle(ActionEvent event) {
		
		MenuItem mItem = (MenuItem) event.getSource();
		String task = mItem.getText();
		
		if ("Add".equalsIgnoreCase(task)) {
			System.out.println("You have chosen to add.");
		} else if ("Import".equalsIgnoreCase(task)) {
			try {
				importFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("Export".equalsIgnoreCase(task)) {
			try {
				exportFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    public void exportFile() throws IOException {

    	ObservableList<String> CurrentDB = ConnectionSetup.QueryVehicle();
    	ObservableList<String> CurrentDB2 = ConnectionSetup.QueryRental();
    	
    	final String FILE_NAME = "export_data.txt";
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose location To Save Report");
       
        //choose folder.
        File selectedDirectory = null;
        if(selectedDirectory == null){
        	selectedDirectory = chooser.showDialog(null);
        }

        File file = new File(selectedDirectory + "/" + FILE_NAME);
        PrintWriter outFile = null;
        
        try {
            outFile = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            return;
        }


        for(int i = 0; i<CurrentDB.size(); i++){
            outFile.println(CurrentDB.get(i));
        }
        for(int i = 0; i<CurrentDB2.size(); i++){
            outFile.println(CurrentDB2.get(i));
        }

        outFile.close();
        System.out.println("You have exported a file out!");
    	
    }

    
    public void importFile() throws IOException {
    	
    	//choose file
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(stage);
    	
    	InputStream inputStream = new FileInputStream(selectedFile);
    	Scanner scanner = new Scanner(inputStream);
    	
    	while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] strArr = line.split("\\_");
			String[] strArr2 = line.split("\\:");
			for (int i = 0; i < strArr2.length; i++) {
				if (strArr2[i].equals("none")) {
					strArr2[i] = "0";
					System.out.println(strArr2[i]);
				}
			}
			if ((strArr.length -1) > 2) {
				System.out.println("Record");
				
				String rental_id = strArr2[0];
				RentalRecord r = new RentalRecord(strArr[0], strArr[1],  new DateTime(19, 4, 2019), new DateTime(19, 4, 2019), new DateTime(19, 4, 2019), Double.parseDouble(strArr2[4]), Double.parseDouble(strArr2[5])); 
				
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					//String query = "INSERT INTO " + TABLE_NAME + " VALUES ('Car', 'C_ar', 2019, 'Honda', 'Civic', 7, 'Available', 'CivicGood.png')";
					String query = String.format("INSERT INTO RENTAL_RECORDS VALUES ('%s', '%s', '%s', '%s', '%s', %s, %s)", rental_id, strArr[2], r.getRentDate(), r.getEstimatedReturnDate(), r.getActualReturnDate(), r.getRentalFee(), r.getLateFee());
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME2 + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				
			} else {
				System.out.println("Vehicle");
				Car c = new Car(strArr2[0], Integer.parseInt(strArr2[1]), strArr2[2], strArr2[3], Integer.parseInt(strArr2[4]));
				Model.getCurrentInstance().getValues().add(c);
				System.out.println();
				System.out.println("The database currently has:");
				System.out.println(Model.getCurrentInstance().getValues());
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					//String query = "INSERT INTO " + TABLE_NAME + " VALUES ('Car', 'C_ar', 2019, 'Honda', 'Civic', 7, 'Available', 'CivicGood.png')";
					String query = String.format("INSERT INTO VEHICLES VALUES ('Car', '%s', %d, '%s', '%s', %d, '%s', '%s')", c.getId(), c.getYear(), c.getMake(), c.getModel(), c.NumOfSeats(), c.getStatus().toString(), strArr2[6]);
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
    	}  	
    }


}