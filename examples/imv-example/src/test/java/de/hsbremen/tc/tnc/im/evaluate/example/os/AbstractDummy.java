package de.hsbremen.tc.tnc.im.evaluate.example.os;


public abstract class AbstractDummy {
	
	
	public static final void setLogSettings(){
//		BasicConfigurator.configure();
	}
	
	public static String getTestDescriptionHead(String className, String head){
		StringBuilder b = new StringBuilder();
		b.append("----- \n");
		b.append(className);
		b.append(" - ");
		b.append(head);
		b.append("\n----- \n");
	
		return b.toString();
	}
	
}
