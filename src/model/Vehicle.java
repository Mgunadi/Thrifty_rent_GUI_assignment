package model;

public interface Vehicle {

	String getId();
	
	int getYear();
	
	String getMake();
	
	String getModel();
	
	int NumOfSeats();
	
	Status getStatus();
	
	void setStatus(Status status);
}
