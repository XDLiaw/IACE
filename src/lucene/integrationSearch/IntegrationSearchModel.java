package lucene.integrationSearch;

import iace.entity.BaseSearchModel;

public class IntegrationSearchModel extends BaseSearchModel {
	private String className;
	private String searchText;
	private String researchPlanManager;
	
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

}
