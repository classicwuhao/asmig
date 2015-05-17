package parser.ocl;

public class Context{

	private String context;

	public Context(){}
	public Context(String con){
		context = con;
	}

	public void setContext(String newcontext){
		context = newcontext;
	}
	
	public boolean IsEmpty(){return context.length()==0;}
	public String getContext(){return context;}
	public String toString(){return "Context:"+context+"\n";}

}
