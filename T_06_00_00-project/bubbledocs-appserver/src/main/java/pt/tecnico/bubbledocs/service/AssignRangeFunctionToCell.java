package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignRangeFunctionToCell extends BubbleDocsService{

	private String idCelula;
	private String funcaoIntervalo;
	private int idFolha;
	private String userToken;
	private String result;
	private Integer resultFinal;	
	
	public AssignRangeFunctionToCell(String idCelula, String funcaoIntervalo, int idFolha, String userToken){
		
		this.idCelula = idCelula;
		this.funcaoIntervalo = funcaoIntervalo;
		this.idFolha = idFolha;
		this.userToken = userToken;
		
	}
	
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
		
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();	
    	bd.intervalValido(this.funcaoIntervalo);
    	bd.validaIdCelula(this.idCelula);    	
    	bd.presentInSession(this.userToken); 	
    	bd.spreadSheetExists(this.idFolha);
    	bd.permitedUser(idFolha, userToken);
    	this.result=bd.insereFuncaoIntervaloSucesso(idFolha, idCelula, funcaoIntervalo, userToken);
    	this.resultFinal=bd.insereFuncaoIntervaloSucessoInteger(idFolha, idCelula, funcaoIntervalo, userToken);
	}


	public String getResult() {
		// TODO Auto-generated method stub
		
		return result;
	}


	public Integer getResultFinal() {
		return resultFinal;
	}
	

}

