package exceptions;

public class RentException extends NumberFormatException {
private String desc;

	public RentException(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}

}
