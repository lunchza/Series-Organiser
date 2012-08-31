package sorganiser.main;

public class SOException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public SOException(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}

}
