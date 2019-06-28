package model;

public class RentalRecord {

	private String id;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private double rentalFee;
	private double lateFee;

	
	public RentalRecord(String vehicleId, String customerId, DateTime rentDate, DateTime estimatedReturnDate) {
		this.id = vehicleId + "_" + customerId + "_" + rentDate.getEightDigitDate();
		//this.id = vehicleId;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
	}
	
	public RentalRecord(String vehicleId, String customerId, DateTime rentDate, DateTime estimatedReturnDate, DateTime actualReturnDate, double rentalFee, double lateFee) {
		this.id = vehicleId + "_" + customerId + "_" +  rentDate.getEightDigitDate();
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.rentalFee = rentalFee;
		this.lateFee = lateFee;
	}

	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}
	
	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	
	public double getRentalFee() {
		return rentalFee;
	}
	
	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}
	
	public double getLateFee() {
		return lateFee;
	}
	
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	public String getId() {
		return id;
	}
	
	public DateTime getRentDate() {
		return rentDate;
	}
	
	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}
	
}
