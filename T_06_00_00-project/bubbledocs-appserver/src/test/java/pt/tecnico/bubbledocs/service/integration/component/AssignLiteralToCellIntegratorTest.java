package pt.tecnico.bubbledocs.service.integration.component;


import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.service.integration.*;
import org.junit.Test;

// add needed import declarations

public class AssignLiteralToCellIntegratorTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf"; //ESTE NAO ESTA EM SESSAO
    private static final String USERNAME = "ars"; //ESTE ESTA EM SESSAO e tem permissao
    private static final String PASSWORD = "ars"; 
    private   int ID_SpreadSheet=0; //ID VALIDO
    private static final int INVALID_ID_SpreadSheet = 100; //ID INVALIDO
    private static final String SPREADSHEET_NAME = "spread"; //nome da folha
    private static final String VALID_CELL_ID = "1;1";
    private static final String INVALID_CELL_ID = "0;0";
    private static final String INVALID_CELL_ID_FORMAT = "00";
    private static final String INVALID_LITERAL = "5A"; //CONSTANTE INTEIRA INVALIDA
    private static final String VALID_LITERAL = "5"; //CONSTANTE INTEIRA VALIDA
    private static final String EMAIL= "antonio@gmail";


    // the tokens for user in session
    private String ars;
    private String smf;
   

    @Override
    public void populate4Test() {
        Utilizador ars = createUser(USERNAME, PASSWORD, "António Rito Silva",EMAIL);
        Utilizador smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio Fernandes",EMAIL);
        createSpreadSheet(ars, SPREADSHEET_NAME, 20, 20);
        this.ID_SpreadSheet=getSpreadSheet(SPREADSHEET_NAME).getIdentificador();
        //Adicionar a permissao ao user 'smf' de leitura
        this.addPermissionToUserRead(smf, SPREADSHEET_NAME);
    };

    public void success() {
        AssignLiteralCellIntegrator service = new AssignLiteralCellIntegrator (ars, ID_SpreadSheet,VALID_CELL_ID,VALID_LITERAL);
        service.execute();
        String result = service.getResult();

        
        assert (!result.equals(VALID_LITERAL)) : "The wrong literal"+result; 
    }

    /*
     * All things rigth. Testing only if the service execute all rigth
     */
    @Test
    public void successToInsertLiteral() {
        this.ars = addUserToSession(USERNAME,"António Rito Silva"); //Insert user in session
        success();
    }
      
    /*
     * User doesn't have permission to write in this SpreadSheet
     */
    @Test(expected = UnauthorizedOperationException.class)
    public void notPermitedUser() {
        this.smf = addUserToSession(USERNAME_TO_DELETE, "Sérgio Fernandes");
        new AssignLiteralCellIntegrator(smf, ID_SpreadSheet, VALID_CELL_ID, VALID_LITERAL).execute();
    }
    
    /*
     * User doesn't have permission to write in this SpreadSheet but have permission to read
     */
    @Test(expected = UnauthorizedOperationException.class)
    public void notPermitedUserRead() {
        this.smf = addUserToSession(USERNAME_TO_DELETE, "Sérgio Fernandes");
        new AssignLiteralCellIntegrator(smf, ID_SpreadSheet, VALID_CELL_ID, VALID_LITERAL).execute();
    }

    /*
     * User not in session on this BubbleDocs
     */
    @Test(expected = UserNotInSessionException.class)
    public void UserNotInSession() {
        removeUserFromSession(ars);
    	new AssignLiteralCellIntegrator (ars, ID_SpreadSheet, VALID_CELL_ID, VALID_LITERAL).execute();
    }

    /*
     * SpreadSheet doesn't exist in this BubbleDocs Application
     */
    @Test(expected = UnknownBubbleDocsSpreadSheetException.class)
    public void notValidIdSpreadSheet() {
        String ars = addUserToSession(USERNAME,"António Rito Silva");        
    	new AssignLiteralCellIntegrator(ars, INVALID_ID_SpreadSheet, VALID_CELL_ID, VALID_LITERAL).execute();
    }

    /*
     * Invalid input CellId
     */
    @Test(expected = InvalidCellId.class)
    public void InvalidCellId() {
        String ars = addUserToSession(USERNAME,"António Rito Silva");        
    	new AssignLiteralCellIntegrator(ars, ID_SpreadSheet, INVALID_CELL_ID, VALID_LITERAL).execute();
    }
    
    /*
     * Invalid input CellId format
     */
    @Test(expected = InvalidCellIdFormat.class)
    public void InvalidCellIdFormat() {
    	new AssignLiteralCellIntegrator(ars, ID_SpreadSheet, INVALID_CELL_ID_FORMAT, VALID_LITERAL).execute();
    }
    
    /*
     * Invalid input literal type
     */
    @Test(expected = InvalidLiteralType.class)
    public void InvalidLiteralType() {
        String ars = addUserToSession(USERNAME,"António Rito Silva");        
    	new AssignLiteralCellIntegrator(ars, ID_SpreadSheet, VALID_CELL_ID, INVALID_LITERAL).execute();
    }
    
}
