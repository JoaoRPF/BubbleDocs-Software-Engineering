package pt.tecnico.bubbledocs.exception;

public class CannotLoadDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	private int folhaId;
	
	public CannotLoadDocumentException(int id) {
		this.folhaId = id;
	}
	
	public int FolhaId() {
    	return this.folhaId;
    }
}
