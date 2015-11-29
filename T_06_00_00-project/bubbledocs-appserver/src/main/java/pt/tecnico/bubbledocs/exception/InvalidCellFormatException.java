package pt.tecnico.bubbledocs.exception;

public class InvalidCellFormatException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String userName;

	public InvalidCellFormatException() {
		
	}
	
	public String getUserName() {
		return userName;
	} 
}
