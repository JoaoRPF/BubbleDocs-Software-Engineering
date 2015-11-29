package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Interval extends Interval_Base{
	
	private List<Celula> listCelulas = new ArrayList<Celula>(); 
	public Interval(FolhaCalculo f,Referencia celInicial, Referencia celFinal) {
		super();
		// this.setValor(Integer.parse(refInicio,refFinal));
		// Set<Celula> listCelulas = null;
		this.setCellInicial(celInicial.toString());
		this.setCellFinal(celFinal.toString());
		
		
		int flag = 0;
		Celula newCelula = null;

		if(celInicial.getLinha()== celFinal.getLinha() || celInicial.getColuna()== celFinal.getColuna()){
			for (int i=celInicial.getLinha(); i<= celFinal.getLinha();i++){
				for (int j=celInicial.getColuna();j<= celFinal.getColuna();j++){

					Set<Celula> listCelulasFolha  = f.getCelulaSet();
					for (Celula c : listCelulasFolha){
						if(c.getLinha()==i && c.getColuna()==j){
							flag = 1;
							this.listCelulas.add(c);
						}
					}
					if(flag==0){ 
						newCelula = new Celula(i, j);
						this.listCelulas.add(newCelula);
					}
				} 
			}
		
		}
//		for (Celula cel : listCelulas){
//			//this.setValor(cel.getConteudo().getContent());
//		}
//		
	}
	
	@Override
	public List<Celula> getIntervalo(){
		return listCelulas;
	}

	@Override
	public Integer getContent() {
		// TODO Auto-generated method stub
		return null;
	}
	 
	 
	@Override
	public Integer getvalue(){
		return this.getContent();
	}
//	@Override
//	public Integer getContent() {
//		// TODO Auto-generated method stub
//		return this.getIntervalo();
//	}

}
