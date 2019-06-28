package exceptions;

public class MaintenanceException extends Exception {
	private String message;
	
	public MaintenanceException(String s) {
		message = s;
	}
	
	public String getErrorMsg() {
		return message;
	}

}
