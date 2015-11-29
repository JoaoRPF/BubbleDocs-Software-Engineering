package pt.tecnico.bubbledocs.exception;

public class CellReferingToItselfException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private String celula;
	
    public CellReferingToItselfException(String celula) {
    	this.celula = celula;
    }
	
    public String getReference() {
    	return this.celula;
    }
}