package model;

public class Car extends AbstractVehicle{

	private static final double RATE_FOR_4_SEATER = 78;
	private static final double RATE_FOR_7_SEATER = 113;
	private static final double LATE_RATE = 1.25;
	private double rentalFee;
	private double lateFee;
	

	public Car(String vehicleID, int year, String make, String model, int no_seats) {
		super(vehicleID, year, make, model, no_seats);
	}
	
	public Car(String vehicleID, int year, String make, String model, int no_seats, Status status) {
		super(vehicleID, year, make, model, no_seats, status);
	}

	@Override
	public String returnVehicle(DateTime returnDate) {
		//check if the return date entered is before the rent date listed in the vehicle's records
		if (DateTime.diffDays(returnDate, records.get(records.size()-1).getRentDate()) < 0) {
			return "Enter a valid return date.";
		} else {
			
			setStatus(Status.AVAILABLE);
			
			//calculate how many days rented and late & rental Fee:
			int rental_days = DateTime.diffDays(records.get(records.size()-1).getEstimatedReturnDate(), records.get(records.size()-1).getRentDate()); 
			int late_days = DateTime.diffDays(returnDate, records.get(records.size()-1).getEstimatedReturnDate());
			
			if (this.NumOfSeats() == 4) {
				rentalFee = rental_days * RATE_FOR_4_SEATER;
				lateFee = late_days * RATE_FOR_4_SEATER * LATE_RATE;
			} else if (this.NumOfSeats() == 7) {
				rentalFee = rental_days * RATE_FOR_7_SEATER;
				lateFee = late_days * RATE_FOR_7_SEATER * LATE_RATE;
			}

			//enter into records.
			return enterRecord(returnDate, rentalFee, lateFee) + "You have returned your car on ";
		}
	}
	
	@Override
	public String rent(String customerId, DateTime rentDate, int numOfRentDay) {
		String[] weekday = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
		String[] weekend = {"Friday", "Saturday", "Sunday"};
		String DayName = rentDate.getNameOfDay();
		System.out.println("Car rent method called.");
		
		//Check if rental day is on weekday
		for (String e : weekday) {
			if(e.equals(DayName)) {
				if (numOfRentDay < 2) {
					numOfRentDay = 2;
					super.rent(customerId, rentDate, numOfRentDay);
					return "Minimum number of days you can rent is 2. Car will be rented for 2 days.";
				} else {
					return super.rent(customerId, rentDate, numOfRentDay);
				}
			}
		}
		//Check if rental day is on weekend
		for (String e : weekend) {
			if(e.equals(DayName)) {
				if (numOfRentDay < 3) {
					numOfRentDay = 3;
					super.rent(customerId, rentDate, numOfRentDay);
					return "Minimum number of days you can rent is 3. Car will be rented for 3 days.";
				} else {
					return super.rent(customerId, rentDate, numOfRentDay);
				}
			}
		}	
		return "You have not rented the vehicle.";
	}
}