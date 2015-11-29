package pt.tecnico.bubbledocs.service.integration.system;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import mockit.Expectations;
import mockit.Mocked;

import org.jdom2.output.XMLOutputter;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.service.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.AssignReferenceCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.service.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.service.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.integration.GetSpreadSheetContentIntegrator;
import pt.tecnico.bubbledocs.service.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.integration.component.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;


/*Passos:
 * - Criação de dois utilizadores com o root - DONE
 * - Logar os dois user's  - DONE
 * - Criar Três folhas para o primeiro e uma para o segundo - DONE
 * - Inserir três literais e uma referencia   numa das folhas de cada user e um literal numa das duas folhas que sobram do user 1 -  DONE
 * - Inserir uma funcao binaria e uma de intervalo em cada uma dessas duas folhas - 
 * - Ver o conteudo das duas folhas com conteudos -  DONE
 * - Exportar a folha do user1 que tem apenas um literal  - DONE
 * - Apagar a folha exportada - DONE
 * - e voltar a importar para o mesmo user 1 - DONE
 * - Obter o conteudo da folha importada mas para o user 1  - DONE
 * - Renovar a password de um user - DONE
 * - Apagar o segundo user - DONE
 */


/*Assert's:
*  - 
 */



public class LocalSystemTest extends BubbleDocsServiceTest{  //extender?? para criar o root e preciso
	//Final and global Test
    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "rootroot";
    private static final String User1 = "Sport Lisboa e Benfica";
    private static final String Username1 = "SLB";
    private static final String User2 = "Rumo ao 34";
    private static final String Username2 = "34";
    private static final String PASSWORD1 = "34";
    private static final String PASSWORD2 = "slb";
    private static final String FOLHA1 = "Notas Es";
    private static final String FOLHA2 = "Notas ESEMB";
    private static final String FOLHA3 = "Notas SCOM";
	private byte[] bytes;


    
    @Test
    public void success(@Mocked final IDRemoteServices remote,@Mocked final StoreRemoteServices remote2 ) { 	
    	new Expectations(){
    		{
    			remote.loginUser(ROOT_USERNAME, ROOT_PASSWORD);
    			remote.createUser(Username1, "slb@gmail.com");
    			remote.createUser(Username2, "34@gmail.com");
    			remote.renewPassword(Username1); 
    			remote.removeUser(Username2); 


    		}
    	};
    	
    	createUser(ROOT_USERNAME,ROOT_USERNAME,ROOT_USERNAME,"root@gmail.com");  //criar o root      
        LoginUserIntegrator service_1 = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD); //login do root
    	service_1.execute();
    	String rootToken =service_1.getUserToken(); //token do root
    	CreateUserIntegrator service_2= new CreateUserIntegrator(rootToken,Username1, User1, "slb@gmail.com"); //criacao do 1 user
    	service_2.execute();
    	CreateUserIntegrator service_3= new CreateUserIntegrator(rootToken,Username2, User2, "34@gmail.com"); //criacao do 2 user
    	service_3.execute();
    	
    	LoginUserIntegrator service_4 = new LoginUserIntegrator(Username1, PASSWORD1); //login do 1 user
       	service_4.execute();
       	String user1Token = service_4.getUserToken(); //token do 1 user
       	LoginUserIntegrator service_5 = new LoginUserIntegrator(Username2, PASSWORD2); //login do 2 user
       	service_5.execute();
       	String user2Token = service_5.getUserToken(); //token do 2 user
       	
       	CreateSpreadSheetIntegrator service_6 = new CreateSpreadSheetIntegrator(user1Token,FOLHA1,100,100); //1 folha do user 1
		service_6.execute();
		int idFolha_1=service_6.getSheetId(); //id da 1 folha do user 1

		CreateSpreadSheetIntegrator service_7 = new CreateSpreadSheetIntegrator(user1Token,FOLHA2,100,100); //2 folha do user 1
		service_7.execute();
		int idFolha_2=service_7.getSheetId(); //id da 2 folha do user 1

		CreateSpreadSheetIntegrator service_8 = new CreateSpreadSheetIntegrator(user1Token,FOLHA3,100,100); //3 folha do user 1
		service_8.execute();
		int idFolha_3=service_8.getSheetId(); //id da 3 folha do user 1

		CreateSpreadSheetIntegrator service_9 = new CreateSpreadSheetIntegrator(user2Token,FOLHA1,100,100); //1 folha do user 2
		service_9.execute();
		int idFolha_12=service_9.getSheetId(); //id da 1 folha do user 2
		
        new AssignLiteralCellIntegrator(user1Token, idFolha_1, "1;1", "1").execute(); //literal com valor 1 na folha 1 do user 1
        new AssignLiteralCellIntegrator(user1Token, idFolha_2, "1;1", "1").execute(); //literal com valor 1 na folha 2 do user 1
        new AssignLiteralCellIntegrator(user1Token, idFolha_1, "1;2", "2").execute(); //literal com valor 2 na folha 1 do user 1
        new AssignLiteralCellIntegrator(user1Token, idFolha_1, "1;3", "1").execute(); //literal com valor 1 na folha 1 do user 1
        new AssignReferenceCellIntegrator(user1Token, idFolha_1, "1;4", "=1;1").execute(); //referencia com valor 1;1 na folha 1 do user 1
        
        new AssignLiteralCellIntegrator(user2Token, idFolha_12, "1;1", "1").execute(); //literal com valor 1 na folha 1 do user 2
        new AssignLiteralCellIntegrator(user2Token, idFolha_12, "1;2", "2").execute(); //literal com valor 2 na folha 1 do user 2
        new AssignLiteralCellIntegrator(user2Token, idFolha_12, "1;3", "1").execute(); //literal com valor 1 na folha 1 do user 2
        new AssignReferenceCellIntegrator(user2Token, idFolha_12, "2;1", "=1;1").execute(); //referencia com valor 1;1 na folha 1 do user 2
        
        /* inserir funcao binaria e de intervalo em cada uma das folhas*/
        
        FolhaCalculo f=getSpreadSheet(FOLHA2);
		this.bytes = getBytes(f); //transformar a folha 2 do user 1 em bytes
    	new Expectations(){
    		{
				remote2.storeDocument(Username1, FOLHA2, bytes); 
				remote2.loadDocument(Username1, ""+idFolha_2); result = bytes;


    		}
    	};
        GetSpreadSheetContentIntegrator service_10 = new GetSpreadSheetContentIntegrator(user1Token, idFolha_1); //tirar o conteudo da primeira folha do user 1
		service_10.execute();
		String[][] matriz_1 = service_10.getMatriz();
		GetSpreadSheetContentIntegrator service_11 = new GetSpreadSheetContentIntegrator(user2Token, idFolha_12); //tirar o conteudo da primeira folha do user 2
		service_11.execute();
		String[][] matriz_2 = service_11.getMatriz();	
		
		ExportDocumentIntegrator service_12 = new ExportDocumentIntegrator(user1Token,idFolha_2); //exportar a folha 2, tem apenas um literal do user 1
		service_12.execute();
		
		deleteFolhaCalculo(FOLHA2); //apagar a folha que exportei
		
		ImportDocumentIntegrator importDoc = new ImportDocumentIntegrator(user1Token, ""+idFolha_2); //importar a folha 2 do user 1 para o user 1
		importDoc.execute();
		
        FolhaCalculo f2=getSpreadSheet(FOLHA2);        
        GetSpreadSheetContentIntegrator service_13 = new GetSpreadSheetContentIntegrator(user1Token, f2.getIdentificador()); //tirar o conteudo da primeira folha do user 1 que foi importada
		service_13.execute();
		
		RenewPasswordIntegrator service_14 = new RenewPasswordIntegrator(user1Token); //renovar a pass do user 1
    	service_14.execute();
	
    	DeleteUserIntegrator service_15 = new DeleteUserIntegrator(rootToken,Username2); // apagar o user 2
    	service_15.execute();
    	
    }

 // -----------------------------------------------METODOS AUXILIARES ------------------------------------------------------------------------
    public byte[] getBytes(FolhaCalculo f){
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		jdomDoc.setRootElement(f.exportToXML());
		XMLOutputter xml = new XMLOutputter();
		try {
			return xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    public void deleteFolhaCalculo (String name){
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
	    Set <Utilizador> utilizadores = bd.getUtilizadorSet();
	    for(Utilizador user : utilizadores){
	    	if(user.getUsername().equals(Username1)){
	        	List<FolhaCalculo> folhasUser = user.getDocumentsCreatedByUser(FOLHA2, user);
	        	for(FolhaCalculo folha : folhasUser ){
	        		folha.delete();
	        		
	        	}
	    	}
	    }
    }
    // -----------------------------------------------METODOS AUXILIARES ------------------------------------------------------------------------

}

