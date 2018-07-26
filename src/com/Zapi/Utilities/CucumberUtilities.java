package com.Zapi.Utilities;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.Zapi.Utilities.BaseStep.Status;

import cucumber.api.Scenario;

public class CucumberUtilities {

	static TestCaseMgmtCloud testCaseManager;
	static boolean updateExecution;
	static boolean updateTestStep;
	
	@SuppressWarnings("static-access")
	public CucumberUtilities(boolean updateExecution,boolean updateTestStep) {
		this.updateExecution = updateExecution;
		this.updateTestStep = updateTestStep;
		testCaseManager = new TestCaseMgmtCloud();
	}
	
	
	Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	/**
	 * Runs before every scenario to execution id and step ids
	 */
	public JSONObject jiraBeforeActions(Scenario scenario) throws Exception {
		JSONObject obj = new JSONObject();
		String jiraKey = null;
		String executionId = null;
		int issueId = 0;
		Collection<String> tags = scenario.getSourceTagNames();
		if(updateTestStep||updateExecution) {
			for (String tag : tags) {
				if (tag.toLowerCase()
						.startsWith("@" + testCaseManager.config.getString("project.key").toLowerCase())) {
					jiraKey = tag.replace("@", "");
					issueId = testCaseManager.getIssueId(jiraKey);
					executionId = testCaseManager.createExecution(issueId);
					break;
				}
			}
			if(updateTestStep&&jiraKey!=null) {
				testCaseManager.updateExecutioStatus(issueId, "unexecuted", executionId);
				JSONObject stepObj = testCaseManager.getTestStepIds(jiraKey, executionId, issueId);
				for(String key:JSONObject.getNames(stepObj)) {
					obj.put(key, stepObj.get(key));
				}
			}
			obj.put("jiraKey", jiraKey);
			obj.put("executionId", executionId);
			obj.put("issueId", issueId);
		}else {
			log.info("----Bothe UpdateSteps and updateTestcase can not be false---");
		}
		return obj;
	}
	
	
	/**
	 * Runs After every scenario to update the test case and test step status
	 */
	public void jiraAfterActions(Scenario scenario,JSONObject jiraData) {		
		if(updateTestStep) {
			int stepSize = jiraData.getJSONArray("testStepId").length();
			if(stepSize>0) {
				for(int i=0;i<stepSize;i++) {
					if(!jiraData.getJSONArray("testStepId").get(i).equals("")) {
						updateTestStepStatus(jiraData, i+1, Status.failed);
						break;
					}
				}	
			}
				
		}		
		if(updateExecution&&jiraData!=null) {
			try {
				testCaseManager.updateExecutioStatus(jiraData.getInt("issueId"), scenario.getStatus(), jiraData.getString("executionId"));								
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Jira/Zapi service failed : "+e.getStackTrace());
			}
		}else {
			if(!updateExecution) {
				log.info("Update Execution is Skipping because it is specified as false");
			}else if(jiraData==null){
				log.warn("Json object is null");
			}
		}
	}
	
	
	/**
	 * Use this method inside the step Definition with try catch block and update the status of specified step
	 * @param: stepNumber - Step number of zephyr test case in which need to be updated
	 */
	public void updateTestStepStatus(JSONObject object,int stepNumber,Status status) {
		try {
			if(updateTestStep) {
				if(object.getJSONArray("testStepId").length()>0) {
					testCaseManager.updateTestStepStatus(status, object.getInt("issueId"), object.getJSONArray("testStepId").getString(stepNumber-1), object.getString("executionId"), object.getJSONArray("testResultId").getString(stepNumber-1));
					object.getJSONArray("testStepId").put(stepNumber-1,"");
					object.getJSONArray("testResultId").put(stepNumber-1,"");	
				}else {
					log.info("There is no Steps added for this Test Case in Jira : "+object.getString("jiraKey"));
				}	
			}			
		}catch (Exception e) {
			log.info("Unable to update the Step Status");
			e.printStackTrace();
		}
	}
	
	
}
