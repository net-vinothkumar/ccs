### Web Page Analyzer

### Design Decisions :

Following are the solutions I had  and implemented the first solution.

### 1.Dynamically update the web client page with the verification link details when the user scrolls the web page.

	Pros :
		a.The response will be faster , user can see the web page details + a part of verification link results [i.e limited 10 records per scroll]
		
		b.If the user wants to see further verification link results, user can scroll to view the 
		dynamically updated results.
	Cons :
		We have to design intuitively the web page so that user can know to get more results user has to scroll.


### 2.Pagination :

	Pros :
		The response will be faster , user can see the web page details + a part of verification links results and also compare the different results.
	Cons :
		User has to click the next page button to view the verification link results.
		User has to remember which page has the a particular link results.
		
### 3.Complete the collection of web page details and verifying the links in a single request :

	Pros :
		In single view the user can see the entire results.
	Cons :
		Though the user can view the entire results in one request ,the user has to wait for a long time to view the results.

#### Installations

* Install JDK 8 + Kotlin 1.2.10
* Gradle
* Hapi server (node v9.2.1)
* Vision + handlebars template engine web client.

```
#### How to run REST server

# cd webanalyzer-backend
# gradle bootRun

 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.9.RELEASE)

2018-01-07 20:52:02.655  INFO 8200 --- [           main] d.c.c.WebPageAnalyzerApplicationKt       : Starting WebPageAnalyzerApplicationKt on users-MacBook-Pro.local with PID 8200 (/Users/user/workspace/scout24/git/ccs/webanalyzer-backend/build/classes/main started by user in /Users/user/workspace/scout24/git/ccs/webanalyzer-backend)
....
2018-01-07 20:52:07.810  INFO 8200 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
2018-01-07 20:52:07.838  INFO 8200 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2018-01-07 20:52:08.265  INFO 8200 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2018-01-07 20:52:08.274  INFO 8200 --- [           main] d.c.c.WebPageAnalyzerApplicationKt       : Started WebPageAnalyzerApplicationKt in 6.463 seconds (JVM running 

#### How to run web client

# cd  webanalyzer-frontend
# npm install
# node server.js

Server running at: http://localhost:3000

In browser we can access the web page client : http://localhost:3000

```



#### Improvements / TODOs
* Making the domain links verification cacheable + persistence.
* Asynchronously loading the links verification data from web client.
* Hardcoded the web page analysis REST server url in web client code.
* Extract the Web Page Analyzer component as small sub project.
* Need to write more test cases for different use cases.
* Refactor the web client code.
* Implement the error page in web client.

#### High level design/solution approach
* Built single component using Spring boot (Kotlin) as REST service with two end points. REST BACKEND SERVER
```
Swagger : http://localhost:8080/swagger-ui.html#

POST : http://localhost:8080/analyze

Request Body :
{
  "url": "string"
}

Response :
{
  "containsLoginForm": true,
  "headingTags": [
    {
      "count": 0,
      "headerName": "string"
    }
  ],
  "htmlVersion": "string",
  "numOfExternalLinks": 0,
  "numOfInternalLinks": 0,
  "resourceDetails": [
    {
      "httpResponseCode": 0,
      "link": "string",
      "webPageStatus": "string"
    }
  ],
  "title": "string",
  "uri": "string"
}

GET : http://localhost:8080/verifyLinks?url=http://test.com&startRecord=0&limit=10

Response :

[
  {
    "httpResponseCode": 0,
    "link": "string",
    "webPageStatus": "string"
  }
]

Note : 
'url' = web page that needs to be analyzed, 
'startRecord' = from which record the client wants from the server to get the verification link details.i.e first time it will be 0 ,then 10 ,20 ,30 ..so that the client can get the next chunk of records.[as an improvement this should be made as configurable.]
'limit' = number of records in each request that need to returned by server.i.e server will only verify next 10 links and send the results.

```
* HAPI JS Frontend Server communicates with REST Backend Server and post/get the request ,receive the JSON response and prepare the view.
* Defined the domain model to represent the analysis report result.
* Used Jsoup for traversing the HTML DOM tree
* Used the Hapi server as a web client server to communicate with backend REST server, if suppose team decided to change the UI technology it will not affect the backend REST server.Backend REST server is completely independent and decoupled from UI server.
* UI developed using HTML + Bootstrap + JQuery.







