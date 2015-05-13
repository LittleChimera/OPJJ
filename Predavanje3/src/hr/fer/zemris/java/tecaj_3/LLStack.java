package hr.fer.zemris.java.tecaj_3;

import java.util.EmptyStackException;

public class LLStack {

	private static class Node {
		Object value;
		Node next;
		
		public Node(Object value, Node next) {
			super();
			this.value = value;
			this.next = next;
		}
	}
	
	private Node top;
	private int size;
	
	public void push(Object o) {
		top = new Node(o, top);
		size++;
	}
	
	public Object pop() {
		if (top == null) {
			throw new EmptyStackException();
		}
		
		Object o = top.value;
		top = top.next;
		size--;
		
		return o;
	}
	
	public SizeProvider getSizeProvider1() {
		return new SizeProviderV1(this);
	}
	
	public SizeProvider getSizeProvider2() {
		return new SizeProviderV2();
	}
	
	private static class SizeProviderV1 implements SizeProvider {
		
		private LLStack stack;
		
		public SizeProviderV1(LLStack stack) {
			this.stack = stack;
		}
		
		@Override
		public int getSize() {
			return stack.size;
		}
		
		
	}
	
	public class SizeProviderV2 implements SizeProvider {
		
		@Override
		public int getSize() {
			return LLStack.this.size;
		}
	}
	
}
