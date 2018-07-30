package com.ebsco.leanixmetrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

class LeanixMetricsTests {
	private String apiToken = "";
	private String workspaceID = "";

	LeanixMetricsTests() {
		//file input stream
		InputStream in = getClass().getResourceAsStream("/TestsArguments.txt");

		//try to read the file
		try {
			//reader for the input stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			//add each line to the string builder
			this.apiToken = reader.readLine();
			this.workspaceID = reader.readLine();

			//close the file
			reader.close();
		}
		//else the file can't be read or found
		catch(NullPointerException e) {
			System.out.println("Unable to open file 'TestsArguments.txt'");
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.println("Error reading file 'TestsArguments.txt'");
			e.printStackTrace();
		}
	}
	
	//check to make sure a filtertools is returned from LoadFilterFactsheets
	@Test
	void getDataCountTest() {
		//create the object
		LeanixMetrics lm = new LeanixMetrics(apiToken, this.workspaceID, "randomName");

		//list of factsheet types, there should be a .graphql file for each type
		String[] types = {"boundedContext", "domain", "dataObject", "ITComponent", "behavior",
				"useCase", "epic", "persona"};
    	
		//for each factsheet type
		for (String type : types) {
			//get the number of incomplete factsheets of that type
			int numIncomplete = lm.getDataCount(type);
			assertTrue(numIncomplete > -1);
		}
	}
	
	//test to make sure that an error will be thrown when trying to get info with the wrong api key
	@Test
	void brokenKeyGetTest() {
		//object with fake key
		LeanixMetrics lm = new LeanixMetrics("1234", workspaceID, "randomName");
		
		//try to load factsheets
		int test = lm.getDataCount("domain");
    	
		//it will return null
    	assertEquals(test, -1);
		
	}
	
	//test to make sure an error will be thrown when trying to post info with the wrong api key
	@Test
	void brokenKeyPostTest() {
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
    	LeanixMetrics lm = new LeanixMetrics("1234", workspaceID, "randomName");
    	
    	lm.pushPoint(metrics);
    	
	}
}
