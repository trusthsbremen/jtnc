package de.hsbremen.tc.tnc.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultTncAttributeTypeFactory implements TncAttributeTypeFactory {

	private final List<TncAttributeType> allAttributes;
	private final List<TncAttributeType> clientAttributes;
	private final List<TncAttributeType> serverAttributes;
	
	private static class Singleton {
		private static final TncAttributeTypeFactory INSTANCE = new DefaultTncAttributeTypeFactory();
	}
	
	public static TncAttributeTypeFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private DefaultTncAttributeTypeFactory() {
		Comparator<TncAttributeType> comparator = new IdComparator();

		List<TncAttributeType> commonAttributes = new ArrayList<>();
		commonAttributes.addAll(Arrays.asList(TncCommonAttributeTypeEnum.values()));
		
		this.allAttributes = new ArrayList<>();
		this.allAttributes.addAll(commonAttributes);
		
		this.clientAttributes = new ArrayList<>();
		this.clientAttributes.addAll(Arrays.asList(TncClientAttributeTypeEnum.values()));
		
		this.allAttributes.addAll(this.clientAttributes);
		
		this.clientAttributes.addAll(commonAttributes);
		Collections.sort(this.clientAttributes, comparator);
		
		this.serverAttributes = new ArrayList<>();
		this.serverAttributes.addAll(Arrays.asList(TncServerAttributeTypeEnum.values()));
		
		this.allAttributes.addAll(this.serverAttributes);
		
		this.serverAttributes.addAll(commonAttributes);
		Collections.sort(this.serverAttributes, comparator);
	
		Collections.sort(this.allAttributes, comparator);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#fromId(long)
	 */
	@Override
	public TncAttributeType fromId(final long id){

		Comparator<TncAttributeType> comparator = new IdComparator();

		int index = Collections.binarySearch(this.allAttributes, new TncAttributeType() {
			
			@Override
			public long id() {
				return id;
			}
		}, comparator);
		
		if(index >= 0){
			return this.allAttributes.get(index);
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#fromIdImcOnly(long)
	 */
	@Override
	public TncAttributeType fromIdClientOnly(final long id){

		Comparator<TncAttributeType> comparator = new IdComparator();

		int index = Collections.binarySearch(this.clientAttributes, new TncAttributeType() {
			
			@Override
			public long id() {
				return id;
			}
		}, comparator);
		
		if(index >= 0){
			return this.clientAttributes.get(index);
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#fromIdImvOnly(long)
	 */
	@Override
	public TncAttributeType fromIdServerOnly(final long id){

		Comparator<TncAttributeType> comparator = new IdComparator();

		int index = Collections.binarySearch(this.serverAttributes, new TncAttributeType() {
			
			@Override
			public long id() {
				return id;
			}
		}, comparator);
		
		if(index >= 0){
			return this.serverAttributes.get(index);
		}
		
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#getAllTypes()
	 */
	@Override
	public List<TncAttributeType> getAllTypes() {
		return Collections.unmodifiableList(this.allAttributes);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#getClientTypes()
	 */
	@Override
	public List<TncAttributeType> getClientTypes() {
		return Collections.unmodifiableList(this.clientAttributes);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeTypeFactory#getServerTypes()
	 */
	@Override
	public List<TncAttributeType> getServerTypes() {
		return Collections.unmodifiableList(this.serverAttributes);
	}



	private class IdComparator implements Comparator<TncAttributeType>{
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(TncAttributeType o1, TncAttributeType o2) {
			return (o1.id() < o2.id()) ? -1 : ((o1.id() == o2.id()) ? 0 : 1);
		}
	}
	
}
