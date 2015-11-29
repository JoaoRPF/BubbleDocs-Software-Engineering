package pt.tecnico.bubbledocs.exception;

public class InvalidCellIdFormat extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	public InvalidCellIdFormat(String name) {
		userName = name;
	}
	
	public String getUserName() {
		return userName;
	} 

}