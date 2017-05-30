/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cloudbees.jenkins.plugins.bitbucket;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketRepositoryType;
import com.cloudbees.jenkins.plugins.bitbucket.client.BitbucketCloudApiClient;
import hudson.console.ConsoleNote;
import hudson.model.TaskListener;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.UserRemoteConfig;
import hudson.plugins.mercurial.MercurialSCM;
import hudson.scm.SCM;
import jenkins.plugins.git.AbstractGitSCMSource.SCMRevisionImpl;
import jenkins.scm.api.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests branch scanning with WireMockCase without the use of Mocks via Mockito
 */
public class MocklessBranchScanningTest extends WireMockCase {

    @ClassRule
    public static JenkinsRule j = new JenkinsRule();

    /** The username of the repository owner */
    private static final String repoOwner = "cloudbeers";
    /** The name of the repository */
    private static final String repoName = "potential-train";
    /** A branch in the repository */
    private static final String branchName = "master";

    /**
     * Tests the the URLs get resolved correctly
     * TODO: still uses external http request
     */
    /*@Test
    public void uriResolverTest() throws Exception {
        BitbucketSCMSource source = new BitbucketSCMSource("git", repoOwner, repoName);
        source.setBitbucketCloudUrl("http://localhost:8080");
        // When there is no checkout credentials set, https must be resolved
        assertEquals("https://bitbucket.org/cloudbeers/potential-train.git",
                source.getRemote(repoOwner, repoName, source.getRepositoryType()));

        source = new BitbucketSCMSource("hg", "alexjo", "bugrep-forest");
        source.setBitbucketCloudUrl("http://localhost:8080");

        // Resolve URL for Mercurial repositories
        assertEquals("https://bitbucket.org/alexjo/bugrep-forest",
                source.getRemote("alexjo", "bugrep-forest", source.getRepositoryType()));
    }*/

    /**
     * Tests the getGitRemoveConfigs() method of BitbucketSCMSource
     */
    @Test
    public void remoteConfigsTest() throws Exception {
        BitbucketSCMSource source = new BitbucketSCMSource("git", repoOwner, repoName);
        List<UserRemoteConfig> remoteConfigs = source.getGitRemoteConfigs(new BranchSCMHead("master", BitbucketRepositoryType.GIT));
        assertEquals(1, remoteConfigs.size());
        assertEquals("+refs/heads/master", remoteConfigs.get(0).getRefspec());
    }

    /**
     * Tests the retrieval of HEAD
     * TODO: still uses external http request
     */
    /*@Test
    public void retrieveTest() throws Exception {
        BitbucketSCMSource source = new BitbucketSCMSource("git", repoOwner, repoName);
        source.setBitbucketCloudUrl("http://localhost:8080"); // not used by -->

        BranchSCMHead head = new BranchSCMHead(branchName, BitbucketRepositoryType.GIT);
        SCMRevision rev = source.retrieve(head, getDumbListener()); // this call <--

        // Last revision on master must be returned
        assertEquals("a29f58db6282688a87d23060afbe0fdc9d9692c0", ((SCMRevisionImpl) rev).getHash());

    }*/

    /**
     * Tests branch scanning
     * TODO: still uses external http request
     */
    /*@Test
    public void scanTest() throws Exception {
        BitbucketSCMSource source = new BitbucketSCMSource("git", repoOwner, repoName);
        source.setBitbucketCloudUrl("http://localhost:8080");
        SCMHeadObserverImpl observer = new SCMHeadObserverImpl();
        source.fetch(observer, getDumbListener());

        // Only master must be observed
        assertEquals(3, observer.getBranches().size());
        assertEquals("master", observer.getBranches().get(0));
        assertEquals("origin-pr-branch", observer.getBranches().get(1));
        assertEquals("no-jenkinsfile", observer.getBranches().get(2));
    }*/

    /**
     * Tests scanning pull requests
     * TODO: still uses external http request
     */
    /*@Test
    public void scanTestPullRequests() throws Exception {
        BitbucketSCMSource source = new BitbucketSCMSource("git", repoOwner, repoName);
        source.setBitbucketCloudUrl("http://localhost:8080");
        SCMHeadObserverImpl observer = new SCMHeadObserverImpl();
        source.fetch(observer, getDumbListener());

        // Only branch1 and my-feature-branch PR must be observed
        assertEquals(3, observer.getBranches().size());
        assertEquals("master", observer.getBranches().get(0));
        assertEquals("origin-pr-branch", observer.getBranches().get(1));
        assertEquals("no-jenkinsfile", observer.getBranches().get(2));
    }*/

    /**
     * Tests that SCM sources are build correctly
     */
    @Test
    public void SCMTest() throws Exception {
        BitbucketSCMSource source = new BitbucketSCMSource("git", repoOwner, repoName);
        SCM scm = source.build(new BranchSCMHead("master", BitbucketRepositoryType.GIT));
        assertTrue("SCM must be an instance of GitSCM", scm instanceof GitSCM);

        source = new BitbucketSCMSource("hg", "alexjo", "bugrep-forest");
        scm = source.build(new BranchSCMHead("master", BitbucketRepositoryType.MERCURIAL));
        assertTrue("SCM must be an instance of MercurialSCM", scm instanceof MercurialSCM);
    }

    /**
     * Tests scanning of repos that have a branch name with characters that need to be uri encoded
     */
    @Test
    public void testCheckPathExists () throws Exception {
        BitbucketCloudApiClient api = new BitbucketCloudApiClient("cloudbeers",
                "potential-train", null, "http://localhost:8080");

        assertTrue(api.checkPathExists("master", "Jenkinsfile"));
        assertTrue(api.checkPathExists("master", "README.md"));
        assertFalse(api.checkPathExists("no-jenkinsfile", "Jenkinsfile"));
        assertTrue(api.checkPathExists("no-jenkinsfile", "README.md"));
    }

    /**
     * SCMHeadObserver implementation
     */
    private final class SCMHeadObserverImpl extends SCMHeadObserver {
        public List<String> branches = new ArrayList<String>();
        public void observe(SCMHead head, SCMRevision revision) {
            branches.add(head.getName());
        }
        public List<String> getBranches() {
            return branches;
        }
    }

    /**
     * Creates a TaskListener that does nothing
     * @return a dumb TaskListener
     */
    private TaskListener getDumbListener () {
        return new TaskListener() {
            private PrintStream dumb = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException { /* do nothing */ }
            });

            @Override
            public PrintStream getLogger() { return dumb; }

            @Override
            public void annotate(ConsoleNote ann) throws IOException {}

            @Override
            public void hyperlink(String url, String text) throws IOException {}

            @Override
            public PrintWriter error(String msg) { return new PrintWriter(dumb); }

            @Override
            public PrintWriter error(String format, Object... args) { return new PrintWriter(dumb); }

            @Override
            public PrintWriter fatalError(String msg) { return new PrintWriter(dumb); }

            @Override
            public PrintWriter fatalError(String format, Object... args) { return new PrintWriter(dumb); }
        };
    }

}
