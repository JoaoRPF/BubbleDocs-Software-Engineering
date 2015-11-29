package pt.tecnico.bubbledocs.service;



import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.*;
// add needed import declarations

public class LoginUser extends BubbleDocsService {

    private String userToken;
    private String pass;
    private String name;
    

    public LoginUser(String username, String password) {
    	name = username;
    	pass = password;
    }

	@Override
    protected void dispatch() throws BubbleDocsException{
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
    	
    	bubble.verificaPasswordLocal(name, pass); 
    	bubble.verificaLogin(name);
    	this.userToken=bubble.loginSucess(name, pass);

    				
    }

    public final String getUserToken() {
	return this.userToken;
    }
}
