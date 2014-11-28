package de.hsbremen.tc.tnc.newp.loader.enums;

public enum DefaultConfigurationLineClassifierEnum implements ConfigurationLineClassifier {
	JAVA_IMC("JAVA-IMC"),
	JAVA_IMV("JAVA-IMV");
	
	private String classifier;
	
	private DefaultConfigurationLineClassifierEnum(String classifier){
		this.classifier = classifier;
	}
	
	@Override
	public String linePrefix(){
		return this.classifier;
	}
	
	public DefaultConfigurationLineClassifierEnum fromClassifier(String classifier){
		
		if(classifier.trim().equals(JAVA_IMC.classifier)){
			return JAVA_IMC;
		}
		
		if(classifier.trim().equals(JAVA_IMV.classifier)){
			return JAVA_IMV;
		}
		
		return null;
	}
	
}

