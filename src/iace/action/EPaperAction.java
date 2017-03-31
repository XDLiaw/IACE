package iace.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import core.util.PagedList;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;
import iace.entity.ePaper.EPaper;
import iace.entity.ePaper.EPaperProduceTemplate;
import iace.entity.ePaper.EPaperSearchModel;
import iace.entity.faq.Faq;
import iace.entity.faq.FaqSearchModel;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.industryInfo.IndustryInfoSearchModel;
import iace.entity.news.News;
import iace.entity.news.NewsSearchModel;
import iace.entity.option.BaseOption;
import iace.entity.option.OptionCountry;
import iace.entity.option.OptionGrbDomain;
import iace.entity.option.OptionTrl;
import iace.entity.patent.Patent;
import iace.entity.patent.PatentSearchModel;
import iace.entity.patent.TechField;
import iace.entity.rdFocus.RdFocus;
import iace.entity.rdFocus.RdFocusSearchModel;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.researchPlan.ResearchPlanSearchModel;
import iace.service.ServiceFactory;
import iace.service.activity.ActivityService;
import iace.service.ePaper.EPaperService;
import iace.service.faq.FaqService;
import iace.service.industryInfo.IndustryInfoService;
import iace.service.news.NewsService;
import iace.service.option.OptionCountryService;
import iace.service.option.OptionGrbDomainService;
import iace.service.option.OptionTrlService;
import iace.service.patent.PatentService;
import iace.service.patent.TechFieldService;
import iace.service.rdFocus.RdFocusService;
import iace.service.researchPlan.ResearchPlanService;

public class EPaperAction extends BaseIaceAction {
	private static final long serialVersionUID = 248576610418563326L;

	private EPaperService epaperService = ServiceFactory.getEpaperService();
	private NewsService newsService = ServiceFactory.getNewsService();
	private ActivityService activityService = ServiceFactory.getActivityService();
	private RdFocusService rdFocusService = ServiceFactory.getRdFocusService();
	private ResearchPlanService researchPlanService = ServiceFactory.getResearchPlanService();
	private PatentService patentService = ServiceFactory.getPatentService();
	private IndustryInfoService industryInfoService = ServiceFactory.getIndustryInfoService();
	private FaqService faqService = ServiceFactory.getFaqService();
	private OptionGrbDomainService optionGrbDomainService = ServiceFactory.getOptionGrbDomainService();
	private OptionTrlService optionTrlService = ServiceFactory.getOptionTrlService();
	private OptionCountryService optionCountryService = ServiceFactory.getOptionCountryService();
	private TechFieldService techFieldService = ServiceFactory.getTechFieldService();

	private EPaperSearchModel searchCondition = new EPaperSearchModel();
	private PagedList<EPaper> epaperPagedList;

	private Long id;
	private EPaper epaper;
	private String epaperUrl;

	private String testEmailTo;

	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;

	private boolean loadDefaultSample = false;
	private EPaperProduceTemplate template = new EPaperProduceTemplate();
	private PagedList<News> newsPagedList;
	private PagedList<Activity> activityPagedList;
	private PagedList<RdFocus> rdFocusPagedList;
	private PagedList<ResearchPlan> researchPlanPagedList;
	private PagedList<Patent> patentPagedList;
	private PagedList<IndustryInfo> industryInfoPagedList;
	private PagedList<Faq> faqPagedList;
	private NewsSearchModel newsSearchCondition = new NewsSearchModel();
	private ActivitySearchModel activitySearchCondition = new ActivitySearchModel();
	private RdFocusSearchModel rdFocusSearchCondition = new RdFocusSearchModel();
	private ResearchPlanSearchModel researchPlanSearchCondition = new ResearchPlanSearchModel();
	private PatentSearchModel patentSearchCondition = new PatentSearchModel();
	private IndustryInfoSearchModel industryInfoSearchCondition = new IndustryInfoSearchModel();
	private FaqSearchModel faqSearchCondition = new FaqSearchModel();

	private List<OptionGrbDomain> optionGrbDomainList;
	private List<OptionTrl> optionTrlList;
	private List<BaseOption> yearList;
	private List<TechField> techFieldList;
	private List<OptionCountry> optionCountryList;

	public EPaperAction() {
		super.setTitle("電子報");
	}

	public String init() {
		return index();
	}

	public String index() {
		try {
			this.epaperPagedList = this.epaperService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String showDetail() {
		try {
			this.epaper = this.epaperService.get(this.id);
			if (this.epaper == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	private void getDefaultSample() {
		if (this.loadDefaultSample) {
			// news
			this.template.setNewsList(this.newsService.sampleForEpaper());
			Set<Long> newsIds = new HashSet<Long>();
			for (News news : this.template.getNewsList()) {
				newsIds.add(news.getId());
			}
			this.template.setNewsIds(newsIds);

			// activity
			this.template.setActivityList(this.activityService.sampleForEpaper());
			Set<Long> activityIds = new HashSet<Long>();
			for (Activity a : this.template.getActivityList()) {
				activityIds.add(a.getId());
			}
			this.template.setActivityIds(activityIds);

			// rdFocus
			this.template.setRdFocusList(this.rdFocusService.sampleForEpaper());
			Set<Long> rdFocusIds = new HashSet<Long>();
			for (RdFocus rdFocus : this.template.getRdFocusList()) {
				rdFocusIds.add(rdFocus.getId());
			}
			this.template.setRdFocusIds(rdFocusIds);

			// researchPlan
			this.template.setResearchPlanList(this.researchPlanService.sampleForEpaper());
			Set<Long> researchPlanIds = new HashSet<Long>();
			for (ResearchPlan rp : this.template.getResearchPlanList()) {
				researchPlanIds.add(rp.getId());
			}
			this.template.setResearchPlanIds(researchPlanIds);

			// patent
			this.template.setPatentList(this.patentService.sampleForEpaper());
			Set<Long> patentIds = new HashSet<Long>();
			for (Patent p : this.template.getPatentList()) {
				patentIds.add(p.getId());
			}
			this.template.setPatentIds(patentIds);

			// Industry info
			this.template.setIndustryInfoList(this.industryInfoService.sampleForEpaper());
			Set<Long> industryInfoIds = new HashSet<Long>();
			for (IndustryInfo ii : this.template.getIndustryInfoList()) {
				industryInfoIds.add(ii.getId());
			}
			this.template.setIndustryInfoIds(industryInfoIds);

			// faq
			this.template.setFaqList(this.faqService.sampleForEpaper());
			Set<Long> faqIds = new HashSet<Long>();
			for (Faq faq : this.template.getFaqList()) {
				faqIds.add(faq.getId());
			}
			this.template.setFaqIds(faqIds);

			this.loadDefaultSample = false;
		}
	}

	private void getEntitiesByIds() {
		if (CollectionUtils.isNotEmpty(this.template.getNewsIds()) && CollectionUtils.isEmpty(this.template.getNewsList())) {
			this.template.setNewsList(this.newsService.getAll(this.template.getNewsIds()));
		}
		if (CollectionUtils.isNotEmpty(this.template.getActivityIds()) && CollectionUtils.isEmpty(this.template.getActivityList())) {
			this.template.setActivityList(this.activityService.getAll(this.template.getActivityIds()));
		}
		if (CollectionUtils.isNotEmpty(this.template.getRdFocusIds()) && CollectionUtils.isEmpty(this.template.getRdFocusList())) {
			this.template.setRdFocusList(this.rdFocusService.getAll(this.template.getRdFocusIds()));
		}
		if (CollectionUtils.isNotEmpty(this.template.getResearchPlanIds()) && CollectionUtils.isEmpty(this.template.getResearchPlanList())) {
			this.template.setResearchPlanList(this.researchPlanService.getAll(this.template.getResearchPlanIds()));
		}
		if (CollectionUtils.isNotEmpty(this.template.getPatentIds()) && CollectionUtils.isEmpty(this.template.getPatentList())) {
			this.template.setPatentList(this.patentService.getAll(this.template.getPatentIds()));
		}
		if (CollectionUtils.isNotEmpty(this.template.getIndustryInfoIds()) && CollectionUtils.isEmpty(this.template.getIndustryInfoList())) {
			this.template.setIndustryInfoList(this.industryInfoService.getAll(this.template.getIndustryInfoIds()));
		}
		if (CollectionUtils.isNotEmpty(this.template.getFaqIds()) && CollectionUtils.isEmpty(this.template.getFaqList())) {
			this.template.setFaqList(this.faqService.getAll(this.template.getFaqIds()));
		}
	}

	public String create() {
		try {
			getDefaultSample();
			getEntitiesByIds();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public void validateCreatePreview() {
		validateCreateSubmit();
	}

	public String createPreview() {
		try {
			String fileName = this.epaperService.createPreview(this.template);
			this.epaperUrl = "/ePapers/" + fileName;
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public void validateCreateSubmit() {
		super.validateNotBlankNLength(this.template.getTitle(), 100, "template.title");
		super.validateNotNull(this.template.getPostDate(), "template.postDate");
		getEntitiesByIds();
	}

	public String createSubmit() {
		try {
			this.epaper = this.epaperService.create(this.template);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String update() {
		return showDetail();
	}

	public void validateUpdateSubmit() {
		super.validateNotBlankNLength(this.epaper.getTitle(), 200, "epaper.title");
		super.validateNotNull(this.epaper.getPostDate(), "epaper.postDate");
	}

	public String updateSubmit() {
		try {
			this.epaperService.update(this.epaper, super.getCurrentSysUser(), false, super.getSysLog());
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String delete() {
		return showDetail();
	}

	public String deleteSubmit() {
		try {
			this.epaperService.delete(this.id, false, super.getSysLog());
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	@Deprecated
	public String read() {
		try {
			// this.epaperUrl = "/ePapers/20160706163719.jsp";
			// this.epaperUrl = "/ePapers/20160909144004.jsp";
			// this.epaperUrl = "/ePapers/20170105185502.jsp";

			showDetail();
			this.epaperUrl = this.epaper.getUrl();

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String publish() {
		try {
			this.epaperService.publish(this.id, super.getCurrentSysUser(), this.getSysLog());
			index();
			super.addActionMessage("發佈成功");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String sendTestEmail() {
		try {
			this.epaperService.sendTestEmail(this.id, this.testEmailTo, this.uploadFile);
			index();
			super.addActionMessage("寄送成功");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String newsIndex() {
		try {
			this.newsPagedList = this.newsService.searchBy(this.newsSearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String activityIndex() {
		try {
			log.debug(this.template.getActivityIds());
			this.activityPagedList = this.activityService.searchBy(this.activitySearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String rdFocusIndex() {
		try {
			this.rdFocusPagedList = this.rdFocusService.searchBy(this.rdFocusSearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String researchPlanIndex() {
		try {
			this.researchPlanPagedList = this.researchPlanService.searchBy(this.researchPlanSearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String patentIndex() {
		try {
			this.patentPagedList = this.patentService.searchBy(this.patentSearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String industryInfoIndex() {
		try {
			this.industryInfoPagedList = this.industryInfoService.searchBy(this.industryInfoSearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String faqIndex() {
		try {
			this.faqPagedList = this.faqService.searchBy(this.faqSearchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	// =========================================================================

	public String getEpaperUrl() {
		return epaperUrl;
	}

	public void setEpaperUrl(String epaperUrl) {
		this.epaperUrl = epaperUrl;
	}

	public EPaperService getEpaperService() {
		return epaperService;
	}

	public void setEpaperService(EPaperService epaperService) {
		this.epaperService = epaperService;
	}

	public EPaperSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(EPaperSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<EPaper> getEpaperPagedList() {
		return epaperPagedList;
	}

	public void setEpaperPagedList(PagedList<EPaper> epaperPagedList) {
		this.epaperPagedList = epaperPagedList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EPaper getEpaper() {
		return epaper;
	}

	public void setEpaper(EPaper epaper) {
		this.epaper = epaper;
	}

	public String getTestEmailTo() {
		return testEmailTo;
	}

	public void setTestEmailTo(String testEmailTo) {
		this.testEmailTo = testEmailTo;
	}

	public boolean isLoadDefaultSample() {
		return loadDefaultSample;
	}

	public void setLoadDefaultSample(boolean loadDefaultSample) {
		this.loadDefaultSample = loadDefaultSample;
	}

	public EPaperProduceTemplate getTemplate() {
		return template;
	}

	public void setTemplate(EPaperProduceTemplate template) {
		this.template = template;
	}

	public NewsSearchModel getNewsSearchCondition() {
		return newsSearchCondition;
	}

	public void setNewsSearchCondition(NewsSearchModel newsSearchCondition) {
		this.newsSearchCondition = newsSearchCondition;
	}

	public ActivitySearchModel getActivitySearchCondition() {
		return activitySearchCondition;
	}

	public void setActivitySearchCondition(ActivitySearchModel activitySearchCondition) {
		this.activitySearchCondition = activitySearchCondition;
	}

	public RdFocusSearchModel getRdFocusSearchCondition() {
		return rdFocusSearchCondition;
	}

	public void setRdFocusSearchCondition(RdFocusSearchModel rdFocusSearchCondition) {
		this.rdFocusSearchCondition = rdFocusSearchCondition;
	}

	public ResearchPlanSearchModel getResearchPlanSearchCondition() {
		return researchPlanSearchCondition;
	}

	public void setResearchPlanSearchCondition(ResearchPlanSearchModel researchPlanSearchCondition) {
		this.researchPlanSearchCondition = researchPlanSearchCondition;
	}

	public PatentSearchModel getPatentSearchCondition() {
		return patentSearchCondition;
	}

	public void setPatentSearchCondition(PatentSearchModel patentSearchCondition) {
		this.patentSearchCondition = patentSearchCondition;
	}

	public IndustryInfoSearchModel getIndustryInfoSearchCondition() {
		return industryInfoSearchCondition;
	}

	public void setIndustryInfoSearchCondition(IndustryInfoSearchModel industryInfoSearchCondition) {
		this.industryInfoSearchCondition = industryInfoSearchCondition;
	}

	public FaqSearchModel getFaqSearchCondition() {
		return faqSearchCondition;
	}

	public void setFaqSearchCondition(FaqSearchModel faqSearchCondition) {
		this.faqSearchCondition = faqSearchCondition;
	}

	public PagedList<News> getNewsPagedList() {
		return newsPagedList;
	}

	public PagedList<Activity> getActivityPagedList() {
		return activityPagedList;
	}

	public PagedList<RdFocus> getRdFocusPagedList() {
		return rdFocusPagedList;
	}

	public PagedList<ResearchPlan> getResearchPlanPagedList() {
		return researchPlanPagedList;
	}

	public PagedList<Patent> getPatentPagedList() {
		return patentPagedList;
	}

	public PagedList<IndustryInfo> getIndustryInfoPagedList() {
		return industryInfoPagedList;
	}

	public PagedList<Faq> getFaqPagedList() {
		return faqPagedList;
	}

	public List<OptionGrbDomain> getOptionGrbDomainList() {
		if (this.optionGrbDomainList == null) {
			this.optionGrbDomainList = this.optionGrbDomainService.listForResearchPlan();
		}
		return this.optionGrbDomainList;
	}

	public List<OptionTrl> getOptionTrlList() {
		if (this.optionTrlList == null) {
			this.optionTrlList = this.optionTrlService.listAll();
		}
		return optionTrlList;
	}

	public List<BaseOption> getYearList() {
		if (this.yearList == null) {
			this.yearList = new ArrayList<BaseOption>();
			List<Integer> yearList = this.researchPlanService.getYearList();
			for (int year : yearList) {
				String strYear = String.valueOf(year);
				this.yearList.add(new BaseOption(strYear, strYear + "年"));
			}
		}
		return this.yearList;
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

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileContentType() {
		return uploadFileContentType;
	}

	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
}
