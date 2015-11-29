package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.domain.*;

import java.util.*;

public class GetUsername4TokenServiceTest extends BubbleDocsServiceTest {
	 
	 private String username;
	 private String userTokenAntonio;
	 private static final String USERTOKEN2="bernardo";
	 private Utilizador user;
	 
	 @Override
	 public void populate4Test() {
		
		 this.user=createUser("ars","ars","Antonio Rito Silva","antonio@gmail.com");
		 //this.user=createUser("brs","brs","Bernardo Rito Silva","bernardo@gmail.com");
		
	 }

	 @Test
	 public void success() {
		 this.userTokenAntonio=addUserToSession("ars","Antonio Rito Silva"); 
		 
		 GetUsername4TokenService service = new GetUsername4TokenService(this.userTokenAntonio);
		 service.execute();
		 this.username=service.getUsername();
		 
		 assertEquals(this.username, user.getUsername());
		 
	 }
	 
	 @Test (expected=UserNotInSessionException.class)
		public void invalidUser() {
			userTokenAntonio=addUserToSession("ars","Antonio");
			GetUsername4TokenService service = new GetUsername4TokenService(USERTOKEN2);
			service.execute();
		}
	 
}
