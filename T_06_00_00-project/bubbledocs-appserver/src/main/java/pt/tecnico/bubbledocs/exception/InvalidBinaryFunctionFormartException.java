package pt.tecnico.bubbledocs.exception;

public class InvalidBinaryFunctionFormartException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String content;

	public InvalidBinaryFunctionFormartException(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	} 

}