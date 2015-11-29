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
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.integration.*;



public class ExportDocumentIntegratorTest extends BubbleDocsServiceTest{

	private String root;
	private String ars;
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
		this.bytes = this.getBytes(f);
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

	public static void printDomainInXML(byte[] doc) throws Exception {
		if (doc == null) {
			throw new Exception();
		}

		org.jdom2.Document jdomDoc;
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			jdomDoc = builder.build(new ByteArrayInputStream(doc));
			//System.out.println(jdomDoc);

		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
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

	public static Document getDocXML(byte[] doc) throws Exception {
		if (doc == null) {
			throw new Exception();
		}

		org.jdom2.Document jdomDoc;
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			jdomDoc = builder.build(new ByteArrayInputStream(doc));
			XMLOutputter xml = new XMLOutputter();
			xml.setFormat(Format.getPrettyFormat());
			//System.out.println(xml.outputString(jdomDoc));
			return jdomDoc;
			//System.out.println(jdomDoc);

		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}

	}


	public void success() {
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ars, f.getIdentificador());
		service.execute();
		try {
			//printDomainInXML(service.getDocXML());
			FolhaCalculo f2 = ExportDocumentIntegratorTest.recoverFromBackup(service.getDocXML());

			assertEquals(f.getNumColunas() , f2.getNumColunas());
			assertEquals(f.getNumLinhas() , f2.getNumLinhas());
			assertEquals(f.getNome() , f2.getNome());
			assertEquals(f.getUtilizador(), f2.getUtilizador());
			assertEquals(f.getDataCriacao(), f2.getDataCriacao());    			
			Set<Celula> celulas = f.getCelulaSet();
			Set<Celula> celulas2 = f2.getCelulaSet();
			assertEquals(celulas.size(),celulas2.size());
			for(Celula c : celulas){
				for(Celula c2 : celulas2){
					if(c.getLinha() == c2.getLinha() && c.getColuna() == c2.getColuna()){
						assertEquals(c.getConteudo().getClass() , c2.getConteudo().getClass());
						assertEquals(c.getConteudo().getContent() , c2.getConteudo().getContent());

					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		//service.execute();
	}

	/*
	 * All things right. Testing only if the service execute all right
	 */
	@Test
	public void successToExportDocument() {
		//this.ars = addUserToSession(USERNAME,"António Rito Silva");
		success();
	}

	/*
	 * User not in session.
	 */

	@Test(expected = UserNotInSessionException.class)
	public void notInSession() {

		removeUserFromSession(root);
		new ExportDocumentIntegrator(root,id).execute();

	}

	/*
	 * User doesn't have permission to export this SpreadSheet
	 */
	@Test(expected = UnauthorizedOperationException.class)
	public void notPermitedExport() {
		String smf = addUserToSession(USERNAME_TO_DELETE, "Sérgio Fernandes");
		new ExportDocumentIntegrator(smf, id).execute();
		//service.execute();
	}

	/*
	 * DocID not Existent
	 */

	@Test(expected = RuntimeException.class) //criar excepcao certa...
	public void docIDNotExistent() {
		new ExportDocumentIntegrator(ars,INVALID_ID_SpreadSheet).execute();

	}

	@Test
	public void storeDocuementSucess(@Mocked final StoreRemoteServices remote) {
		//this.userToken=addUserToSession(USERNAME,"João Pereira"); 

		new Expectations(){
			{
				new StoreRemoteServices();
				remote.storeDocument(USERNAME, SPREADSHEET_NAME, bytes); 
			}
		};

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ars, f.getIdentificador());
		service.execute();
		boolean t = Arrays.equals(service.getDocXML(), this.bytes);
		assertTrue(t);
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void storeDocuementUnvailableException(@Mocked final StoreRemoteServices remote) {
		//this.userToken=addUserToSession(USERNAME,"João Pereira"); 

		new Expectations(){
			{
				new StoreRemoteServices();
				remote.storeDocument(USERNAME, SPREADSHEET_NAME, bytes); result = new RemoteInvocationException();
			}
		};

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ars, f.getIdentificador());
		service.execute();
	}
	
	@Test(expected = CannotStoreDocumentException.class)
	public void storeDocuementCanNotStoreException(@Mocked final StoreRemoteServices remote) {
		//this.userToken=addUserToSession(USERNAME,"João Pereira"); 

		new Expectations(){
			{
				new StoreRemoteServices();
				remote.storeDocument(USERNAME, SPREADSHEET_NAME, bytes); result = new CannotStoreDocumentException(f.getIdentificador());
			}
		};

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ars, f.getIdentificador());
		service.execute();
	}

}
