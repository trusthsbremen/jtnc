package de.hsbremen.tc.tnc.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultImConnectionStateFactory implements ImConnectionStateFactory {

	private final List<ImConnectionState> allAttributes;
	
	private static class Singleton {
		private static final ImConnectionStateFactory INSTANCE = new DefaultImConnectionStateFactory();
	}
	
	public static ImConnectionStateFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private DefaultImConnectionStateFactory() {
		Comparator<ImConnectionState> comparator = new StateComparator();

		this.allAttributes = new ArrayList<>();
		allAttributes.addAll(Arrays.asList(ImTncConnectionStateEnum.values()));

		Collections.sort(this.allAttributes, comparator);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#fromId(long)
	 */
	@Override
	public ImConnectionState fromState(final long state){

		Comparator<ImConnectionState> comparator = new StateComparator();

		int index = Collections.binarySearch(this.allAttributes, new ImConnectionState() {
			
			@Override
			public long state() {
				// TODO Auto-generated method stub
				return state;
			}
		}, comparator);
		
		if(index >= 0){
			return this.allAttributes.get(index);
		}
		
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#getAllTypes()
	 */
	@Override
	public List<ImConnectionState> getAllStates() {
		return Collections.unmodifiableList(this.allAttributes);
	}


	private class StateComparator implements Comparator<ImConnectionState>{
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(ImConnectionState o1, ImConnectionState o2) {
			return (o1.state() < o2.state()) ? -1 : ((o1.state() == o2.state()) ? 0 : 1);
		}
	}
	
}
