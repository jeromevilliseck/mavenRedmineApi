/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import bull.internal.issues.Upsource;
import bull.logger.Logger;

/**
 * Class with all the Upsource's extractor methods
 *
 * @author Bull/Atos
 */
public class UpsourceUtils {
    private static final String RESULT = "result";
    private final String credentialBase64 = Base64.getEncoder().encodeToString("ghe:ghe".getBytes());
    private static final String WARNING_MESSAGE = "Data extraction failure, reason:";
    private String connectorResponse;

    /**
     * Performs a request to Upsource.
     */
    private void doRequest(String method, String paramsJson) {
        String url = "http://dev.sics.bull.fr/upsource/~rpc/" + method;
        // Perform a POST request to pass a payload in the body.
        PostMethod post = new PostMethod(url);
        // Basic authorization header. If not provided, the request will be executed with guest permissions.
        post.addRequestHeader("Authorization", "Basic " + credentialBase64);
        post.setRequestBody(paramsJson); /*SonarLint minor issue*/

        // Execute and return the response body.
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
        } catch (IOException e) {
            Logger.getInstance().warn(WARNING_MESSAGE, e);
        }
        try {
            this.connectorResponse = post.getResponseBodyAsString();
        } catch (IOException e) {
            Logger.getInstance().warn(WARNING_MESSAGE, e);
        }
    }

    /**
     * Gets from Upsource the project name, ID, date and message of a revision (commit)
     * to store it in the Revisions List passed in parameter.
     * @param upsource  List of revisions
     */
    public void revisionsListFiltered(List<Upsource> upsource){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateRef = new Date();
        String currentDate= dateFormat.format(dateRef);
        Date limitDateRef = new Date(dateRef.getTime() - 86400000);
        String limitDate = dateFormat.format(limitDateRef);
        // Perform a raw request: string -> string.
        doRequest("getRevisionsListFiltered",
                "{\"projectId\": \"bms\", \"limit\": 1000, \"query\": \"date: "
                + limitDate
                +".."
                + currentDate
                + "\"}");
        String response = connectorResponse;
        JSONObject revList = new JSONObject(response).getJSONObject(RESULT);
        JSONArray revision = revList.getJSONArray("revision");
        long date;
        for (int i = 0; i < revision.length(); i++) {
            JSONObject currentRev = revision.getJSONObject(i);
            date = (long) currentRev.get("revisionDate") / 1000;
            upsource.add(new Upsource(currentRev.getString("projectId"), currentRev.getString("revisionId"),
                    convertDateToString((int) date), currentRev.getString("revisionCommitMessage")));
        }
    }

    /**
     * Checks if a revision has a review. If it does, gets the review ID and the state
     * from Upsource to store them in the Revisions List passed in parameter.
     * @param upsourceData List of revisions
     */
    public void revisionReviewInfo(List<Upsource> upsourceData){
        final String REVIEWINFO = "reviewInfo";
        String response;
        JSONObject reviewList;
        for (Upsource anUpsourceData : upsourceData) {
            doRequest("getRevisionReviewInfo", "{\"projectId\": \"bms\", \"revisionId\": "
                    + "\""
                    + anUpsourceData.getRevisionId()
                    + "\" }");
            response = connectorResponse;
            reviewList = new JSONObject(response).getJSONObject(RESULT).getJSONArray(REVIEWINFO).getJSONObject(0);
            if (reviewList.length() != 0) {
                anUpsourceData.setState(reviewList.getJSONObject(REVIEWINFO).getInt("state"));
                anUpsourceData.setReviewId(reviewList.getJSONObject(REVIEWINFO).getJSONObject("reviewId").getString("reviewId"));
            }
        }
    }

    /**
     * Gets from Upsource the branch of a revision to store it in the revisions list passed in parameter.
     * @param upsourceData List of revisions
     */
    public void revisionBranches(List<Upsource> upsourceData){
        String response;
        String branch;
        for (Upsource anUpsourceData : upsourceData) {
            doRequest("getRevisionBranches", "{\"projectId\": \"bms\", \"revisionId\": "
                    + "\""
                    + anUpsourceData.getRevisionId()
                    + "\" }");
            response = connectorResponse;
            branch = new JSONObject(response).getJSONObject(RESULT).getJSONArray("branchName").getString(0);
            anUpsourceData.setBranch(branch);
        }
    }

    /**
     * If a revision has a review, gets from Upsource the date of the review and the update date (if there is an update)
     * to store it int the revisions list passed in parameter.
     * @param upsourceData List of revisions
     */
    public void reviews(List<Upsource> upsourceData){
        String response;
        long createdAt;
        long updatedAt;
        for (Upsource anUpsourceData : upsourceData) {
            // Check if the revision has a review
            if (anUpsourceData.getReviewId() != "") {
                doRequest("getReviewDetails", "{\"projectId\": \"bms\", \"reviewId\": "
                        + "\""
                        + anUpsourceData.getReviewId()
                        + "\" }");
                response = connectorResponse;
                createdAt = (long) new JSONObject(response).getJSONObject(RESULT).get("createdAt") / 1000;
                anUpsourceData.setReviewDate(convertDateToString((int) createdAt));
                updatedAt = (long) new JSONObject(response).getJSONObject(RESULT).get("updatedAt") / 1000;
                anUpsourceData.setReviewUpdateDate(convertDateToString((int) updatedAt));
            }
        }
    }

    private String convertDateToString(int upsourcedate) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(1533731222, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return dateTime.format(formatter);
    }
}
