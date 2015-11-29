package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.service.GetUserInfoService;


public class GetUserInfoTest extends BubbleDocsServiceTest {
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
	public void GetUserInfoServiceSuccess(){ 
		GetUserInfoService s =  new GetUserInfoService(USERNAME);
		s.execute();
		String [] info = s.getUserInfo();
		assertEquals(u.getNome(), info[0]);
		assertEquals(u.getEmail(), info[1]);

	}

	@Test(expected = UnknownBubbleDocsUserException.class)
	public void GetUserInfoServiceException() throws Exception{ 
		GetUserInfoService s =  new GetUserInfoService("USERNAME");
		s.execute();
	}

}
