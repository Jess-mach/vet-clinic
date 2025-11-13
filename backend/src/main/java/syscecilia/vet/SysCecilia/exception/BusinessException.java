package syscecilia.vet.SysCecilia.exception;

public class BusinessException extends RuntimeException {
    
    private Object details;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Object details) {
        super(message);
        this.details = details;
    }
    
    public Object getDetails() {
        return details;
    }
}

