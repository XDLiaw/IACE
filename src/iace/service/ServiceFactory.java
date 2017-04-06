package iace.service;

import iace.dao.DaoFactory;
import iace.service.about.AboutService;
import iace.service.activity.ActivityAttachService;
import iace.service.activity.ActivityService;
import iace.service.activity.PopularActivityService;
import iace.service.consulting.ConsultingService;
import iace.service.coopExample.CoopExAttachFileService;
import iace.service.coopExample.CoopExImgService;
import iace.service.coopExample.CoopExService;
import iace.service.coopExample.CoopExVideoService;
import iace.service.customerService.ContactUsService;
import iace.service.ePaper.EPaperMailOpenLogService;
import iace.service.ePaper.EPaperService;
import iace.service.ePaper.EPaperSubscriberService;
import iace.service.enterpriseNeed.EnterpriseInfoService;
import iace.service.enterpriseNeed.EnterpriseRequireTechService;
import iace.service.faq.FaqService;
import iace.service.httpRequestLog.HttpRequestLogService;
import iace.service.incubationCenter.IncubationCenterService;
import iace.service.industryInfo.IndustryInfoService;
import iace.service.literature.LiteratureService;
import iace.service.lucene.LuceneIndexService;
import iace.service.marquee.MarqueeService;
import iace.service.member.MemberService;
import iace.service.news.NewsAttachService;
import iace.service.news.NewsService;
import iace.service.option.OptionCompanyLocationService;
import iace.service.option.OptionConsultService;
import iace.service.option.OptionCooperateModeService;
import iace.service.option.OptionCountryService;
import iace.service.option.OptionDomainService;
import iace.service.option.OptionGrbDomainService;
import iace.service.option.OptionHadTecSrcService;
import iace.service.option.OptionHrstService;
import iace.service.option.OptionIndustryService;
import iace.service.option.OptionOrganizationClassService;
import iace.service.option.OptionOrganizationTypeService;
import iace.service.option.OptionSchoolService;
import iace.service.option.OptionSubjectService;
import iace.service.option.OptionTrlService;
import iace.service.patent.PatentExcelService;
import iace.service.patent.PatentService;
import iace.service.patent.TechFieldService;
import iace.service.qnrCooperateWay.QnrCooperateWayExcelService;
import iace.service.qnrCooperateWay.QnrCooperateWayService;
import iace.service.rdFocus.RdFocusAttachService;
import iace.service.rdFocus.RdFocusService;
import iace.service.relatedWebsite.RelatedWebsiteService;
import iace.service.researchPlan.ResearchPlanExcelService;
import iace.service.researchPlan.ResearchPlanService;
import iace.service.researchPlan.TechnologyService;
import iace.service.sys.SysLogService;
import iace.service.sys.SysRoleService;
import iace.service.sys.SysUserService;
import iace.service.talentedPeople.TalentedPeopleMainProjectService;
import iace.service.talentedPeople.TalentedPeoplePDPLService;
import iace.service.talentedPeople.TalentedPeopleRdResultService;
import iace.service.talentedPeople.TalentedPeopleService;
import iace.service.talentedPeople.TalentedPeopleTransferCaseService;
import iace.service.videosArea.VideoService;
import iace.service.videosArea.VideosAreaService;


public class ServiceFactory {
	private static LuceneIndexService luceneIndexService;
	
	private static SysRoleService sysRoleService;
	private static SysUserService sysUserService;
	private static SysLogService sysLogService;
	private static HttpRequestLogService httpRequestLogService;
	
	private static OptionCompanyLocationService optionCompanyLocationService;
	private static OptionConsultService optionConsultService;
	private static OptionCooperateModeService optionCooperateModeService;
	private static OptionCountryService optionCountryService;
	private static OptionGrbDomainService optionGrbDomainService;
	private static OptionHadTecSrcService optionHadTecSrcService;
	private static OptionHrstService optionHrstService;
	private static OptionIndustryService optionIndustryService;
	private static OptionDomainService optionDomainService;
	private static OptionOrganizationClassService optionOrganizationClassService;
	private static OptionOrganizationTypeService optionOrganizationTypeService;
	private static OptionTrlService optionTrlService;
	private static OptionSubjectService optionSubjectService;
	private static OptionSchoolService optionSchoolService;
	
	private static PatentService patentService;
	private static TechFieldService techFieldService;
	private static PatentExcelService patentExcelService;
	
	private static ResearchPlanService researchPlanService;
	private static TechnologyService technologyService;	
	private static ResearchPlanExcelService researchPlanExcelService;
	
	private static ConsultingService consultingService;
	
	private static EnterpriseInfoService enterpriseInfoService;
	private static EnterpriseRequireTechService enterpriseRequireTechService;
	
	private static QnrCooperateWayService qnrCooperateWayService;
	private static QnrCooperateWayExcelService qnrCooperateWayExcelService;
	
	private static CoopExService coopExService;
	private static CoopExImgService coopExImgService;
	private static CoopExVideoService coopExVideoService;
	private static CoopExAttachFileService coopExAttachFileService;
	
	private static TalentedPeopleService talentedPeopleService;
	private static TalentedPeopleRdResultService talentedPeopleRdResultService;
	private static TalentedPeopleTransferCaseService talentedPeopleTransferCaseService;
	private static TalentedPeopleMainProjectService talentedPeopleMainProjectService;
	private static TalentedPeoplePDPLService talentedPeoplePDPLService;
	
	private static IncubationCenterService incubationCenterService;
	
	private static LiteratureService literatureService;
	
	private static AboutService aboutService;
	
	private static FaqService faqService;
	
	private static NewsService newsService;
	private static NewsAttachService newsAttachService;
	
	private static RdFocusService rdFocusService;
	private static RdFocusAttachService rdFocusAttachService;
	
	private static ActivityService activityService;
	private static ActivityAttachService activityAttachService;
	private static PopularActivityService popularActivityService;
	
	private static MemberService memberService;
	
	private static ContactUsService contactUsService;
	
	private static IndustryInfoService industryInfoService; 
	
	private static RelatedWebsiteService relatedWebsiteService;
	
	private static VideosAreaService videosAreaService;
	private static VideoService videoService;
	
	private static EPaperService epaperService;
	private static EPaperMailOpenLogService epaperMailOpenLogService;
	private static EPaperSubscriberService epaperSubscriberService;
	
	private static MarqueeService marqueeService;
	
	// =========================================================================
	
	public static LuceneIndexService getLuceneIndexService() {
		if (luceneIndexService == null) {
			luceneIndexService = new LuceneIndexService(
					DaoFactory.getResearchPlanDao(),
					DaoFactory.getTechnologyDao(),
					DaoFactory.getPatentDao(),
					DaoFactory.getTalentedPeopleDao(),
					DaoFactory.getCoopExDao(),
					DaoFactory.getLiteratureDao(),
					DaoFactory.getIncubationCenterDao(),
					DaoFactory.getActivityDao(),
					DaoFactory.getIndustryInfoDao(),
					DaoFactory.getNewsDao());
		}
		return luceneIndexService;
	}
	
	public static SysRoleService getSysRoleService() {
		if (sysRoleService == null) {
			sysRoleService = new SysRoleService(DaoFactory.getSysRoleDao());
		}
		return sysRoleService;
	}
	
	public static SysUserService getSysUserService() {
		if (sysUserService == null) {
			sysUserService = new SysUserService(DaoFactory.getSysUserDao());
		}
		return sysUserService;
	}
	
	public static SysLogService getSysLogService() {
		if (sysLogService == null) {
			sysLogService = new SysLogService(DaoFactory.getSysLogDao());
		}
		return sysLogService;
	}
	
	public static HttpRequestLogService getHttpRequestLogService() {
		if (httpRequestLogService == null) {
			httpRequestLogService = new HttpRequestLogService(DaoFactory.getHttpRequestLogDao());
		}
		return httpRequestLogService;
	}

	public static OptionCompanyLocationService getOptionCompanyLocationService() {
		if (optionCompanyLocationService == null) {
			optionCompanyLocationService = new OptionCompanyLocationService(DaoFactory.getOptionCompanyLocationDao());
		}
		return optionCompanyLocationService;
	}
	
	public static OptionConsultService getOptionConsultService() {
		if (optionConsultService == null) {
			optionConsultService = new OptionConsultService(DaoFactory.getOptionConsultDao());
		}
		return optionConsultService;
	}
	
	public static OptionCooperateModeService getOptionCooperateModeService() {
		if (optionCooperateModeService == null) {
			optionCooperateModeService = new OptionCooperateModeService(DaoFactory.getOptionCooperateModeDao());
		}
		return optionCooperateModeService;
	}
	
	public static OptionCountryService getOptionCountryService() {
		if (optionCountryService == null) {
			optionCountryService = new OptionCountryService(DaoFactory.getOptionCountryDao());
		}
		return optionCountryService;
	}
	
	public static OptionGrbDomainService getOptionGrbDomainService() {
		if (optionGrbDomainService == null) {
			optionGrbDomainService = new OptionGrbDomainService(DaoFactory.getOptionGrbDomainDao());
		}
		return optionGrbDomainService;
	}

	public static OptionHadTecSrcService getOptionHadTecSrcService() {
		if (optionHadTecSrcService == null) {
			optionHadTecSrcService = new OptionHadTecSrcService(DaoFactory.getOptionHadTecSrcDao());
		}
		return optionHadTecSrcService;
	}
	
	public static OptionHrstService getOptionHrstService() {
		if (optionHrstService == null) {
			optionHrstService = new OptionHrstService(DaoFactory.getOptionHrstDao());
		}
		return optionHrstService;
	}

	public static OptionIndustryService getOptionIndustryService() {
		if (optionIndustryService == null) {
			optionIndustryService = new OptionIndustryService(DaoFactory.getOptionIndustryDao());
		}
		return optionIndustryService;
	}
	
	public static OptionDomainService getOptionDomainService() {
		if (optionDomainService == null) {
			optionDomainService = new OptionDomainService(DaoFactory.getOptionDomainDao());
		}
		return optionDomainService;
	}
	
	public static OptionOrganizationClassService getOptionOrganizationClassService() {
		if (optionOrganizationClassService == null) {
			optionOrganizationClassService = new OptionOrganizationClassService(DaoFactory.getOptionOrganizationClassDao());
		}
		return optionOrganizationClassService;
	}
		
	public static OptionOrganizationTypeService getOptionOrganizationTypeService() {
		if (optionOrganizationTypeService == null) {
			optionOrganizationTypeService = new OptionOrganizationTypeService(DaoFactory.getOptionOrganizationTypeDao());
		}
		return optionOrganizationTypeService;
	}
	
	public static OptionTrlService getOptionTrlService() {
		if (optionTrlService == null) {
			optionTrlService = new OptionTrlService(DaoFactory.getOptionTrlDao());
		}
		return optionTrlService;
	}	
	
	public static OptionSubjectService getOptionSubjectService() {
		if (optionSubjectService == null) {
			optionSubjectService = new OptionSubjectService(DaoFactory.getOptionSubjectDao());
		}
		return optionSubjectService;
	}
	
	public static OptionSchoolService getOptionSchoolService() {
		if (optionSchoolService == null) {
			optionSchoolService = new OptionSchoolService(DaoFactory.getOptionSchoolDao());
		}
		return optionSchoolService;
	}

	public static PatentService getPatentService() {
		if (patentService == null) {
			patentService = new PatentService(
					DaoFactory.getPatentDao(),
					DaoFactory.getTechFieldDao(),
					DaoFactory.getOptionCountryDao());
		}
		return patentService;
	}
	
	public static TechFieldService getTechFieldService() {
		if(techFieldService == null) {
			techFieldService = new TechFieldService(DaoFactory.getTechFieldDao());
		}		
		return techFieldService;
	}
	
	public static PatentExcelService getPatentExcelService() {
		if (patentExcelService == null) {
			patentExcelService = new PatentExcelService();
		}
		return patentExcelService;
	}

	public static ResearchPlanService getResearchPlanService() {
		if (researchPlanService == null) {
			researchPlanService = new ResearchPlanService(
					DaoFactory.getResearchPlanDao(),
					DaoFactory.getOptionGrbDomainDao(),
					DaoFactory.getOptionTrlDao());
		}
		
		return researchPlanService;
	}
	
	public static TechnologyService getTechnologyService() {
		if (technologyService == null) {
			technologyService = new TechnologyService(
					DaoFactory.getTechnologyDao(),
					DaoFactory.getOptionGrbDomainDao());
		}
		return technologyService;
	}
	
	public static ResearchPlanExcelService getResearchPlanExcelService() {
		if (researchPlanExcelService == null) {
			researchPlanExcelService = new ResearchPlanExcelService();
		}
		return researchPlanExcelService;
	}

	public static ConsultingService getConsultingService() {
		if (consultingService == null) {
			consultingService = new ConsultingService(
					DaoFactory.getConsultingDao(),
					DaoFactory.getConsultingManagerDao());
		}
		return consultingService;
	}
	
	public static EnterpriseInfoService getEnterpriseInfoService() {
		if (enterpriseInfoService == null) {
			enterpriseInfoService = new EnterpriseInfoService(
					DaoFactory.getEnterpriseInfoDao(),
					DaoFactory.getOptionDomainDao(),
					DaoFactory.getOptionCompanyLocationDao(),
					DaoFactory.getOptionHadTecSrcDao(),
					DaoFactory.getOptionCooperateModeDao());
		}
		return enterpriseInfoService;
	}
	

	public static EnterpriseRequireTechService getEnterpriseRequireTechService() {
		if (enterpriseRequireTechService == null) {
			enterpriseRequireTechService = new EnterpriseRequireTechService(DaoFactory.getEnterpriseRequireTechDao());
		}
		return enterpriseRequireTechService;
	}
	
	public static QnrCooperateWayService getQnrCooperateWayService(){
		if (qnrCooperateWayService == null) {
			qnrCooperateWayService = new QnrCooperateWayService(DaoFactory.getQnrCooperateWayDao());
		}
		return qnrCooperateWayService;
	}
	
	public static QnrCooperateWayExcelService getQnrCooperateWayExcelService() {
		if (qnrCooperateWayExcelService == null) {
			qnrCooperateWayExcelService = new QnrCooperateWayExcelService();
		}
		return qnrCooperateWayExcelService;
	}

	public static CoopExService getCoopExService() {
		if (coopExService == null) {
			coopExService = new CoopExService(
					DaoFactory.getCoopExDao(),
					DaoFactory.getCoopExImgDao(),
					DaoFactory.getCoopExVideoDao(),
					DaoFactory.getCoopExAttachFileDao());
		}
		return coopExService;
	}
	
	public static CoopExImgService getCoopExImgService() {
		if (coopExImgService == null) {
			coopExImgService = new CoopExImgService(DaoFactory.getCoopExImgDao());
		}
		return coopExImgService;
	}

	public static CoopExVideoService getCoopExVideoService() {
		if (coopExVideoService == null) {
			coopExVideoService = new CoopExVideoService(DaoFactory.getCoopExVideoDao());
		}
		return coopExVideoService;
	}

	public static CoopExAttachFileService getCoopExAttachFileService() {
		if (coopExAttachFileService == null) {
			coopExAttachFileService = new CoopExAttachFileService(DaoFactory.getCoopExAttachFileDao());
		}
		return coopExAttachFileService;
	}

	public static TalentedPeopleService getTalentedPeopleService() {
		if (talentedPeopleService == null) {
			talentedPeopleService = new TalentedPeopleService(
					DaoFactory.getTalentedPeopleDao(),
					DaoFactory.getOptionGrbDomainDao(),
					DaoFactory.getSysUserDao(),
					DaoFactory.getSysRoleDao());
		}
		return talentedPeopleService;
	}

	public static TalentedPeopleRdResultService getTalentedPeopleRdResultService() {
		if (talentedPeopleRdResultService == null) {
			talentedPeopleRdResultService = new TalentedPeopleRdResultService(
					DaoFactory.getTalentedPeopleRdResultDao(),
					DaoFactory.getOptionCountryDao());
		}
		return talentedPeopleRdResultService;
	}

	public static TalentedPeopleTransferCaseService getTalentedPeopleTransferCaseService() {
		if (talentedPeopleTransferCaseService == null) {
			talentedPeopleTransferCaseService = new TalentedPeopleTransferCaseService(DaoFactory.getTalentedPeopleTransferCaseDao());
		}
		return talentedPeopleTransferCaseService;
	}

	public static TalentedPeopleMainProjectService getTalentedPeopleMainProjectService() {
		if (talentedPeopleMainProjectService == null) {
			talentedPeopleMainProjectService = new TalentedPeopleMainProjectService(DaoFactory.getTalentedPeopleMainProjectDao());
		}
		return talentedPeopleMainProjectService;
	}

	public static TalentedPeoplePDPLService getTalentedPeoplePDPLService() {
		if (talentedPeoplePDPLService == null) {
			talentedPeoplePDPLService = new TalentedPeoplePDPLService(
					DaoFactory.getTalentedPeoplePDPLDao(),
					DaoFactory.getTalentedPeopleDao());
		}
		return talentedPeoplePDPLService;
	}
	
	public static IncubationCenterService getIncubationCenterService() {
		if (incubationCenterService == null) {
			incubationCenterService = new IncubationCenterService(DaoFactory.getIncubationCenterDao());
		}
		return incubationCenterService;
	}

	public static LiteratureService getLiteratureService() {
		if (literatureService == null) {
			literatureService = new LiteratureService(DaoFactory.getLiteratureDao());
		}
		return literatureService;
	}

	public static AboutService getAboutService() {
		if (aboutService == null) {
			aboutService = new AboutService(DaoFactory.getAboutDao());
		}
		return aboutService;
	}

	public static FaqService getFaqService() {
		if (faqService == null) {
			faqService = new FaqService(DaoFactory.getFaqDao());
		}
		return faqService;
	}

	public static NewsService getNewsService() {
		if (newsService == null) {
			newsService = new NewsService(DaoFactory.getNewsDao());
		}
		return newsService;
	}

	public static NewsAttachService getNewsAttachService() {
		if (newsAttachService == null) {
			newsAttachService = new NewsAttachService(DaoFactory.getNewsAttachDao());
		}
		return newsAttachService;
	}
	
	public static RdFocusService getRdFocusService() {
		if (rdFocusService == null) {
			rdFocusService = new RdFocusService(DaoFactory.getRdFocusDao());
		}
		return rdFocusService;
	}

	public static RdFocusAttachService getRdFocusAttachService() {
		if (rdFocusAttachService == null) {
			rdFocusAttachService = new RdFocusAttachService(DaoFactory.getRdFocusAttachDao());
		}
		return rdFocusAttachService;
	}

	public static ActivityService getActivityService() {
		if (activityService == null) {
			activityService = new ActivityService(
					DaoFactory.getActivityDao(),
					DaoFactory.getActivityVideoDao());
		}
		return activityService;
	}
	
	public static ActivityAttachService getActivityAttachService() {
		if (activityAttachService == null) {
			activityAttachService = new ActivityAttachService(DaoFactory.getActivityAttachDao());
		}
		return activityAttachService;
	}
	
	public static PopularActivityService getPopularActivityService() {
		if (popularActivityService == null) {
			popularActivityService = new PopularActivityService(
					DaoFactory.getPopularActivityDao(),
					DaoFactory.getActivityDao(),
					DaoFactory.getHttpRequestLogDao());
		}
		return popularActivityService;
	}

	public static MemberService getMemberService() {
		if (memberService == null) {
			memberService = new MemberService(
					DaoFactory.getMemberDao(), 
					DaoFactory.getOptionIndustryDao(), 
					DaoFactory.getOptionDomainDao());
		}
		return memberService;
	}

	public static ContactUsService getContactUsService() {
		if (contactUsService == null) {
			contactUsService = new ContactUsService(
					DaoFactory.getContactUsDao(),
					DaoFactory.getContactUsManagerDao());
		}
		return contactUsService;
	}

	public static IndustryInfoService getIndustryInfoService() {
		if (industryInfoService == null) {
			industryInfoService = new IndustryInfoService(DaoFactory.getIndustryInfoDao());
		}
		return industryInfoService;
	}

	public static RelatedWebsiteService getRelatedWebsiteService() {
		if (relatedWebsiteService == null) {
			relatedWebsiteService = new RelatedWebsiteService(DaoFactory.getRelatedWebsiteDao());
		}
		return relatedWebsiteService;
	}

	public static VideosAreaService getVideosAreaService() {
		if (videosAreaService == null) {
			videosAreaService = new VideosAreaService(DaoFactory.getVideosAreaDao());
		}
		return videosAreaService;
	}

	public static VideoService getVideoService() {
		if (videoService == null) {
			videoService = new VideoService(DaoFactory.getVideoDao());
		}
		return videoService;
	}

	public static EPaperService getEpaperService() {
		if (epaperService == null) {
			epaperService = new EPaperService(
					DaoFactory.getEpaperDao(), 
					DaoFactory.getePaperSubscriberDao(),
					DaoFactory.getMemberDao(),
					DaoFactory.getSysParameterDao());
		}
		return epaperService;
	}
	
	public static EPaperMailOpenLogService getEpaperMailOpenLogService() {
		if (epaperMailOpenLogService == null) {
			epaperMailOpenLogService = new EPaperMailOpenLogService(DaoFactory.getEpaperMailOpenLogDao());
		}
		return epaperMailOpenLogService;
	}

	public static EPaperSubscriberService getEpaperSubscriberService() {
		if (epaperSubscriberService == null) {
			epaperSubscriberService = new EPaperSubscriberService(DaoFactory.getePaperSubscriberDao());
		}
		return epaperSubscriberService;
	}

	public static MarqueeService getMarqueeService() {
		if (marqueeService == null) {
			marqueeService = new MarqueeService(DaoFactory.getMarqueeDao());
		}
		return marqueeService;
	}
	
	
}
