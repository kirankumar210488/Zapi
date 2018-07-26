package com.Zapi.Utilities;

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
		System.out.println("GET : "+response.toString());
		if (response.getStatus() != 200) {
			throw new Exception("Unable to connect to JIRA");
		}
		return response.readEntity(String.class);
	}
	
	private String doDelete(String url) throws Exception {
		Client client = ClientBuilder.newClient();
		Response response = null;
		if (url.contains(ZAPI_BASEURL)) {
			ZFJCloudRestClient client1 = ZFJCloudRestClient.restBuilder(ZAPI_BASEURL, ACCESS_KEY, SECRET_KEY, USER_NAME)
					.build();
			JwtGenerator jwtGenerator = client1.getJwtGenerator();
			URI uri = new URI(url);
			int expirationInSec = 360;
			String jwt = jwtGenerator.generateJWT("DELETE", uri, expirationInSec);
			// Print the URL and JWT token to be used for making the REST call
			System.out.println("FINAL API : " + uri.toString());
			System.out.println("JWT Token : " + jwt);
			response = client.target(uri).request().header("Authorization", jwt).header("zapiAccessKey", ACCESS_KEY)
					.get();
		} else {
			response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE)
					.header("Authorization", JIRA_AUTHORIZATION).delete();
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

	private String createVersion() throws Exception {
		String versionId = "";
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

	private String getCycleId() throws Exception {
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
	
	public String createExecution(int issueId) throws Exception {
		String payload = "{\"cycleId\": \"" + CYCLE_ID + "\",\"issueId\": \"" + issueId + "\",\"projectId\": \""
				+ PROJECT_ID + "\",\"versionId\": \"" + VERSION_ID + "\",\"assigneeType\": \"currentUser\"}";
		System.out.println(payload);
		String responseString = doPost(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "execution", payload);
		JSONObject obj = new JSONObject(responseString);
		String executionId = obj.getJSONObject("execution").getString("id");
		log.info("Execution Created : " + executionId);
		return executionId;
	}

	public JSONObject getTestStepIds(String jiraKey, String executionId, int issueId) throws Exception {
		JSONArray steps = getTestSteps(issueId);
		JSONObject stepIds = new JSONObject();
		List<String> testStepId = new ArrayList<String>();
		List<String> testResultIds = new ArrayList<String>();
		if(steps.length()>0) {
			String response = doGet(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "stepresult/search?issueId=" + issueId
					+ "&executionId=" + executionId + "&isOrdered=true");
			System.out.println(response);
			JSONObject obje = new JSONObject(response);
			System.out.println(response);
			
			JSONArray arr = obje.getJSONArray("stepResults");
			for (int i=0;i<steps.length();i++) {		
				JSONObject stepObj = (JSONObject) steps.get(i);
				String testResultId = "";
				for(int j=0;j<stepObj.length();j++) {
					if(arr.getJSONObject(j).getString("stepId").equals(stepObj.getString("id"))) {
						arr.getJSONObject(j).getString("stepId");
						testResultId = arr.getJSONObject(j).getString("id");
						System.out.println(testResultId);
						testResultIds.add(testResultId);
						break;
					}
				}
				String testStep = stepObj.getString("id");
				testStepId.add(testStep);			
			}	
		}
		stepIds.put("testStepId", testStepId);
		stepIds.put("testResultId", testResultIds);
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
		String response = doDelete(JIRA_BASEURL + JIRA_API_CONTEXT + "attachment/" + attachmentId);
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
			throws Exception {
		String payload = "";
		String response = doPost(JIRA_BASEURL + JIRA_API_CONTEXT + "issue/" + jiraKey + "/attachments", payload);
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
	
	public String updateTestStepStatus(Status status, int issueId, String stepId, String executionId, String resultId) throws Exception {
		String changeStatus = "";
		int statusId = -1;
		String payload = "";
		if (status.toString().equals("passed")) {
			statusId = 1;
		} else if (status.toString().equals("failed")) {
			statusId = 2;
		}
		payload = "{\"status\":{\"id\":\"" + statusId + "\"},\"issueId\":" + issueId + ",\"stepId\":\"" + stepId
					+ "\",\"executionId\":\"" + executionId + "\"}";
		String response = doPut(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "stepresult/" + resultId, payload);
		log.info("Test Step Status Updated : " + response);
		return changeStatus;
	}

	public JSONObject updateExecutioStatusAndLogDefects(HashMap<String, String> attachmentPath, String priority,
			String jiraKey, String status, JSONObject stepDetailsObj) throws Exception {
		int issueId = getIssueId(jiraKey);
		JSONObject DataObject = new JSONObject();
		int statusId = -1;
		switch (status.toLowerCase()) {
		case "passed":
			statusId = 1;
			break;
		case "failed":
			statusId = 2;
			break;
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

	public void updateExecutioStatus(int issueId,String status,String executionId) throws Exception {
//		int issueId = getIssueId(jiraKey);
		updateExecutioStatus(issueId, status,executionId,null);		
	}
	
	private void updateExecutioStatus(int issueId, String status,String executionId,String defectKey) throws Exception {
		String payload = null;
		int statusId = -1;
		switch (status.toLowerCase()) {
		case "passed":
			statusId = 1;
			payload = "{\"status\":{\"id\":" + statusId + "},\"issueId\":" + issueId + ",\"projectId\":" + PROJECT_ID
					+ ",\"id\":\"" + executionId + "\"}";
			break;
		case "failed":
			statusId = 2;
//			if(defectKey==null) {
//				String defectId = createNewDefect();
//				payload = "{\"status\":{\"id\":" + statusId + "},\"issueId\":" + issueId + ",\"projectId\":" + PROJECT_ID
//						+ ",\"id\":\"" + executionId + "\"},\"defects\":[\""+defectId+"\"]";	
//			}else {
				payload = "{\"status\":{\"id\":" + statusId + "},\"issueId\":" + issueId + ",\"projectId\":" + PROJECT_ID
						+ ",\"id\":\"" + executionId + "\"}";	
//			}			
			break;
		default:
			payload = "{\"status\":{\"id\":" + statusId + "},\"issueId\":" + issueId + ",\"projectId\":" + PROJECT_ID
			+ ",\"id\":\"" + executionId + "\"}";	
			break;
		}		
		String responseString = doPut(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "execution/" + executionId, payload);
		System.out.println(responseString);
		log.info("Execution status updated");
	}
	
	private String updateTestStepStatus(String status, int issueId, String stepId, String executionId) throws Exception {
		String changeStatus = "";
		int statusId = -1;
		String BugId = "";
		String payload = "";
		if (status.equals("passed")) {
			statusId = 1;
		} else if (status.equals("failed")) {
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
//		String response = doPut(ZAPI_BASEURL + ZAPI_URI_CONTEXT + "stepresult/" + resultId, payload);
//		log.info("Test Step Status Updated : " + response);
		return changeStatus;
	}
	
	private JSONArray getTestSteps(int issueId) throws Exception {
		String response = doGet(ZAPI_BASEURL+ZAPI_URI_CONTEXT+"teststep/"+issueId+"?projectId="+PROJECT_ID);
		JSONArray obj = new JSONArray(response);
		System.out.println("New : "+obj.toString());
		return obj;
	}
	
}
