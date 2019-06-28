package exceptions;

public class ReturnException extends Exception {
	private String message;
	
	public ReturnException(String s) {
		message = s;
		ErrorWindow error = new ErrorWindow(message);
		error.getDialogBox().show();
	}

}
