package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import pt.tecnico.bubbledocs.service.integration.*;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest {
	private String userToken;
	private String newPass;
	
	private static final String USERNAME = "jp";
    private static final String PASSWORD = "jp#";
    private static final String USERNAME2 = "mr";
    private static final String PASSWORD2 = "mr1";
    private static final String EMAIL= "antonio@gmail";
    private static final String EMAIL2= "joao@hotmail";
    
    Utilizador u;
    Utilizador u2;
    @Override
	public void populate4Test() {
		this.u=createUser(USERNAME, PASSWORD, "João Pereira",EMAIL);
		this.u2=createUser(USERNAME2, PASSWORD2, "Maria R",EMAIL2);
	 }
    
    
    @Test
	public void renewPasswordSucess(@Mocked final IDRemoteServices remote) throws Exception{ 
    	this.userToken=addUserToSession(USERNAME,"João Pereira"); 
    	
    	new Expectations(){
    		{
    			new IDRemoteServices();
    			remote.renewPassword(USERNAME); 
    		}
    	};

    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
    	service.execute();
    	assertFalse(u.isPassLocal());
    }
    
    @Test (expected = UnavailableServiceException.class)
	public void renewPasswordServerDown(@Mocked final IDRemoteServices remote) throws Exception{ 
    	this.userToken=addUserToSession(USERNAME,"João Pereira"); 
    	
    	new Expectations(){
    		{
    			new IDRemoteServices();
    			remote.renewPassword(USERNAME); result = new RemoteInvocationException();
    		}
    	};

    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
    	service.execute();
    }
    
    @Test (expected = LoginBubbleDocsException.class)
	public void renewPasswordWrongLogin(@Mocked final IDRemoteServices remote) throws Exception{ 
    	this.userToken=addUserToSession(USERNAME,"João Pereira"); 
    	
    	new Expectations(){
    		{
    			new IDRemoteServices();
    			remote.renewPassword(USERNAME); result = new LoginBubbleDocsException(anyString);
    		}
    	};

    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
    	service.execute();
    }
    
    
    @Test (expected = UserNotInSessionException.class)
    public void userNotInSessionMockit(@Mocked final IDRemoteServices remote) throws Exception{ 
    	this.userToken=addUserToSession(USERNAME2, "Maria R");
    	removeUserFromSession(userToken);

    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
    	service.execute();
    }
 
}
