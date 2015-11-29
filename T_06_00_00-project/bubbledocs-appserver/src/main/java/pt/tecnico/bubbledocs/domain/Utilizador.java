package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;



public class Utilizador extends Utilizador_Base {
    private String password=null;
    private boolean passLocal = true;
    
    public Utilizador(String nome) {
        super();
        setNome(nome);

    }
    
    public Utilizador() {
        super();
    }
    
    public void generateToken(){
        Random rn = new Random();
        int numberToken;
        numberToken = rn.nextInt(9 - 0 + 1) + 0;
        String userToken = this.getUsername() + "" + numberToken;
        this.setToken(userToken);
    }
    
    public List<FolhaCalculo> getDocumentsCreatedByUser(String filename, Utilizador user){
    	Set<FolhaCalculo> folhas = super.getBubbledocs().getFolhacalculoSet();
    	List<FolhaCalculo> folhasDoUser = new ArrayList<FolhaCalculo>();
    	for(FolhaCalculo f : folhas){
    		if(f.getNome().equals(filename) && f.getUtilizador().equals(user)){
    			folhasDoUser.add(f);
    		}
    	}
    	return folhasDoUser;
    }
    
    public void importFromXML(Element personElement) {
        setNome(personElement.getAttribute("nome").getValue());
        Element contacts = personElement.getChild("contacts");
        
        for (Element contactElement : contacts.getChildren("contact")) {
            FolhaCalculo fc = new FolhaCalculo();
            fc.importFromXML(contactElement);
        }
        
    }
    
    public Element exportToXML() {
        Element element = new Element("Utilizador");
        
        element.setAttribute("name", getNome());
        
        Element folhasElement = new Element("folhacalculo");
        element.addContent(folhasElement);
        for (FolhaCalculo fc : getFolhacalculoEscreverSet()) {
            folhasElement.addContent(fc.exportToXML());
        }
        for (FolhaCalculo fc : getFolhacalculoLerSet()) {
            folhasElement.addContent(fc.exportToXML());
        }
        
        return element;
    }
    
    public void delete(){
    	for(FolhaCalculo folhas : this.getFolhacalculoSet()){
    		folhas.delete();		
    	}
        Sessao s= this.getSessao();
        if(s==null){}
        else
            s.delete();
        this.setToken(null);
    	this.setNome(null);
    	this.setPassword(null);
    	this.setUsername(null);
    	this.setToken(null);
    	this.getBubbledocs().getUtilizadorSet().remove(this);
        this.deleteDomainObject();
    }
    
    public void addFolhacalculoEscrever(FolhaCalculo folha){
    	if(!this.getFolhacalculoEscreverSet().contains(folha)){
    		folha.addEscritor(this);
    		super.addFolhacalculoEscrever(folha);
    	}
    }
    
    public void addFolhacalculoLer(FolhaCalculo folha){
    	if(!this.getFolhacalculoLerSet().contains(folha)){
    		folha.addLeitor(this);
    		super.addFolhacalculoLer(folha);
    	}
    }
    
    public void setBubbleDocs(BubbleDocs d){
    	d.addUtilizador(this);
	}
    
    public void setPass(String pass){
    	this.password=pass;
    	setPassLocal(true);
    }
    public String getPass(){
    	return this.password;
    }

	public boolean isPassLocal() {
		return passLocal;
	}

	public void setPassLocal(boolean passLocal) {
		this.passLocal = passLocal;
	}
	
	@Override
	public String toString(){
		String idFolhas = null;
		for(FolhaCalculo f : this.getFolhacalculoSet()){
			if(idFolhas.length() < 1)
				idFolhas="" + f.getIdentificador();
			else
				idFolhas=idFolhas + "" + f.getIdentificador();
		}
		
		String s = this.getUsername() + "" + this.getPassword() + "" + this.getEmail() + "" + this.getNome() + "" + idFolhas;
		return s;
	}
}
