package pt.tecnico.bubbledocs.exception;

public class InvalidColumnInputException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private int column;
	
    public InvalidColumnInputException(int column) {
    	this.column = column;
    }
	
    public int getColumn() {
    	return this.column;
    }
}
