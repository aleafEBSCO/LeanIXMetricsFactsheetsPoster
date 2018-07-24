package com.ebsco.leanixmetrics;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main (String[] args) {
    	
    	String apiToken = "";
    	String workspaceID = "";
    	
    	//create a new LeanixMetricsObject
    	LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID);
    	
    	//the process can take a few seconds so let the user know it's starting
    	System.out.println("Starting...");
    	
    	//list of factsheet types, there should be a .graphql file for each type
    	String[] types = {"boundedContext", "domain", "dataObject", "ITComponent", "behavior",
    			"useCase", "epic", "persona"};
    	
    	//map to hold returned incomplete factsheet numbers
    	Map<String, Integer> metrics = new HashMap<String, Integer>();
    	
    	//for each factsheet type
    	for (int i = 0; i < types.length; i++) {
    		//get the number of incomplete factsheets of that type
    		metrics.put(types[i], lm.getDataCount(types[i]));
    		//let the user know the number
    		System.out.println(metrics.get(types[i]) + " problem factsheets of type: " + types[i]);
    		if (metrics.get(types[i]) == -1) {
    			System.out.println("Error getting factsheet data");
    			return;
    		}
    	}
    	
    	//push the number of incomplete factsheets to leanix
    	lm.pushPoint(metrics);
    }
}
