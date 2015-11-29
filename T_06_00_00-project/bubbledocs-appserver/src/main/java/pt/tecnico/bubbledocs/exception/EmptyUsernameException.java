package pt.tecnico.bubbledocs.exception;

public class EmptyUsernameException extends BubbleDocsException {

    private static final long serialVersionUID = 1L;

    private String conflictingName;
	
    public EmptyUsernameException(String conflictingName) {
    	this.conflictingName = conflictingName;
    }
	
    public String getConflictingName() {
    	return this.conflictingName;
    }
}
