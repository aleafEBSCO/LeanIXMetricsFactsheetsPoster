# LeanIXMetricsFactsheetsPoster  
Program to query factsheets from LeanIX, filter out the complete ones, and post information on the number of incomplete factsheets to LeanIX metrics. Please be sure to only use Java 8 (1.8) when running the program as LeanIX's Java SDK doesn't work with the latest version of Java as of writing this.  
## How To Use  
To use the program, you will need your Workspace ID, API Token, and the name of the Measurement group you want to post to. The Workspace ID and API Tokens can be found in the API Tokens section on the Administrator Page. Measurement group names can be found in the "Metrics" tab under "Add-ons" or you can make your own. Once you have all that information and the jar file from releases, you can run the following command:  
`java -jar LeanIXMetricsFactsheetsPoster.jar {apiToken} {workspaceID} {measurementName}`

## Testing  
To test the program, you will need to first create a folder called "resources" under the "test" directory. 
In the folder, you will need to make a file called "TestsArguments.txt". On the 
first line in the file, put the API Token you'll be using. On the second line, 
enter the Workspace ID. You can now run the tests.

## Why it Needs to be Run before 7:00pm  
All the times in LeanIX Metrics are in UTC. UTC is 4 hours ahead of 
Eastern Daylight Time (Spring and Summer), and 5 hours ahead of Eastern
Standard Time (Fall and Winter). This means that posting the metrics
after 8:00pm in the Summer or 7:00pm in the Winter, would be posting
the metrics for tomorrow as UTC time would be past midnight. To prevent
confusion, the program will no allow posts to be made when the local time
zone date is different than the UTC date. To also help avoid confusion
more, we shouldn't run the program after 7:00pm. Even though we can do 
this during the Spring and Summer, we would have to change it for the Fall
and Winter.