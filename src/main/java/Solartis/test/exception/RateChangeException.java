package Solartis.test.exception;

public class RateChangeException extends Exception 
{
    private static final long serialVersionUID = 1L;
    
    public RateChangeException(String message)
	{
    	super (message);
	}
    
    public RateChangeException(Exception e) 
    {
        super(e);
    }

    public RateChangeException(String message, Exception e) 
    {
        super(message, e);
    }
}