package iace.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.about.About;
import iace.entity.about.AboutSearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.about.AboutService;

public class AboutAction extends BaseIaceAction {
	private static final long serialVersionUID = -4902616364110673840L;

	private AboutService aboutService = ServiceFactory.getAboutService();
	
	private AboutSearchModel searchCondition = new AboutSearchModel();
	private PagedList<About> aboutPagedList;
	
	private List<BaseOption> aboutMenuList;
	
	private Long id;
	private About about;
	
	public AboutAction() {
		super.setEntityClass(About.class);
		super.setTitle("關於");
	}

	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.aboutPagedList = this.aboutService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, About.class);
			this.about = this.aboutService.get(this.id);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String frontendShowDetailInit() {
		try {
			List<About> aboutList = this.aboutService.listAll();
			Collections.sort(aboutList);
			this.aboutMenuList = new ArrayList<BaseOption>();
			for (About about : aboutList) {
				this.aboutMenuList.add(new BaseOption(about.getId()+"", about.getTitle()));
			}
			
			this.about = aboutList.get(0);
			new ClickNumCounterDao().increaseClickNum(this.about.getId(), About.class);
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String frontendShowDetail() {
		try {
			List<About> aboutList = this.aboutService.listAll();
			Collections.sort(aboutList);
			this.aboutMenuList = new ArrayList<BaseOption>();
			for (About about : aboutList) {
				this.aboutMenuList.add(new BaseOption(about.getId()+"", about.getTitle()));
				if (about.getId() == this.id) {
					this.about = about;
					new ClickNumCounterDao().increaseClickNum(this.about.getId(), About.class);
				}
			}
			
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
			this.aboutService.create(this.about, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.about = this.aboutService.get(id);
			if (this.about == null) {
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
			this.aboutService.update(this.about, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.about = this.aboutService.get(id);
			if (this.about == null) {
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
			this.aboutService.delete(this.id, false, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.about.getTitle(), 200, "about.title");
		super.validateTextMaxLength(this.about.getMetaTitle(), 200, "about.metaTitle");
	}

	public AboutSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(AboutSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<About> getAboutPagedList() {
		return aboutPagedList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public About getAbout() {
		return about;
	}

	public void setAbout(About about) {
		this.about = about;
	}

	public List<BaseOption> getAboutMenuList() {
		return aboutMenuList;
	}
	
	
}
