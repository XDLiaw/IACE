package iace.action;

import java.util.List;

import iace.entity.option.BaseOption;
import iace.entity.option.OptionCountry;
import iace.entity.option.OptionDomain;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.talentedPeople.TalentedPeopleRdResult;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;
import iace.service.ServiceFactory;
import iace.service.talentedPeople.TalentedPeopleRdResultService;
import iace.service.talentedPeople.TalentedPeopleService;

public class TalentedPeopleRdResultAction extends BaseIaceAction {

	private static final long serialVersionUID = -3986142131111758874L;

	private TalentedPeopleService talentedPeopleService = ServiceFactory.getTalentedPeopleService();
	private TalentedPeopleRdResultService rdResultService = ServiceFactory.getTalentedPeopleRdResultService();
	
	private TalentedPeopleSearchModel searchCondition = new TalentedPeopleSearchModel();
	private String scrollTo;
	
	private boolean isSelfMaintain;
	
	private Long talentedPeopleId;
	private TalentedPeople talentedPeople;

	private Long id;
	private TalentedPeopleRdResult talentedPeopleRdResult;
	
	private List<OptionDomain> mainDomainList;
	private List<BaseOption> rdResultTypeList = TalentedPeopleRdResult.getTypeList();
	private List<OptionCountry> countryList;
	
	public TalentedPeopleRdResultAction() {
		super.setEntityClass(TalentedPeopleRdResult.class);
		super.setTitle("產學人才 > 重要研發成果");
	}
	
	public String showDetail() {
		try {
			this.talentedPeopleRdResult = this.rdResultService.get(this.id);
			if (this.talentedPeopleRdResult == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeopleRdResult.getTalentedPeople().getSysUser().getId()) {
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
			this.talentedPeopleRdResult.setTalentedPeople(this.talentedPeople);

			// create
			this.rdResultService.create(this.talentedPeopleRdResult, super.getCurrentSysUser(), false, super.getSysLog());
			
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
			this.talentedPeopleRdResult.setTalentedPeople(this.talentedPeople);
			
			// update
			this.rdResultService.update(this.talentedPeopleRdResult, super.getCurrentSysUser(), false, super.getSysLog());
			
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
			this.talentedPeopleRdResult = this.rdResultService.get(this.id);
			if (this.talentedPeopleRdResult == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			if (this.isSelfMaintain) {
				if (super.getCurrentSysUser().getId() != this.talentedPeopleRdResult.getTalentedPeople().getSysUser().getId()) {
					super.addActionError("您只能維護自己的資料");
					return ERROR;
				}
			}
			this.talentedPeopleId = this.talentedPeopleRdResult.getTalentedPeople().getId();
			
			// delete 
			this.rdResultService.delete(this.talentedPeopleRdResult, false, super.getSysLog());
			
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
	
	//==========================================================================

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

	public TalentedPeopleRdResult getTalentedPeopleRdResult() {
		return talentedPeopleRdResult;
	}

	public void setTalentedPeopleRdResult(TalentedPeopleRdResult talentedPeopleRdResult) {
		this.talentedPeopleRdResult = talentedPeopleRdResult;
	}

	public List<OptionDomain> getMainDomainList() {
		if (mainDomainList == null) {
			mainDomainList = ServiceFactory.getOptionDomainService().listAll();
		}
		return mainDomainList;
	}
	
	public List<BaseOption> getRdResultTypeList() {
		return rdResultTypeList;
	}

	public List<OptionCountry> getCountryList() {
		if (countryList == null) {
			countryList = ServiceFactory.getOptionCountryService().listAll();
		}
		return countryList;
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
	
}
