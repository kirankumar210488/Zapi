package com.Zapi.Trigger;

import com.Zapi.Utilities.TestCaseMgmtCloud;

public class Main {

	
	
	public static void main(String[] args) throws Exception {
		TestCaseMgmtCloud tcm = new TestCaseMgmtCloud();
		tcm.updateExecutioStatus("ZAP-2", "passed");
		tcm.updateExecutioStatus("ZAP-3", "wip");
		tcm.updateExecutioStatus("ZAP-1", "blocked");
	}

}
