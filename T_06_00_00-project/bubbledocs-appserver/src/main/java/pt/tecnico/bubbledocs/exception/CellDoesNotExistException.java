package pt.tecnico.bubbledocs.exception;

public class CellDoesNotExistException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private String celula;
	
    public CellDoesNotExistException(String celula) {
    	this.celula = celula;
    }
	
    public String getReference() {
    	return this.celula;
    }

}
