package iace.action;

import java.util.List;

import core.util.PagedList;
import iace.entity.DbFile;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.activity.ActivityService;

public class ActivityAction extends BaseIaceAction {
	private static final long serialVersionUID = 2068270305221952698L;

	private ActivityService activityService = ServiceFactory.getActivityService();
	
	private ActivitySearchModel searchCondition = new ActivitySearchModel();
	private PagedList<Activity> activityPagedList;
			
	private Long id;
	private Activity activity;
	
	private List<BaseOption> categoryList = Activity.getCategoryList();
	private List<BaseOption> fileTypeList = DbFile.getFileTypeList();
	
	public ActivityAction() {
		super.setEntityClass(Activity.class);
		super.setTitle("活動人培");
	}

	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.activityPagedList = this.activityService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			this.activity = this.activityService.get(this.id);
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
			this.activityService.create(this.activity, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.activity = this.activityService.get(id);
			if (this.activity == null) {
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
			this.activityService.update(this.activity, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.activity = this.activityService.get(id);
			if (this.activity == null) {
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
			this.activityService.delete(this.id, true, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.activity.getTitle(), 200, "activity.title");
		super.validateNotNull(this.activity.getPostDate(), "activity.postDate");
		super.validateTextMaxLength(this.activity.getMetaTitle(), 200, "activity.metaTitle");
	}
	
	//==========================================================================

	public ActivitySearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(ActivitySearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<BaseOption> getFileTypeList() {
		return fileTypeList;
	}

	public void setFileTypeList(List<BaseOption> fileTypeList) {
		this.fileTypeList = fileTypeList;
	}

	public PagedList<Activity> getActivityPagedList() {
		return activityPagedList;
	}

	public List<BaseOption> getCategoryList() {
		return categoryList;
	}
	
}
