package iace.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import core.util.AESEncrypter;
import core.util.PagedList;
import iace.entity.coopExample.CoopEx;
import iace.entity.incubationCenter.IncubationCenter;
import iace.entity.literature.Literature;
import iace.entity.option.BaseOption;
import iace.entity.patent.Patent;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.researchPlan.ResearchPlanManagerSearchResult;
import iace.entity.researchPlan.Technology;
import iace.entity.talentedPeople.TalentedPeople;
import iace.service.ServiceFactory;
import iace.service.lucene.LuceneIndexService;
import iace.service.researchPlan.ResearchPlanService;
import lucene.integrationSearch.IntegrationSearchModel;
import lucene.integrationSearch.IntegrationSearchResult;

public class LuceneAction extends BaseIaceAction {

	private static final long serialVersionUID = 7669450808497278799L;

	private LuceneIndexService luceneIndexService = ServiceFactory.getLuceneIndexService();
	private ResearchPlanService researchPlanService = ServiceFactory.getResearchPlanService();
	
	private IntegrationSearchModel searchCondition = new IntegrationSearchModel();

	private PagedList<IntegrationSearchResult> pagedList;
	private List<ResearchPlanManagerSearchResult> rpManagerList;
	
	private String rpManagerJsonString;
	
	private boolean svgDisplayStatus;
	
	private String keyForRebuildIndex;
	
	public LuceneAction() {
		super.setTitle("Lucene全文檢索");
	}
	
	public String rebuildIndexWithoutLogin() {
		try {
			String decryptKey = AESEncrypter.decrypt("iace@!QAZ", this.keyForRebuildIndex);
			if ("sysvin".equals(decryptKey) == false) {
				throw new Exception("key值錯誤");
			} else {
				return rebuildIndex();
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateRebuildIndex() {
		String sysRoleName = super.getCurrentSysUser().getSysRole().getName();
		if (StringUtils.equals(sysRoleName, "系統開發人員") == false) {
			super.addActionError("沒有權限");
		}
	}
	
	public String rebuildIndex() {
		try {
			Date d1 = new Date();
			this.luceneIndexService.rebuildIndex();
			this.researchPlanService.rebuildResearchPlanIndex();
			Date d2 = new Date();
			long spendSec = (d2.getTime()-d1.getTime())/1000;
			this.addActionMessage("索引重建完成!用時"+spendSec+"秒.");
			
			this.setTitle("開發者專屬功能");
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String integrationSearchInit() {
		return SUCCESS;
	}
	
	public void validateIntegrationSearch() {
		//  需要給className 防止為空 例如直接輸入index網址 最上面的浮動選單搜尋 都不會設定className
		if(this.searchCondition.getClassName()==null){
			this.searchCondition.setClassName("");
		}
		//若以"全部""其他"查詢文字欄位不可為空
		if((this.searchCondition.getClassName().equals(""))||(this.searchCondition.getClassName().equals("OTHER"))){
			super.validateNotBlank(this.searchCondition.getSearchText(), "searchCondition.searchText");
		}
	}
	
	public String integrationSearch() {
		try {
			if (StringUtils.equals(this.searchCondition.getClassName(), ResearchPlan.class.getName()) == false) {
				this.pagedList = this.luceneIndexService.integrationSearch(this.searchCondition);
			} else {
				// 取得網絡圖的資料
				this.rpManagerList = this.researchPlanService.searchManagerFromResearchPlanIndex(this.searchCondition);	
				generateResearchPlanManagersJsonString();

				// 取得搜尋結果列表的資料
				if (StringUtils.isBlank(this.searchCondition.getResearchPlanManager())) {
					this.pagedList = this.luceneIndexService.integrationSearch(this.searchCondition);
				} else {
					// 取得被點擊的計劃主持人的所有研發成果
					List<ResearchPlan> rpList = new ArrayList<ResearchPlan>();
					for (ResearchPlanManagerSearchResult res : this.rpManagerList) {
						for (Long researchPlanId : res.getResearchPlanIdList()) {
							ResearchPlan rp  = this.researchPlanService.get(researchPlanId);
							rpList.add(rp);
						}
					}
					
					// 研發成果做分頁處理
					int pIndex = this.searchCondition.getPageIndex();
					int pSize = this.searchCondition.getPageSize();
					List<IntegrationSearchResult> list = new ArrayList<IntegrationSearchResult>();
					for (int i=pIndex*pSize; i<(pIndex+1)*pSize && i<rpList.size(); i++) {
						IntegrationSearchResult r = new IntegrationSearchResult();
						r.setType(ResearchPlan.class.getName());
						r.setResearchPlan(rpList.get(i));
						list.add(r);
					}
					this.pagedList = new PagedList<IntegrationSearchResult>(list, rpList.size(), pSize, pIndex);
				}
			}
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void generateResearchPlanManagersJsonString() {
		JsonArray nodes = new JsonArray();
		JsonArray links = new JsonArray();
		{
			JsonObject node = new JsonObject();
			String displayText = this.searchCondition.getResearchPlanSearchModel().getKeyword();
			node.addProperty("displayText", displayText);
			node.addProperty("width", displayText.length()*13+15);
			node.addProperty("height", 23);
			node.addProperty("type", "type-keyword");
			node.addProperty("nodeValue", displayText);
			nodes.add(node);
		}
		for (int i = 0; i<this.rpManagerList.size(); i++) {
			ResearchPlanManagerSearchResult rpM = this.rpManagerList.get(i);
			
			JsonObject node = new JsonObject();
			String displayText = rpM.getManager() + "：" + rpM.getResearchPlanCount();
			node.addProperty("displayText", displayText);
			node.addProperty("width", displayText.length()*13+10);
			node.addProperty("height", 20);
			node.addProperty("type", "type-manager");
			node.addProperty("nodeValue", rpM.getManager());
			nodes.add(node);
			
			JsonObject link = new JsonObject();
			link.addProperty("source", 0);
			link.addProperty("target", i+1);
			links.add(link);
		}
		
		JsonObject result = new JsonObject();
		result.add("nodes", nodes);
		result.add("links", links);
		
		this.rpManagerJsonString = result.toString();
	}

	public IntegrationSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(IntegrationSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<IntegrationSearchResult> getPagedList() {
		return pagedList;
	}

	public void setPagedList(PagedList<IntegrationSearchResult> pagedList) {
		this.pagedList = pagedList;
	}
	
	public List<ResearchPlanManagerSearchResult> getRpManagerList() {
		return rpManagerList;
	}
	
	public String getRpManagerJsonString() {
		return rpManagerJsonString;
	}

	public List<BaseOption> getClassList() {
		List<BaseOption> list = new ArrayList<BaseOption>();
		list.add(new BaseOption(Technology.class.getName(), "研發成果"));
		list.add(new BaseOption(Patent.class.getName(), "專利資料"));
		list.add(new BaseOption(TalentedPeople.class.getName(), "產學人才"));
		list.add(new BaseOption(CoopEx.class.getName(), "合作案例"));
		list.add(new BaseOption(Literature.class.getName(), "法規/文獻"));
		list.add(new BaseOption(IncubationCenter.class.getName(), "育成中心"));
		list.add(new BaseOption("OTHER", "其他"));
		return list;
	}

	public boolean isSvgDisplayStatus() {
		return svgDisplayStatus;
	}

	public void setSvgDisplayStatus(boolean svgDisplayStatus) {
		this.svgDisplayStatus = svgDisplayStatus;
	}

	public String getKeyForRebuildIndex() {
		return keyForRebuildIndex;
	}

	public void setKeyForRebuildIndex(String keyForRebuildIndex) {
		this.keyForRebuildIndex = keyForRebuildIndex;
	}

}
