### Web Page Analyzer

### Design Decisions :

Following are the solutions I had  and implemented the first solution.

### 1.Dynamically update the web client page with the verification link details when the user scrolls the web page.

	Pros :
		a.The response will be faster , user can see the web page details + a part of verification link results [i.e limited 10 records / scroll]
		
		b.If the user wants to see further verification link results, user can scroll to view the 
		dynamically updated results.
	Cons :
		We have to design intuitively the web page so that user can know to get more results user has to scroll.


### 2.Pagination :

	Pros :
		The response will be faster , user can see the web page details + a part of verification links results.
	Cons :
		User has to click the next page button to view the verification link results.
		User has to remember which page has the a particular link results
		
### 3.Complete the entire task in single request :
Complete the collection of web page details and verifying the links in a single request

Pros :
	In single view the user can see the entire results.
Cons :
	Though the user can view the entire results in one request ,the user has to wait for a long time to view the results

#### Installations

* Install JDK 8 + Kotlin 1.2.10
* Gradle
* Hapi server (node v9.2.1)
* Vision + handlebars template engine web client.

```
#### How to run REST server

# cd webanalyzer-backend
# gradle bootRun

#### How to run web client

# cd  webanalyzer-frontend
# npm install
# node server.js
```



#### Limitations/TODOs
* Making the domain links verification cacheable.
* Asynchronously loading the links verification data from web client.
* Hardcoded the web page analysis REST server url in web client code.

#### High level design/solution approach
* Built single component using Spring boot (Kotlin) as REST service.
* Defined the domain model to represent the analysis report result.
* Used Jsoup for traversing the HTML DOM tree
* Used the Hapi server as a web client server to communicate with backend REST server, if suppose team decided to change the UI technology it will not affect the backend REST server.Backend REST server is completely independent and decoupled from UI server.






