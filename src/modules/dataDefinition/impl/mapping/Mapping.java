package modules.dataDefinition.impl.mapping;

public class Mapping<T,S> {
    private T car;
    private S cdr;
    public Mapping(T car, S cdr){
        this.car = car;
        this.cdr = cdr;
    }
    public T getCar(){return car;}
    public S getCdr(){return cdr;}
    public void setCar(T car) {this.car = car;}
    public void setCdr(S cdr) {this.cdr = cdr;}
}
