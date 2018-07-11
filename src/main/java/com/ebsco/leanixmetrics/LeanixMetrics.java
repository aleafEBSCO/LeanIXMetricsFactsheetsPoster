package com.ebsco.leanixmetrics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;

import net.leanix.api.common.ApiException;
import net.leanix.metrics.api.PointsApi;

/**
 * Hello world!
 *
 */
public class LeanixMetrics 
{
    //public final PointsApi pointsApi;
	private net.leanix.api.common.ApiClient commonClient;
	private net.leanix.api.common.ApiClientBuilder commonClientBuidler;
	private net.leanix.dropkit.apiclient.ApiClient metricClient;
	private net.leanix.dropkit.apiclient.ApiClientBuilder metricClientBuilder;
    
    public PointsApi LeanixMetricsConnection() {
    	net.leanix.dropkit.apiclient.ApiClient apiClient = new net.leanix.dropkit.apiclient.ApiClientBuilder()
    			.withBasePath("https://us.leanix.net/services/metrics/v1")
    			.withTokenProviderHost("https://us.leanix.net")
    			.withApiToken("")
    			.build();
    	PointsApi pointsApi = new PointsApi(apiClient);
    	return pointsApi;
    }
    
    public net.leanix.api.common.ApiClient QueryClient() {
    	net.leanix.api.common.ApiClient apiClient = new net.leanix.api.common.ApiClientBuilder()
    			.withBasePath("https://us.leanix.net/services/pathfinder/v1")
    			.withApiToken("")
    			.withTokenProviderHost("us.leanix.net")
    			.build();
    	return apiClient;
    }
    
    public int getDataCount(String type) {
    	net.leanix.api.common.ApiClient apiClient = QueryClient();
    	
    	Query query = new Query();    	
		Map<String, Map<String, Object>> data = query.getInfo(apiClient, "/" + type + ".graphql");
		//System.out.println("Testing what's in the object");
		
		
		List<Map<String, Object>> edgeList = (List<Map<String, Object>>) data.get("allFactSheets").get("edges");

		FilterTools ft = new FilterTools(edgeList, type);
		//System.out.println(edgeList.size());
		
		int retNum = ft.retFilteredData();
		
		return retNum;
		
		//System.out.println(edgeList.size());
		
		/*
		for (Map<String, Object> edge : edgeList) {
		  Map<String, Object> node = (Map<String, Object>) edge.get("node");
		  System.out.println(node.get("displayName"));
		}*/
		
		//return 1;

    }
    
    public static void main (String[] args) {
    	
    	LeanixMetrics lm = new LeanixMetrics();
    	
    	System.out.println("Starting...");
    	System.out.println(lm.getDataCount("boundedContext") + " problem factsheets");
    	System.out.println(lm.getDataCount("domain") + " problem factsheets");
    	System.out.println(lm.getDataCount("dataObject") + " problem factsheets");
    	System.out.println(lm.getDataCount("ITComponent") + " problem factsheets");
    	System.out.println(lm.getDataCount("behavior") + " problem factsheets");
    	//System.out.println(lm.getDataCount("useCase") + " problem factsheets");
    	System.out.println(lm.getDataCount("epic") + " problem factsheets");
    	//System.out.println(lm.getDataCount("persona") + " problem factsheets");
    }
    
    
    /*
    public static void main( String[] args )
    {
        
    }
    */
}
