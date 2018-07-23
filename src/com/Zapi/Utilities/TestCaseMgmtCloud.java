package com.Zapi.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.Configuration;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

public class TestCaseMgmtCloud extends BaseStep {
	String JIRA_BASEURL = "";
	String PROJECT_KEY = "";
	String RELEASE_VERSION = "";
	String TESTCYCLE_NAME = "";
	String ZAPI_URI_CONTEXT = "/public/rest/api/1.0/";
	String JIRA_API_CONTEXT = "/rest/api/2/";
	String PROJECT_ID = "";
	String JIRA_AUTHORIZATION = "";
	String ZAPI_BASEURL = "";
	String ACCESS_KEY = "";
	String SECRET_KEY = "";
	String USER_NAME = "";
	String VERSION_ID = "";
	String CYCLE_ID = "";
	Configuration config = null;

	Logger log = Logger.getLogger(this.getClass().getSimpleName());

	public TestCaseMgmtCloud() {
		try {
			config = initialize();
			JIRA_BASEURL = config.getString("jira.baseurl");
			PROJECT_KEY = config.getString("project.key");
			RELEASE_VERSION = config.getString("release.version");
			TESTCYCLE_NAME = config.getString("testcycle.name");
			JIRA_AUTHORIZATION = config.getString("jira.authorization");
			JIRA_AUTHORIZATION = "Basic " + JIRA_AUTHORIZATION;
			PROJECT_ID = config.getString("project.id");// getProjectId();
			ZAPI_BASEURL = config.getString("zapi.baseurl");
			ACCESS_KEY = config.getString("zapi.accesskey");
			SECRET_KEY = config.getString("zapi.secretkey");
			USER_NAME = config.getString("zapi.userName");
			VERSION_ID = getVersionId();
			CYCLE_ID = getCycleId();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("Unable to load properties file");
		}
	}

	private String doGet(String url) throws Exception {
		Client client = ClientBuilder.newClient();
		Response response = null;
		if (url.contains(ZAPI_BASEURL)) {
			ZFJCloudRestClient client1 = ZFJCloudRestClient.restBuilder(ZAPI_BASEURL, ACCESS_KEY, SECRET_KEY, USER_NAME)
					.build();
			JwtGenerator jwtGenerator = client1.getJwtGenerator();
			URI uri = new URI(url);
			int expirationInSec = 360;
			String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
			// Print the URL and JWT token to be used for making the REST call
			System.out.println("FINAL API : " + uri.toString());
			System.out.println("JWT Token : " + jwt);
			response = client.target(uri).request().header("Authorization", jwt).header("zapiAccessKey", ACCESS_KEY)
					.get();
		} else {
			response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE)
					.header("Authorization", JIRA_AUTHORIZATION).get();
		}
		if (response.getStatus() != 200) {
			throw new Exception("Unable to connect to JIRA");
		}
		return response.readEntity(String.class);
	}

	@SuppressWarnings("rawtypes")
	private String doPost(String url, String payload) throws Exception {
		Client client = ClientBuilder.newClient();
		Entity payloadEntity = Entity.json(payload);
		Response response = null;
		if (url.contains(ZAPI_BASEURL)) {
			ZFJCloudRestClient client1 = ZFJCloudRestClient.restBuilder(ZAPI_BASEURL, ACCESS_KEY, SECRET_KEY, USER_NAME)
					.build();
			JwtGenerator jwtGenerator = client1.getJwtGenerator();
			URI uri = new URI(url);
			int expirationInSec = 360;
			String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
			// Print the URL and JWT token to be used for making the REST call
			System.out.println("FINAL API : " + uri.toString());
			System.out.println("JWT Token : " + jwt);
			response = client.target(uri).request().header("Authorization", jwt).header("zapiAccessKey", ACCESS_KEY)
					.post(payloadEntity);
		} else {
			response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE)
					.header("Authorization", JIRA_AUTHORIZATION).post(payloadEntity);
		}
		if (response.getStatus() != 200) {
			if (response.getStatus() != 201) {
				throw new Exception("Unable to connect to JIRA");
			}
		}
		return response.readEntity(String.class);
	}

	@SuppressWarnings("rawtypes")
	private String doPut(String url, String payload) throws Exception {
		Client client = ClientBuilder.newClient();
		Entity payloadEntity = Entity.json(payload);
		Response response = null;
		if (url.contains(ZAPI_BASEURL)) {
			ZFJCloudRestClient client1 = ZFJCloudRestClient.restBuilder(ZAPI_BASEURL, ACCESS_KEY, SECRET_KEY, USER_NAME)
					.build();
			JwtGenerator jwtGenerator = client1.getJwtGenerator();
			URI uri = new URI(url);
			int expirationInSec = 360;
			String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec);
			// Print the URL and JWT token to be used for making the REST call
			System.out.println("FINAL API : " + uri.toString());
			System.out.println("JWT Token : " + jwt);
			response = client.target(uri).request().header("Authorization", jwt).header("zapiAccessKey", ACCESS_KEY)
					.put(payloadEntity);
		} else {
			response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE)
					.header("Authorization", JIRA_AUTHORIZATION).put(payloadEntity);
		}
		if (response.getStatus() != 200) {
			throw new Exception("Unable to connect to JIRA");
		}
		return response.readEntity(String.class);
	}

	// private String getProjectId() throws Exception {
	// String projectId = "";
	// String responseString = doGet(JIRA_BASEURL + JIRA_API_CONTEXT
	// + "project/" + PROJECT_KEY);
	// JSONObject obj = new JSONObject(responseString);
	// projectId = obj.getString("id");
	// if (projectId.isEmpty()) {
	// throw new Exception("Unable to find the Project " + PROJECT_KEY);
	// }
	// log.info("Project Id Retrived : " + projectId);
	// return projectId;
	// }

	private String createVersion() throws Exception {
		String versionId = "";
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date currentDate = new Date();
//		String newDate = dateFormat.format(currentDate);
		String payload = "{\"description\": \"New Version Created through Automation\",\"name\":\"" + RELEASE_VERSION
				+ "\",\"archived\": false,\"released\": " + config.getBoolean("released") + ",\"releaseDate\": \""
				+ config.getString("release.endDate") + "\",\"startDate\": \""+config.getString("release.startDate")+"\",\"project\":\"" + PROJECT_KEY + "\",\"projectId\": \"" + PROJECT_ID + "\"}";
		String responseString = doPost(JIRA_BASEURL + JIRA_API_CONTEXT + "version", payload);
		JSONObject obj = new JSONObject(responseString);
		versionId = obj.getString("id");
		log.info("Version Created : " + versionId);
		return versionId;
	}

	private String getVersionId() throws Exception {
		String versionId = "";
		String responseString = doGet(JIRA_BASEURL + JIRA_API_CONTEXT + "project/" + PROJECT_KEY + "/versions");
		JSONArray arr = new JSONArray(responseString);
		for (Object object : arr) {
			JSONObject obj = (JSONObject) object;
			if (RELEASE_VERSION.equals(obj.getString("name"))) {
				versionId = obj.getString("id");
			}
		}
		if (versionId.isEmpty()) {
			try {
				versionId = createVersion();
			} catch (Exception e) {
				throw new Exception("Unable to find the release version " + RELEASE_VERSION);
			}
		}
		log.info("Version Id Retrived : " + versionId);
		return versionId;
	}

	public String getCycleId() throws Exception {
		String cycleId = "";
		String responseString = doGet(
				ZAPI_BASEURL + ZAPI_URI_CONTEXT + "cycles/search?projectId=" + PROJECT_ID + "&versionId=" + VERSION_ID);
		JSONArray obj = new JSONArray(responseString);
		for (int i = 0; i < obj.length(); i++) {
			JSONObject jsonObject = obj.getJSONObject(i);
			if (TESTCYCLE_NAME.equals(jsonObject.getString("name"))) {
				cycleId = jsonObject.getString("id");
			}
		}
		if (cycleId.isEmpty()) {
			cycleId = createTestCycle(VERSION_ID);
		}
		log.info("Cycle Id Retrived : " + cycleId);
		return cycleId;
	}

	private String createTestCycle(String versionId) throws Exception {
		String cycleId = "";
		String payload = "{\"clonedCycleId\": \"\", \"name\": \"" + TESTCYCLE_NAME
				+ "\", \"build\": \"\",\"environment\": \"\",\"description\": \"Test Cycle created for Automation Framework Test Execution on"
				+ new Date() + "\",\"startDate\": \"\",\"endDate\": \"\",\"projectId\": \"" + PROJECT_ID
				+ "\",\"versionId\": \"" + versionId + "\"}";

		String responseString = doPost(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "cycle", payload);
		JSONObject obj = new JSONObject(responseString);
		cycleId = obj.getString("id");
		log.info("Test Cycle Created : " + cycleId);
		return cycleId;
	}

	public int getIssueId(String jiraKey) throws Exception {
		int issueId;
		String responseString = doGet(JIRA_BASEURL + JIRA_API_CONTEXT + "issue/" + jiraKey + "?fields=id");
		JSONObject obj = new JSONObject(responseString);
		issueId = Integer.valueOf(obj.getString("id"));
		log.info("Issue Id Retrived : " + issueId);
		return issueId;
	}

	private String createExecution(String cycleId, int issueId, String versionId) throws Exception {
		String payload = "{\"cycleId\": \"" + cycleId + "\",\"issueId\": \"" + issueId + "\",\"projectId\": \""
				+ PROJECT_ID + "\",\"versionId\": \"" + versionId + "\",\"assigneeType\": \"currentUser\"}";
		String responseString = doPost(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "execution", payload);
		JSONObject obj = new JSONObject(responseString);
		String executionId = obj.getJSONObject("execution").getString("id");
		log.info("Execution Created : " + executionId);
		return executionId;
	}

	private JSONObject getTestStepIds(String jiraKey, String executionId, int issueId) throws Exception {
		JSONObject stepIds = new JSONObject();
		List<String> testStepId = new ArrayList<String>();
		List<String> testResultIds = new ArrayList<String>();
		List<String> linkedIssueId = new ArrayList<String>();
		List<String> linkedIssueStatus = new ArrayList<String>();
		List<String> linkedIssueKey = new ArrayList<String>();
		JSONArray array = null;
		String response = doGet(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "stepresult/search?issueId=" + issueId
				+ "&executionId=" + executionId + "&isOrdered=true");
		JSONObject obje = new JSONObject(response);
		JSONArray arr = obje.getJSONArray("stepResults");
		for (Object object : arr) {
			JSONObject obj = (JSONObject) object;
			String testResultId = obj.getString("id");
			String testStep = obj.getString("stepId");
			try {
				array = obj.getJSONArray("defects");
				if (array.length() > 0) {
					linkedIssueId.add(Integer.toString(array.getJSONObject(0).getInt("id")));
					linkedIssueKey.add(array.getJSONObject(0).getString("key"));
					linkedIssueStatus.add(array.getJSONObject(0).getJSONObject("status").getString("name"));
				} else {
					linkedIssueId.add("");
					linkedIssueKey.add("");
					linkedIssueStatus.add("");
					log.info("There is no linked issue");
				}
			} catch (Exception e) {
				linkedIssueId.add("");
				linkedIssueKey.add("");
				linkedIssueStatus.add("");
				log.info("There is no linked issue");
			}
			testStepId.add(testStep);
			testResultIds.add(testResultId);
		}
		stepIds.put("testStepId", testStepId);
		stepIds.put("testResultId", testResultIds);
		stepIds.put("linkedIssueId", linkedIssueId);
		stepIds.put("linkedIssueKey", linkedIssueKey);
		stepIds.put("linkedIssueStatus", linkedIssueStatus);
		log.info("Step Ids Retrived : " + stepIds);
		return stepIds;
	}

	private String createNewBug(HashMap<String, String> attachmentPath, String testKey, String priority, String summary,
			String description, String steps, String assignee) throws Exception {
		String bugId = "";
		String testCaseId = "";
		System.out.println(steps);
		System.out.println(summary);
		System.out.println(description);
		System.out.println(testKey);
		System.out.println(priority);
		String payload = "{\"fields\": {\"project\":{\"key\": \"" + PROJECT_KEY + "\"},\"summary\": \"" + summary
				+ "\",\"description\": \"" + description
				+ "\",\"issuetype\": {\"name\": \"Bug\"},\"customfield_10412\":\"" + testKey
				+ "\",\"customfield_10409\":\"" + steps + "\",\"assignee\":{\"name\":\"" + assignee + "\",\"key\":\""
				+ assignee + "\"},\"priority\":{\"name\":\"" + priority + "\"}}}";
		String response = doPost(JIRA_BASEURL + JIRA_API_CONTEXT + "issue", payload);
		JSONObject obj = new JSONObject(response);
		bugId = obj.getString("id");
		log.info("New Bug Created ID : " + bugId);
		testCaseId = obj.getString("key");
		log.info("New Bug Created Key : " + testCaseId);
		attachScreenShotToIssue(attachmentPath, testCaseId, testKey);
		return bugId;
	}

	private void updateBug() {

	}

	private void deleteAttachment(String attachmentId) throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpDelete deleteRequest = new HttpDelete(JIRA_BASEURL + JIRA_API_CONTEXT + "attachment/" + attachmentId);
		deleteRequest.setHeader("Authorization", JIRA_AUTHORIZATION);
		deleteRequest.setHeader("X-Atlassian-Token", "nocheck");
		HttpResponse response = httpClient.execute(deleteRequest);
		System.out.println(response);
	}

	private HashMap<String, String> getAttachmentId(String linkedIssueKey) throws Exception {
		HashMap<String, String> details = new HashMap<String, String>();
		String response = doGet(JIRA_BASEURL + JIRA_API_CONTEXT + "issue/" + linkedIssueKey);
		String issueKey = new JSONObject(response).getString("key");
		JSONObject fieldObj = new JSONObject(response).getJSONObject("fields");
		JSONArray array = fieldObj.getJSONArray("attachment");
		String attachmentId = "";
		if (array.length() > 0) {
			attachmentId = fieldObj.getJSONArray("attachment").getJSONObject(0).getString("id");
		}
		details.put("issueKey", issueKey);
		details.put("attachmentId", attachmentId);
		return details;
	}

	private void attachScreenShotToIssue(HashMap<String, String> filePath, String jiraKey, String testKey)
			throws ClientProtocolException, IOException {
		File fileUpload = new File(filePath.get("@" + testKey));
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(JIRA_BASEURL + JIRA_API_CONTEXT + "issue/" + jiraKey + "/attachments");
		postRequest.setHeader("Authorization", JIRA_AUTHORIZATION);
		postRequest.setHeader("X-Atlassian-Token", "nocheck");
		MultipartEntityBuilder entity = MultipartEntityBuilder.create();
		entity.addPart("file", new FileBody(fileUpload));
		postRequest.setEntity(entity.build());
		HttpResponse response = httpClient.execute(postRequest);
		log.info("Attachment Added to the issue : " + response);
	}

	private String updateTestStepStatus(HashMap<String, String> attachmentPath, String priority, String jiraKey,
			String status, int issueId, String stepId, String executionId, String resultId, String linkedIssueId,
			String linkedIssueKey, String linkedIssueStatus, JSONObject stepDetailsObj) throws Exception {
		String changeStatus = "";
		int statusId = -1;
		String BugId = "";
		String payload = "";
		if (status.equals("passed")) {
			if (!linkedIssueStatus.equals("") && linkedIssueStatus.equalsIgnoreCase("Qa Testing")) {
				// This step need to be implemented
				// As of now we are not updating the Bug status automatically it
				// will be done by manually after validation
				updateBug();
			}
			statusId = 1;
		} else if (status.equals("failed")) {
			if (linkedIssueStatus.equals("")) {
				BugId = createNewBug(attachmentPath, jiraKey, priority, stepDetailsObj.getString("summary"),
						stepDetailsObj.getString("description"), stepDetailsObj.getString("steps"), USER_NAME);
				changeStatus = "New";
			} else {
				HashMap<String, String> details = getAttachmentId(linkedIssueKey);
				String attachmentId = details.get("attachmentId");
				if (!attachmentId.equals("")) {
					deleteAttachment(attachmentId);
				}
				attachScreenShotToIssue(attachmentPath, linkedIssueKey, jiraKey);
				// This step need to be implemented
				// As of now we are not updating the Bug status automatically it
				// will be done by manually after validation
				updateBug();
				changeStatus = "Screen Shot Modified";
			}
			statusId = 2;
		}
		if (!BugId.equals("")) {
			payload = "{\"defects\":[" + Integer.valueOf(BugId) + "],\"status\":{\"id\":\"" + statusId
					+ "\"},\"issueId\":" + issueId + ",\"stepId\":\"" + stepId + "\",\"executionId\":\"" + executionId
					+ "\"}";
		} else {
			payload = "{\"status\":{\"id\":\"" + statusId + "\"},\"issueId\":" + issueId + ",\"stepId\":\"" + stepId
					+ "\",\"executionId\":\"" + executionId + "\"}";
		}
		String response = doPut(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "stepresult/" + resultId, payload);
		log.info("Test Step Status Updated : " + response);
		return changeStatus;
	}

	public JSONObject updateExecutioStatusAndLogDefects(HashMap<String, String> attachmentPath, String priority,
			String jiraKey, String status, JSONObject stepDetailsObj) throws Exception {
		int issueId = getIssueId(jiraKey);
		JSONObject DataObject = new JSONObject();
		int statusId = -1;
		if (status.equalsIgnoreCase("passed")) {
			statusId = 1;
		} else {
			statusId = 2;
		}
		String executionId = createExecution(CYCLE_ID, issueId, VERSION_ID);
		String payload = "{\"status\":{\"id\":" + statusId + "},\"issueId\":" + issueId + ",\"projectId\":" + PROJECT_ID
				+ ",\"id\":\"" + executionId + "\"}";
		String responseString = doPut(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "execution/" + executionId, payload);
		System.out.println(responseString);
		System.out.println("Testcase executed successfully");
		JSONObject testStepIDObject = getTestStepIds(jiraKey, executionId, issueId);
		JSONArray testStepIds = testStepIDObject.getJSONArray("testStepId");
		JSONArray testResultIds = testStepIDObject.getJSONArray("testResultId");
		JSONArray linkedIssueId = testStepIDObject.getJSONArray("linkedIssueId");
		JSONArray linkedIssueKey = testStepIDObject.getJSONArray("linkedIssueKey");
		JSONArray linkedIssueStatus = testStepIDObject.getJSONArray("linkedIssueStatus");
		JSONArray changeStatus = new JSONArray();
		for (int i = 0; i < stepDetailsObj.getJSONArray("status").length(); i++) {
			String Status = updateTestStepStatus(attachmentPath, priority, jiraKey,
					stepDetailsObj.getJSONArray("status").getString(i), issueId, testStepIds.getString(i), executionId,
					testResultIds.getString(i), linkedIssueId.getString(i), linkedIssueKey.getString(i),
					linkedIssueStatus.getString(i), stepDetailsObj);
			changeStatus.put(Status);
		}
		JSONObject testObject = getTestStepIds(jiraKey, executionId, issueId);
		DataObject.put("testcaseid", jiraKey);
		DataObject.put("linkedissueid", testObject.getJSONArray("linkedIssueKey"));
		DataObject.put("issuestatus", testObject.getJSONArray("linkedIssueStatus"));
		DataObject.put("change", changeStatus);
		log.info("Execution status updated");
		return DataObject;
	}

	public void updateExecutioStatus(String jiraKey, String status) throws Exception {
		int issueId = getIssueId(jiraKey);
		updateExecutioStatus(jiraKey, issueId, status);		
	}
	
	public void updateExecutioStatus(String jiraKey,int issueId, String status) throws Exception {
		String executionId = createExecution(CYCLE_ID, issueId, VERSION_ID);
		int statusId = -1;
		switch (status.toLowerCase()) {
		case "passed":
			statusId = 1;
			break;
		case "failed":
			statusId = 2;
			break;
		case "wip":
			statusId = 3;
			break;
		case "blocked":
			statusId = 4;
			break;
		default:
			statusId = 5;
			break;
		}
		String payload = "{\"status\":{\"id\":" + statusId + "},\"issueId\":" + issueId + ",\"projectId\":" + PROJECT_ID
				+ ",\"id\":\"" + executionId + "\"}";
		String responseString = doPut(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "execution/" + executionId, payload);
		System.out.println(responseString);
		log.info("Execution status updated");
	}
	
}
