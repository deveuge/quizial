package com.deveuge.quizial.util.pagination;

public class PageItem {

	private int number;
	private boolean current;
	
	
	public PageItem(int number, boolean current) {
		super();
		this.number = number;
		this.current = current;
	}

	public int getNumber() {
		return number;
	}

	public boolean isCurrent() {
		return current;
	}
	
}
