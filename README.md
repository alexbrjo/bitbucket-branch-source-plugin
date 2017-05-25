Bitbucket Public API Testing Approach
=====================================
Given you must check against the Public API and have Unit Test coverage over all methods (including API calls) on every run.

### Criteria and considerations for [bitbucket branch source plugin](https://github.com/jenkinsci/bitbucket-branch-source-plugin/)
* Ammount of work: quality is a priority with this plugin because a lot of people could be using this plugin, so ammount of work may be less important then having stable and efficient tests
* Unit Tests Offline: this plugin requires connectivity so it's reasonable to have tests that directly aganist the API. Offline tests would be useful if there was a local internet outage or Bitbucket server outages. 

|Strategy |Ammount of work |Unit Tests Offine |
|---	                |---	|---	|
|[Directly Against API](#directly-against-api)|Less |No   |
|[Test Against Custom Mock](#test-against-confirmed-mock-api)|Most |Yes	  |
|[Wire Mock](#test-against-confirmed-mock-api)|More |Yes  |
|[REST Assured](#test-against-confirmed-mock-api)|More |Yes  |
|[Docker Acceptance Tests](#acceptance-tests-with-docker)|Extra| Yes |

Directly Against API
--------------------
Tests rely directly on Public API could not run in offline mode
1. `Tests` -> `Public API`: run tests against 

Test Against Confirmed Mock API
-------------------------------
Tests do not rely on internet connectivity. Could use [WireMock](http://wiremock.org/docs/request-matching/), [REST assured](https://github.com/rest-assured/rest-assured/wiki/Usage) or a custom Mocked REST API (Jackson was suggested for data binding). 
1. `Mock API` -> `Public API`: confirm that Mock API correctly mocks Public API
2. `Tests` -> `Mock API`: run tests against Mock API

Acceptance Tests with Docker
----------------------------
Run API tests before release with Docker Container. This would only work with Bitbucket server and there would still be some hurdles because [Bitbucket Server is not free](https://www.atlassian.com/software/bitbucket/download), but the Atlassian SDK does provide a way to [test against it](https://developer.atlassian.com/bitbucket/server/docs/latest/). Confirming that certain use cases work against the Bitbucket Server may be enough to be confident that it would work with Bitbucket cloud too.

Conclusion
----------
The strongest approach to is to Unit Test against a Mock API (using a [real Bitbucket cloud account](https://bitbucket.org/cloudbeers/)) and later assess the benefit of more expensive Acceptance Tests. 
* When Altassian inevitably makes API changes, tests wouldn't fail but confirmation routine would show that the Public API specs have changed 
* Protection against random outages
* Not abusing Altassian's [generous](https://confluence.atlassian.com/bitbucket/rate-limits-668173227.html) rate limits
* More of a time commitment 

### Other Sources
1. [Stackoverflow - test internal implementation or public behaviour?](https://stackoverflow.com/questions/856115/should-one-test-internal-implementation-or-only-test-public-behaviour)
2. [Testing a REST API (advocates REST Assured)](http://www.baeldung.com/integration-testing-a-rest-api)
3. [WireMock: Mock your REST APIs](https://dzone.com/articles/wiremock-mock-your-rest-apis)
