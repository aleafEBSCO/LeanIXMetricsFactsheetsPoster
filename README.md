# LeanixMetricsSaver  
Program to query factsheets from LeanIX, filter out the complete ones, and post information on the number of incomplete factsheets to LeanIX metrics. Please be sure to only use Java 8 (1.8) when running the program as LeanIX's Java SDK doesn't work with the latest version of Java as of writing this.  
## How To Use  
To use the program, you will need your Workspace ID, API Token, and the name of the Measurement group you want to post to. The Workspace ID and API Tokens can be found in the API Tokens section on the Administrator Page. Measurement group names can be found in the "Metrics" tab under "Add-ons" or you can make your own. Once you have all that information and the jar file from releases, you can run the following command:  
`java -jar metricsSaver.jar apiToken workspaceID measurementName`

## Testing  
To test the program, you will need to set the API token and Workspace ID equal to their corresponding variables in QueryTests.java and LeanixMetricsTests.java. You can then run the tests.