package pt.tecnico.bubbledocs.exception;

public class UnknownBubbleDocsUserException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	public UnknownBubbleDocsUserException(String name) {
		userName = name;
	}
	
	public String getUserName() {
		return userName;
	} 

}