package pt.tecnico.bubbledocs.exception;

public class InvalidSpreadSheetIdException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private int docId;
	
    public InvalidSpreadSheetIdException(int docId) {
    	this.docId = docId;
    }
	
    public int getDocId() {
    	return this.docId;
    }

}
