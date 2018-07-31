package com.ebsco.leanixmetrics;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.leanix.api.common.ApiClient;
import org.junit.jupiter.api.Test;

class QueryTests {

	private String apiToken = "";
	private String workspaceID = "";
	private String[] types = {"boundedContext", "domain", "dataObject", "ITComponent", "behavior",
			"useCase", "epic", "persona"};
	private Map<String, String> ebscoToLeanix = new HashMap<String, String>();

	QueryTests() {
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

	//Test to make sure that on only factsheets of the given type are returned and that at least one factsheet is returned when Query is used
	@Test
	void typeTest() {
		
		ebscoToLeanix.put("domain", "BusinessCapability");
		ebscoToLeanix.put("useCase", "Process");
		ebscoToLeanix.put("persona", "UserGroup");
		ebscoToLeanix.put("epic", "Project"); 
		ebscoToLeanix.put("boundedContext", "Application");
		ebscoToLeanix.put("behavior", "Interface");
		ebscoToLeanix.put("dataObject", "DataObject");
		ebscoToLeanix.put("ITComponent", "ITComponent");

		//go through all the types
		for (String type1 : types) {
			//use Leanix Metrics to get an api client
			LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID, "randomName");
			ApiClient apiClient = lm.QueryClient();

			Query q = new Query();
			//bool to make sure only the given type appears
			boolean onlyType = true;
			//bool to make sure that at least one factsheet is returned
			boolean atLeastOne = false;

			//query the data
			Map<String, Map<String, Object>> data = q.getInfo(apiClient, "/" + type1 + ".graphql");
			//get the list of edges
			List<Map<String, Object>> edgeList = (List<Map<String, Object>>) data.get("allFactSheets").get("edges");

			//go through all the edges
			for (Map<String, Object> anEdgeList : edgeList) {
				//a factsheet was returned
				atLeastOne = true;
				//get node
				Map<String, Object> node = (Map<String, Object>) anEdgeList.get("node");
				//confirm the factsheet's type is the one we're expecting
				String type = node.get("type").toString();
				if (!(type.equals(ebscoToLeanix.get(type1)))) {
					onlyType = false;
				}
			}

			//assert that no other types appeared and at least one factsheet was returned
			assertTrue(onlyType && atLeastOne);

		}
		
	}
	
	@Test
	void MissingFileTest() {
		
		LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID, "randomName");
		net.leanix.api.common.ApiClient apiClient = lm.QueryClient();
		
		Query q = new Query();
		
		//query the data
		Map<String, Map<String, Object>> data = q.getInfo(apiClient, "/doesntExist.graphql");
		
		assertNull(data);
		
	}
	
}
