package minDistPoints;

enum ExceptionType {
	HAVESAMEPOINT,
	INDEXOUTOFBOUNDS
}

public class PointException extends Exception{
	String message;
	ExceptionType exceptionType;
	
	public ExceptionType getExceptionType(){
		return exceptionType;
	}
	
	public PointException(){}
	
	public PointException(ExceptionType exceptionType){
		this.exceptionType = exceptionType;
	}
	
	public PointException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}
