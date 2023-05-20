package com.deveuge.quizial.util.pagination;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPages;
	private int itemsPerPage;
	private int currentPage;
	private List<PageItem> pages;
	
	private static final int VISIBLE_PAGES = 3;
	
	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<>();
		
		itemsPerPage = page.getSize();
		totalPages = page.getTotalPages();
		currentPage = page.getNumber() + 1;
		
		int from = currentPage - (VISIBLE_PAGES / 2);
        int to = currentPage + (VISIBLE_PAGES / 2);

        if (from < 1) {
            from = 1;
            to = totalPages < VISIBLE_PAGES ? totalPages : VISIBLE_PAGES;
        }
        if (to > totalPages) {
            from = (totalPages - VISIBLE_PAGES) < 1 ? 1 : totalPages - (VISIBLE_PAGES - 1);
            to = totalPages;
        }
		
		for(int i = from; i <= to; i++) {
			pages.add(new PageItem(i, currentPage == i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean hasNext() {
		return page.hasNext();
	}
	
	public boolean hasPrevious() {
		return page.hasPrevious();
	}

	public int getVisiblePages() {
		return pages.size() + (isFirst() ? 0 : 1) + (isLast() ? 0 : 1);
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}
	
	public int getReloadPage() {
		int current = currentPage - 1;
		int substractPages = (page.getNumberOfElements() > 1 ? 0 : 1);
		return current == 0  ? 0 : current - substractPages;
	}
	
}
