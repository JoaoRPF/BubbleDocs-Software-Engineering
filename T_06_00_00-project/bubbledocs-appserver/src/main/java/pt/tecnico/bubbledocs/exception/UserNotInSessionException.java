package pt.tecnico.bubbledocs.exception;

public class UserNotInSessionException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String token;

	public UserNotInSessionException(String token) {
		this.token = token;
	}
	
	public String getUserName() {
		return "Token: " + token +" Errada";
	} 

}