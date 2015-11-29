package pt.tecnico.bubbledocs.exception;

public class UnknownBubbleDocsSpreadSheetException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private int userName;

	public UnknownBubbleDocsSpreadSheetException(int name) {
		userName = name;
	}
	
	public int getIdspreadSheet() {
		return this.userName;
	} 

}