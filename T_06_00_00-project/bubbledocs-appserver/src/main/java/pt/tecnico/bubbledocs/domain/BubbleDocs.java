package pt.tecnico.bubbledocs.domain;

import java.io.ByteArrayInputStream;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CellDoesNotExistException;
import pt.tecnico.bubbledocs.exception.CellReferingToItselfException;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidCellFormatException;
import pt.tecnico.bubbledocs.exception.InvalidCellId;
import pt.tecnico.bubbledocs.exception.InvalidCellIdFormat;
import pt.tecnico.bubbledocs.exception.InvalidIntervalType;
import pt.tecnico.bubbledocs.exception.InvalidLiteralType;
import pt.tecnico.bubbledocs.exception.InvalidLiteralTypeException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceFormatException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsSpreadSheetException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.WrongFunctionFormatException;
import pt.tecnico.bubbledocs.exception.WrongPasswordException;



public class BubbleDocs extends BubbleDocs_Base {


	public static BubbleDocs getInstance() {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		if (bd == null)
			bd = new BubbleDocs();

		return bd;
	} 

	//Nova instancia de BubbleDoc

	private BubbleDocs() {
		FenixFramework.getDomainRoot().setBubbledocs(this);;
	}

	public Element exportToXML() {
		Element element = new Element("bubbledoc");
		Element usersElement = new Element("users");
		element.addContent(usersElement);
		for (Utilizador user : getUtilizadorSet()) {
			usersElement.addContent(user.exportToXML());
		}
		return element;
	}

	public void importFromXML(Element bubbledocElement) {
		Element users = bubbledocElement.getChild("users");

		for (Element user : users.getChildren("utilizador")) {
			Utilizador u = new Utilizador();
			u.importFromXML(user); 
			addUtilizador(u);
		}
	}
	public Utilizador getUtilizadorByUsername(String name) {
		for (Utilizador u : this.getUtilizadorSet()){

			if (name.equals(u.getUsername())){
				return u;
			}
		}
		return null;
	}

	@Override
	public void addUtilizador(Utilizador u){
		try{
			Utilizador uu = getUtilizadorByUsername(u.getUsername());
			if (uu !=null)
				throw new DuplicateUsernameException(u.getUsername());
			super.addUtilizador(u);
		}catch(BubbleDocsException e){
			System.out.println("Error: "+ e);
		}
	}

	public void addFolhaEscritaToUser(Utilizador userComPermissoes, Utilizador user, FolhaCalculo folha){
		if(folha.getUtilizador().equals(userComPermissoes) || folha.getEscritorSet().contains(userComPermissoes)){
			folha.addEscritor(user);
		}
	}

	public void addFolhaLeituraToUser(Utilizador userComPermissoes, Utilizador user, FolhaCalculo folha){
		if(folha.getUtilizador().equals(userComPermissoes) || folha.getEscritorSet().contains(userComPermissoes)){
			folha.addLeitor(user);

		}

	}
	public FolhaCalculo getFolhaById(int id){
		for(FolhaCalculo f : this.getFolhacalculoSet()){
			if(f.getIdentificador() == id)
				return f;
		}
		return null;
	}

	public String realNameUserInSession(String token){
		Set <Sessao> s = this.getSessaoSet();
		for(Sessao sessao : s){
			if(sessao.getUserToken().equals(token))
				return sessao.getUserName();
		}
		return null;
	}

	public Sessao extractUserSession(String token){
		Set <Sessao> s = this.getSessaoSet();
		for(Sessao sessao : s){
			if(sessao.getUserToken().equals(token)){
				return sessao;
			}
		}
		return null;
	}

	//------------------------------------METODOS AUXILIARES SERVICOS -----------------------------------------------------------------------
	@Override
	public void removeUtilizador(Utilizador u){
		u.delete();
	}

	public void restartUserSession(String userToken){
		Sessao s = this.extractUserSession(userToken);
		if(s!=null)
			s.restartTime();
	}

	public void literalvalido(String literal){
		try { 
			Integer.parseInt(literal); 
		} catch(NumberFormatException e) { 
			throw new InvalidLiteralType(literal); 
		} 
	}
	public void verificaUtilizador(String name) {
		for (Utilizador u : this.getUtilizadorSet()){   	
			if (name.equals(u.getUsername())){
				return;
			}
		}
		throw new UnknownBubbleDocsUserException(name);
	}

	 public void intervalValido(String intervalo){
    	String[] argumentos2 = {};
    	try{
    		String[] argumentos = intervalo.split(":");
    		String arg1 = argumentos[0];
    		String arg2 = argumentos[1];
    		argumentos2 = arg1.split("\\(");
    		//System.out.println("argumentos2222222 " + argumentos2[0]);
    		String arg11 = argumentos2[0];
    		String arg12 = argumentos2[1];
    		this.validaNomeFuncao(arg11);
    		this.validaIdCelula(arg12);
    		String[] argumentos3 = arg2.split("\\)");
    		String arg21 = argumentos3[0];
    		this.validaIdCelula(arg21);
    	}catch(Exception e) {
    		throw new InvalidIntervalType(intervalo);
    	}
    }


	public void verificaPass(String name, String pass){
		Utilizador u = this.getUtilizadorByUsername(name);
		if(!pass.equals(u.getPassword())){
			throw new WrongPasswordException(pass);
		}
	}

	public void verificaLogin(String name){
		Utilizador c = this.getUtilizadorByUsername(name);
		Set<Sessao> sessoes = this.getSessaoSet();		
		for(Sessao s : sessoes){
			if(s.getUserName().equals(c.getUsername())){
				this.removeSessao(s);
				s.delete();
				break;
			}				
		}
	}

	
    public boolean validaNomeFuncao(String func){
    	if (func.equals("AVG") || func.equals("PRD")){
    		return true;
    	}
    	else
    		return false;
    }

	public String loginSucess(String name, String pass){
		Utilizador c = this.getUtilizadorByUsername(name);
		DateTime date = new DateTime();
		DateTime date2 = new DateTime();
		c.generateToken();
		c.setPassword(pass);
		Sessao sessao = new Sessao(c.getUsername(), c.getToken(), date);
		sessao.setUtilizador(c);
		Set<Sessao> sessoes = this.getSessaoSet();		
		this.addSessao(sessao);	
		for(Sessao s : sessoes){
			if(date2.getHourOfDay() - s.getDuracao().getHourOfDay() >= 2){				
				if(date2.getMinuteOfDay() - s.getDuracao().getMinuteOfDay() >= 0){
					sessoes.remove(s);
					s.delete();
				}
			}
		}
		return c.getToken();
	}

	public void presentInSession(String token){
		int flag3=0;
		//Sessao userSession = null;
		Set <Sessao> s = this.getSessaoSet();
		for(Sessao sessao : s){
			if(sessao.getUserToken().equals(token)){
				flag3=1;
			}
		}
		if(flag3==0)
			throw new UserNotInSessionException(token); //token nao esta em sessao
	}


	public void validaIdCelula(String id){
		if(id.contains(";")){
			try{
				String[] partes = id.split(";");  
				Integer.parseInt(partes[0]);
				Integer.parseInt(partes[1]); //vec[0]=linha, vec[1]=coluna
				//int[] vec=vec1;
			}
			catch (NumberFormatException e){
				throw new InvalidCellIdFormat(id);
			}
		}
		else
			throw new InvalidCellIdFormat(id); //O formato da localizacao da celula e invalido
	}

	public void spreadSheetExists(int id){
		if(this.getFolhaById(id)==null)
			throw new UnknownBubbleDocsSpreadSheetException(id); //o id da folha nao existe
	}

	public void permitedUser (int id, String token){
		int flag2=0;
		String name=this.realNameUserInSession(token);
		Utilizador user= this.getUtilizadorByUsername(name);
		Set <FolhaCalculo> fs = user.getFolhacalculoEscreverSet();
		for(FolhaCalculo folha : fs){
			if(folha.getIdentificador()==id){
				flag2=1;
			}
		}


		if(flag2==0)
			throw new UnauthorizedOperationException(token); //utilizador nao tem permissao para escrever na folha
	}

	public String insereLiteralSucesso (int idFolha, String idCell, String literal, String token){
		int[] vec = null;
		int flag1=0;
		FolhaCalculo f = this.getFolhaById(idFolha);
		if(idCell.contains(";")){
			String[] partes = idCell.split(";");  
			int [] vec1 = {Integer.parseInt(partes[0]), Integer.parseInt(partes[1])}; //vec[0]=linha, vec[1]=coluna
			vec=vec1;
		}
		if(vec[0]>0 && vec[0]<=f.getNumLinhas() && vec[1]>0 && vec[1]<=f.getNumColunas()){
			Set <Celula> celulas = f.getCelulaSet();
			for(Celula cell : celulas){
				if(cell.getLinha()==vec[0] && cell.getColuna()==vec[1]){
					flag1=1;
					Conteudo conteudo=Conteudo.parseConteudo(f,literal);
					conteudo.setStringContent(literal);
					cell.insereConteudo(conteudo);
					Sessao userSession= this.extractUserSession(token);
					userSession.restartTime();
					return cell.getConteudo().toString();
				}
			}
			if(flag1==0){
				//criar a celula
				Celula c = new Celula(vec[0],vec[1]);
				c.setContent(literal);
				Conteudo conteudo=Conteudo.parseConteudo(f,literal);
				conteudo.setStringContent(literal);
				c.insereConteudo(conteudo);
				f.addCelula(c); 
				Sessao userSession= this.extractUserSession(token);
				userSession.restartTime();
				return c.getConteudo().toString();
			}
		}
		else
			throw new InvalidCellId(idCell); //a string da locCell nao esta no formato correcto
		return null;
	}
    
    public String insereFuncBinariaSucesso(int idFolha, String idCell, String content, String token){
        int[] vec = null;
        int flag1=0;
        FolhaCalculo f = this.getFolhaById(idFolha);
        // parse das coordenadas da celula
        if(idCell.contains(";")){
            String[] partes = idCell.split(";");
            int[] vec1 = {Integer.parseInt(partes[0]), Integer.parseInt(partes[1])}; //vec[0]=linha, vec[1]=coluna
            vec=vec1;
            //falta adiconar uma excepcao aqui
        }
        
        //parse da funcao binaria em si
        
        if(vec[0]>0 && vec[0]<=f.getNumLinhas() && vec[1]>0 && vec[1]<=f.getNumColunas()){
            Celula cell1 = new Celula(vec[0],vec[1]);
            Conteudo conteudo=Conteudo.parseConteudo(f,content);
            //System.out.println("NOME FUNCAO "+ conteudo.getNomeFuncao());
            if(!conteudo.getNomeFuncao().equals("ADD") && !conteudo.getNomeFuncao().equals("SUB") && !conteudo.getNomeFuncao().equals("MUL") && !conteudo.getNomeFuncao().equals("DIV")){
                throw new WrongFunctionFormatException();
            }
            else{
                String[] argumentoss = conteudo.getArgs();
                for(String s: argumentoss){
                    if(s.contains(";")){
                        try {
                            String[] args = s.split(";");
                            int linhaREF= Integer.parseInt(args[0]);
                            int colunaREF = Integer.parseInt(args[1]);
                        }
                        catch(NumberFormatException ex) {
                            throw new InvalidCellFormatException();
                        }
                    }
                    else {
                        try {
                            int literal222 = Integer.parseInt(s);
                        }
                        catch(NumberFormatException ex){
                            throw new InvalidLiteralTypeException();
                        }
                    }
                    
                }
            }
            conteudo.setStringContent(content);
            cell1.insereConteudo(conteudo);
            Sessao userSession= this.extractUserSession(token);
            userSession.restartTime();
            return cell1.getConteudo().toString();
        }
        else
            throw new CellDoesNotExistException(idCell);       
    }

	public String insereReferenciaSucesso (int idFolha, String idCell, String reference, String token) {
		int linhaCelula=0;
		int colunaCelula=0;
		if(idCell.contains(";")){
			String[] partes = idCell.split(";");  
			linhaCelula=Integer.parseInt(partes[0]);
			colunaCelula = Integer.parseInt(partes[1]); //vec[0]=linha, vec[1]=coluna
		}
		FolhaCalculo f =this.getFolhaById(idFolha);
		if (linhaCelula > 0 && linhaCelula <= f.getNumLinhas() && colunaCelula > 0 && colunaCelula <= f.getNumColunas()){
			Celula celula = new Celula (linhaCelula, colunaCelula);
			int linhaRef;
			int colunaRef;
			if (reference.contains(";")){
				String[] partesReferencia = reference.split(";");
				String parteRef1 = partesReferencia[0];
				String parteRefColuna = partesReferencia[1];
				if(parteRef1.contains("=")){
					try{
						String[] partesReferenciaIgual = parteRef1.split("=");
						String parteRefLinha = partesReferenciaIgual[1];
						linhaRef = Integer.parseInt(parteRefLinha);
						colunaRef = Integer.parseInt(parteRefColuna);
					}
					catch (NumberFormatException e){
						throw new InvalidReferenceFormatException(reference);
					}
				}
				else
					throw new InvalidReferenceFormatException(reference);
			}
			else
				throw new InvalidReferenceFormatException(reference);

			if (linhaRef > 0 && linhaRef <= f.getNumLinhas() && colunaRef > 0 && colunaRef <= f.getNumColunas()){
				if(linhaCelula==linhaRef && colunaCelula == colunaRef){
					throw new CellReferingToItselfException(idCell);

					
				}
				else{
					int [] vec = new int[2];
					vec[0] = linhaRef;
					vec[1] = colunaRef;
					Referencia referencia = new Referencia(f,vec);
					referencia.setStringContent(reference);
					celula.setConteudo(referencia);
					celula.setContent(reference);
					f.addCelula(celula);
					Sessao s= this.extractUserSession(token);
					s.restartTime();
					return celula.getConteudo().toString();
			}
			}
			else
				throw new CellDoesNotExistException(reference);
		}
		else
			throw new CellDoesNotExistException(idCell);
	}

	//--------------------------INSERE FUNCAO INTERVALO COM SUCESSO 
    public String insereFuncaoIntervaloSucesso (int idFolha, String idCell, String nomeFuncao, String userToken) {
   	 int[] vec = null;
   	 int flag1=0;
   	 FolhaCalculo f = this.getFolhaById(idFolha);
   	 if(idCell.contains(";")){
   		 String[] partes = idCell.split(";");  
   		 int [] vec1 = {Integer.parseInt(partes[0]), Integer.parseInt(partes[1])}; //vec[0]=linha, vec[1]=coluna
   		 vec=vec1;
   	 }
   	 if(vec[0]>0 && vec[0]<=f.getNumLinhas() && vec[1]>0 && vec[1]<=f.getNumColunas()){
   		 Set <Celula> celulas = f.getCelulaSet();
   		 for(Celula cell : celulas){
   			 if(cell.getLinha()==vec[0] && cell.getColuna()==vec[1]){
   				 flag1=1;
   				 Conteudo conteudo=Conteudo.parseConteudo(f,nomeFuncao);
   				 conteudo.setStringContent(nomeFuncao);
   				 cell.insereConteudo(conteudo);
   				 cell.setConteudo(conteudo);
   				 cell.setContent(String.valueOf(conteudo.getContent()));
   				 Sessao userSession= this.extractUserSession(userToken);
   				 userSession.restartTime();
   				 return cell.getConteudo().getStringContent();
   			 }
   		 }
   		 if(flag1==0){
   			 //criar a celula
   			 Celula c = new Celula(vec[0],vec[1]);
   			 c.setContent(nomeFuncao);
   			 Conteudo conteudo=Conteudo.parseConteudo(f,nomeFuncao);
   			// System.out.println("IIIIIII = "+String.valueOf(conteudo.getContent()));
   			 conteudo.setStringContent(nomeFuncao);
   			 c.insereConteudo(conteudo);
   			 c.setConteudo(conteudo);
   			 c.setContent(String.valueOf(conteudo.getContent()));
   			 f.addCelula(c);
   			 Sessao userSession= this.extractUserSession(userToken);
   			 userSession.restartTime();
   			 return c.getConteudo().getStringContent();
   		 }
   	 }
   	 else
   		 throw new InvalidCellId(idCell); //a string da locCell nao esta no formato correcto
   	 return null;
    }
    
    //-------------------------------------------INSERE FUNCAO INTERVALO COM SUCESSO INTEGER ------------------------------------
    
    public Integer insereFuncaoIntervaloSucessoInteger (int idFolha, String idCell, String nomeFuncao, String userToken) {
      	 int[] vec = null;
      	 int flag1=0;
      	 FolhaCalculo f = this.getFolhaById(idFolha);
      	 if(idCell.contains(";")){
      		 String[] partes = idCell.split(";");  
      		 int [] vec1 = {Integer.parseInt(partes[0]), Integer.parseInt(partes[1])}; //vec[0]=linha, vec[1]=coluna
      		 vec=vec1;
      	 }
      	 if(vec[0]>0 && vec[0]<=f.getNumLinhas() && vec[1]>0 && vec[1]<=f.getNumColunas()){
      		 Set <Celula> celulas = f.getCelulaSet();
      		 for(Celula cell : celulas){
      			 if(cell.getLinha()==vec[0] && cell.getColuna()==vec[1]){
      				 return cell.getConteudo().getContent();
      			 }
      		 }
      		 
      	 }
      	 else
      		 throw new InvalidCellId(idCell); //a string da locCell nao esta no formato correcto
      	 return null;
       }
     
    //-------------------------------------------------------------------------------------------------------------------
	public Utilizador getUtilizadorByUserToken(String userToken){ //metodo do create spreadsheet
		Utilizador u;
		this.presentInSession(userToken);
		u=this.extractUserSession(userToken).getUtilizador();
		return u;

	}

	public void checkIfIsRoot(String userToken){ //metodo criado para create user
		int flag=0;
		Set <Sessao> sessao = this.getSessaoSet();
		for(Sessao s : sessao){
			if(s.getUserToken().equals(userToken)){
				if(!s.getUserName().equals("root"))
					flag=1;
			}
		}
		if (flag==1)
			throw new UnauthorizedOperationException(userToken);
	}

	public void checkValidityOfUsername(String username){
		if(username.length()<3 && username.length()>8){
			throw new InvalidUsernameException("Username invalido! Tem de ter entre 3 e 8 caracteres");
		}
		for (Utilizador user : this.getUtilizadorSet()){
			if (username.equals(user.getUsername())){
				throw new DuplicateUsernameException(username);
			}
		}
		if (username.isEmpty()){
			throw new EmptyUsernameException("");
		}

	}

	public void checkValidityOfEmail(String email){
		for (Utilizador user : this.getUtilizadorSet()){
			if (email.equals(user.getEmail())){
				throw new DuplicateEmailException(email);
			}
		}
	}


	public int setFolha(int rows,int columns,String name,Utilizador user ){
		FolhaCalculo c=new FolhaCalculo(rows,columns,name,user);
		this.addFolhacalculo(c);
		return c.getIdentificador();
	}

	public void novoUtilizador(String nome,String email,String username, String userToken){
		Utilizador u = new Utilizador(nome);
		u.setUsername(username);
		u.setPassLocal(true);
		u.setEmail(email);
		u.setToken(null);
		this.addUtilizador(u);
		Set <Sessao> sessao = this.getSessaoSet();
		for(Sessao s : sessao){
			if(s.getUserToken().equals(userToken)){
				s.restartTime();
			}
		}
	}


	public boolean getSessao(String token){
		for(Sessao s : this.getSessaoSet()){
			if(s.getUserToken().equals(token))
				return true;
		}
		return false;
	}
	public boolean CheckLeitura(String token, int docID) {
		Set <Utilizador> users = this.getUtilizadorSet();
		Set<Sessao> sessao= this.getSessaoSet();
		for(Sessao s : sessao){
			if(s.getUserToken().equals(token)){
				for(Utilizador user1 : users){
					if(user1.getUsername().equals(s.getUserName())){
						Set <FolhaCalculo> fs = user1.getFolhacalculoLerSet();
						for(FolhaCalculo folha : fs){
							if(folha.getIdentificador()==docID){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean CheckEscrita(String token, int docID) {
		Set <Utilizador> users = this.getUtilizadorSet();
		Set<Sessao> sessao= this.getSessaoSet();
		for(Sessao s : sessao){
			if(s.getUserToken().equals(token)){
				for(Utilizador user1 : users){
					if(user1.getUsername().equals(s.getUserName())){
						Set <FolhaCalculo> fs = user1.getFolhacalculoEscreverSet();
						for(FolhaCalculo folha : fs){
							if(folha.getIdentificador()==docID){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean CheckAuthor(String token, int docID) {
		Set <Utilizador> users = this.getUtilizadorSet();
		Set<Sessao> sessao= this.getSessaoSet();
		for(Sessao s : sessao){
			if(s.getUserToken().equals(token)){
				for(Utilizador user1 : users){
					if(user1.getUsername().equals(s.getUserName())){
						Set <FolhaCalculo> fs = user1.getFolhacalculoSet();
						for(FolhaCalculo folha : fs){
							if(folha.getIdentificador()==docID){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean checkDocId(int docID){
		Set <FolhaCalculo> folhas = this.getFolhacalculoSet();
		for(FolhaCalculo f : folhas){
			if(f.getIdentificador() == docID){
				return true;
			}
		}
		return false;
	}

	public void verificaPasswordLocal(String nome, String pass){
		Utilizador u= this.getUtilizadorByUsername(nome);
		if(!pass.equals(u.getPass())) // se ja tem login mas tem q fazer update da pass
			u.setPass(pass);
	}

	public void verificaPasswordLocalOff(String nome, String pass){
		Utilizador u= this.getUtilizadorByUsername(nome);
		if(pass.equals(u.getPass())) // se ja tem login mas tem q fazer update da pass
			return;
		else
			throw new UnavailableServiceException(nome, pass);
	}
	/****************** Metodos 4ª Parte ***************************/
	public String[] descreveUtilizador(String username){
		Utilizador u = this.getUtilizadorByUsername(username);
		String [] infos = new String [2];
		if(u != null){
			infos[0]=u.getNome();
			infos[1]=u.getEmail();
			return infos;
		}
		else
			return null;
	}

	public FolhaCalculo recoverFromBackup(byte[] doc, String username) {
		org.jdom2.Document jdomDoc;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			jdomDoc = builder.build(new ByteArrayInputStream(doc));

		} catch (Exception e) {
			throw new ImportDocumentException(doc);
		}
		Element rootElement = jdomDoc.getRootElement(); //esta inicializado a null!?
		FolhaCalculo f = new FolhaCalculo();
		f.importFromXML(rootElement);
		f.getLeitorSet().clear();
		f.getEscritorSet().clear();		
		if(!f.getUtilizador().getUsername().equals(username)){
			throw new UnauthorizedOperationException(username);
		}
		Utilizador user= this.getUtilizadorByUsername(username);
		user.addFolhacalculo(f);
		user.addFolhacalculoEscrever(f);
		//falta associar esta nova folha ao utilzador!!!!
		return f;
	}



	public String[][] getConteudoFolha(int idFolha){
		FolhaCalculo fc = getFolhaById(idFolha);
		int maiorLinha = fc.getNumLinhas();
		int maiorColuna = fc.getNumColunas();
		String [][] matriz = new String[maiorLinha+1][maiorColuna+1];
		int linha;
		int coluna;

		for (Celula c : fc.getCelulaSet()){
			linha = c.getLinha();
			coluna = c.getColuna();
			matriz[linha][coluna] = c.getContent();
		}

		for (int i=1; i<=maiorLinha; i++){
			for (int j=1; j<=maiorColuna; j++){
				if (matriz[i][j] == null){
					matriz[i][j] = "";
				}
			}
		}
		return matriz;
	}
	
    public String getUsernameByUserToken(String userToken) { //quando nao existe?? falta aqui um null...
    	String username;
    	this.presentInSession(userToken);
    	username=this.extractUserSession(userToken).getUtilizador().getUsername();
    	return username;
    }
}


