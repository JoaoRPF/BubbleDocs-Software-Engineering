package pt.tecnico.bubbledocs.exception;

public class InvalidLiteralTypeException extends BubbleDocsException {
	
	private static final long serialVersionUID = 1L;

	private String userName;

	public InvalidLiteralTypeException() {
		
	}
	
	public String getUserName() {
		return userName;
	} 

}
