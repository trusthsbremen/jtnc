package de.hsbremen.tc.tnc.tnccs.message.handler;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;

public class DefaultTnccHandler implements TnccHandler{

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultTnccHandler.class);

	private final Attributed sessionAttributes;
	private final String tnccLanguagePreference;
	
	private String tempTnccsLanguagePrefernce;
	private TncConnectionState state;
	private boolean handshakeStartet;
	
	
	public DefaultTnccHandler(Attributed sessionAttributes){
		this.sessionAttributes = sessionAttributes;
		this.state = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
		this.tnccLanguagePreference = HSBConstants.HSB_DEFAULT_LANGUAGE;
		this.handshakeStartet = false;
	}
	
	@Override
	public TncConnectionState getAccessDecision() {
		return this.state;
	}
	@Override
	public void setConnectionState(TncConnectionState state) {
		LOGGER.debug("Connection state has changed to " + state.toString() + ".");
		this.state = state;
		if(this.state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE)){
			this.handshakeStartet = true;
		}
		
	}
	@Override
	public List<TnccsMessage> requestMessages() {
		List<TnccsMessage> messages = new ArrayList<>();
		if(handshakeStartet){
			TnccsMessage message = null;
			try {
				message = PbMessageFactoryIetf.createLanguagePreference(this.tnccLanguagePreference);
				messages.add(message);
			} catch (ValidationException e) {
				LOGGER.warn("TnccsMessage with language preference could not be created and will be ignored.", e);
			}
			this.handshakeStartet = false;
		}
		
		
		return messages;
				
	}
	@Override
	public List<TnccsMessage> forwardMessage(TnccsMessage message) {
		
		if(message == null || message.getValue() == null){
			LOGGER.debug("Because Message or message value is null, it is ignored.");
			return new ArrayList<TnccsMessage>();
		}
		
		TnccsMessageValue value = message.getValue();
		
		LOGGER.debug("Message value received: " + value.toString());
		
		if(value instanceof PbMessageValueAccessRecommendation){
			this.handleRecommendation((PbMessageValueAccessRecommendation) value);
		}else if(value instanceof PbMessageValueLanguagePreference){
			this.handleLanguagePreference((PbMessageValueLanguagePreference) value);
		}
		
		List<TnccsMessage> messages = new ArrayList<>();
		if(handshakeStartet){
			TnccsMessage m = null;
			try {
				m = PbMessageFactoryIetf.createLanguagePreference(this.tnccLanguagePreference);
				messages.add(m);
			} catch (ValidationException e) {
				LOGGER.warn("TnccsMessage with language preference could not be created and will be ignored.", e);
			}
			this.handshakeStartet = false;
		}
		return messages;
	}

	private void handleLanguagePreference(PbMessageValueLanguagePreference value) {
		
		String lang = value.getPreferedLanguage();
		
		if(lang != null && !lang.isEmpty()){
			this.tempTnccsLanguagePrefernce  = lang.trim();
		}
	}

	private void handleRecommendation(PbMessageValueAccessRecommendation value) {
		PbMessageAccessRecommendationEnum rec = value.getRecommendation();
		switch(rec){
		case ALLOWED:
			this.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ALLOWED);
			break;
		case DENIED:
			this.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE);
			break;
		case QURANTINED:
			this.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ISOLATED);
			break;
		}
	}

	@Override
	public List<TnccsMessage> lastCall() {
		if(this.tempTnccsLanguagePrefernce != null){
			try{
				this.sessionAttributes.setAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE, this.tempTnccsLanguagePrefernce);
			}catch(TncException | UnsupportedOperationException e){
				LOGGER.warn("Language preference could not be set and will be ignored.");
			}
			this.tempTnccsLanguagePrefernce = null;
		}
		return new ArrayList<>();
	}
	
	
	
	
	
}
