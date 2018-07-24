package com.ebsco.leanixmetrics;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.leanix.metrics.api.PointsApi;
import net.leanix.metrics.api.models.Field;
import net.leanix.metrics.api.models.Point;

public class LeanixMetrics 
{
	//below are the object paths for leanix api clients and client builders
	//the common clients/builders are used for accessing information about factsheets
	//the dropkit client/builders are used for posting that information to leanix metrics
    /*
	private net.leanix.api.common.ApiClient commonClient;
	private net.leanix.api.common.ApiClientBuilder commonClientBuidler;
	private net.leanix.dropkit.apiclient.ApiClient metricClient;
	private net.leanix.dropkit.apiclient.ApiClientBuilder metricClientBuilder;
    */
	private String apiToken = "";
	private String workspaceID = "";
	
	public LeanixMetrics(String at, String wi) {
		this.apiToken = at;
		this.workspaceID = wi;
	}
    
	//create a Metrics API object to post to leanix metrics
    public PointsApi LeanixMetricsAPI() {
    	net.leanix.dropkit.apiclient.ApiClient apiClient = new net.leanix.dropkit.apiclient.ApiClientBuilder()
    			//url for the post request
    			.withBasePath("https://us.leanix.net/services/metrics/v1")
    			//to get oauth2 token
    			.withTokenProviderHost("us.leanix.net")
    			//api token from the workspace you'll be using
    			.withApiToken(this.apiToken)
    			.build();
    	//create the api object and return it
    	PointsApi pointsApi = new PointsApi(apiClient);
    	return pointsApi;
    }
    
    //post the point given the incomplete factsheet information
    public void pushPoint(Map<String, Integer> metrics) {
    	//create an empty point
    	Point point = new Point();
    	
    	//General Information
    	//get the current time from UTC. When I tried making it EST, the post request failed
    	OffsetDateTime currentTime= OffsetDateTime.now(ZoneOffset.UTC);
    	//use OffsetDateTime.parse("2018-03-17T13:21:42.318Z") to use a specific date/time
    	point.setTime(currentTime);
    	System.out.println(currentTime);
    	
    	//Title of the metrics.
    	point.setMeasurement("Audit Report Metric Tests");
    	//workspace id. Should be from same workspace as api token. It can be found in the API tokens tab
    	//in the Admin panel
    	point.setWorkspaceId(this.workspaceID);

    	//the field will hold the key value pair. The key is the factsheet type and the value is the
    	//number of incomplete factsheets
    	Field tempField = new Field();
    	for (String key : metrics.keySet()) {
    		tempField = new Field();
    		//set key to factsheet type
    		tempField.setK(key);
    		//set value to number of incomplete factsheets
    		tempField.setV(metrics.get(key).doubleValue());
    		//add the field to the list
    		point.addFieldsItem(tempField);
    	}
    	
    	//No tags are used now but the below code is how you would add it if you wanted to
    	/*
    	Tag t1 = new Tag();
    	t1.setK("factsheetID");
    	t1.setV("123456");
    	point.addTagsItem(t1);
    	*/
    	
    	System.out.println(point);
    	
    	//get the api object used to post the new point
       	PointsApi pa = LeanixMetricsAPI();
    	
       	//try to post the point, else there's an error
    	try {
			pa.createPoint(point);
			System.out.println("Point Created");
		} catch (net.leanix.dropkit.apiclient.ApiException e) {
			e.printStackTrace();
			System.out.println("Error posting point");
		}
		
    }
    
    //apiclient used to get information about factsheets
    public net.leanix.api.common.ApiClient QueryClient() {
    	net.leanix.api.common.ApiClient apiClient = new net.leanix.api.common.ApiClientBuilder()
    			//url to make get request from
    			.withBasePath("https://us.leanix.net/services/pathfinder/v1")
    			//api token from the workspace you will be using
    			.withApiToken(this.apiToken)
    			//leanix server
    			.withTokenProviderHost("us.leanix.net")
    			.build();
    	return apiClient;
    }
    
    //get the number of incomplete factsheets of the given type
    public int getDataCount(String type) {
    	//get the api client
    	net.leanix.api.common.ApiClient apiClient = QueryClient();
    	
    	//create a query object
    	Query query = new Query();
    	//send the api client and type of factsheet to query to get a map of factsheet information
		Map<String, Map<String, Object>> data = query.getInfo(apiClient, "/" + type + ".graphql");
		
		try {
			//get the list of edges from the map
			List<Map<String, Object>> edgeList = (List<Map<String, Object>>) data.get("allFactSheets").get("edges");
	
			//create a new FilterTools object with the list of factsheet nodes and the type
			FilterTools ft = new FilterTools(edgeList, type);
			
			//get the number of incomplete factsheets
			int retNum = ft.retFilteredData();

			//return it
			return retNum;
		}
		catch (NullPointerException e) {
			System.out.println("Error getting factsheets. Likely caused by a wrong/expired API token.");
			e.printStackTrace();
			return -1;
		}

    }
    
}
