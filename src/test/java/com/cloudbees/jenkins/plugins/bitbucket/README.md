Testing Approach PoC
====================

```
if (CONFIRMATION_FLAG)
    ip = https://bitbucket.org
else
    ip = http://localhost:8080
```


```
function httpStatus ()
    switch (uri)
        case "[/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile][0]":
        case "[/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md][1]":
        case "[/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md][2]":
            return 200
        default:
            return 404
```

[0] : "https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile"
[1] : "https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md"
[2] : "https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/Jenkinsfile"
[3] : "https://bitbucket.org/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md"
