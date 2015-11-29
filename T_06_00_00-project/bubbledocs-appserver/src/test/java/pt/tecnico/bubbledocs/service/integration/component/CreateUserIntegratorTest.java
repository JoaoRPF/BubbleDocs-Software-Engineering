package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegratorTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String USERNAME2= "brs";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_NOT_EXISTS = "diamantino";
    
    private static final String EMAIL= "antonio@gmail";
    private static final String EMAIL2= "joao@hotmail";
    private static final String ROOT_EMAIL= "root@gmail";
    private static final String WRONG_EMAIL="mail";

    private static final String PASSWORD = "ars";
    
    private static final String NAME="António Rito Silva";

    @Override
    public void populate4Test() {
        Utilizador rootUser;
    	createUser(USERNAME, PASSWORD, "António Rito Silva",EMAIL);
        rootUser = getUserFromUsername("root");   //extrai utilizador
        if(rootUser==null)
        	rootUser= createUser(ROOT_USERNAME, "rootroot","root",ROOT_EMAIL);
        this.root = addUserToSession("root","root"); //token 
        ars = addUserToSession("ars", "António Rito Silva"); //token
    }

    @Test
    public void success(@Mocked final IDRemoteServices remote) {
        
    	new Expectations(){
    		{
    			remote.createUser(USERNAME2, EMAIL2);
    		}
    	};
    	CreateUserIntegrator service = new CreateUserIntegrator(root,USERNAME2,NAME,EMAIL2);
    	service.execute();
 
    	Utilizador u= getUserFromUsername(USERNAME2);
   
    	assertFalse(u==null);
    	assertEquals(USERNAME2,u.getUsername());
    	assertEquals(EMAIL2,u.getEmail());
    	assertEquals(NAME,u.getNome());
    }
    
    @Test(expected = UnavailableServiceException.class)
    public void removesUserWhenServerFails (@Mocked final IDRemoteServices remote){
    	new Expectations(){
    		{
    			remote.createUser(USERNAME2, EMAIL2); result = new RemoteInvocationException();
    		}
    	};
    	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME2, "jose", EMAIL2);
        service.execute();
        
        Utilizador u = getUserFromUsername(USERNAME2);
        assertTrue(u==null);
    }

    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists(@Mocked final IDRemoteServices remote) {
    	
    	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME, "jose", EMAIL2);
        service.execute();
       
    }

    @Test(expected = EmptyUsernameException.class)     
    public void emptyUsername(@Mocked final IDRemoteServices remote) throws Exception {
    	CreateUserIntegrator service = new CreateUserIntegrator(root, "", "jose", EMAIL2);
        service.execute();
    }

    @Test(expected = UnavailableServiceException.class)
    public void unauthorizedUserCreation(@Mocked final IDRemoteServices remote) {
    	new Expectations(){
    		{
    			remote.createUser(USERNAME2, EMAIL2); result = new RemoteInvocationException();
    		}
    	};
    	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME2, "jose", EMAIL2);
        service.execute();
    }

   @Test(expected = UserNotInSessionException.class) 
    public void accessUsernameNotExist(@Mocked final IDRemoteServices remote) throws Exception {
	   removeUserFromSession(root);
       CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME2, "jose",EMAIL2);
       service.execute();
    }
   
   @Test(expected = DuplicateEmailException.class)
   public void emailExists(@Mocked final IDRemoteServices remote) {
	   
	   CreateUserIntegrator service = new CreateUserIntegrator(root, "AAA", "jose", EMAIL);
	   service.execute();
	  
   }
   
   @Test(expected = InvalidEmailException.class)
   public void invalidEmail(@Mocked final IDRemoteServices remote) {
	   new Expectations(){
  		{
  			remote.createUser(USERNAME2, WRONG_EMAIL); result = new InvalidEmailException(WRONG_EMAIL);
  		}
  	};
      
      	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME2, "jose", WRONG_EMAIL);
      	service.execute();
   
   }
   @Test(expected = InvalidUsernameException.class)
   public void invalidUsername(@Mocked final IDRemoteServices remote) {
	   new Expectations(){
		   {
			   remote.createUser(USERNAME_NOT_EXISTS, EMAIL2); result = new InvalidUsernameException(EMAIL2);
		   }
	   };
      	
       CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_NOT_EXISTS, "jose", EMAIL2);
       service.execute();
   }
   
   
   @Test(expected = UnauthorizedOperationException.class)
   public void notRoot(@Mocked final IDRemoteServices remote) throws Exception {
	   CreateUserIntegrator service = new CreateUserIntegrator(ars, USERNAME2, "jose", EMAIL2);
	   service.execute();
   }
   
    
}
