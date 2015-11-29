package pt.tecnico.bubbledocs.service;

import java.util.Set;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.domain.Sessao;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidColumnInputException;
import pt.tecnico.bubbledocs.exception.InvalidRowInputException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;


// VERIFICAR ID UNICO QUANDO CRIAR FOLHA DE CALCULO

public class CreateSpreadSheet extends BubbleDocsService {
    private int sheetId;  // id of the new sheet
    private String userToken;
    private String name;
    private int rows;
    private int columns;
    private Utilizador user;


    public int getSheetId() {
        return sheetId;
    }

    public CreateSpreadSheet(String userToken, String name, int rows, int columns) {
    	this.userToken=userToken;
    	this.name=name;
    	this.rows=rows;
    	this.columns=columns;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {

    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		this.user= bd.getUtilizadorByUserToken(this.userToken);
		this.sheetId= bd.setFolha(this.rows,this.columns,this.name,user);
		bd.restartUserSession(userToken);
    }
    
}