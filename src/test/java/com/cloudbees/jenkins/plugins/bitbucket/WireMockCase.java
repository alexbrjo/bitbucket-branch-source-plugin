package com.cloudbees.jenkins.plugins.bitbucket;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

/**
 * The suite of classes to be run against the Bitbucket API captured by WireMockCase
 * @author Alex Johnson
 */
public abstract class WireMockCase {

    /** The WireMockCase server */
    private WireMockServer server;
    /** If WireMockCase should act as a proxy and capture Bitbucket responses */
    private boolean captureResponses = false;

    /**
     * Sets up the WireMockCase server
     */
    @Before
    public void setUp() {
            server = new WireMockServer(8080);
            if (captureResponses) {
                server.stubFor(any(urlMatching(".*"))
                        .willReturn(aResponse().proxiedFrom("https://bitbucket.org")));
            }
            server.start();
    }

    /**
     * Shuts down the server
     */
    @After
    public void tearDown() {
        server.stop();
    }
}
