package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.service.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.domain.*;
import mockit.Expectations;


import mockit.Mocked;

import org.junit.Test;

// add needed import declarations

public class DeleteUserIntegratorTest extends BubbleDocsServiceTest {

	private static final String USERNAME_TO_DELETE = "smf";
	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String SPREADSHEET_NAME = "spread";
	private static final String EMAIL= "antonio@gmail";
	private static final String EMAIL2= "joao@hotmail";
	private static final String EMAIL3= "joana@hotmail";
	// the tokens for user root
	private String root;
	private String userToken;

	@Override
	public void populate4Test() {
		Utilizador root;
		createUser(USERNAME, PASSWORD, "António Rito Silva",EMAIL);
		Utilizador smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio Fernandes",EMAIL3);
		createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);
		root = getUserFromUsername("root");
		if(root==null)
			root = createUser(ROOT_USERNAME,"rootroot",ROOT_USERNAME,EMAIL2);
		this.root = addUserToSession(ROOT_USERNAME,"root");
	};

	
	@Test
	public void removeUserSucessInSession(@Mocked final IDRemoteServices remote) throws Exception{ 
    	this.userToken=addUserToSession(USERNAME,"Pizza Hut"); 
    	
    	new Expectations(){
    		{
    			new IDRemoteServices();
    			remote.removeUser(USERNAME); 
    		}
    	};

    	DeleteUserIntegrator service = new DeleteUserIntegrator(root,USERNAME);
    	service.execute();
    	boolean deleted = getUserFromUsername(USERNAME) == null;
    	assertTrue("user was not deleted", deleted);
    	assertNull("Spreadsheet was not deleted", getSpreadSheet(SPREADSHEET_NAME));
    	
    }
	
	@Test
	public void removeUserSucessNotInSession(@Mocked final IDRemoteServices remote) throws Exception{ 
    	
    	new Expectations(){
    		{
    			new IDRemoteServices();
    			remote.removeUser(USERNAME); 
    		}
    	};

    	DeleteUserIntegrator service = new DeleteUserIntegrator(root,USERNAME);
    	service.execute();
    	boolean deleted = getUserFromUsername(USERNAME) == null;
    	assertTrue("user was not deleted", deleted);
    	assertNull("Spreadsheet was not deleted", getSpreadSheet(SPREADSHEET_NAME));
    	
    }
	
	 //Testar dando ao serviço um user que nao exista
    @Test(expected = LoginBubbleDocsException.class)
    public void loginUnknownUser(@Mocked final IDRemoteServices remote) throws Exception{
    	new Expectations(){
    		{
    			remote.removeUser(USERNAME); result = new LoginBubbleDocsException(USERNAME);
    			
    		}
    	};
    	
    	DeleteUserIntegrator  service = new DeleteUserIntegrator(root, USERNAME);
    	service.execute();
    }
    
  //Testar quando o serviço esta off e a copia local diferente da passada, nao faz login, lanca excecao
    @Test(expected = UnavailableServiceException.class) 
    public void removeUserServerDown(@Mocked final IDRemoteServices remote) throws Exception {
    	new Expectations(){
    		{
    			remote.removeUser(USERNAME); result = new RemoteInvocationException();
    		}
    	};
    	
    	addUserToSession(USERNAME,"Antonio Rito Silva" );
    	DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME);
    	service.execute();
    }
    
    @Test (expected = UserNotInSessionException.class)
    public void userNotInSessionMockit(@Mocked final IDRemoteServices remote) throws Exception{ 
    	removeUserFromSession(root);
    
    	DeleteUserIntegrator service = new DeleteUserIntegrator(root,USERNAME);
    	service.execute();
    }
    
    @Test (expected = UnauthorizedOperationException.class)
    public void userNotRoot(@Mocked final IDRemoteServices remote) throws Exception{
    	this.userToken= addUserToSession(USERNAME, "Antonio Rito Silva");
    	
    	DeleteUserIntegrator service = new DeleteUserIntegrator(userToken,USERNAME_TO_DELETE);
    	service.execute();
    	
    }
    
    @Test(expected = UnavailableServiceException.class)
    public void createsUserWhenServerFails (@Mocked final IDRemoteServices remote){
    	new Expectations(){
    		{
    			remote.removeUser(USERNAME); result = new RemoteInvocationException();
    		}
    	};
    	DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME);
        service.execute();
        
        Utilizador u = getUserFromUsername(USERNAME);
        assertTrue(u==null);
    }
    
    
}



