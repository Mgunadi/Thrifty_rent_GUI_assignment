package model;

public class Van extends AbstractVehicle {

	private static final int RATE_FOR_HIRE = 235;
	private static final int LATE_RATE = 299;
	private double rentalFee;
	private double lateFee;
	private DateTime lastMaintenance;
	
	public Van(String vehicleID, int year, String make, String model, int no_seats) {
		super(vehicleID, year, make, model, no_seats);
	}
	
	public Van(String vehicleID, int year, String make, String model, int no_seats, Status status) {
		super(vehicleID, year, make, model, no_seats, status);
	}
	
	@Override
	public String rent(String customerId, DateTime rentDate, int numOfRentDay) {
		//check for violation of maintenance schedule.
		DateTime estimatedReturn = new DateTime (rentDate, numOfRentDay);
		if(DateTime.diffDays(estimatedReturn, lastMaintenance)> 12) {
			return "This vehicle cannot be rented as it must undergo maintenance.";
		} else { 
			if (numOfRentDay > 0) {
				super.rent(customerId, rentDate, numOfRentDay);
				return "The date of last maintenance is " + lastMaintenance;
			} else {
				super.rent(customerId, rentDate, 1);
				return "Minimum number of days to rent is 1 day. Vehicle will be rented for 1 day.";
			}
		}
	}
	
	@Override
	public String returnVehicle(DateTime returnDate) {
		//Ask for valid return date.
		if (DateTime.diffDays(returnDate, records.get(records.size()-1).getRentDate()) < 0) {
			return "Enter a valid return date.";
		} else {

			setStatus(Status.AVAILABLE);
			//calculate how many days rented and rentalFee:
			int rental_days = DateTime.diffDays(records.get(records.size()-1).getEstimatedReturnDate(), records.get(records.size()-1).getRentDate()); 
			int late_days = DateTime.diffDays(returnDate, records.get(records.size()-1).getEstimatedReturnDate());
			rentalFee = rental_days * RATE_FOR_HIRE;
			lateFee = late_days * LATE_RATE;
			
			;
			return enterRecord(returnDate, rentalFee, lateFee) + "You have returned your van on ";
		}
	}
	
	public void setLastMaintenance(DateTime lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}
	
	public DateTime getMaintenance() {
		return lastMaintenance;
	}
	
	@Override
	public String completeMaintenance(AbstractVehicle v) {
		if (v.getStatus() == Status.MAINTENANCE) {
			v.setStatus(Status.AVAILABLE);
			return "The vehicle has completed maintenance. It is now available to rent.";	
		} else {
			return "This vehicle is not currently undergoing maintenance.";
		}
	}

	
	@Override
	public String toString() {
		String details = getId() + ":"+ getYear() + ":" + getMake() + ":" + getModel() + ":" + NumOfSeats() + ":" + getStatus().toString() + ":" + this.getMaintenance();
		return details;
	}

	@Override
	public String getDetail() {
		// Print details for newly added Vehicle.
		if (records.size() == 0) {
			return entries[0] + getId() + entries[1] + getYear() + entries[2] + getMake() + entries[3] + getModel() + 
					entries[4] + NumOfSeats() + entries[5] + getStatus() + "\nLast maintenance date: \t" + lastMaintenance + entries[6] + "\tEMPTY";
		} else {
			// Print details for rented vehicle.
			if (getStatus() == Status.RENTED) {		
				String Longlist =  entries[0] + getId() + entries[1] + getYear() + entries[2] + getMake() + entries[3] + getModel() + 
						entries[4] + NumOfSeats() + entries[5] + getStatus() + "\nLast maintenance date: \t" + lastMaintenance + entries[6];
				
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
				String Longlist2 = entries[0] + getId() + entries[1] + getYear() + entries[2] + getMake() + entries[3] + getModel() + 
						entries[4] + NumOfSeats() + entries[5] + getStatus() + "\nLast maintenance date: \t" + lastMaintenance + entries[6];
				
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
