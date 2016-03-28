package org.ietf.nea.pt.socket.simple;

import org.ietf.nea.pt.socket.sasl.Dummy;
import org.junit.Assert;
import org.junit.Test;

public class VersionComparatorTest {
    
    Range source = new Range(3,7);
    Range larger = new Range(1,10);
    Range shiftedRight = new Range(5,10);
    Range shiftedLeft = new Range(1,5);
    Range smaller = new Range(4, 6);
    Range rightOverflow = new Range(3,10);
    Range leftOverflow = new Range(1,7);
    Range outOfRange = new Range(1,2);
    
    //TODO make real test out of it
    
    @Test
    public void testLarger(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with larger range."));
        int version = findMax(source, larger);
        Assert.assertEquals(7, version);
    }
    
    @Test
    public void testShiftedRight(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with right shifted range."));
        int version = findMax(source, shiftedRight);
        Assert.assertEquals(7, version);
    }
    
    @Test
    public void testShiftedLeft(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with shifted left range."));
        int version = findMax(source, shiftedLeft);
        Assert.assertEquals(5, version);
    }
    
    @Test
    public void testSmaller(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with smaller range."));
        int version = findMax(source, smaller);
        Assert.assertEquals(6, version);
    }
    
    @Test
    public void testRightOverflow(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with right overflowing range."));
        int version = findMax(source, rightOverflow);
        Assert.assertEquals(7, version);
    }
    
    @Test
    public void testLeftOverflow(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with left overflowing range."));
        int version = findMax(source, leftOverflow);
        Assert.assertEquals(7, version);
    }
    
    @Test
    public void testOutOfRange(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test range with non overlapping range."));
        int version = findMax(source, outOfRange);
        Assert.assertEquals(0, version);
    }
    
    public int findMax(Range source, Range other){
       
        // test null
        if(source == null || other == null){
            return 0;
        }
        
        // test out of range
        if(source.max < other.min || source.min > other.max){
            return 0;
        }
        
        // find max number in both range
        return (source.max > other.max) ? other.max
                : ((source.max == other.max) ? other.max : source.max);
    }
    
    static class Range{
        
        public int min;
        
        public int max;

        public Range(int min, int max) {

            this.min = min;
            this.max = max;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Range [min=" + this.min + ", max=" + this.max + "]";
        }
        
        
    }
}
