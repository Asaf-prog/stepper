package modules.mappings;

public interface HasTarget<T> {
    public void setTarget(T target);
    public T getTarget();
}
