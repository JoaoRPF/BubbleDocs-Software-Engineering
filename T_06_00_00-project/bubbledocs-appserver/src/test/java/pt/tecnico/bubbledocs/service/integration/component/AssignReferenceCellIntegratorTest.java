package pt.tecnico.bubbledocs.service.integration.component;
 
 
import static org.junit.Assert.assertEquals;
import pt.tecnico.bubbledocs.service.integration.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.domain.*;

import java.util.*;
 
 
public class AssignReferenceCellIntegratorTest extends BubbleDocsServiceTest{
     
    private String jp; // the token for user jp
   // private String root; // the token for user root
    private String ab; //the token for user ab
    private String rf; //the token for user rf
 
    private static final String USERNAME = "jp";
    private static final String PASSWORD = "jp#";
   // private static final String ROOT_USERNAME = "root";
    private static final String USERNAME2 = "ab";
    private static final String PASSWORD2 = "bc";
    private static final String USERNAME3 = "rf";
    private static final String PASSWORD3 = "rf2";
    private static int row = 100;
    private static int column = 100;
    private  int docId = 0;
    private static String SPREADSHEET_NAME="FOLHA_JP_1";
    private static final String EMAIL= "antonio@gmail";
         
    @Override
    public void populate4Test() {
         
        createUser(USERNAME, PASSWORD, "João Pereira",EMAIL);
        //createUser(ROOT_USERNAME, "rootp","root user");
        createUser(USERNAME2, PASSWORD2, "Abilio Batata",EMAIL);
        createUser(USERNAME3, PASSWORD3, "Ricardo Ferreira",EMAIL);
        jp = addUserToSession("jp","João Pereira");
        //root = addUserToSession("root","root");
        ab = addUserToSession("ab","Abilio Batata");
        rf = addUserToSession("rf","Ricardo Ferreira");
        createSpreadSheet(getUserFromUsername(USERNAME), SPREADSHEET_NAME, row, column);
        this.docId=getSpreadSheet(SPREADSHEET_NAME).getIdentificador();

//        this.addPermissionToUserRead(this.getUserFromUsername(USERNAME2), SPREADSHEET_NAME);
        
    }
     
    @Test
    public void success() {  //verifica a existencia da celula referenciada
         
        AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "2;2", "=1;1");
        service.execute();
         
        Utilizador utilizador = getUserFromUsername(USERNAME);
        Set<FolhaCalculo> folhas = utilizador.getFolhacalculoSet();
        for(FolhaCalculo f: folhas){
        	int auxId = f.getIdentificador();
        	assertEquals(auxId,docId);
        	Set<Celula> celulas = f.getCelulaSet();
        	for(Celula c : celulas){
        		assertEquals(c.getLinha(),2);
        		assertEquals(c.getColuna(),2);
        		Referencia referencia =(Referencia)c.getConteudo();
        		int refLinha = referencia.getLinha();
        		int refColuna = referencia.getColuna();
        		assertEquals(refLinha,1);
        		assertEquals(refColuna,1);
        	}
        }
    }
    
    @Test
    public void success2(){ //verifica se um utilizador que nao o criador da folha, mas com autorizacao, pode escrever
    	getSpreadSheet("FOLHA_JP_1").addEscritor(getUserFromUsername(USERNAME3));
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(rf, docId, "4;4", "=3;3");
        service.execute();
        
        Utilizador utilizador = getUserFromUsername(USERNAME3);
        Set<FolhaCalculo> folhas = utilizador.getFolhacalculoSet();
        for(FolhaCalculo f: folhas){
        	int auxId = f.getIdentificador();
        	assertEquals(auxId,docId);
        	Set<Celula> celulas = f.getCelulaSet();
        	for(Celula c : celulas){
        		assertEquals(c.getLinha(),4);
        		assertEquals(c.getColuna(),4);
        		Referencia referencia =(Referencia)c.getConteudo();
        		int refLinha = referencia.getLinha();
        		int refColuna = referencia.getColuna();
        		assertEquals(refLinha,3);
        		assertEquals(refColuna,3);
        	}
        }
    }
    
    
    @Test (expected = InvalidCellIdFormat.class)
    public void invalidCellFormat(){
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "A;2", "=1;1");
    	service.execute();
    }
    
    @Test (expected = InvalidReferenceFormatException.class)
    public void invalidReferenceFormat(){
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "3;2", "=1;1D");
    	service.execute();
    }
 
    @Test(expected = CellDoesNotExistException.class)
    public void cellDoesNotExist() {
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "101;2", "=1;1");
        service.execute();
    }
    
    @Test(expected = CellDoesNotExistException.class)
    public void referencedCellDoesNotExist() {
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "2;2", "=1;200");
        service.execute();
    }
    
    @Test(expected = CellReferingToItselfException.class)
    public void sameCells(){
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "5;10", "=5;10");
    	service.execute();
    }
    
    @Test(expected = UnauthorizedOperationException.class)
    public void userWithoutPermission(){
    	AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(ab, docId, "1;10", "=15;15");
    	service.execute();
    }
    
    @Test(expected = UnknownBubbleDocsSpreadSheetException.class)
    public void invalidId() {
        AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, 20000, "5;4", "=7;10");
    	service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(jp);
        AssignReferenceCellIntegrator service = new AssignReferenceCellIntegrator(jp, docId, "5;4", "=7;10");
    	service.execute();
    }
    
    /*
     * User doesn't have permission to write in this SpreadSheet but have permission to read
     */
    @Test(expected = UnauthorizedOperationException.class)
    public void notPermitedUserRead() {
        String smf = addUserToSession(USERNAME2, "Sérgio Fernandes");
        new AssignReferenceCellIntegrator(smf, docId, "5;4", "=7;10").execute();   
    }
    
}

               
