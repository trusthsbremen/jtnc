package de.hsbremen;

import org.junit.Test;

public class Test1 {

	@Test
	public void test(){
		Context ctx = new Context();
		ctx.handle();
	}
	
	class State{
	
		public String handle(Context ctx){
			String s =  "SimpleState";
		
			ctx.setState(new State1());
			
			
			return s;
		}
		
	}
	
	class State1 extends State{
		
		public String handle(Context ctx){
			String s =  "SimpleState1";
			return s;
		}
	}
	
	class Context{
		
		private State s;
		
		public Context(){
			this.s = new State();
		}
		
		public void handle(){
			String b = s.handle(this);
			System.out.println(b);
			String c = s.handle(this);
			System.out.println(c);
		}
		
		public void setState(State s){
			this.s = s;
		}
		
	}
	
}
