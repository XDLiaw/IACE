package iace.action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import core.util.PagedList;
import iace.entity.sys.SysLog;
import iace.entity.sys.SysLogSearchModel;
import iace.entity.sys.SysUser;
import iace.service.ServiceFactory;
import iace.service.sys.SysLogService;
import iace.service.sys.SysUserService;

public class SysLogAction extends BaseIaceAction {

	private static final long serialVersionUID = 444347145788274098L;

	private SysLogService sysLogService = ServiceFactory.getSysLogService();
	private SysUserService sysUserService = ServiceFactory.getSysUserService();
	
	private List<SysUser> sysUserList;
	private Map<String, String> namespaces;
	private Map<String, String> actionNames;
	
	private SysLogSearchModel searchCondition = new SysLogSearchModel();
	private PagedList<SysLog> sysLogPagedList;
	
	private Long id;
	private SysLog sysLog;
	
	public SysLogAction() {
		super.setTitle("系統LOG");
	}
	
	public String init() {
		return SUCCESS;
	}
	
	public String index() {
		try {
			this.sysLogPagedList = this.sysLogService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			this.sysLog = this.sysLogService.get(this.id);
			if (this.sysLog == null) {
				super.addActionError("找不到資料");
				return INPUT;
			}
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String getActionNameMap() {
		try {
			settingActionNameMap();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void settingActionNameMap() {
		this.actionNames = new TreeMap<String, String>();
		if (StringUtils.isNotBlank(this.searchCondition.getNamespace())) {
			List<String> actionNameList = this.sysLogService.getActionNameList(this.searchCondition.getNamespace());
			for (String actionName:actionNameList) {
				if (SysLog.actionNames.containsKey(actionName)) {
					this.actionNames.put(actionName, SysLog.actionNames.get(actionName));
				}
			}
		}
	}

	// =========================================================================
	
	public SysLogSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(SysLogSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysLog getSysLog() {
		return sysLog;
	}

	public void setSysLog(SysLog sysLog) {
		this.sysLog = sysLog;
	}

	public List<SysUser> getSysUserList() {
		if (sysUserList == null) {
			sysUserList = this.sysUserService.listAll();
		}
		return sysUserList;
	}
	
	public Map<String, String> getNamespaces() {
		if (namespaces == null) {
			this.namespaces = new TreeMap<String, String>();
			List<String> namespaceList = this.sysLogService.getNamespaceList();
			for (String namespace:namespaceList) {
				if (SysLog.namespaces.containsKey(namespace)) {
					this.namespaces.put(namespace, SysLog.namespaces.get(namespace));
				}
			}
		}
		return namespaces;
	}

	public Map<String, String> getActionNames() {
		if (this.actionNames == null) {
			settingActionNameMap();
		}
		return actionNames;
	}

	public PagedList<SysLog> getSysLogPagedList() {
		return sysLogPagedList;
	}

}
