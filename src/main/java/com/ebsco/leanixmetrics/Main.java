package com.ebsco.leanixmetrics;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main (String[] args) {
    	
    	String apiToken = args[0];
    	String workspaceID = args[1];
    	String measurementName = args[2];
    	
    	//create a new LeanixMetricsObject
    	LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID, measurementName);
    	
    	//the process can take a few seconds so let the user know it's starting
    	System.out.println("Starting...");
    	
    	//list of factsheet types, there should be a .graphql file for each type
    	String[] types = {"boundedContext", "domain", "dataObject", "ITComponent", "behavior",
    			"useCase", "epic", "persona"};
    	
    	//map to hold returned incomplete factsheet numbers
    	Map<String, Integer> metrics = new HashMap<String, Integer>();
    	
    	//for each factsheet type
			for (String type : types) {
				//get the number of incomplete factsheets of that type
				metrics.put(type, lm.getDataCount(type));
				//let the user know the number
				System.out.println(metrics.get(type) + " problem factsheets of type: " + type);
				if (metrics.get(type) == -1) {
					System.out.println("Error getting factsheet data");
					return;
				}
			}
    	
    	//push the number of incomplete factsheets to leanix
    	lm.pushPoint(metrics);
    }
}
