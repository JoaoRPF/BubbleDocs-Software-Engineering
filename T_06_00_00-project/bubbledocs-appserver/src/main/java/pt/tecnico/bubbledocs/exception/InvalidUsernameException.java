package pt.tecnico.bubbledocs.exception;

public class InvalidUsernameException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	public InvalidUsernameException(String username) {
		username = username;
	}
	
	public String getUserName() {
		return "USERNAME: " + username +" Errada";
	} 

}