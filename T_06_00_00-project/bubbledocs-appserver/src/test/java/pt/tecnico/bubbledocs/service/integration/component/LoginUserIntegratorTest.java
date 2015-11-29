package pt.tecnico.bubbledocs.service.integration.component;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import pt.tecnico.bubbledocs.service.integration.*;

import org.junit.Test;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.domain.*;

import java.util.*;

import mockit.*;


// add needed import declarations

public class LoginUserIntegratorTest extends BubbleDocsServiceTest {


    private static final String USERNAME = "jp";
    private static final String USERNAME2 = "jp2";

    private static final String PASSWORD = "jp#";
    private static final String PASSWORD2 = "jp2";
    
    private static final String EMAIL= "antonio@gmail";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "João Pereira",EMAIL); // so o login e q recebe a password, falta aqui o email...
    }

    // returns the time of the last access for the user with token userToken.
    // It must get this data from the session object of the application
    private LocalTime getLastAccessTimeInSession(String userToken) {
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
    	Set<Sessao> sessoes = bubble.getSessaoSet();
    	for(Sessao s : sessoes){
    		if(s.getUserToken().equals(userToken)){
    			return s.getDuracao().toLocalTime();
    		}
    	}
    	return null;
    }
    //Testar se o LoginUser foi executado com sucesso alterando a copia local da password
    @Test
    public void success(@Mocked final IDRemoteServices remote) { 	
    	new Expectations(){
    		{
    			remote.loginUser(USERNAME, PASSWORD);
    		}
    	};
    	
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
		LocalTime currentTime = new LocalTime();
		Utilizador user = getUserFromSession(service.getUserToken());
        assertEquals(PASSWORD, user.getPass());
        assertEquals(PASSWORD, user.getPassword());
		assertEquals(USERNAME, user.getUsername());
        int difference = Seconds.secondsBetween(getLastAccessTimeInSession(service.getUserToken()), currentTime).getSeconds();
        assertTrue("Access time in session not correctly set", difference >= 0);
        assertTrue("diference in seconds greater than expected", difference < 2);
    }
    
    //Testar dois LoginUser em que a copia local fica com o valor do último login
    @Test
    public void successLoginTwice(@Mocked final IDRemoteServices remote) {
    	new Expectations(){
    		{
    			remote.loginUser(USERNAME, PASSWORD);
    		}
    	};
    	
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
        String token1 = service.getUserToken();

        service.execute();
        String token2 = service.getUserToken();

        Utilizador user = getUserFromSession(token1);
        assert(user==null);  //apagou a sessao anterior
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPass());
        assertEquals(PASSWORD, user.getPassword());
    }
    //Testar dando ao serviço um user que nao exista
    @Test(expected = LoginBubbleDocsException.class)
    public void loginUnknownUser(@Mocked final IDRemoteServices remote) throws Exception{
    	new Expectations(){
    		{
    			remote.loginUser(USERNAME2, PASSWORD); result = new LoginBubbleDocsException(USERNAME2);
    		}
    	};
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME2, PASSWORD);
        service.execute();
    }
    //Testar quando o serviço esta off e a copia local diferente da passada, nao faz login, lanca excecao
    @Test(expected = UnavailableServiceException.class) 
    public void loginUserWithCertoPassword(@Mocked final IDRemoteServices remote) throws Exception {
    	new Expectations(){
    		{
    			remote.loginUser(USERNAME, PASSWORD2); result = new RemoteInvocationException();
    		}
    	};
    	addUserToSession(USERNAME, PASSWORD);
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD2);
        service.execute();
        
    }
    
    //Testar quando o serviço esta off e a copia local igual da passada,  faz login
    @Test
    public void loginUserWithinWrongPassword(@Mocked final IDRemoteServices remote) throws Exception {
    	new Expectations(){
    		{
    			remote.loginUser(USERNAME, PASSWORD); result = new RemoteInvocationException();
    		}
    	};
    	addUserToSession(USERNAME, PASSWORD);
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
        LocalTime currentTime = new LocalTime();
		Utilizador user = getUserFromSession(service.getUserToken());
        assertEquals(PASSWORD, user.getPass());
        assertEquals(PASSWORD, user.getPassword());
		assertEquals(USERNAME, user.getUsername());
        int difference = Seconds.secondsBetween(getLastAccessTimeInSession(service.getUserToken()), currentTime).getSeconds();
        assertTrue("Access time in session not correctly set", difference >= 0);
        assertTrue("diference in seconds greater than expected", difference < 2);
        
    }
}
