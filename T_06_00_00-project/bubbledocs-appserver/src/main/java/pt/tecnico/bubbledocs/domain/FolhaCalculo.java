package pt.tecnico.bubbledocs.domain;

import java.util.Set;

import org.jdom2.Element;
import org.jdom2.DataConversionException;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidColumnInputException;
import pt.tecnico.bubbledocs.exception.InvalidRowInputException;


public class FolhaCalculo extends FolhaCalculo_Base {
   
	private static int lastID=0;
	
    public FolhaCalculo(int linhas, int colunas, String nome, Utilizador utilizador) {
        super();
        if (linhas<=0){
			throw new InvalidRowInputException(linhas);
		}
        if (colunas<=0){
        	throw new InvalidColumnInputException(colunas);
        }
        BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
        this.setNumLinhas(linhas);
        this.setNumColunas(colunas);
        this.setNome(nome);
        LocalDate data = new LocalDate();
        this.setDataCriacao(data);
        this.setUtilizador(utilizador);
        this.addEscritor(utilizador);
        this.setIdentificador(lastID+1);
        lastID=lastID+1;
    }
    
    public FolhaCalculo() {
    	super();
    }
    
    public void iniciarCelulas(){
    	int linhas = this.getNumLinhas();
    	int colunas = this.getNumColunas();
    	for(int i=1;  i <= linhas ; i++){
    		for(int j=1;  j <= colunas ; j++){
    			Celula c = new Celula(i, j);
    	    	this.addCelula(c);
        	}
    	}
    }
   public void addCelula(Celula celula){
	   Set<Celula> celulas= this.getCelulaSet();
	   if(celulas != null){
		   for(Celula c : celulas){
			   if(c.getLinha()==celula.getLinha() && c.getColuna()==celula.getColuna()){
				   return;
			   }
		   }
		   super.addCelula(celula);
	   }
	   super.addCelula(celula);
   }
    
    public void delete(){
    	Set<Celula> celulas = this.getCelulaSet();
    	for(Celula c : celulas){
    		this.removeCelula(c);
    		c.delete();
    	}
    	Set<Utilizador> utilizadores = this.getBubbledocs().getUtilizadorSet();
    	for(Utilizador u : utilizadores){
    		if(u.getFolhacalculoSet().contains(this))
    			u.removeFolhacalculo(this);
    		if(u.getFolhacalculoEscreverSet().contains(this)){
    			u.removeFolhacalculoEscrever(this);
    			this.getEscritorSet().remove(u);
    		}
    		if(u.getFolhacalculoLerSet().contains(this)){
    			u.removeFolhacalculoLer(this);
    			this.getLeitorSet().remove(u);
    		}
    	}
    	//this.setEscritor(null);
    	//this.setLeitor(null);
    	this.setUtilizador(null);
    	this.setBubbledocs(null);
    	this.deleteDomainObject();
    }
    
  
       
    public void addEscritor(Utilizador user, String username, String pass){
    	if(!this.getEscritorSet().contains(user)){
    		super.addEscritor(user);
    		
    		user.addFolhacalculoEscrever(this);
    		
    		if(this.getLeitorSet().contains(user)){
    			this.removeLeitor(user);
    			user.removeFolhacalculoLer(this);
    		}
    	}
    }
    
    public void addLeitor(Utilizador user, String username, String pass){
    	if(!this.getLeitorSet().contains(user)){
		    super.addLeitor(user);
		    user.addFolhacalculoLer(this);
		    		
		    if(this.getEscritorSet().contains(user)){
		    	this.removeEscritor(user);
		    	user.removeFolhacalculoEscrever(this);
		    }
		 }
	}
    
    public void importFromXML(Element element) {
    	BubbleDocs bd = BubbleDocs.getInstance();
        setNome(element.getAttribute("nome").getValue());
        try {
            this.setIdentificador(lastID +1);
            lastID = lastID +1;
            setNumLinhas(element.getAttribute("linhas").getIntValue());
            setNumColunas(element.getAttribute("colunas").getIntValue());
            setBubbledocs(bd);
            LocalDate date = new LocalDate(getDataCriacao());
            setDataCriacao(date);
            Set <Utilizador> users = bd.getUtilizadorSet();
            for(Utilizador uu : users){
            	if(uu.getUsername().equals(element.getAttribute("utilizador").getValue())){
                    setUtilizador(uu);
            	}
            }
//            System.out.println("VOU BUSCAR CELULAS");
            Element celulas = element.getChild("celulas");
//            System.out.println("VOU BUSCAR CELULAS 111111111111111111");
            for (Element celulaElement : celulas.getChildren("celula")) {
//            	System.out.println("VOU BUSCAR CELULAS 2222222222222222");
                Celula c = new Celula();
//                System.out.println("VOU BUSCAR CELULAS 3333333333333333");
                c.importFromXML(this,celulaElement);
//                System.out.println("VOU BUSCAR CELULAS 4444444444444444");
                addCelula(c);
//                System.out.println("VOU BUSCAR CELULAS 5555555555555555");
            }
//            System.out.println("ACABEI CELULAS");
        } catch (DataConversionException e) {
            throw new RuntimeException(); 
        }
    }
    
    public Element exportToXML() {
    	
        Element element = new Element("folhaCalculo");
        element.setAttribute("nome", getNome());
        element.setAttribute("identificador", Integer.toString(getIdentificador()));
        element.setAttribute("linhas", Integer.toString(getNumLinhas()));
        element.setAttribute("colunas", Integer.toString(getNumColunas()));
        String s = ""+ getDataCriacao();
        element.setAttribute("data_criacao", s);
        element.setAttribute("utilizador",getUtilizador().getUsername());
      
       // element.setAttribute("bubbleDocs", ""+ this.getBubbledocs().getOid());
       // System.out.println("PRINT BUBBLEDOCS: " + this.getBubbledocs().getOid());
        Element celulasElement = new Element("celulas");
        element.addContent(celulasElement);
        for (Celula c : this.getCelulaSet()) {
        	celulasElement.addContent(c.exportToXML());
        }

        return element;
    }
    
    /*************************PARTE 3 ********************************/
    
    public boolean checkCriadorOuEscritor(String username){
    	if(this.getUtilizador().getUsername().equals(username))
    		return true;
    	
    	Set<Utilizador> users = this.getEscritorSet();
    	for(Utilizador u : users){
    		if(u.getUsername().equals(username))
    			return true;
    	}
		return false;
    }
    
    
}
