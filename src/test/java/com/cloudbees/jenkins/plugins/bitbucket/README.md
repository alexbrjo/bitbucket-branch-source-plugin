PoC for Contract Testing Approach
=================================
Below is the basic concept. The tests get run against a local mock and can be confirmed against the
server. This could be improved by creating Maven targets to test/confirm.
```
if (CONFIRMATION_FLAG)
    ip = https://public.api.com
else
    ip = http://localhost:PORT
```

Mock API
--------
Something like this could be setup from [scratch](httpserver) or with [WireMock](wiremock).
```
function setHTTPstatus ()
    switch (uri)
        case "/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile":
        case "/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md":
        case "/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md":
            status = 200
        default:
            status = 404
```

Actual Cloud API
----------------
* `200` [master/Jenkinsfile](https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile)
* `200` [master/README.md](https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md)
* `404` [no-jenkinsfile/Jenkinsfile](https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/Jenkinsfile)
* `200` [no-jenkinsfile/README.md](https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md)
