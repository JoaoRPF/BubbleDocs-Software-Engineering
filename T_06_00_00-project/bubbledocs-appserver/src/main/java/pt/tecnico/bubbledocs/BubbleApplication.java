package pt.tecnico.bubbledocs;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Sessao;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.domain.Celula;
import pt.tecnico.bubbledocs.domain.Conteudo;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.service.*;

import javax.transaction.*;

public class BubbleApplication {

    public static void main(String[] args) {
   	System.out.println("Welcome to the Bubble application!");

	TransactionManager tm = FenixFramework.getTransactionManager();
    	boolean committed = false;
	    String userToken = null;
	    byte[] docXML = null;
	    
	    ArrayList <byte[]> byteVetor=new ArrayList<byte[]>();


   	try {
	    tm.begin();
	    BubbleDocs bd = BubbleDocs.getInstance();
	    populateDomain(bd);
	    //-----------------------MAIN COM SERVICOS---------------------------------------------
	    int tem=0;
	    Set <Utilizador> utilizador = bd.getUtilizadorSet();
	    for (Utilizador utilizador1 : utilizador) {
	    	System.out.println("The real name of the Username: "+utilizador1.getUsername()+ " is: "+utilizador1.getNome()+ " and the password: "+utilizador1.getPassword());
		    Set <FolhaCalculo> folhas = utilizador1.getFolhacalculoSet();
	    	for(FolhaCalculo f : folhas){
	    		if(f!=null)
	    			System.out.println("SpreadSheet Name: " +  f.getNome() + " ID: " + f.getIdentificador());
	    			tem=1;
	    	}
	    	if(tem==0)
    			System.out.println("Username "+ utilizador1.getUsername()+ " don't have any spreadsheet");
	    }
	    
	   // org.jdom2.Document doc= new Document();
	    Utilizador u = acedeUtilizador ("pf");
	    Set <Sessao> sessao= bd.getSessaoSet();
    	for(Sessao s : sessao){
    		if(s.getUserName().equals(u.getUsername())){
    			userToken=s.getUserToken();
    		}
    	}
	    for (FolhaCalculo fc : u.getFolhacalculoSet()){
	    		docXML= ExportDocument(userToken,fc.getIdentificador());
	    		byteVetor.add(docXML);
	    		System.out.println("Converting ... 100%");
	    		printDomainInXML(docXML);
	    }
	    
	    
	    Set <Utilizador> utilizadores = bd.getUtilizadorSet();
	    for(Utilizador user : utilizadores){
	    	if(user.getUsername().equals("pf")){
	        	List<FolhaCalculo> folhasUser = user.getDocumentsCreatedByUser("Notas ES", user);
	        	for(FolhaCalculo folha : folhasUser ){
	        		folha.delete();
	        		
	        	}
	        	Set <FolhaCalculo> folhaux= user.getFolhacalculoSet();
	        	if(folhaux.isEmpty())
		        	System.out.println("O Username " + user.getUsername()+ " não tem folhas");
	        	else{
	        		for(FolhaCalculo folhaa : folhaux) {
	        			System.out.println("O Username: "+ user.getUsername()+" have this spreadsheet: "+ folhaa.getNome()+" Id: "+folhaa.getIdentificador());
	        		}
	        	}
	    	}	
	    }
	    
	    
	    for(byte[] b : byteVetor){
	    	FolhaCalculo f2= recoverFromBackup(b);
	    	System.out.println("Importing ... 100%");
	    }
	    
	    Set <Utilizador> utilizadores1 = bd.getUtilizadorSet();
	    for(Utilizador utilizadoress : utilizadores1){
	    	if(utilizadoress.getUsername().equals("pf")){
	        	Set <FolhaCalculo> folhaux= utilizadoress.getFolhacalculoSet();
	        	if(folhaux.isEmpty())
		        	System.out.println("O Username " + utilizadoress.getUsername()+ " não tem folhas");
	        	else{
	        		for(FolhaCalculo folhaa : folhaux) {
	        			System.out.println("O Username: "+ utilizadoress.getUsername()+ " have this spreadsheet: "+folhaa.getNome()+ " Id: "+ folhaa.getIdentificador());
	        		}
	        	}
	    	}
	    }
	    
	    Utilizador u2 = acedeUtilizador ("pf");
	    Set <Sessao> sessao1= bd.getSessaoSet();
    	for(Sessao s : sessao1){
    		if(s.getUserName().equals(u.getUsername())){
    			userToken=s.getUserToken();
    		}
    	}
	    for (FolhaCalculo fc : u2.getFolhacalculoSet()){
	    	docXML = ExportDocument(userToken,fc.getIdentificador());
		    System.out.println("Converting ... 100%");
	    	printDomainInXML(docXML);
	    }
	    //-----------------------------------------------------------------------------

	    tm.commit();
	    committed = true;

	}catch (SystemException| NotSupportedException | RollbackException| HeuristicMixedException | HeuristicRollbackException ex) {
		System.err.println("Error in execution of transaction: " + ex);
	    System.out.println(ex.getCause());

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
	    if (!committed) 
		try {
		    tm.rollback();
		} catch (SystemException ex) {
		    System.err.println("Error in roll back of transaction: " + ex);
		    System.out.println(ex.getCause());
		}
    	}
  }

    @Atomic
    private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
        BubbleDocs bd = BubbleDocs.getInstance();
        
        bd.importFromXML(jdomDoc.getRootElement());
    }

    
//-------------------------------------------SERVICES-------------------------------------------------------------------------
    
    //LOGIN USER
    public static void LoginUser (String username, String password){
    	LoginUser loginUser = new LoginUser (username,password);
    	loginUser.execute();
    }
    
    //CREATE A USER
    private static void CreateUser (String token,String personName,String password, String nome) {
            CreateUser createPerson = new CreateUser (token,personName,password,nome);
            createPerson.execute();
    }
    
    //CREATE A SPREADSHEET
    public static void CreateSpreadSheet(String userToken, String name, int rows,
            int columns) {
    	CreateSpreadSheet createSpreadSheet = new CreateSpreadSheet (userToken,name,rows,columns);
        createSpreadSheet.execute();
    }
    
    //INSERIR LITERAL
    public static void AssignLiteralCell(String accessUsername, int docId, String cellId, String literal){
    	AssignLiteralCell assignLiteralCell = new AssignLiteralCell (accessUsername,docId,cellId,literal);
    	assignLiteralCell.execute();
    }
    
    //INSERIR REFERENCIA
    public static void AssignReferenceCell(String tokenUser, int docId, String cellId, String reference){
    	AssignReferenceCell assignReferenceCell = new AssignReferenceCell( tokenUser,  docId,  cellId,  reference);
    	assignReferenceCell.execute();
    }
    
    //EXPORT SPREADSHEET
    public static byte[] ExportDocument(String userToken, int docId){
    	ExportDocument exportDocument = new ExportDocument( userToken,  docId);
    	exportDocument.execute();
    	return exportDocument.getDocXML();
    	
    }
    
    //DELETE A USER
    public static void DeleteUser(String userToken, String toDeleteUsername) {
    	DeleteUser deleteUser = new DeleteUser(userToken,  toDeleteUsername);
    	deleteUser.execute();
    }
    
    
    //-----------------------------------------------------------------------------------------------------------------
    
    public static void populateDomain(BubbleDocs bd) {
	if (!bd.getUtilizadorSet().isEmpty())
	    return;



	//------------------------------------''-----------------------------------------

  	Utilizador utilizador3 = new Utilizador("root");
   	utilizador3.setUsername("root");
   	utilizador3.setPassword("rootroot"); 
   	bd.addUtilizador(utilizador3);
  	LoginUser("root", "rootroot");
  	
  	Set <Sessao> sessoes = bd.getSessaoSet();
  	for(Sessao s :sessoes){
  		if(s.getUserName().equals("root")){
  			CreateUser(s.getUserToken(),"pf","sub","Paul Door");
  			CreateUser(s.getUserToken(),"ra","cor","Step Rabbit");
  		}
  	}
  	
  	LoginUser("pf", "sub");
  	
  	Set <Sessao> sessao = bd.getSessaoSet();
  	for(Sessao s :sessao){
  		if(s.getUserName().equals("pf")){
  			CreateSpreadSheet(s.getUserToken(), "Notas ES", 300,20); 
  			AssignLiteralCell(s.getUserToken(), 1, "3;4", "5");
  			AssignReferenceCell(s.getUserToken(), 1, "2;1", "=5;6");
  		}
  	}
  	
  	Set<FolhaCalculo> folhas = bd.getFolhacalculoSet();
  	for(FolhaCalculo f : folhas){
  		if(f.getIdentificador()==1){
  			 Celula c3 = new Celula(5,6);
  			 String valor3="=ADD(2,3;4)";
  			 c3.setContent(valor3);
  			 Conteudo conteudo3=Conteudo.parseConteudo(f,valor3);
  			 c3.insereConteudo(conteudo3);
  			 conteudo3.setStringContent(valor3);
  			 f.addCelula(c3); //insere funcao ADD
  			 Celula c4 = new Celula(2,2);
  			 String valor4="=DIV(1;1,3;4)";
  			 c4.setContent(valor4);
  			 Conteudo conteudo4=Conteudo.parseConteudo(f,valor4);
  			 c4.insereConteudo(conteudo4);
  			 conteudo4.setStringContent(valor4);
  			 f.addCelula(c4); //insere funcao DIV
  		}
  	}


    
 
    }
	//-------------------------------------''----------------------------------------
    
//    @Atomic
//    public static void convertToXML(FolhaCalculo fc, String userToken) {
//	
//	//org.jdom2.Document jdomDoc = new org.jdom2.Document();
//	//jdomDoc.setRootElement(fc.exportToXML());
//
//	//return jdomDoc;    
//	}

    @Atomic
    public static void printDomainInXML(org.jdom2.Document jdomDoc) {
	XMLOutputter xml = new XMLOutputter();
	xml.setFormat(Format.getPrettyFormat());
	System.out.println(xml.outputString(jdomDoc));
    }
    
    private static FolhaCalculo recoverFromBackup(byte[] doc) {
        try {
            //ImportPhoneBookService importService = new ImportPhoneBookService(doc);
            //importService.execute();
            org.jdom2.Document jdomDoc;

            SAXBuilder builder = new SAXBuilder();
            builder.setIgnoringElementContentWhitespace(true);
            try {
                jdomDoc = builder.build(new ByteArrayInputStream(doc));
            } catch (JDOMException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception();
            }

            Element rootElement = jdomDoc.getRootElement();
            FolhaCalculo f = new FolhaCalculo();
            f.importFromXML(rootElement);
            return f;
        } catch (Exception e) {
            System.err.println("Error importing document");
        }
        return null;
    }
    
    public static void printDomainInXML(byte[] doc) throws Exception {
        if (doc == null) {
            System.err.println("Null Document to print");
            return;
        }

        org.jdom2.Document jdomDoc;
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);
        try {
            jdomDoc = builder.build(new ByteArrayInputStream(doc));
        } catch (JDOMException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception();
        }

        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());
        System.out.println(xml.outputString(jdomDoc));
    }
    
    
    public static Utilizador acedeUtilizador (String username) throws UserNaoExisteException{ //Regra Negocio 1.2
    	BubbleDocs bd = BubbleDocs.getInstance();
       	for (Utilizador u : bd.getUtilizadorSet()){
       		if (username.equals(u.getUsername())){
       			return u;
       		}
       	}
       	throw new UserNaoExisteException(username);
    }
    
}
