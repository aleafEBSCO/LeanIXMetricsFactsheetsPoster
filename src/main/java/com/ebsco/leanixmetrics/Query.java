package com.ebsco.leanixmetrics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import net.leanix.api.GraphqlApi;
import net.leanix.api.common.ApiClient;
import net.leanix.api.common.ApiException;
import net.leanix.api.models.GraphQLRequest;
import net.leanix.api.models.GraphQLResult;


//class for getting factsheet information from LeanIX
public class Query {
	
	//get the query as a string from a .graphql file
	//the file has to be streamed in to the information can still be read if project becomes a jar
	public String fileToString(String filename) {
		//var to hold each line
		String line;
		//builder to put all lines back together
		StringBuilder builder = new StringBuilder();
		//file input stream
		InputStream in = getClass().getResourceAsStream(filename);
		
		//try to read the file
		try {
			//reader for the input stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			//add each line to the string builder
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			//close the file
			reader.close();
		}
		//else the file can't be read or found
		catch(FileNotFoundException e) {
			System.out.println("Unable to open file '" + filename + "'");
		}
		catch(IOException e) {
			System.out.println("Error reading file '" + filename + "'");
		}
		
		//use the builder to put all the lines into one string and return it
		return builder.toString();
	}
	
	//method to query leanix and get the factsheet information
	public Map<String, Map<String, Object>> getInfo(ApiClient apiClient, String graphQLQuery) {
		//get the query string using the given graphql file name
		String query = fileToString(graphQLQuery);
		//create a graphqlapi object with the given api client
		GraphqlApi graphqlApi = new GraphqlApi(apiClient);
		
		//create an new request
		GraphQLRequest request = new GraphQLRequest();
		//set the query of the request with the one gotten from the file
		request.setQuery(query);
		//create an result to hold the response
		GraphQLResult result = new GraphQLResult();

		//map to hold the factsheet data in the response
		Map<String, Map<String, Object>> retData = new LinkedHashMap<String, Map<String, Object>>();
		try {
			//process the query and save the result
			result = graphqlApi.processGraphQL(request);
			//save the factsheet data from the response
			retData = (LinkedHashMap<String, Map<String, Object>>) result.getData();
		//else there was an error with the call
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		//return the factsheet information
		return retData;
	}

}
