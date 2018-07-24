package com.ebsco.leanixmetrics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ebsco.leanixmetrics.LeanixMetrics;
import com.ebsco.leanixmetrics.Query;

public class QueryTests {

	String apiToken = "";
	String workspaceID = "";
	String[] types = {"boundedContext", "domain", "dataObject", "ITComponent", "behavior",
			"useCase", "epic", "persona"};
	Map<String, String> ebscoToLeanix = new HashMap<String, String>();
	
	@Test
	public void typeTest() {
		
		ebscoToLeanix.put("domain", "BusinessCapability");
		ebscoToLeanix.put("useCase", "Process");
		ebscoToLeanix.put("persona", "UserGroup");
		ebscoToLeanix.put("epic", "Project"); 
		ebscoToLeanix.put("boundedContext", "Application");
		ebscoToLeanix.put("behavior", "Interface");
		ebscoToLeanix.put("dataObject", "DataObject");
		ebscoToLeanix.put("ITComponent", "ITComponent");
		
		for (int i = 0; i < types.length; i++) {
		
			LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID);
			net.leanix.api.common.ApiClient apiClient = lm.QueryClient();
			
			Query q = new Query();
			//bool to make sure only the given type appears
			boolean onlyType = true;
			//bool to make sure that at least one factsheet is returned
			boolean atLeastOne = false;
			
			//query the data
			Map<String, Map<String, Object>> data = q.getInfo(apiClient, "/" + types[i] + ".graphql");
			//get the list of edges
			List<Map<String, Object>> edgeList = (List<Map<String, Object>>) data.get("allFactSheets").get("edges");

			//go through all the edges
			Iterator<Map<String, Object>> it = edgeList.iterator();
			while (it.hasNext()) {
				//a factsheet was returned
				atLeastOne = true;
				//get node
				Map<String, Object> edge = it.next();
				Map<String, Object> node = (Map<String, Object>) edge.get("node");
				//confirm the factsheet's type is the one we're expecting
				String type = node.get("type").toString();
				if (!(type.equals(ebscoToLeanix.get(types[i])))) {
					onlyType = false;
				}
			}
			
			//assert that no other types appeared and at least one factsheet was returned
			assert(onlyType && atLeastOne);
			
		}
		
	}
	
	@Test
	public void MissingFileTest() {
		
		LeanixMetrics lm = new LeanixMetrics(apiToken, workspaceID);
		net.leanix.api.common.ApiClient apiClient = lm.QueryClient();
		
		Query q = new Query();
		
		//query the data
		Map<String, Map<String, Object>> data = q.getInfo(apiClient, "/doesntExist.graphql");
		
		Assert.assertNull(data);
		
	}
	
}
