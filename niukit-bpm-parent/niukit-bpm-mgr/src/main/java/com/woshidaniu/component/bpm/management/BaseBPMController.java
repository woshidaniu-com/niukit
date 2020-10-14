package com.woshidaniu.component.bpm.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：com.woshidaniu.component.bpm.management.BaseBPMController.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public abstract class BaseBPMController {
	protected static final transient Logger log = LoggerFactory.getLogger(BaseBPMController.class);

	protected static final String ERROR_VIEW = "/exception/busiError";

	protected static ObjectMapper objectMapper = new ObjectMapper();

	protected void logException(Exception ex) {
		if (log.isErrorEnabled()) {
			log.error(ex.getMessage(), ex);
		}
	}

	protected static enum BPMMessageKey {
		DO_SUCCESS("I99007","success"),
		DO_FAIL("I99008","fail"),
		
		SAVE_SUCCESS("I99001","success"),
		SAVE_FAIL("I99002","fail"),
		
		MODIFY_SUCCESS("I99003","success"),
		MODIFY_FAIL("I99004","fail"),
		
		DELETE_SUCCESS("I99005","success"),
		DELETE_FAIL("I99006","fail"),
		
		ROLE_DELETE_FAIL("I99009","fail"),
		
		SYSTEM_ERROR("S00001","error"),
		
		PASSWORD_INIT_SUCCESS("I99010","success"),
		PASSWORD_INIT_FAIL("I99011","fail"),
		
		CACHE_REFRESH_SUCCESS("C99001","success"),
		CACHE_REFRESH_NONE("C99002","success"),
		
		ASSIGNMENT_QUERY_FAIL("P99091", "fail"),
		ASSIGNMENT_QUERY_SUCCESS("P99092", "success"),
		
		PROCESS_DEFINITION_NOT_FOUND("P99001", "fail"), 
		PROCESS_MODEL_DATA_QUERY_FAIL("P99002","fail"), 
		PROCESS_DEFINITION_ACTIVE_SUCCESS("P99011", "success"), 
		PROCESS_DEFINITION_ACTIVE_FAIL("P99012", "fail"), 
		PROCESS_DEFINITION_SUSPEND_SUCCESS("P99021","success"), 
		PROCESS_DEFINITION_SUSPEND_FAIL("P99022","fail"), 
		PROCESS_DEFINITION_DEL_SUCCESS("P99031","success"), 
		PROCESS_DEFINITION_DEL_FAIL("P99032","fail"), 
		PROCESS_MODEL_ADD_SUCCESS("P99041","success"), 
		PROCESS_MODEL_ADD_FAIL("P99042","fail"), 
		PROCESS_MODEL_DEL_SUCCESS("P99051","success"), 
		PROCESS_MODEL_DEL_FAIL("P99052","fail"), 
		PROCESS_MODEL_SAVE_SUCCESS("P99061","success"), 
		PROCESS_MODEL_SAVE_FAIL("P99062","fail"), 
		PROCESS_MODEL_COPY_SUCCESS("P99071","success"), 
		PROCESS_MODEL_COPY_FAIL("P99072","fail"), 
		PROCESS_MODEL_DEPLOY_SUCCESS("P99081","success"), 
		PROCESS_MODEL_DEPLOY_FAIL("P99082","fail"),;

		private String key;
		private String status;

		BPMMessageKey(String key, String status) {
			this.key = key;
			this.status = status;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public ObjectNode getJson(Object[] params) {
			String message = BPMMessageUtil.getText(key, params);
			ObjectNode json = objectMapper.createObjectNode();
			json.put("message", message);
			json.put("status", status);
			return json;
		}

		public ObjectNode getJson() {
			return getJson(null);
		}
	}

}
