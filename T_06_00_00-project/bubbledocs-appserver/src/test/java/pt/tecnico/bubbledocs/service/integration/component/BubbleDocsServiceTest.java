package pt.tecnico.bubbledocs.service.integration.component;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.*;

import java.util.*;
// add needed import declarations

public class BubbleDocsServiceTest {
    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
            BubbleDocs.getInstance();
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {
    }

    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the iniital state and checking that the service has the expected behavior
    public Utilizador createUser(String username, String password, String name , String email) {
    	Utilizador u = new Utilizador(name);
    	u.setEmail(email);
    	u.setUsername(username);
    	u.setPassLocal(true);
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
    	bubble.addUtilizador(u);
    	return u;
    }

   public FolhaCalculo createSpreadSheet(Utilizador user, String name, int row, int column) {
	// add code here
	   FolhaCalculo folha = new FolhaCalculo(row,column,name, user);
	   LocalDate data = new LocalDate();
       folha.setDataCriacao(data);
       user.addFolhacalculo(folha); 
       user.addFolhacalculoLer(folha);
       user.addFolhacalculoEscrever(folha); 
       BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
       bubble.addFolhacalculo(folha);
       return folha;
    }
//
//    // returns a spreadsheet whose name is equal to name
//     
    public FolhaCalculo getSpreadSheet(String name) {
	// add code here
         BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
         Set <FolhaCalculo> folhas = bubble.getFolhacalculoSet();
         for(FolhaCalculo f : folhas){
        	 if(f.getNome().equals(name)){
        		 return f;
        	 }
         }
        return null;
    }

    // remove a user from session given its token
    void removeUserFromSession(String token) {
	// add code here
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	Set <Sessao> sessoes = bd.getSessaoSet();
    	for(Sessao s : sessoes) {
    		if(s.getUserToken().equals(token)){
    			s.delete();
    		}
    	}
    }

    
    // returns the user registered in the application whose username is equal to username
    Utilizador getUserFromUsername(String username) {
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	for (Utilizador u : bd.getUtilizadorSet()){
       		if (username.equals(u.getUsername())){
       			return u;
       		}
       	}
       	return null;
    }
    
    // put a user into session and returns the token associated to it
    String addUserToSession(String username, String nome) {
        ///FALTA POR O SET A PASSWORD, TANTO A DO DML COMO A LOCAL....

    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	Utilizador ut = bd.getUtilizadorByUsername(username);
    	ut.setPass(nome);
    	ut.setPassword(nome);  
    	if(ut!=null){
    		DateTime date = new DateTime();
    		Sessao s = new Sessao(username,username,date);
    		s.setUtilizador(ut);
    		ut.generateToken();
    		s.setUserToken(ut.getToken());
    		s.setUserName(username);
    		bd.addSessao(s);
    		return s.getUserToken();
    	}
    	return null;
    }
    
    // return the user registered in session whose token is equal to token
    Utilizador getUserFromSession(String token) {
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
    	Set<Sessao> sessoes = bubble.getSessaoSet();
    	for(Sessao s : sessoes){
    		if(s.getUserToken().equals(token)){
    			return s.getUtilizador();
    		}
    	}
    	return null;
    }
    
    // add a permission to user to read some spreadsheet
    void addPermissionToUserRead(Utilizador u, String spreadSheetName) {
	// add code here
    	FolhaCalculo f = this.getSpreadSheet(spreadSheetName);
    	u.addFolhacalculoLer(f);
    }
    
    //return the user with that token
    Utilizador getUserFromUserToken(String userToken){
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	for (Utilizador u : bd.getUtilizadorSet()){
    		if(userToken.equals(u.getToken())){
    			return u;
    		}
    	}
    	return null;
    }
    
}
