package pt.tecnico.bubbledocs.exception;

public class LoginBubbleDocsException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String username;
	
	public LoginBubbleDocsException (String us){
		username = us;
	}
	
	public String getUserName(){
		return "USERNAME " + username + " Errado";
	}
}
