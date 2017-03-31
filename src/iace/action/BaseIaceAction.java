package iace.action;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import core.action.BaseAction;
import iace.entity.sys.SysLog;
import iace.entity.sys.SysUser;
import iace.interceptor.SessionInterceptor;

public abstract class BaseIaceAction extends BaseAction implements SessionAware {

	private static final long serialVersionUID = 1771742807180192593L;

	private String title;
	
	protected Map<String, Object> session;
	
	private String exceptionUuid;
	private Date exceptionTime;
	private String exceptionName;
	private String exceptionMessage;
	private StackTraceElement[] exceptionStack;
	
	private SysLog sysLog = new SysLog();
	
	private Class<?> entityClass;
	
	private Long openedEpaperId;

	// =========================================================================

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		this.exceptionUuid = UUID.randomUUID().toString(); 
		log.error(this.exceptionUuid, e);
		this.addActionError(e.getMessage());
		this.exceptionTime = new Date();
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

	public String getExceptionUuid() {
		return exceptionUuid;
	}

	public void setExceptionUuid(String exceptionUuid) {
		this.exceptionUuid = exceptionUuid;
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
	
	public Date getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(Date exceptionTime) {
		this.exceptionTime = exceptionTime;
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

	public String getNamespace() {
		try {
			return ServletActionContext.getActionMapping().getNamespace();
		} catch (Exception e) {
			return null;
		}
	}

	public String getActionName() {
		try {
			return ServletActionContext.getActionMapping().getName();
		} catch (Exception e) {
			return null;
		}
	}

	public Long getOpenedEpaperId() {
		return openedEpaperId;
	}

	public void setOpenedEpaperId(Long openedEpaperId) {
		this.openedEpaperId = openedEpaperId;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	protected void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	
}
