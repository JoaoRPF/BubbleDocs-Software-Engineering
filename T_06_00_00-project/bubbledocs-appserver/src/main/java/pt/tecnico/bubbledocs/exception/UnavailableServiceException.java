package pt.tecnico.bubbledocs.exception;


public class UnavailableServiceException extends BubbleDocsException {
    
    /**
     */
    private static final long serialVersionUID = 1L;
    
    private String pass;
    private String name;
    
    public UnavailableServiceException(String name, String password) {
        name=name;
        pass = password;
    }
    
    public String getUserName() {
        return "PASSWORD and NAME: " + pass + " "+ name +" wrong";
    }
}