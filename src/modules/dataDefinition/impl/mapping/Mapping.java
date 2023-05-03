package modules.dataDefinition.impl.mapping;

import java.io.Serializable;

public class Mapping<T,S> implements Serializable{
    private T car;
    private S cdr;
    public Mapping(T car, S cdr){
        this.car = car;
        this.cdr = cdr;
    }
    @Override
    public String toString(){
        return "< "+car.toString() + " - " + cdr.toString()+" >";
    }
    public T getCar(){return car;}
    public S getCdr(){return cdr;}
    public void setCar(T car) {this.car = car;}
    public void setCdr(S cdr) {this.cdr = cdr;}
}
