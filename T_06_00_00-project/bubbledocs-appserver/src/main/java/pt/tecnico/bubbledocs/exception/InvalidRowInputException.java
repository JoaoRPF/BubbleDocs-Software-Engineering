package pt.tecnico.bubbledocs.exception;

public class InvalidRowInputException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private int row;
	
    public InvalidRowInputException(int row) {
    	this.row = row;
    }
	
    public int getRow() {
    	return this.row;
    }
}
