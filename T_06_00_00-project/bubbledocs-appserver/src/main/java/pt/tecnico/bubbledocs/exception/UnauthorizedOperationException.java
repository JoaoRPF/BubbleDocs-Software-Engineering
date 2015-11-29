package pt.tecnico.bubbledocs.exception;

public class UnauthorizedOperationException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private String token;
	
    public UnauthorizedOperationException(String token) {
    	this.token = token;
    }
	
    public String getExceptionToken() {
    	return this.token;
    }
   
}
