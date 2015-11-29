package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import pt.tecnico.bubbledocs.service.integration.*;

import org.junit.Test;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.domain.*;

import java.util.*;

// add needed import declarations

public class CreateSpreadSheetIntegratorTest extends BubbleDocsServiceTest {
	private static final int NUM_LINHAS=500;
	private static final int NUM_COLUNAS=30;
	private String userToken; // Terá de ser root para criar folha ?? NAO!!!
	private static final String NOME_FOLHA = "NovaFolha";
	private static final String EMAIL= "gmail";
	
	private static final String USERNAME = "jp";
    private static final String PASSWORD = "jp#";
   
    private Utilizador u;
    private static String smf;
	
	 @Override
	 public void populate4Test() {
		 this.u=createUser(USERNAME, PASSWORD, "João Pereira",EMAIL);
		 
		 

	 }
	
	 @Test
	 public void success() { //verifica se o que foi criado esta ok
		 this.userToken=addUserToSession(USERNAME,"João Pereira"); //nao da para extrair token desta maneira
		 CreateSpreadSheetIntegrator service = new CreateSpreadSheetIntegrator(this.userToken,NOME_FOLHA,NUM_LINHAS,NUM_COLUNAS);
		 service.execute();
		 
		 BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		 FolhaCalculo folha = getSpreadSheet(NOME_FOLHA);
//		 System.out.println("o TOKEN DA FOLHA e:" + folha.getUtilizador().getSessao().getUserToken());

		 assertEquals(this.u.getUsername(), folha.getUtilizador().getUsername());
		
		 //System.out.println("o meu TOKEN e:" + userToken);
		 //System.out.println("o TOKEN DA FOLHA e:" + folha.getUtilizador().getToken());Nao FUNCIONA ASSIM!
	     assertEquals(NOME_FOLHA, folha.getNome());
	     assertEquals(NUM_LINHAS, folha.getNumLinhas());
	     assertEquals(NUM_COLUNAS,folha.getNumColunas());
		 
    }

	@Test (expected=InvalidRowInputException.class)
	public void invalidInputRow() {
		userToken=addUserToSession(USERNAME,"João Pereira");
		CreateSpreadSheetIntegrator service = new CreateSpreadSheetIntegrator(userToken,NOME_FOLHA,0,NUM_COLUNAS);
		service.execute();
	}
	
	@Test (expected=InvalidColumnInputException.class)
	public void invalidInputColumn() {
		userToken=addUserToSession(USERNAME,"João Pereira");
		CreateSpreadSheetIntegrator service = new CreateSpreadSheetIntegrator(userToken,NOME_FOLHA,NUM_LINHAS,0);
		service.execute();
	}
	
	@Test (expected=UserNotInSessionException.class)
	public void invalidUser() {
		userToken=addUserToSession(USERNAME,"João Pereira");
		//removeUserFromSession(userToken);
		CreateSpreadSheetIntegrator service = new CreateSpreadSheetIntegrator(smf,NOME_FOLHA,NUM_LINHAS,NUM_COLUNAS);
		service.execute();
	}
}
	
