package model;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import exceptions.MaintenanceException;

import java.util.List;

import model.*;


public class ThriftyRent {

	private List<AbstractVehicle> vehicles = new ArrayList<>();
	String menu[] = {"\nAdd vehicle: \t \t 1", "Rent vehicle: \t \t 2", "Return vehicle: \t 3",
			"Vehicle Maintenance: \t 4", "Complete Maintenance: \t 5", "Display All Vehicles: \t 6", 
			"Exit Program: \t \t 7", "Enter your choice:"};
	private int option;
	private boolean valid = false;
	private String vehicleId = "NULL";
	private DateTime rentDate_ = null;
	private String rentDate = "";
	Scanner input = new Scanner(System.in);

	public void start() throws MaintenanceException {

		do {
			//print out Menu options.
			System.out.println();
			System.out.println("***  ThriftyRent SYSTEM MENU  ***");
			System.out.println();
			for (int i = 0; i < menu.length; i++) {
				System.out.println(menu[i]);	
			}
			valid = false;
			do {
				try {
					option = input.nextInt();
					valid = true;
				}
				catch(InputMismatchException e) {
					System.out.println("Must be a valid integer.");
					input.next();
				}
			} while(!valid);
			
			switch(option) {
			
			case 1:
				addVehicle();
				break;
			
			case 2:
				rentVehicle();
				break;
			
			case 3:
				returnVehicle();
				break;
				
			case 4:
				Pmaintenance();
				break;
				
			case 5: 
				Cmaintenance();
				break;
				
			//display all vehicles + records
			case 6:
				for (AbstractVehicle k : vehicles) {
					System.out.println(k.getDetail());
				}
				System.out.println();
				break;
			
			case 7:
				System.out.println("You have chosen to exit.");
				break;
			
			default :
				System.out.println("Error: Please type an option between 1-7.");	
			}
			
		} while(option !=7);
		
		
	}
	
	public void addVehicle() {
		//Ask for valid ID
		System.out.println("Vehicle id: ");
		valid = false;
		while(valid == false) {
			System.out.println("(Please enter an ID beginning with 'C_' if a car, or 'V_'  if a van. Must be a new ID not in the system.)");
			vehicleId = input.next();
			String collection = "";
			for (AbstractVehicle v : vehicles) {
				collection += v.toString() + " ";
			}
			if (!collection.contains(vehicleId)) {
				valid = (vehicleId.startsWith("C_") || (vehicleId.startsWith("V_")));
			}
		}
		//Ask for generic vehicle details
		int year = 0;
		valid = false;
		do {
			try {
				System.out.println("Year: ");
				year = input.nextInt();
				valid = true;
			}
			catch(InputMismatchException e) {
				System.out.println("Must be a valid year.");
				input.next(); // waits and blocks until the user inputs.
			}
		} while(!valid);
		
		System.out.println("Make: ");
		String make = input.next();
		System.out.println("Model: ");
		String model = input.next();
		
		//limits vehicle fleet to 50
		if (vehicles.size() <= 50) {
			if (vehicleId.startsWith("C_")) {
				addCar(vehicleId, year, make, model);
			} else {
				addVan(vehicleId, year, make, model);
			}
		} else {
			System.out.println("You have exceeded the number of vehicles in the fleet. Cannot add any more!");
		}
	}
	
	public void addCar(String vehicleId, int year, String make, String model) {
		// ask for Car specific details.
		int no_seats;
		System.out.println("Number of passengers: ");
		valid = false;
		do {
			System.out.println("(Only 4 or 7 seaters available)");
			no_seats = input.nextInt();
			valid = (no_seats ==4 || no_seats ==7);
		} while(valid == false);
		
		//Create new instance of car to add to vehicle fleet.
		AbstractVehicle v = new Car(vehicleId, year, make, model, no_seats);
		vehicles.add(v);
		System.out.println();
		System.out.println("The vehicle collection now includes: ");
		for (AbstractVehicle i : vehicles) {
			System.out.println(i.getId());
		}

	}
	
	public void addVan(String vehicleId, int year, String make, String model) {
		//ask for van-specific details.
		int no_seats = 15;
		DateTime today = new DateTime();
		
		//creates new instance of Van to add to vehicle fleet.
		AbstractVehicle v = new Van(vehicleId, year, make, model, no_seats);
		((Van)v).setLastMaintenance(today);
		vehicles.add(v);
		System.out.println();
		System.out.println("The vehicle collection now includes: ");
		for (AbstractVehicle i : vehicles) {
			System.out.println(i.getId() + " " + i.getClass());
		}
	}
	
	public void rentVehicle() {
		//user input.
		vehicleId = askID();
		System.out.println("Customer id: ");
		String customerId = input.next();
		valid = false;
		
		//ask for valid rental date in DateTime format.
		rentDate_ = askDate();
		
		// Ask for valid number of days to rent.
		int numOfRentDay = 0;
		do {
			try {
				System.out.println("How many days?: ");
				numOfRentDay = input.nextInt();
				if (numOfRentDay > 0) {
					valid = true;
				} 	
			}
			catch(InputMismatchException e) {
				System.out.println("Must be a valid number.");
				input.next();
			}
		} while(!valid || numOfRentDay > 14);
		//Polymorphically calls the relevant method to rent (car vs van)
		for (AbstractVehicle v : vehicles) {
			if (vehicleId.equals(v.getId())) {
				v.rent(customerId, rentDate_, numOfRentDay);
			}
		}		
	}	
	
	public void returnVehicle() {
		//user input:
		vehicleId = askID();	
		DateTime actualReturnDate_ = askDate();	
		
		//Calls the method to return vehicle and polymorphically calls method to get detail.
		for (AbstractVehicle v : vehicles) {
		if (vehicleId.equals(v.getId())) {
				v.returnVehicle(actualReturnDate_);
				System.out.println("Latest rental record:");
				System.out.println(v.getDetail());
			}
		}
	}
	
	public void Pmaintenance() throws MaintenanceException {
		//user input- check valid vehicle.
		do {
			System.out.println("Vehicle id: ");
			vehicleId = input.next();
			valid = false;
			int counter = 0;
			for (AbstractVehicle v : vehicles) {
				if (vehicleId.equals(v.getId())) {
					v.performMaintenance();
					valid = true;	
				} 
			counter += 1;
			}	
			if (counter == vehicles.size() -1) { 
				System.out.println("This vehicle ID is not in the system.");
			}
		} while(valid ==false);
		System.out.println();
	}
	
	public void Cmaintenance() {
		//user input - check valid vehicle.
		System.out.println("Vehicle id: ");
		valid = false;
		while(valid == false) {
			System.out.println("(Please enter an ID in the system undergoing maintenance.)");
			vehicleId = input.next();
			for (AbstractVehicle v : vehicles) {
				if (v.getId().equals(vehicleId)  && v.getStatus() == Status.MAINTENANCE)  {
					// Sets new Last Maintenance date
					try {
						v.completeMaintenance(v);
					} catch (MaintenanceException e) {
						e.printStackTrace();
					}
					valid = true;
				}
			}
		}		
	}

	public DateTime askDate() {
		do {
			try {
				System.out.println("Enter date (dd/mm/yyyy): ");
				rentDate = input.next();
				int day = Integer.parseInt(rentDate.substring(0,2));
				int month = Integer.parseInt(rentDate.substring(3,5));
				int yr = Integer.parseInt(rentDate.substring(6,10));
				rentDate_ = new DateTime(day, month, yr);
				valid = (rentDate.length() == 10);
			} 
			catch(Exception e) {
				System.out.println("Not a valid date format.");
			}
		} while(valid == false);
		return rentDate_;
	}
	
	public String askID() {
		System.out.println("Vehicle id: ");		
		valid = false;
		while(valid == false) {
			System.out.println("Please enter an existing VehicleID in the system.");
			vehicleId = input.next();
			String collection = "";
			for (AbstractVehicle v : vehicles) {
				collection += v.toString() + " ";
			}
			if (collection.contains(vehicleId)) {
				valid = true; 
			} else {
				System.out.println("VehicleID is not in records.");
			}
		}
		return vehicleId;
	}

	public void test() {
		// TEST entries
				DateTime goodFriday = new DateTime(19, 4, 2019);
				DateTime goodFriday2 = new DateTime(20, 4, 2019);
				DateTime goodFriday3 = new DateTime(25, 4, 2019);

				//preexisting added car:
				Car HEE = new Car("C_ar", 2019, "Honda", "Civic", 7);
				vehicles.add(HEE);
				System.out.println();
				
				//preexisting rented van:
				Van HEIL = new Van("V_an", 2019, "Honda", "Accord", 15);
				vehicles.add(HEIL);
				HEIL.setStatus(Status.RENTED);
				HEIL.setRecord("V_an", "Matt", goodFriday, goodFriday2);
				HEIL.setLastMaintenance(new DateTime(2,5,2019));
				System.out.println();
				
				//preexisting returned van:
				Van HET = new Van("V_ar", 2016, "Toyota", "Wagonwheels", 15);
				vehicles.add(HET);
				HET.setStatus(Status.AVAILABLE);
				HET.setRecord("V_ar", "Jarryd", goodFriday, goodFriday2, goodFriday3, 200, 300);
				HET.setLastMaintenance(new DateTime(2,5,2019));
				System.out.println();
				
				
				//preexisting rented van with 2 history entries
				Van Hist = new Van("V_hist", 2019, "Mitsubishi", "limo", 23);
				vehicles.add(Hist);
				Hist.setStatus(Status.RENTED);
				Hist.setRecord("V_hist", "Lucy", new DateTime(2,1,2018), new DateTime(3,1,2018), new DateTime(4,1,2018), 234, 343);
				Hist.setRecord("V_hist", "Jayden", new DateTime(2,3,2019), new DateTime(3,3,2019), new DateTime(4,5,2019), 234, 343);
				Hist.setLastMaintenance(new DateTime(12,4,2019));
				System.out.println();
				
				//preexisting rented car with 2 history:
				Car HEEQ = new Car("C_abby", 2019, "Ford", "Mustang", 4);
				vehicles.add(HEEQ);
				HEEQ.setStatus(Status.RENTED);
				HEEQ.setRecord("C_ar", "Daria", new DateTime(7,3,2018), new DateTime(9,3,2018), new DateTime(11,5,2018), 234, 343);
				HEEQ.setRecord("C_ar", "Homer", new DateTime(2,12,2018), new DateTime(3,12,2018), new DateTime(14,12,2018), 2994, 100);
				System.out.println();
	}

}
	

