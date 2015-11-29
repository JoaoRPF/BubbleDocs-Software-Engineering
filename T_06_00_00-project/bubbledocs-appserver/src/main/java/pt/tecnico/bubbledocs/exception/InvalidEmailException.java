package pt.tecnico.bubbledocs.exception;

public class InvalidEmailException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String email;

	public InvalidEmailException(String email) {
		email = email;
	}
	
	public String getUserName() {
		return "EMAIL: " + email +" Errada";
	} 

}