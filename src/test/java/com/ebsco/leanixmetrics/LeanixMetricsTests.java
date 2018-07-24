package com.ebsco.leanixmetrics;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ebsco.leanixmetrics.FilterTools;
import com.ebsco.leanixmetrics.LeanixMetrics;

public class LeanixMetricsTests {
	String apiToken = "";
	String workspaceID = "";
	
	//check to make sure a filtertools is returned from LoadFilterFactsheets
	@Test
	public void getDataCountTest() {
		//create the object
		LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID);

		//list of factsheet types, there should be a .graphql file for each type
    	String[] types = {"boundedContext", "domain", "dataObject", "ITComponent", "behavior",
    			"useCase", "epic", "persona"};
    	
    	//for each factsheet type
    	for (int i = 0; i < types.length; i++) {
    		//get the number of incomplete factsheets of that type
    		int numIncomplete = lm.getDataCount(types[i]);
    		assert(numIncomplete > -1);

    	}
	
		
	}
	
	//test to make sure that an error will be thrown when trying to get info with the wrong api key
	@Test
	public void brokenKeyGetTest() {
		//object with fake key
		LeanixMetrics lm = new LeanixMetrics("1234", workspaceID);
		
		//try to load factsheets
		int test = lm.getDataCount("domain");
    	
		//it will return null
    	assertEquals(test, -1);
		
	}
	
	//test to make sure an error will be thrown when trying to post info with the wrong api key
	@Test
	public void brokenKeyPostTest() {
		//fake metrics
		Map<String, Integer> metrics = new HashMap<String, Integer>();
    	metrics.put("relation", 1);
    	metrics.put("accountable", 2);
    	metrics.put("responsible", 3);
    	metrics.put("businessCriticality", 4);
    	metrics.put("ownerPersona", 5);
    	metrics.put("functionalFit", 6);
    	metrics.put("technicalFit", 7);
    	metrics.put("qualitySeal", 8);
    	metrics.put("modelStatus", 9);
    	metrics.put("score", 10);
    	metrics.put("documents", 11);
    	metrics.put("lifecycle", 12);
    	metrics.put("businessValueRisk", 13);
    	
    	//create object with fake key
    	LeanixMetrics lm = new LeanixMetrics("1234", workspaceID);
    	
    	lm.pushPoint(metrics);
    	
	}
}
