package pt.tecnico.bubbledocs.exception;

public class WrongPasswordException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String pass;

	public WrongPasswordException(String password) {
		pass = password;
	}
	
	public String getUserName() {
		return "PASSWORD: " + pass +" Errada";
	} 

}