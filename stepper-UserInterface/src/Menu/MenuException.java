package Menu;

public class MenuException extends Exception{
    MenuExceptionItems exceptionItem;
    Object additionalInfo=null;

    public MenuException(MenuExceptionItems exceptionItem) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
    }

    public MenuException(MenuExceptionItems exceptionItem, Object additionalInfo) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
        if (additionalInfo!=null) {
            this.additionalInfo = additionalInfo;
        }

    }
}
