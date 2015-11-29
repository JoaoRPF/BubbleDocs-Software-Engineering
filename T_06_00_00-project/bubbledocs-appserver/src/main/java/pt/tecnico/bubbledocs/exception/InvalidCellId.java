package pt.tecnico.bubbledocs.exception;

public class InvalidCellId extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	public InvalidCellId(String name) {
		userName = name;
	}
	
	public String getUserName() {
		return userName;
	} 

}