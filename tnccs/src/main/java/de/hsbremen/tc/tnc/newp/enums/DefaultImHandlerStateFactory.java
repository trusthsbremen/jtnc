package de.hsbremen.tc.tnc.newp.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;

public class DefaultImHandlerStateFactory implements ImHandlerStateFactory {

	private final List<ImHandlerState> allStates;
	
	private static class Singleton {
		private static final ImHandlerStateFactory INSTANCE = new DefaultImHandlerStateFactory();
	}
	
	public static ImHandlerStateFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private DefaultImHandlerStateFactory() {
		Comparator<ImHandlerState> comparator = new StateComparator();

		this.allStates = new ArrayList<>();
		allStates.addAll(Arrays.asList(DefaultImHandlerStateEnum.values()));

		Collections.sort(this.allStates, comparator);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#fromId(long)
	 */
	@Override
	public ImHandlerState fromState(final long state){

		Comparator<ImHandlerState> comparator = new StateComparator();

		int index = Collections.binarySearch(this.allStates, new ImHandlerState() {
			
			@Override
			public long state() {
				// TODO Auto-generated method stub
				return state;
			}
		}, comparator);
		
		if(index >= 0){
			return this.allStates.get(index);
		}
		
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#getAllTypes()
	 */
	@Override
	public List<ImHandlerState> getAllStates() {
		return Collections.unmodifiableList(this.allStates);
	}


	private class StateComparator implements Comparator<ImHandlerState>{
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(ImHandlerState o1, ImHandlerState o2) {
			return (o1.state() < o2.state()) ? -1 : ((o1.state() == o2.state()) ? 0 : 1);
		}
	}

	@Override
	public ImHandlerState fromConnectionState(TncConnectionState state) {
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE)){
			return this.fromState(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_START.state());
		}
		
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ALLOWED)){
			return this.fromState(DefaultImHandlerStateEnum.HSB_SESSION_STATE_DECIDED_ALLOWED.state());
		}
		
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ISOLATED)){
			return this.fromState(DefaultImHandlerStateEnum.HSB_SESSION_STATE_DECIDED_ISOLATED.state());
		}
		
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE)){
			return this.fromState(DefaultImHandlerStateEnum.HSB_SESSION_STATE_DECIDED_DENIED.state());
		}
		
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE)){
			return this.fromState(DefaultImHandlerStateEnum.HSB_SESSION_STATE_DELETE.state());
		}
		
		return this.fromState(DefaultImHandlerStateEnum.HSB_SESSION_STATE_UNKNOWN.state());
		
	}
	
}
