package iace.action;

import java.util.List;

import iace.entity.activity.Activity;
import iace.entity.coopExample.CoopEx;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.literature.Literature;
import iace.entity.news.News;
import iace.entity.patent.Patent;
import iace.entity.researchPlan.Technology;
import iace.entity.talentedPeople.TalentedPeople;
import iace.service.ServiceFactory;
import iace.service.activity.ActivityService;
import iace.service.coopExample.CoopExService;
import iace.service.industryInfo.IndustryInfoService;
import iace.service.literature.LiteratureService;
import iace.service.news.NewsService;
import iace.service.patent.PatentService;
import iace.service.researchPlan.TechnologyService;
import iace.service.talentedPeople.TalentedPeopleService;

public class HomeAction extends BaseIaceAction {

	private static final long serialVersionUID = -8420589647403574999L;

	private NewsService newsService = ServiceFactory.getNewsService();
	private ActivityService activityService = ServiceFactory.getActivityService();
	private IndustryInfoService industryInfoService = ServiceFactory.getIndustryInfoService();
	private TechnologyService technologyService = ServiceFactory.getTechnologyService();
	private PatentService patentService = ServiceFactory.getPatentService();
	private CoopExService coopExService = ServiceFactory.getCoopExService();
	private TalentedPeopleService talentedPeopleService = ServiceFactory.getTalentedPeopleService();
	private LiteratureService literatureService = ServiceFactory.getLiteratureService();
	
	private List<News> newsList;
	private List<Activity> activityList;
	private List<IndustryInfo> industryInfoList1;
	private List<IndustryInfo> industryInfoList2;
	private List<Technology> technologyList;
	private List<Patent> patentList;
	private List<CoopEx> coopExList;
	private List<TalentedPeople> talentedPeopleList;
	private List<Literature> literatureList;
	private List<Literature> policyList;
	
	public HomeAction() {
		super.setTitle("首頁");
	}
	
	public String init() {
		try {
			this.technologyList = this.technologyService.sampleForHomePage();
			this.coopExList = this.coopExService.sampleForHomePage();
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String init2() {
		try {
			this.newsList = this.newsService.sampleForHomePage();
			this.activityList = this.activityService.sampleForHomePage();
			this.industryInfoList1 = this.industryInfoService.sampleForHomePage(IndustryInfo.getCategoryList().get(0).getCode());
			this.industryInfoList2 = this.industryInfoService.sampleForHomePage(IndustryInfo.getCategoryList().get(1).getCode());
			this.technologyList = this.technologyService.sampleForHomePageV2();
			this.patentList = this.patentService.sampleForHomePage();
			this.coopExList = this.coopExService.sampleForHomePage();
			this.talentedPeopleList = this.talentedPeopleService.sampleForHomePage();
			this.literatureList = this.literatureService.sampleForHomePage("文獻");
			this.policyList = this.literatureService.sampleForHomePage("法規政策");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	//==========================================================================

	public List<News> getNewsList() {
		return newsList;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}
	
	public List<IndustryInfo> getIndustryInfoList1() {
		return industryInfoList1;
	}

	public List<IndustryInfo> getIndustryInfoList2() {
		return industryInfoList2;
	}

	public List<Technology> getTechnologyList() {
		return technologyList;
	}
	
	public List<Patent> getPatentList() {
		return patentList;
	}

	public List<CoopEx> getCoopExList() {
		return coopExList;
	}

	public List<TalentedPeople> getTalentedPeopleList() {
		return talentedPeopleList;
	}

	public List<Literature> getLiteratureList() {
		return literatureList;
	}

	public List<Literature> getPolicyList() {
		return policyList;
	}
	
}
