package de.hsbremen.tc.tnc.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultTncConnectionStateFactory implements TncConnectionStateFactory {

	private final List<TncConnectionState> allAttributes;
	
	private static class Singleton {
		private static final TncConnectionStateFactory INSTANCE = new DefaultTncConnectionStateFactory();
	}
	
	public static TncConnectionStateFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private DefaultTncConnectionStateFactory() {
		Comparator<TncConnectionState> comparator = new StateComparator();

		this.allAttributes = new ArrayList<>();
		allAttributes.addAll(Arrays.asList(DefaultTncConnectionStateEnum.values()));

		Collections.sort(this.allAttributes, comparator);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#fromId(long)
	 */
	@Override
	public TncConnectionState fromState(final long state){

		Comparator<TncConnectionState> comparator = new StateComparator();

		int index = Collections.binarySearch(this.allAttributes, new TncConnectionState() {
			
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
	public List<TncConnectionState> getAllStates() {
		return Collections.unmodifiableList(this.allAttributes);
	}


	private class StateComparator implements Comparator<TncConnectionState>{
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(TncConnectionState o1, TncConnectionState o2) {
			return (o1.state() < o2.state()) ? -1 : ((o1.state() == o2.state()) ? 0 : 1);
		}
	}
	
}
