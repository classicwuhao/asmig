package examples.queen;
import java.lang.RuntimeException;

public final class QueenException extends RuntimeException{
	private String message;
	
	public QueenException(String m){
		message = m;
	}

	public String getMessage(){
		return message;
	}
	
}
