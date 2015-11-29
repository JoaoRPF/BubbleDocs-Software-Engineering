package pt.tecnico.bubbledocs.exception;

public class CannotStoreDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	private int folhaId;
	
	public CannotStoreDocumentException(int id) {
		this.folhaId = id;
	}
	
	public int FolhaId() {
    	return this.folhaId;
    }
}
