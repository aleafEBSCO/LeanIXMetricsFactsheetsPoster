package com.ebsco.leanixmetrics;

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
    
    public String getDataCount() {
    	net.leanix.api.common.ApiClient apiClient = QueryClient();
    	
    	Query query = new Query();    	
		return query.getData(apiClient, "/epic.graphql");

    }
    
    public static void main (String[] args) {
    	
    	LeanixMetrics lm = new LeanixMetrics();
    	
    	System.out.println("Seems to be working");
    	System.out.println(lm.getDataCount());
    }
    
    
    /*
    public static void main( String[] args )
    {
        
    }
    */
}
