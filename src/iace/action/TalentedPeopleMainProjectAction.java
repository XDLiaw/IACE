package iace.action;

import java.util.ArrayList;
import java.util.List;

import iace.entity.option.BaseOption;
import iace.entity.option.OptionDomain;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.talentedPeople.TalentedPeopleMainProject;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;
import iace.service.ServiceFactory;
import iace.service.talentedPeople.TalentedPeopleMainProjectService;
import iace.service.talentedPeople.TalentedPeopleService;

public class TalentedPeopleMainProjectAction extends BaseIaceAction {

	private static final long serialVersionUID = 5220758303773554432L;
	
	private TalentedPeopleService talentedPeopleService = ServiceFactory.getTalentedPeopleService();
	private TalentedPeopleMainProjectService mainProjectService = ServiceFactory.getTalentedPeopleMainProjectService();
	
	private TalentedPeopleSearchModel searchCondition = new TalentedPeopleSearchModel();
	private String scrollTo;
	
	private boolean isSelfMaintain;
	
	private long talentedPeopleId;
	private TalentedPeople talentedPeople;

	private Long id;
	private TalentedPeopleMainProject talentedPeopleMainProject;
	
	private List<OptionDomain> mainDomainList;
	private List<BaseOption> yearList;
	private List<BaseOption> monthList;

	public TalentedPeopleMainProjectAction() {
		super.setEntityClass(TalentedPeopleMainProject.class);
		super.setTitle("產學人才 > 主要產學合作計畫案例");
	}

	public String showDetail() {
		try {
			this.talentedPeopleMainProject = this.mainProjectService.get(this.id);
			if (this.talentedPeopleMainProject == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeopleMainProject.getTalentedPeople().getSysUser().getId()) {
					super.addActionError("您只能維護自己的資料");
					return ERROR;
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
	
	public String createSubmit() {
		try {
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeopleId);
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeople.getSysUser().getId()) {
					super.addActionError("您只能維護自己的資料");
					return ERROR;
				}
			}			
			this.talentedPeopleMainProject.setTalentedPeople(this.talentedPeople);

			// create
			this.mainProjectService.create(this.talentedPeopleMainProject, super.getCurrentSysUser(), false, super.getSysLog());
			
			// get the newest talentedPeople data
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeopleId);
			
			super.addActionMessage("CREATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String selfCreateSubmit() {
		this.isSelfMaintain = true;
		return createSubmit();
	}
	
	public String update() {
		return showDetail();
	}
	
	public String selfUpdate() {
		this.isSelfMaintain = true;
		return update();
	}
	
	public String updateSubmit() {
		try {
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeopleId);
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeople.getSysUser().getId()) {
					super.addActionError("您只能維護自己的資料");
					return ERROR;
				}
			}
			this.talentedPeopleMainProject.setTalentedPeople(this.talentedPeople);
			
			// update
			this.mainProjectService.update(this.talentedPeopleMainProject, super.getCurrentSysUser(), false, super.getSysLog());
			
			// get the newest talentedPeople data
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeopleId);
			
			super.addActionMessage("UPDATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String selfUpdateSubmit() {
		this.isSelfMaintain = true;
		return updateSubmit();
	}
	
	public String delete() {
		return showDetail();
	}
	
	public String selfDelete() {
		this.isSelfMaintain = true;
		return delete();
	}
	
	public String deleteSubmit() {
		try {
			this.talentedPeopleMainProject = this.mainProjectService.get(this.id);
			if (this.talentedPeopleMainProject == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeopleMainProject.getTalentedPeople().getSysUser().getId()) {
					super.addActionError("您只能維護自己的資料");
					return ERROR;
				}
			}
			this.talentedPeopleId = this.talentedPeopleMainProject.getTalentedPeople().getId();
			
			// delete 
			this.mainProjectService.delete(this.talentedPeopleMainProject, false, super.getSysLog());
			
			// get the newest talentedPeople data
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeopleId);
			
			super.addActionMessage("DELETE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String selfDeleteSubmit() {
		this.isSelfMaintain = true;
		return deleteSubmit();
	}
	
	// =========================================================================

	public TalentedPeopleSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(TalentedPeopleSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getScrollTo() {
		return scrollTo;
	}

	public void setScrollTo(String scrollTo) {
		this.scrollTo = scrollTo;
	}

	public long getTalentedPeopleId() {
		return talentedPeopleId;
	}

	public void setTalentedPeopleId(long talentedPeopleId) {
		this.talentedPeopleId = talentedPeopleId;
	}

	public TalentedPeople getTalentedPeople() {
		return talentedPeople;
	}

	public void setTalentedPeople(TalentedPeople talentedPeople) {
		this.talentedPeople = talentedPeople;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TalentedPeopleMainProject getTalentedPeopleMainProject() {
		return talentedPeopleMainProject;
	}

	public void setTalentedPeopleMainProject(TalentedPeopleMainProject talentedPeopleMainProject) {
		this.talentedPeopleMainProject = talentedPeopleMainProject;
	}
	
	public List<OptionDomain> getMainDomainList() {
		if (mainDomainList == null) {
			mainDomainList = ServiceFactory.getOptionDomainService().listAll();
		}
		return mainDomainList;
	}

	public List<BaseOption> getYearList() {
		if (yearList == null) {
			yearList = new ArrayList<BaseOption>();
			for (int i = 1990; i < 2020; i++) {
				yearList.add(new BaseOption(i + "", i + "年"));
			}
		}
		return yearList;
	}

	public List<BaseOption> getMonthList() {
		if (monthList == null) {
			monthList = new ArrayList<BaseOption>();
			monthList.add(new BaseOption("1", "1月"));
			monthList.add(new BaseOption("2", "2月"));
			monthList.add(new BaseOption("3", "3月"));
			monthList.add(new BaseOption("4", "4月"));
			monthList.add(new BaseOption("5", "5月"));
			monthList.add(new BaseOption("6", "6月"));
			monthList.add(new BaseOption("7", "7月"));
			monthList.add(new BaseOption("8", "8月"));
			monthList.add(new BaseOption("9", "9月"));
			monthList.add(new BaseOption("10", "10月"));
			monthList.add(new BaseOption("11", "11月"));
			monthList.add(new BaseOption("12", "12月"));
		}
		return monthList;
	}
	
	
}
