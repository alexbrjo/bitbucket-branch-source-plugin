package com.cloudbees.jenkins.plugins.bitbucket.httpserver;

import com.cloudbees.jenkins.plugins.bitbucket.client.BitbucketCloudApiClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Alex Johnson
 */
public class BitbucketHttpStatusTest {

    /** If the test are being run against the Public API or fake API */
    private static final boolean CONFIRMATION_FLAG = false;

    /** The port this test instance is using */
    private int port;

    /**
     * Sets up the Mocked api server
     */
    @Before
    public void setUp () {
        port = MockBitbucketCloudApi.start();
    }

    /**
     * Tears down the Mocked api server
     */
    @After
    public void tearDown () throws Exception{
        MockBitbucketCloudApi.close(port);
    }

    /**
     * Tests if a path exists in the api
     */
    @Test
    public void testCheckPathExists() throws IOException, InterruptedException {
        BitbucketCloudApiClient api = new BitbucketCloudApiClient("cloudbeers", "potential-train",
                null, CONFIRMATION_FLAG ? "https://bitbucket.org" : "http://localhost:" + port);

        // both files exist
        assertTrue(api.checkPathExists("master", "Jenkinsfile"));
        assertTrue(api.checkPathExists("master", "README.md"));

        // Jenkinsfile does not exist
        assertFalse(api.checkPathExists("no-jenkinsfile", "Jenkinsfile"));
        assertTrue(api.checkPathExists("no-jenkinsfile", "README.md"));
    }

}
