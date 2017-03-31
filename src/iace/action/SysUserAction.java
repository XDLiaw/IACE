package iace.action;

import java.util.List;

import org.hibernate.exception.JDBCConnectionException;

import core.util.PagedList;
import iace.entity.sys.SysRole;
import iace.entity.sys.SysUser;
import iace.entity.sys.SysUserSearchModel;
import iace.interceptor.SessionInterceptor;
import iace.service.ServiceFactory;
import iace.service.sys.SysRoleService;
import iace.service.sys.SysUserService;

public class SysUserAction extends BaseIaceAction {
	private static final long serialVersionUID = 8080624976522044142L;

	private SysUserService sysUserService = ServiceFactory.getSysUserService();
	private SysRoleService sysRoleService = ServiceFactory.getSysRoleService();

	private PagedList<SysUser> sysUserPagedList;
	private SysUserSearchModel searchCondition = new SysUserSearchModel();

	private Long id;
	private SysUser sysUser;

	private List<SysRole> sysRoleList;

	public SysUserAction() {
		super.setTitle("系統使用者");
	}

	public String init() {
		return index();
	}

	public String index() {
		try {
			this.sysUserPagedList = this.sysUserService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String create() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String createSubmit() {
		try {
			SysRole role = this.sysRoleService.get(this.sysUser.getSysRole().getId());
			this.sysUser.setSysRole(role);
			this.sysUserService.create(this.sysUser, super.getCurrentSysUser(), false, super.getSysLog());
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.sysUser = this.sysUserService.get(this.id);
			if (this.sysUser == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String updateSubmit() {
		try{
			SysRole role = this.sysRoleService.get(this.sysUser.getSysRole().getId());
			this.sysUser.setSysRole(role);
			this.sysUserService.update(this.sysUser, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String login() {
		return SUCCESS;
	}

	public String loginSubmit() {
		try {
			if (this.sysUserService.loginCheck(this.sysUser.getAccount(), this.sysUser.getPassword())) {
				this.sysUser = this.sysUserService.getBy(this.sysUser.getAccount(), this.sysUser.getPassword());
//				ActionContext actionContext = ActionContext.getContext();
//				actionContext.getSession().put(LoginInterceptor.SESSION_KEY_SYS_USER, this.sysUser);
				super.session.put(SessionInterceptor.SESSION_KEY_SYS_USER, this.sysUser);
				
				//sysLog
				super.getSysLog().setSysUser(sysUser);
				super.getSysLog().setEnableLog(true);
				
				return SUCCESS;
			} else {
				super.addActionError("帳號或密碼錯誤!");
				return INPUT;
			}
		} catch (JDBCConnectionException e) {
			log.error("資料庫連線錯誤，請重新嘗試!", e);
			super.addActionError("資料庫連線錯誤，請重新嘗試!");
			return INPUT;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String logout() {
		try {
			//sysLog
			this.sysUser = (SysUser) super.session.get(SessionInterceptor.SESSION_KEY_SYS_USER);
			super.getSysLog().setSysUser(sysUser);
			super.getSysLog().setEnableLog(sysUser != null);
			
			this.session.clear();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public List<SysRole> getSysRoleList() {
		if (sysRoleList == null) {
			sysRoleList = this.sysRoleService.listAll();
		}
		return sysRoleList;
	}

	public void setSysRoleList(List<SysRole> sysRoleList) {
		this.sysRoleList = sysRoleList;
	}

	public SysUserSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(SysUserSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<SysUser> getSysUserPagedList() {
		return sysUserPagedList;
	}

}
