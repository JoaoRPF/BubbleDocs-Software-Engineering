package pt.tecnico.bubbledocs.exception;

public class UserNaoExisteException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	public UserNaoExisteException(String name) {
		userName = name;
	}
	
	public String getUserName() {
		return userName;
	} 

}