package pt.tecnico.bubbledocs.exception;

public class DuplicateEmailException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String email;

	public DuplicateEmailException(String email) {
		email = email;
	}
	
	public String getUserName() {
		return "EMAIL: " + email +" Errada";
	} 

}