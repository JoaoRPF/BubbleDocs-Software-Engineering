package pt.tecnico.bubbledocs.service.integration.component;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Set;

import mockit.Expectations;
import mockit.Mocked;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Celula;
import pt.tecnico.bubbledocs.domain.Conteudo;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.integration.*;



public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest{

	private String root;
	private String ars;
	private String smf;
	private byte[] bytes;
	private static final String USERNAME_TO_DELETE = "smf";
	private static final int ID_SpreadSheet = 2; //ID VALIDO
	private static final int INVALID_ID_SpreadSheet = 100; //ID INVALIDO
	private static final String SPREADSHEET_NAME = "spread"; //nome da folha
	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String EMAIL= "antonio@gmail";
	private static final String ROOT_EMAIL= "root@gmail";
	private static final String EMAIL2= "joao@hotmail";

	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private int id;
	private FolhaCalculo f;
	@Override
	public void populate4Test() {
		Utilizador root;
		Utilizador ars = createUser(USERNAME, PASSWORD, "António Rito Silva",EMAIL);
		Utilizador smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio Fernandes",EMAIL2);
		root = getUserFromUsername("root");
		if(root==null)
			root = createUser("root", "rootroot", "rootroot",ROOT_EMAIL);

		f = createSpreadSheet(ars, SPREADSHEET_NAME, 20, 20);
		Celula c3 = new Celula(5,6);
		String valor3="=ADD(2,3;4)";
		c3.setContent(valor3);
		Conteudo conteudo3=Conteudo.parseConteudo(f,valor3);
		c3.insereConteudo(conteudo3);
		conteudo3.setStringContent(valor3);
		f.addCelula(c3); //insere funcao ADD
		id = f.getIdentificador();
		this.root = addUserToSession("root", "root");
		this.ars = addUserToSession(USERNAME , "António Rito Silva");
		this.bytes = getBytes(f);
	};
	
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
	
	@Test(expected = ImportDocumentException.class)
	public void importDocExcpetion(){
		ImportDocumentIntegrator importDoc = new ImportDocumentIntegrator(this.ars, null);
		importDoc.execute();
	}
	
	@Test(expected = UnauthorizedOperationException.class)
	public void userIsNotSpreadSheetOwner(@Mocked final StoreRemoteServices remote){
		this.smf = addUserToSession(USERNAME_TO_DELETE , "Sérgio Fernandes");
		new Expectations(){
			{
				new StoreRemoteServices();
				remote.loadDocument(USERNAME_TO_DELETE, ""+f.getIdentificador()); result = bytes;
			}
		};
		ImportDocumentIntegrator importDoc = new ImportDocumentIntegrator(this.smf, "" + f.getIdentificador());
		importDoc.execute();
	}
	
	@Test
	public void sucess(@Mocked final StoreRemoteServices remote){
		
		new Expectations(){
			{
				new StoreRemoteServices();
				remote.loadDocument(USERNAME, ""+f.getIdentificador()); result = bytes;
			}
		};
		ImportDocumentIntegrator importDoc = new ImportDocumentIntegrator(this.ars, ""+f.getIdentificador());
		importDoc.execute();
		assertEquals(2, this.getUserFromUsername(USERNAME).getFolhacalculoSet().size());
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void loadDocuementUnvailableException(@Mocked final StoreRemoteServices remote) {
		//this.userToken=addUserToSession(USERNAME,"João Pereira"); 

		new Expectations(){
			{
				new StoreRemoteServices();
				remote.loadDocument(USERNAME, ""+f.getIdentificador()); result = new RemoteInvocationException();
			}
		};

		ImportDocumentIntegrator service = new ImportDocumentIntegrator(this.ars, ""+f.getIdentificador());
		service.execute();
	}
	
	@Test(expected = CannotLoadDocumentException.class)
	public void loadDocuementCanNotStoreException(@Mocked final StoreRemoteServices remote) {
		//this.userToken=addUserToSession(USERNAME,"João Pereira"); 

		new Expectations(){
			{
				new StoreRemoteServices();
				remote.loadDocument(USERNAME, ""+f.getIdentificador()); result = new CannotLoadDocumentException(f.getIdentificador());
			}
		};

		ImportDocumentIntegrator service = new ImportDocumentIntegrator(this.ars, ""+f.getIdentificador());
		service.execute();
	}
}
