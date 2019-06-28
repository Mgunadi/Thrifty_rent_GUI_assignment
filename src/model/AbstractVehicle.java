package model;

import java.util.*;
import exceptions.*;


public abstract class AbstractVehicle implements Vehicle {
	
	private String vehicleID;
	private int year;
	private String make;
	private String model;
	private int no_seats;
	private Status status = Status.AVAILABLE;
	private String image; 
	protected List<RentalRecord> records = new ArrayList<RentalRecord>();
	protected String[] entries = {" \nVehicle ID: \t\t\t", " \nYear: \t\t\t\t", " \nMaKe \t\t\t\t", " \nModel: \t\t\t\t", 
			" \nNumber of seats: \t\t", " \nStatus: \t\t\t\t", " \n\nRENTAL RECORD:"}; 
	protected String[] rented_entries = {"Record ID: \t\t\t\t", "\nRent Date: \t\t\t\t", "\nEstimated Return Date: \t\t"};
	protected String[] returned_entries = {"Record ID: \t\t\t", "\nRent Date: \t\t\t", "\nEstimated Return Date: \t", "\nActual Return Date \t\t",
			"\nRental Fee \t\t\t", "\nLate Fee: \t\t\t\t"};

	protected AbstractVehicle(String vehicleID, int year, String make, String model, int no_seats) {
		this.vehicleID = vehicleID;
		this.year = year;
		this.make = make;
		this.model = model;
		this.no_seats = no_seats;
	}
	
	protected AbstractVehicle(String vehicleID, int year, String make, String model, int no_seats, Status status) {
		this.vehicleID = vehicleID;
		this.year = year;
		this.make = make;
		this.model = model;
		this.no_seats = no_seats;
		this.status = status;
	}

	@Override
	public String getId() {
		return vehicleID;
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public String getMake() {
		return make;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public int NumOfSeats() {
		return no_seats;
	}

	@Override
	public Status getStatus() {
		return status;
	}
	
	@Override
	public void setStatus(Status status) {
		this.status = status;
	}

	//set Record when rented
	public void setRecord(String vehicleId, String customerId, DateTime rentDate, DateTime estimatedReturnDate) {
		RentalRecord record_added = new RentalRecord(vehicleId, customerId, rentDate, estimatedReturnDate); 
		records.add(record_added);
	}
	
	//set Record when returned
	public void setRecord(String vehicleId, String customerId, DateTime rentDate, DateTime estimatedReturnDate, DateTime actualReturnDate, double rentalFee, double lateFee) {
		RentalRecord record_added = new RentalRecord(vehicleId, customerId, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, lateFee); 
		records.add(record_added);
	}
	
	public String rent(String customerId, DateTime rentDate, int numOfRentDay) {
		//check vehicle status
		switch(status) {
			case AVAILABLE:
				status = Status.RENTED;
				// calculate the estimated return date.
				DateTime today = rentDate;
				DateTime estimatedReturnDate = new DateTime(today, numOfRentDay);
				
				//add RentalRecord into vehicle records
				RentalRecord record = new RentalRecord(vehicleID, customerId, today, estimatedReturnDate);
				records.add(record);
				return String.format("Vehicle %s is now rented by customer: %s. \nPlease return your vehicle by: %s, %s", vehicleID, customerId, estimatedReturnDate.getNameOfDay(), estimatedReturnDate);
				
			case RENTED:
				return "This vehicle is not available.";
			case MAINTENANCE:
				return "This vehicle is currently being serviced.";
			default: return "";
		}
	}
	
	public String enterRecord(DateTime returnDate, double rentalFee, double lateFee) {
		records.get(records.size()-1).setRentalFee(rentalFee);
		records.get(records.size()-1).setLateFee(lateFee);
		records.get(records.size()-1).setActualReturnDate(returnDate);
		return "Your rental fee for this trip is: $" + records.get(records.size()-1).getRentalFee() + "\n" +
		"Your late fee for this trip is: $" + records.get(records.size()-1).getLateFee() + "\n";
	}
	
	public RentalRecord getCurrentRecord() {
		return records.get(records.size()-1);
	}
	public abstract String returnVehicle(DateTime returnDate);
	
	public String performMaintenance() {
		if (status == Status.AVAILABLE) {
			status = Status.MAINTENANCE;
			return "The vehicle will undergo maintenance.";
		} else {
			return "The vehicle is not available for maintenance at this time.";
		}
	}
	
	public String completeMaintenance(AbstractVehicle v) throws MaintenanceException {
		if (status == Status.MAINTENANCE) {
			status = Status.AVAILABLE;
			return "The vehicle has completed maintenance. It is now available to rent.";
			
		} else {
			throw new MaintenanceException("This vehicle is not currently undergoing maintenance.");
			//return "This vehicle is not currently undergoing maintenance.";
		}
	}

	@Override
	public String toString() {
		String details = vehicleID + ":"+ year + ":" + make + ":" + model + ":" + no_seats + ":" + status.toString();
		return details;
	}
	
	public List<RentalRecord> getRecord() {
		return records;
	}
	
	public String getDetail() {
		// Print details for newly added Vehicle.
		//System.out.println("_____________________________________");
		if (records.size() == 0) {
			return entries[0] + vehicleID + entries[1] + year + entries[2] + make + entries[3] + model + 
					entries[4] + no_seats + entries[5] + status + entries[6] + "\t\tEMPTY";
		} else {
			// Print details for rented vehicle.
			if (status == Status.RENTED) {		
				String Longlist =  entries[0] + vehicleID + entries[1] + year + entries[2] + make + entries[3] + model + 
						entries[4] + no_seats + entries[5] + status + entries[6];
				
				for (int i = records.size() -1; i >= 0; i--) {
						Longlist += ("\n------------------\n" + rented_entries[0] + records.get(i).getId() + rented_entries[1] + records.get(i).getRentDate() +
						rented_entries[2] + records.get(i).getEstimatedReturnDate());
						if (i == records.size()-11) {
							break;
						}
				}
				return Longlist;
			} else {
				// Print details for Available or Maintenance vehicle with history.
				String Longlist2 = entries[0] + vehicleID + entries[1] + year + entries[2] + make + entries[3] + model + 
						entries[4] + no_seats + entries[5] + status + entries[6];
				
				for (int i = records.size() -1; i >= 0; i--) {
						Longlist2 += ("\n------------------\n" + returned_entries[0] + records.get(i).getId() + returned_entries[1] + records.get(i).getRentDate() +
						returned_entries[2] + records.get(i).getEstimatedReturnDate()  +returned_entries[3] + records.get(i).getActualReturnDate() +
						returned_entries[4] + records.get(i).getRentalFee() + returned_entries[5] + records.get(i).getLateFee());
						if (i == records.size()-11) {
							break;
						}
				}
				return Longlist2;
			}
		}
	}
}
