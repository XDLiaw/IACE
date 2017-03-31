package iace.action;

import java.util.Date;
import java.util.List;

import core.util.AESEncrypter;
import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.industryInfo.IndustryInfoSearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.industryInfo.IndustryInfoService;

public class IndustryInfoAction extends BaseIaceAction {

	private static final long serialVersionUID = 6797243583037760865L;

	private IndustryInfoService industryInfoService = ServiceFactory.getIndustryInfoService();
	
	private IndustryInfoSearchModel searchCondition = new IndustryInfoSearchModel();
	private PagedList<IndustryInfo> industryInfoPagedList;
	
	private Long id;
	private IndustryInfo industryInfo;
	
	private List<BaseOption> categoryList = IndustryInfo.getCategoryList();
	
	private String keyForSyncData;
	
	public IndustryInfoAction() {
		super.setTitle("產業情報");
	}

	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.industryInfoPagedList = this.industryInfoService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, IndustryInfo.class);
			this.industryInfo = this.industryInfoService.get(this.id);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String increaseClickNum() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, IndustryInfo.class);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String syncDataWithoutLogin() {
		try {
			String decryptKey = AESEncrypter.decrypt("iace@!QAZ", this.keyForSyncData);
			if ("sysvin".equals(decryptKey) == false) {
				throw new Exception("key值錯誤");
			} else {
				return syncData();
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String syncData() {
		try {
			super.setTitle("");
			Date d1 = new Date();
			this.industryInfoService.syncData(super.getCurrentSysUser(), true, null);
			Date d2 = new Date();
			long spendSec = (d2.getTime()-d1.getTime())/1000;
			this.addActionMessage("產業情報資料同步完成!用時"+spendSec+"秒.");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	//==========================================================================
	
	public IndustryInfoSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(IndustryInfoSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IndustryInfo getIndustryInfo() {
		return industryInfo;
	}

	public void setIndustryInfo(IndustryInfo industryInfo) {
		this.industryInfo = industryInfo;
	}

	public PagedList<IndustryInfo> getIndustryInfoPagedList() {
		return industryInfoPagedList;
	}

	public List<BaseOption> getCategoryList() {
		return categoryList;
	}

	public String getKeyForSyncData() {
		return keyForSyncData;
	}

	public void setKeyForSyncData(String keyForSyncData) {
		this.keyForSyncData = keyForSyncData;
	}
	
	
}
