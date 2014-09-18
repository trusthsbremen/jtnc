package de.hsbremen.tc.tnc.im.loader.enums;

public enum ImTypeEnum {
	JAVA_IMC("JAVA-IMC"),
	JAVA_IMV("JAVA-IMV");
	
	private String classifier;
	
	private ImTypeEnum(String classifier){
		this.classifier = classifier;
	}
	
	public String classifier(){
		return this.classifier;
	}
	
	public ImTypeEnum fromClassifier(String classifier){
		
		if(classifier.trim().equals(JAVA_IMC.classifier)){
			return JAVA_IMC;
		}
		
		if(classifier.trim().equals(JAVA_IMV.classifier)){
			return JAVA_IMV;
		}
		
		return null;
	}
	
}

