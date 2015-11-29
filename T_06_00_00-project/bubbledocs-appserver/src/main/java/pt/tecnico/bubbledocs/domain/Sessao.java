package pt.tecnico.bubbledocs.domain;

import org.joda.time.DateTime;

public class Sessao extends Sessao_Base {
    
    public Sessao() {
        super();
    }
    
    public Sessao(String username, String userToken){
    	this.setUserName(username);
    	this.setUserToken(userToken);
    	this.setDuracao(new DateTime());
    }
    
    public Sessao(String username, String userToken, DateTime duracao){
    	this.setUserName(username);
    	this.setUserToken(userToken);
    	this.setDuracao(duracao);
    }
    
    public void delete(){
    	this.getUtilizador().setToken(null);
    	this.getUtilizador().setSessao(null);
    	this.setUtilizador(null);
    	this.setDuracao(null);
    	this.setUserName(null);
    	this.setUserToken(null);
    	this.setBubbledocs(null);
    	this.deleteDomainObject();
    }
    public void restartTime(){
    	this.setDuracao(new DateTime());
    }
}
