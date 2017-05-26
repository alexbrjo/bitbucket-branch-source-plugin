package com.cloudbees.jenkins.plugins.bitbucket.wiremock;

import com.cloudbees.jenkins.plugins.bitbucket.BranchSourceConfig;
import com.cloudbees.jenkins.plugins.bitbucket.client.BitbucketCloudApiClient;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests the BitbucketCloudApiClient using WireMock
 * @author Alex Johnson
 */
public class BitbucketHttpStatusTest {
    /** The WireMock server */
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    /**
     * Set up responses just as they would happen on the server
     */
    @Before
    public void setUp () {
        if (!BranchSourceConfig.CONFIRMATION_RUN) {
            stubFor(get(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "text/plain")
                            .withBody("node {\n" +
                                    "    checkout scm\n" +
                                    "    echo \"hello from pipeline\"\n" +
                                    "}")));
            stubFor(get(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "text/plain")
                            .withBody("# Potential Train\n" +
                                    "\n" +
                                    "This is a public repository with a Jenkinsfile")));
            stubFor(get(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "text/plain")
                            .withBody("# Potential Train\n" +
                                    "\n" +
                                    "This is a public repository with a Jenkinsfile"))); // lies
            stubFor(get(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/Jenkinsfile"))
                    .willReturn(aResponse()
                            .withStatus(404)
                            .withHeader("Content-Type", "text/plain")
                            .withBody("Not Found")));
        }
    }

    /**
     * Tests the method checkPathExists() of class BitbucketCloudApiClient
     */
    @Test
    public void testCheckPathExistsWhenPathExists() throws IOException, InterruptedException{
        BitbucketCloudApiClient api = new BitbucketCloudApiClient("cloudbeers",
                "potential-train", null, BranchSourceConfig.CONFIRMATION_RUN ? "https://bitbucket.org" : "http://localhost:8089");

        assertTrue(api.checkPathExists("master", "Jenkinsfile"));
        assertTrue(api.checkPathExists("master", "README.md"));
        assertFalse(api.checkPathExists("no-jenkinsfile", "Jenkinsfile"));
        assertTrue(api.checkPathExists("no-jenkinsfile", "README.md"));
    }

    /**
     * If WireMock was used, confirm that the correct urls were called
     */
    @After
    public void confirmCalled () {
        if (!BranchSourceConfig.CONFIRMATION_RUN) {
            verify(getRequestedFor(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile")));
            verify(getRequestedFor(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md")));
            verify(getRequestedFor(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/Jenkinsfile")));
            verify(getRequestedFor(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md")));
        }
    }

}
