package iace.action;

import java.util.List;

import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.DbFile;
import iace.entity.option.BaseOption;
import iace.entity.rdFocus.RdFocus;
import iace.entity.rdFocus.RdFocusSearchModel;
import iace.service.ServiceFactory;
import iace.service.rdFocus.RdFocusService;

public class RdFocusAction extends BaseIaceAction {
	private static final long serialVersionUID = 4998685391434229314L;

	private RdFocusService rdFocusService = ServiceFactory.getRdFocusService();
	
	private RdFocusSearchModel searchCondition = new RdFocusSearchModel();
	private PagedList<RdFocus> rdFocusPagedList;
	
	private Long id;
	private RdFocus rdFocus;
	
	private List<BaseOption> categoryList = RdFocus.getCategoryList();
	private List<BaseOption> fileTypeList = DbFile.getFileTypeList();
	
	public RdFocusAction() {
		super.setTitle("研發焦點");
	}
	
	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.rdFocusPagedList = this.rdFocusService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, RdFocus.class);
			this.rdFocus = this.rdFocusService.get(this.id);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String create() {
		return SUCCESS;
	}
	
	public void validateCreateSubmit() {
		validateBeforeSubmit();
	}
	
	public String createSubmit() {
		try {
			this.rdFocusService.create(this.rdFocus, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.rdFocus = this.rdFocusService.get(id);
			if (this.rdFocus == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateUpdateSubmit() {
		validateBeforeSubmit();
	}
	
	public String updateSubmit() {
		try {
			this.rdFocusService.update(this.rdFocus, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.rdFocus = this.rdFocusService.get(id);
			if (this.rdFocus == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String deleteSubmit() {
		try {
			this.rdFocusService.delete(this.id, true, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.rdFocus.getTitle(), 200, "rdFocus.title");
		super.validateNotNull(this.rdFocus.getPostDate(), "rdFocus.postDate");
		super.validateTextMaxLength(this.rdFocus.getMetaTitle(), 200, "rdFocus.metaTitle");
	}
	
	//==========================================================================

	public RdFocusSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(RdFocusSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RdFocus getRdFocus() {
		return rdFocus;
	}

	public void setRdFocus(RdFocus rdFocus) {
		this.rdFocus = rdFocus;
	}

	public PagedList<RdFocus> getRdFocusPagedList() {
		return rdFocusPagedList;
	}

	public List<BaseOption> getCategoryList() {
		return categoryList;
	}

	public List<BaseOption> getFileTypeList() {
		return fileTypeList;
	}

}
