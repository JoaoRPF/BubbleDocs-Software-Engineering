package pt.tecnico.bubbledocs.service;


import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;


// add needed import declarations

public class GetUserInfoService extends BubbleDocsService {
    private String username;
    private String [] info;
   // private String nomeUser;
    
    public GetUserInfoService(String username) {
	// add code here	
    	this.username = username;
    	info = null;
    }
    
    public String[] getUserInfo() {
    	return this.info;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {  	    	  	
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	this.info = bd.descreveUtilizador(username);
    	if(info == null){
    		throw new UnknownBubbleDocsUserException(username);
    	}
    }

}
