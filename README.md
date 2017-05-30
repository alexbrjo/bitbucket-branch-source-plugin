bitbucket branch source plugin - WireMock Demo
==============================================
1. Clone this repo, delete current captured responses and open project in IDE.
```
git clone https://github.com/alexbrjo/bitbucket-branch-source-plugin.git
cd bitbucket-branch-source-plugin/
git checkout dev-wiremock
rm -f src/test/resources/__files/*.json src/test/resources/mappings/*.json
open ./ -a IntelliJ\ IDEA\ CE # if you have IntelliJ IDEA Community Edition
```

2. Delete target and run tests with capture flag
```
mvn -DargLine="-DWIREMOCK_CAPTURE=true" clean install
```

3. Delete target and run tests without capture flag (Turn off wifi if you want to confirm)
```
mvn -DargLine="-DWIREMOCK_CAPTURE=false" clean install
```
