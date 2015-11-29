package pt.tecnico.bubbledocs.exception;

public class InvalidReferenceFormatException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String reference;

	public InvalidReferenceFormatException(String reference) {
		this.reference = reference;
	}
	
	public String getReference() {
		return reference;
	} 

}