package lucene.integrationSearch;

import java.util.ArrayList;
import java.util.List;

import iace.entity.BaseSearchModel;
import iace.entity.coopExample.CoopEx;
import iace.entity.coopExample.CoopExSearchModel;
import iace.entity.incubationCenter.IncubationCenter;
import iace.entity.incubationCenter.IncubationCenterSearchModel;
import iace.entity.literature.LiteratureSearchModel;
import iace.entity.option.BaseOption;
import iace.entity.option.OptionCountry;
import iace.entity.option.OptionDomain;
import iace.entity.option.OptionGrbDomain;
import iace.entity.option.OptionTrl;
import iace.entity.patent.PatentSearchModel;
import iace.entity.patent.TechField;
import iace.entity.researchPlan.ResearchPlanSearchModel;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;
import iace.service.ServiceFactory;
import iace.service.option.OptionCountryService;
import iace.service.option.OptionGrbDomainService;
import iace.service.option.OptionTrlService;
import iace.service.patent.TechFieldService;
import iace.service.researchPlan.ResearchPlanService;

public class IntegrationSearchModel extends BaseSearchModel {
	private String className;
	private String searchText;
	private String researchPlanManager;

	// 需求:整合查詢要求各個細查 先給相應欄位的searchModel
	private ResearchPlanSearchModel researchPlanSearchModel = new ResearchPlanSearchModel();
	private PatentSearchModel patentSearchModel = new PatentSearchModel();
	private TalentedPeopleSearchModel talentedPeopleSearchModel = new TalentedPeopleSearchModel();
	private CoopExSearchModel coopExSearchModel = new CoopExSearchModel();
	private LiteratureSearchModel literatureSearchModel = new LiteratureSearchModel();
	private IncubationCenterSearchModel incubationCenterSearchModel = new IncubationCenterSearchModel();

	// by researchPlan
	private ResearchPlanService researchPlanService = ServiceFactory.getResearchPlanService();
	private List<BaseOption> yearList;
	private List<OptionTrl> optionTrlList;
	private List<OptionGrbDomain> optionGrbDomainList;
	private OptionTrlService optionTrlService = ServiceFactory.getOptionTrlService();
	private OptionGrbDomainService optionGrbDomainService = ServiceFactory.getOptionGrbDomainService();
	// by patent
	private List<OptionCountry> optionCountryList;
	private TechFieldService techFieldService = ServiceFactory.getTechFieldService();
	private OptionCountryService optionCountryService = ServiceFactory.getOptionCountryService();
	private List<TechField> techFieldList;
	// by talentedPeople
	private List<OptionDomain> mainDomainList;
	// by coopEx
	private List<BaseOption> typeList = CoopEx.getTypeList();
	// by literature
	// by incubationCenter
	private List<BaseOption> attributeList = IncubationCenter.getAttributeList();

	public IntegrationSearchModel() {
		super.setPageSize(10);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getResearchPlanManager() {
		return researchPlanManager;
	}

	public void setResearchPlanManager(String researchPlanManager) {
		this.researchPlanManager = researchPlanManager;
	}

	
	public ResearchPlanSearchModel getResearchPlanSearchModel() {
		return researchPlanSearchModel;
	}

	public void setResearchPlanSearchModel(ResearchPlanSearchModel researchPlanSearchModel) {
		this.researchPlanSearchModel = researchPlanSearchModel;
	}

	public PatentSearchModel getPatentSearchModel() {
		return patentSearchModel;
	}

	public void setPatentSearchModel(PatentSearchModel patentSearchModel) {
		this.patentSearchModel = patentSearchModel;
	}

	public TalentedPeopleSearchModel getTalentedPeopleSearchModel() {
		return talentedPeopleSearchModel;
	}

	public void setTalentedPeopleSearchModel(TalentedPeopleSearchModel talentedPeopleSearchModel) {
		this.talentedPeopleSearchModel = talentedPeopleSearchModel;
	}

	public CoopExSearchModel getCoopExSearchModel() {
		return coopExSearchModel;
	}

	public void setCoopExSearchModel(CoopExSearchModel coopExSearchModel) {
		this.coopExSearchModel = coopExSearchModel;
	}

	public LiteratureSearchModel getLiteratureSearchModel() {
		return literatureSearchModel;
	}

	public void setLiteratureSearchModel(LiteratureSearchModel literatureSearchModel) {
		this.literatureSearchModel = literatureSearchModel;
	}

	public IncubationCenterSearchModel getIncubationCenterSearchModel() {
		return incubationCenterSearchModel;
	}

	public void setIncubationCenterSearchModel(IncubationCenterSearchModel incubationCenterSearchModel) {
		this.incubationCenterSearchModel = incubationCenterSearchModel;
	}
	
	public List<BaseOption> getYearList() {
		if (this.yearList == null) {
			this.yearList = new ArrayList<BaseOption>();
			List<Integer> yearList = this.researchPlanService.getYearList();
			for (int year : yearList) {
				String strYear = String.valueOf(year);
				this.yearList.add(new BaseOption(strYear, strYear+"年"));
			}
		}
		return this.yearList;
	}
	
	public List<OptionTrl> getOptionTrlList() {
		if (this.optionTrlList == null) {
			this.optionTrlList = this.optionTrlService.listAll();
		}
		return optionTrlList;
	}
	
	public List<OptionGrbDomain> getOptionGrbDomainList() {
		if (this.optionGrbDomainList == null) {
			this.optionGrbDomainList = this.optionGrbDomainService.listForResearchPlan();
		}
		return this.optionGrbDomainList;
	}
	
	public List<OptionCountry> getOptionCountryList() {
		if (optionCountryList == null) {
			this.optionCountryList = this.optionCountryService.listAll();
		}
		return optionCountryList;
	}
	
	public List<TechField> getTechFieldList() {
		if (techFieldList == null) {
			this.techFieldList = this.techFieldService.listAll();
		}
		return techFieldList;
	}
	
	public List<OptionDomain> getMainDomainList() {
		if (mainDomainList == null) {
			mainDomainList = ServiceFactory.getOptionDomainService().listAll();
		}
		return mainDomainList;
	}
	
	public List<BaseOption> getTypeList() {
		return typeList;
	}
	
	public List<BaseOption> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<BaseOption> attributeList) {
		this.attributeList = attributeList;
	}
}
