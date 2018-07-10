package com.ebsco.leanixmetrics;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FilterTools {
	
	private List<Map<String, Object>> initialList;
	private List<Map<String, Object>> filteredList;
	private String type;
	
	public FilterTools(List<Map<String, Object>> info, String t) {
		this.initialList = info;
		this.type = t;
	}
	
	public int retFilteredData() {
		System.out.println("Filtering was called");
		this.filteredList = this.initialList;
		filterLeafNodes();
		if (this.type.equals("epic")) {
			filterDocuments();
			filterLifecycle();
		}
		
		return this.filteredList.size();
	}
	
	private void filterLeafNodes() {
		Iterator<Map<String, Object>> it = this.filteredList.iterator();
		while (it.hasNext()) {
		  Map<String, Object> edge = it.next();
		  Map<String, Object> node = (Map<String, Object>) edge.get("node");
		  Map<String, Integer> innerNode = (Map<String, Integer>) node.get("relToChild");
		  if (innerNode.get("totalCount") != 0) {
			  it.remove();
		  }
		}
	}

	private void filterDocuments() {
		Iterator<Map<String, Object>> it = this.filteredList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get("documents");
			if (innerNode.get("totalCount") != 0) {
				it.remove();
			}
		}
	}
	
	private void filterLifecycle() {
		Iterator<Map<String, Object>> it = this.filteredList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if (node.get("lifecycle") != null) {
				it.remove();
				/*
				Map<String, Object[]> innerNode = (Map<String, Object[]>) node.get("lifecycle");
				Object ar[] = innerNode.get("phases");
				if (ar != null || ar.length != 0) {
					it.remove();
				}*/
			}
		}
	}
}
