package pt.tecnico.bubbledocs.service.integration;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator{
	private String userToken;
    private String pass;
    private String name;
    
    public final String getUserToken() {
    	return this.userToken;
        }
    
    public LoginUserIntegrator (String username, String password) {
    	name = username;
    	pass = password;
    }
    
    
    
    @Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
    	IDRemoteServices remote = new IDRemoteServices();
    	try {
    		remote.loginUser(name, pass); 

    	} catch (RemoteInvocationException rie) { 
    		bubble.verificaPasswordLocalOff(name, pass);
    		bubble.verificaLogin(name);
    		this.userToken=bubble.loginSucess(name, pass); 
    	}
    	LoginUser loginUser = new LoginUser (this.name,this.pass);
    	loginUser.execute();
    	this.userToken=loginUser.getUserToken();
    	
   	}
}

