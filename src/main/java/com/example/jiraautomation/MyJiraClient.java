package com.example.jiraautomation;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MyJiraClient {

    private static final String USER_NAME = "er.prashantsingh1410@gmail.com";
    private static final String JIRA_URL = "https://team-prashantsingh.atlassian.net";
//    private static final String PASS = "Pgd@388842";
    private static final String PASS = "7K7iQlitetr5Zgxmm2YJ9470";
    private String username;
    private String password;
    private String jiraUrl;
    private JiraRestClient restClient;

    private MyJiraClient(String username, String password, String jiraUrl) {
        this.username = username;
        this.password = password;
        this.jiraUrl = jiraUrl;
        this.restClient = getJiraRestClient();
    }

    public static void test() throws IOException {

        MyJiraClient myJiraClient = new MyJiraClient(USER_NAME, PASS, JIRA_URL);
        final String issueKey = myJiraClient.createIssue("JEA", 10001L, "Issue created for test");
        myJiraClient.updateIssueDescription(issueKey, "This is description from my Jira Client");
        Issue issue = myJiraClient.getIssue(issueKey);
        System.out.println(issue.getDescription());

        myJiraClient.voteForAnIssue(issue);

        System.out.println(myJiraClient.getTotalVotesCount(issueKey));

        myJiraClient.addComment(issue, "This is comment from my Jira Client");

        List<Comment> comments = myJiraClient.getAllComments(issueKey);
        comments.forEach(c -> System.out.println(c.getBody()));

        myJiraClient.deleteIssue(issueKey, true);

        myJiraClient.restClient.close();
    }

    private String createIssue(String projectKey, Long issueType, String issueSummary) {

        IssueRestClient issueClient = restClient.getIssueClient();

        IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).build();

        return issueClient.createIssue(newIssue).claim().getKey();
    }

    private Issue getIssue(String issueKey) {
        return restClient.getIssueClient().getIssue(issueKey).claim();
    }

    private void voteForAnIssue(Issue issue) {
        restClient.getIssueClient().vote(issue.getVotesUri()).claim();
    }

    private int getTotalVotesCount(String issueKey) {
        BasicVotes votes = getIssue(issueKey).getVotes();
        return votes == null ? 0 : votes.getVotes();
    }

    private void addComment(Issue issue, String commentBody) {
        restClient.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
    }

    private List<Comment> getAllComments(String issueKey) {
        return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false)
          .collect(Collectors.toList());
    }

    private void updateIssueDescription(String issueKey, String newDescription) {
        IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
        restClient.getIssueClient().updateIssue(issueKey, input).claim();
    }

    private void deleteIssue(String issueKey, boolean deleteSubtasks) {
        restClient.getIssueClient().deleteIssue(issueKey, deleteSubtasks).claim();
    }

    private JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
          .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}