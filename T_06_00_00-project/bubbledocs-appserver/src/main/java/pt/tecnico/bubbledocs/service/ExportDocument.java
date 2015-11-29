package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadSheetIdException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;



import java.io.UnsupportedEncodingException;


import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;




public class ExportDocument extends BubbleDocsService {
private byte[] docXML;
    


	private int docID;
	private String token;
	//private org.jdom2.Document doc;
    

    public byte[] getDocXML() {
    	return docXML;
    }

    public ExportDocument(String userToken, int docId) {

    	this.token = userToken;
    	docID = docId;
    }
    	
    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
        	//falta lançar expepcoes, e verificar a merda dos testes
    	if(bubble.checkDocId(this.docID)==false){
    		throw new InvalidSpreadSheetIdException(this.docID);
    	}
    	
    	if(bubble.getSessao(this.token) == false){
    		throw new UserNotInSessionException(this.token);
    	}
    	
		if(bubble.CheckAuthor(this.token, this.docID)==false && bubble.CheckEscrita(this.token, this.docID)==false && bubble.CheckLeitura(this.token, this.docID)==false){
		
    			//System.out.println("Não é autor");
    			throw new UnauthorizedOperationException(this.token);
    	}

    	FolhaCalculo folha = bubble.getFolhaById(this.docID);
    	
    	org.jdom2.Document jdomDoc = new org.jdom2.Document();
    	jdomDoc.setRootElement(folha.exportToXML());
    	XMLOutputter xml = new XMLOutputter();
    	try{
        this.docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
    	 } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
             throw new ExportDocumentException(folha.getIdentificador());
         }
        bubble.restartUserSession(token);
    }
   
} 
    
    
    
    
    
    
    
    

    