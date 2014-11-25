package de.hsbremen.tc.tnc.imhandler.loader.enums;

public enum ConfigurationLineClassifierEnum implements ConfigurationLineClassifier {
	JAVA_IMC("JAVA-IMC"),
	JAVA_IMV("JAVA-IMV");
	
	private String classifier;
	
	private ConfigurationLineClassifierEnum(String classifier){
		this.classifier = classifier;
	}
	
	@Override
	public String linePrefix(){
		return this.classifier;
	}
	
	public ConfigurationLineClassifierEnum fromClassifier(String classifier){
		
		if(classifier.trim().equals(JAVA_IMC.classifier)){
			return JAVA_IMC;
		}
		
		if(classifier.trim().equals(JAVA_IMV.classifier)){
			return JAVA_IMV;
		}
		
		return null;
	}
	
}

