package pt.tecnico.bubbledocs.exception;

public class WrongFunctionFormatException extends BubbleDocsException {
	
	
	private static final long serialVersionUID = 1L;

	private String userName;

	public WrongFunctionFormatException() {
		
	}
	
	public String getUserName() {
		return userName;
	} 

}
