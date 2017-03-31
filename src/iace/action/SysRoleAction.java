package iace.action;

import java.util.List;

import iace.entity.sys.SysRole;
import iace.service.ServiceFactory;
import iace.service.sys.SysRoleService;

public class SysRoleAction extends BaseIaceAction {

	private static final long serialVersionUID = -2427947003851670636L;
	
	private SysRoleService sysRoleService = ServiceFactory.getSysRoleService();

	
	private List<SysRole> sysRoleList;
	
	private Long id;
	private SysRole sysRole;
	
	public SysRoleAction() {
		super.setTitle("系統角色");
	}

	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.sysRoleList = this.sysRoleService.listAll();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String create() {
		try {
			this.sysRole = new SysRole();

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String createSubmit() {
		try {
			this.sysRoleService.create(this.sysRole, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.sysRole = this.sysRoleService.get(this.id);
			if (this.sysRole == null) {
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
		try {
			this.sysRoleService.update(sysRole, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	
	public List<SysRole> getSysRoleList() {
		return sysRoleList;
	}

	public void setSysRoleList(List<SysRole> sysRoleList) {
		this.sysRoleList = sysRoleList;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
	
}
