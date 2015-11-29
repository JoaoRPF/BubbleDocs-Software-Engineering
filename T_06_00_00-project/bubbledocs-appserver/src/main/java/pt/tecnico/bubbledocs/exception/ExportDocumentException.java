package pt.tecnico.bubbledocs.exception;

public class ExportDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	private int folhaId;
	
	public ExportDocumentException(int id) {
		this.folhaId = id;
	}
	
	public int FolhaId() {
    	return this.folhaId;
    }
}
