package pt.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import pt.tecnico.bubbledocs.exception.InvalidLiteralTypeException;

public class Literal extends Literal_Base {
    
    public Literal(String value) {
        super();
        try {
            this.setValor(Integer.parseInt(value));
        }catch(NumberFormatException e) {
            throw new InvalidLiteralTypeException();
        }
        
    }
    
    @Override
    public Integer getvalue(){
        return this.getContent();
    }
    @Override
    public Integer getContent() {
        // TODO Auto-generated method stub
        return this.getValor();
    }
    

    
}
