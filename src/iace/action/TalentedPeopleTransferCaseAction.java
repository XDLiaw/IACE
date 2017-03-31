package iace.action;

import java.util.ArrayList;
import java.util.List;

import iace.entity.option.BaseOption;
import iace.entity.option.OptionDomain;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;
import iace.entity.talentedPeople.TalentedPeopleTransferCase;
import iace.service.ServiceFactory;
import iace.service.talentedPeople.TalentedPeopleService;
import iace.service.talentedPeople.TalentedPeopleTransferCaseService;

public class TalentedPeopleTransferCaseAction extends BaseIaceAction {
	private static final long serialVersionUID = -5705792156398771486L;
	
	private TalentedPeopleService talentedPeopleService = ServiceFactory.getTalentedPeopleService();
	private TalentedPeopleTransferCaseService transferCaseService = ServiceFactory.getTalentedPeopleTransferCaseService();
	
	private TalentedPeopleSearchModel searchCondition = new TalentedPeopleSearchModel();
	private String scrollTo;
	
	private boolean isSelfMaintain;
	
	private Long talentedPeopleId;
	private TalentedPeople talentedPeople;

	private Long id;
	private TalentedPeopleTransferCase talentedPeopleTransferCase;
	
	private List<OptionDomain> mainDomainList;
	private List<BaseOption> yearList;
	private List<BaseOption> monthList;

	public TalentedPeopleTransferCaseAction() {
		super.setEntityClass(TalentedPeopleTransferCase.class);
		super.setTitle("產學人才 > 成果移轉及授權案例");
	}

	public String showDetail() {
		try {
			this.talentedPeopleTransferCase = this.transferCaseService.get(this.id);
			if (this.talentedPeopleTransferCase == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeopleTransferCase.getTalentedPeople().getSysUser().getId()) {
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
			this.talentedPeopleTransferCase.setTalentedPeople(this.talentedPeople);

			// create
			this.transferCaseService.create(this.talentedPeopleTransferCase, super.getCurrentSysUser(), false, super.getSysLog());
			
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
			this.talentedPeopleTransferCase.setTalentedPeople(this.talentedPeople);
			
			// update
			this.transferCaseService.update(this.talentedPeopleTransferCase, super.getCurrentSysUser(), false, super.getSysLog());
			
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
			this.talentedPeopleTransferCase = this.transferCaseService.get(this.id);
			if (this.talentedPeopleTransferCase == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeopleTransferCase.getTalentedPeople().getSysUser().getId()) {
					super.addActionError("您只能維護自己的資料");
					return ERROR;
				}
			}
			this.talentedPeopleId = this.talentedPeopleTransferCase.getTalentedPeople().getId();
			
			// delete 
			this.transferCaseService.delete(this.talentedPeopleTransferCase, false, super.getSysLog());
			
			// get the newest talentedPeople data
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeopleId);
			
			super.addActionMessage("DELETE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	//==========================================================================
	
	public String selfDeleteSubmit() {
		this.isSelfMaintain = true;
		return deleteSubmit();
	}

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

	public Long getTalentedPeopleId() {
		return talentedPeopleId;
	}

	public void setTalentedPeopleId(Long talentedPeopleId) {
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

	public TalentedPeopleTransferCase getTalentedPeopleTransferCase() {
		return talentedPeopleTransferCase;
	}

	public void setTalentedPeopleTransferCase(TalentedPeopleTransferCase talentedPeopleTransferCase) {
		this.talentedPeopleTransferCase = talentedPeopleTransferCase;
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
