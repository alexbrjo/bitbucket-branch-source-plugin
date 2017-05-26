package com.cloudbees.jenkins.plugins.bitbucket.wiremock;

import com.cloudbees.jenkins.plugins.bitbucket.client.BitbucketCloudApiClient;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Alex Johnson
 */
public class WireMockTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void swag() throws IOException, InterruptedException{
        stubFor(get(urlEqualTo("/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{book book book}")));

        BitbucketCloudApiClient api = new BitbucketCloudApiClient("cloudbeers",
                "potential-train", null, "http://localhost:8089");

        assertTrue(api.checkPathExists("master", "Jenkinsfile"));

        verify(getRequestedFor(urlMatching("/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile")));
    }

}
