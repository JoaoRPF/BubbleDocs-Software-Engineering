package pt.tecnico.bubbledocs.exception;

public class InvalidLiteralType extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	public InvalidLiteralType(String name) {
		userName = name;
	}
	
	public String getUserName() {
		return userName;
	} 

}