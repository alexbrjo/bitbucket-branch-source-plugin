bitbucket branch source plugin - WireMock Demo
==============================================
1. Clone this repo, delete current captured responses and open project in IDE
```
git clone https://github.com/alexbrjo/bitbucket-branch-source-plugin.git
cd bitbucket-branch-source-plugin/
git checkout dev-wiremock
rm -f src/test/resources/__files/*.json src/test/resources/mappings/*.json
open ./ -a IntelliJ\ IDEA\ CE # if you have IntelliJ IDEA Community Edition
```
In test/java/.../WireMockCase.java change the `captureResponses` flag to `true`
``` private static boolean captureResponses = true; ```

2. Delete target and run tests
``` mvn clean install ```

3. Check target/test-classes/mappings and __files to see the captured API responses

4. To use those captured responses in the next test flip the `captureResponses` flag back to `false`
``` private static boolean captureResponses = false; ```

5. Delete target and run tests (Turn off wifi if you want to confirm)
``` mvn clean install ```
