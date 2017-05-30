package com.cloudbees.jenkins.plugins.bitbucket;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * The suite of classes to be run against the Bitbucket API captured by WireMockCase
 * @author Alex Johnson
 */
public abstract class WireMockCase {

    /** The WireMockCase server */
    private static WireMockServer server;
    /** If WireMockCase should act as a proxy and capture Bitbucket responses */
    private static boolean captureResponses = false;

    /**
     * Sets up the WireMockCase server
     */
    @BeforeClass
    public static void setUp() {
        server = new WireMockServer(8080);
        server.enableRecordMappings(new SingleRootFileSource("src/test/resources/mappings"),
                new SingleRootFileSource( "src/test/resources/__files"));
        if (captureResponses) {
            server.stubFor(get(urlMatching(".*"))
                    .willReturn(aResponse().proxiedFrom("https://bitbucket.org")));
        }
        server.start();
    }

    /**
     * Shuts down the server
     */
    @AfterClass
    public static void tearDown() {
        server.shutdown();
    }
}
