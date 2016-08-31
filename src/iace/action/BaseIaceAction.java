package iace.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import core.action.BaseAction;
import iace.entity.questionnaire.QnrTable;
import iace.entity.sys.SysLog;
import iace.entity.sys.SysUser;
import iace.interceptor.SessionInterceptor;
import iace.service.ServiceFactory;

public class BaseIaceAction extends BaseAction implements SessionAware {

	private static final long serialVersionUID = 1771742807180192593L;

	private String title;
	
	protected Map<String, Object> session;
	
	private String exceptionName;
	private String exceptionMessage;
	private StackTraceElement[] exceptionStack;
	
	private SysLog sysLog = new SysLog();

	// =========================================================================

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<QnrTable> getQnrTemplateList() {
		return ServiceFactory.getQnrTemplateService().listAll();
	}
	
	/**
	 * 測試文字是否可以轉換為double (testValue == null 會通過, 但空白不會)
	 * @param testValue
	 * @param fieldName
	 * @param errMsg
	 * @return
	 */
	protected boolean validateStringAsDouble(String testValue, String fieldName, String errMsg) {
		try {
			Double.parseDouble(testValue);
			return true;
		} catch (NullPointerException | NumberFormatException e) {
			if (testValue != null && testValue.trim().equals("N/A") == false) {
				this.addFieldError(fieldName, errMsg);
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	/**
	 * 測試文字是否可以轉換為double (testValue == null 會通過, 但空白不會)
	 * @param testValue
	 * @param fieldName
	 * @param errMsg 錯誤訊息預設為 "Not a number!"
	 * @return
	 */
	protected boolean validateStringAsDouble(String testValue, String fieldName) {
		return validateStringAsDouble(testValue, fieldName, "Not a number!");
	}
	
	protected void showExceptionToPage(Exception e) {
		log.error("", e);
		this.addActionError(e.getMessage());
		this.exceptionName = e.getClass().getName();
		this.exceptionMessage = e.getMessage();
		this.exceptionStack = e.getStackTrace();
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public StackTraceElement[] getExceptionStack() {
		return exceptionStack;
	}

	public void setExceptionStack(StackTraceElement[] exceptionStack) {
		this.exceptionStack = exceptionStack;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public SysLog getSysLog() {
		return sysLog;
	}

	public void setSysLog(SysLog sysLog) {
		this.sysLog = sysLog;
	}

	public SysUser getCurrentSysUser() {
		return (SysUser) this.session.get(SessionInterceptor.SESSION_KEY_SYS_USER);
	}
}
