package com.cloudbees.jenkins.plugins.bitbucket.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * A mocked BitbucketCloudApi. Don't use this use BitbucketLocalServlet
 *
 * @author Alex Johnson
 */
public class MockBitbucketCloudApi implements Runnable {
    /** The default port to listen to */
    private static final int DEFAULT_PORT = 9090;
    /** List of open servers */
    private static List<MockBitbucketCloudApi> services = new LinkedList<MockBitbucketCloudApi>();

    /**
     * Starts a new Mock api client with the default port
     */
    public static int start () {
        return start(DEFAULT_PORT);
    }

    /**
     * Starts a new Mock api client
     * @param reqPort the port to listen to
     */
    public static int start (int reqPort) {
        MockBitbucketCloudApi m = new MockBitbucketCloudApi(reqPort);
        m.run();
        services.add(m);
        return m.getPort();
    }

    /**
     * Closes an existing Mock api client
     * @param reqPort the port to listen to
     */
    public static boolean close (int reqPort) {
        for (MockBitbucketCloudApi service : services) {
            if (service.getPort() == reqPort) {
                service.close();
                return true;
            }
        }
        return false;
    }

    /** ************************************************************************************ **/

    /** The HTTP server */
    private HttpServer server;
    /** The port serving the api */
    private int port;

    /**
     * Creates a new MockBitbucketCloudApi for a starting port
     *
     * @param startPort
     */
    private MockBitbucketCloudApi(final int startPort) {
        this.port = startPort;
    }

    /**
     * Runs the server
     */
    @Override
    public void run() {
        int tries = 0;
        while (tries < 10) {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/api/", new ManualRequestHandler());
                server.start();
                return;
            } catch (IOException e) {
                tries++;
            }
        }
    }

    /**
     * Handles API calls, this would be done a lot more gracefully
     */
    private class ManualRequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            int status = getStatus(httpExchange.getRequestURI().toString());
            String json = "{'http_status': "  + status + "}";

            httpExchange.sendResponseHeaders(status, json.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        }

        private int getStatus (String uri) {
            switch (uri) {
                case "/api/1.0/repositories/cloudbeers/potential-train/raw/master/Jenkinsfile":
                case "/api/1.0/repositories/cloudbeers/potential-train/raw/master/README.md":
                case "/api/1.0/repositories/cloudbeers/potential-train/raw/no-jenkinsfile/README.md":
                    return 200;
                default:
                    return 404;
            }
        }
    }

    /**
     * Gets the port that is being listened to
     *
     * @return the port listening to
     */
    public int getPort() {
        return port;
    }

    /**
     * Stops the server
     */
    public void close () {
        server.stop(0);
    }
}

