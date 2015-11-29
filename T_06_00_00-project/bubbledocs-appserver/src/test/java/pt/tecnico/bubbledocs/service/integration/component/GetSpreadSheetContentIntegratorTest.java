package pt.tecnico.bubbledocs.service.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Celula;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsSpreadSheetException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.integration.GetSpreadSheetContentIntegrator;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;


public class GetSpreadSheetContentIntegratorTest extends BubbleDocsServiceTest {

	private static final int NUM_LINHAS=500;
	private static final int NUM_COLUNAS=30;
	private String userToken; 
	private String userToken2;
	private static final String NOME_FOLHA = "NovaFolha";
	private static final String EMAIL= "gmail";
	
	private static final String USERNAME = "somos Porto";
    private static final String PASSWORD = "jp#";
    private static final String USERNAME2 = "ab";
   
    private Utilizador u;
    private Utilizador u2;
    private static String smf;
    private FolhaCalculo folha;
	
	 @Override
	 public void populate4Test() {
		 this.u=createUser(USERNAME, PASSWORD, "João Pereira",EMAIL);
		 this.userToken=addUserToSession(USERNAME,"João Pereira");
		 folha = createSpreadSheet(u, NOME_FOLHA, NUM_LINHAS, NUM_COLUNAS);
		 this.u2 = createUser(USERNAME2, PASSWORD, "Ab","ab@pt");
		 this.userToken2=addUserToSession(USERNAME2,"Ab");
	 }
	 
	 @Test
	 public void success() { 
		 
		 BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		 bd.insereLiteralSucesso(folha.getIdentificador(), "500;30", "5", u.getToken());
		 
		 GetSpreadSheetContentIntegrator service = new GetSpreadSheetContentIntegrator(userToken, folha.getIdentificador());
		 service.execute();
		 
		 String[][] result = service.getMatriz();

		 assertEquals(result.length-1,NUM_LINHAS); //-1 PORQUE (0;0) nao conta
		 assertEquals(result[1].length-1,NUM_COLUNAS); //-1 PORQUE (0;0) nao conta
		 assertEquals(result[2][1],"");
		 assertEquals(result[500][30],"5");
    }
	 
	 @Test(expected = UserNotInSessionException.class)
	 public void accessUsernameNotExist() {
	     removeUserFromSession(userToken);
	     AssignReferenceCell service = new AssignReferenceCell(userToken,folha.getIdentificador(), "5;4", "=7;10");
	     service.execute();
	 }
	 
	 @Test(expected = UnknownBubbleDocsSpreadSheetException.class)
	 public void spreadSheetExists() {
	     AssignReferenceCell service = new AssignReferenceCell(userToken,2000, "5;4", "=7;10");
	     service.execute();
	 }
	 
	 @Test(expected = UnauthorizedOperationException.class)
	 public void notPermitedUser(){
		 AssignReferenceCell service = new AssignReferenceCell(userToken2,folha.getIdentificador(), "5;4", "=7;10");
	     service.execute();
	 }
}
