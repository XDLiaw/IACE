package iace.action;

import java.util.List;

import core.util.PagedList;
import iace.entity.enterpriseNeed.EnterpriseRequireTech;
import iace.entity.enterpriseNeed.EnterpriseRequireTechSearchModel;
import iace.entity.option.OptionDomain;
import iace.service.ServiceFactory;
import iace.service.enterpriseNeed.EnterpriseRequireTechService;

public class EnterpriseRequireTechAction extends BaseIaceAction {

	private static final long serialVersionUID = 8119543004095785805L;
	
	private EnterpriseRequireTechService enterpriseRequireTechService = ServiceFactory.getEnterpriseRequireTechService();

	private EnterpriseRequireTechSearchModel searchCondition = new EnterpriseRequireTechSearchModel();
	private PagedList<EnterpriseRequireTech> enterpriseRequireTechPagedList;
	
	private List<OptionDomain> optionDomainList;
	
	public EnterpriseRequireTechAction() {
		super.setEntityClass(EnterpriseRequireTech.class);
		super.setTitle("企業技術需求資料");
	}
	
	public String init() {
		this.searchCondition.setOptionDomainId(this.getOptionDomainList().get(0).getId());
		return index();
	}
	
	public String index() {
		try {
			this.enterpriseRequireTechPagedList = this.enterpriseRequireTechService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	// =========================================================================
	
	public EnterpriseRequireTechSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(EnterpriseRequireTechSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<EnterpriseRequireTech> getEnterpriseRequireTechPagedList() {
		return enterpriseRequireTechPagedList;
	}

	public List<OptionDomain> getOptionDomainList() {
		if (optionDomainList == null) {
			optionDomainList = ServiceFactory.getOptionDomainService().listAll();
		}
		return optionDomainList;
	}

	
}
