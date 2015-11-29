package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CreateUser extends BubbleDocsService {

	private String userToken;
	private String username;
	private String name;
	private String email;
	
	public CreateUser(String userToken, String username, String name ,String email){
		this.userToken = userToken;
		this.username = username;
		this.email = email;
		this.name = name;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	bd.checkIfIsRoot(this.userToken);
    	bd.presentInSession(this.userToken);
    	bd.checkValidityOfUsername(this.username);
    	bd.checkValidityOfEmail(this.email);
    	bd.novoUtilizador(this.name, this.email, this.username ,this.userToken);	  		
    }
}
