Testing Approach PoC
====================
Below is the basic concept. The tests get run against a local mock and can be confirmed against the
server. This could be improved by created maven targets to test/confirm.
```
if (CONFIRMATION_FLAG)
    ip = https://public.api.com
else
    ip = http://localhost:PORT
```


Mock API
--------
```
function getHttpStatus ()
    switch (uri)
        case "/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile":
        case "/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md":
        case "/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md":
            return 200
        default:
            return 404
```

Actual API
----------
200: [https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile]
200: [https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md]
404: [https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/Jenkinsfile]
200: [https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md]
