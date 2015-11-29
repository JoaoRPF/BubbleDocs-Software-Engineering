package pt.tecnico.bubbledocs.exception;

public class ImportDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	private byte[] folhaImport;
	
	public ImportDocumentException(byte[] folha) {
		this.folhaImport = folha;
	}
	
	public byte[] FolhaId() {
    	return this.folhaImport;
    }
}
