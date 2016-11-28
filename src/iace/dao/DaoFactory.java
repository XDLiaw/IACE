package iace.dao;

import iace.dao.about.AboutDao;
import iace.dao.about.IAboutDao;
import iace.dao.activity.ActivityAttachDao;
import iace.dao.activity.ActivityDao;
import iace.dao.activity.ActivityVideoDao;
import iace.dao.activity.IActivityAttachDao;
import iace.dao.activity.IActivityDao;
import iace.dao.activity.IActivityVideoDao;
import iace.dao.consulting.ConsultingDao;
import iace.dao.consulting.IConsultingDao;
import iace.dao.coopExample.CoopExAttachFileDao;
import iace.dao.coopExample.CoopExDao;
import iace.dao.coopExample.CoopExImgDao;
import iace.dao.coopExample.CoopExVideoDao;
import iace.dao.coopExample.ICoopExAttachFileDao;
import iace.dao.coopExample.ICoopExDao;
import iace.dao.coopExample.ICoopExImgDao;
import iace.dao.coopExample.ICoopExVideoDao;
import iace.dao.customerService.ContactUsDao;
import iace.dao.customerService.IContactUsDao;
import iace.dao.enterpriseNeed.EnterpriseAcademiaCoopDao;
import iace.dao.enterpriseNeed.EnterpriseInfoDao;
import iace.dao.enterpriseNeed.EnterpriseRequireTechDao;
import iace.dao.enterpriseNeed.EnterpriseSituationDao;
import iace.dao.enterpriseNeed.IEnterpriseAcademiaCoopDao;
import iace.dao.enterpriseNeed.IEnterpriseInfoDao;
import iace.dao.enterpriseNeed.IEnterpriseRequireTechDao;
import iace.dao.enterpriseNeed.IEnterpriseSituationDao;
import iace.dao.faq.FaqDao;
import iace.dao.faq.IFaqDao;
import iace.dao.incubationCenter.IIncubationCenterDao;
import iace.dao.incubationCenter.IncubationCenterDao;
import iace.dao.literature.ILiteratureDao;
import iace.dao.literature.LiteratureDao;
import iace.dao.member.IMemberDao;
import iace.dao.member.MemberDao;
import iace.dao.news.INewsAttachDao;
import iace.dao.news.INewsDao;
import iace.dao.news.NewsAttachDao;
import iace.dao.news.NewsDao;
import iace.dao.option.IOptionDao;
import iace.dao.option.IOptionGrbDomainDao;
import iace.dao.option.IOptionSchoolDao;
import iace.dao.option.IOptionSubjectDao;
import iace.dao.option.OptionCompanyLocationDao;
import iace.dao.option.OptionConsultDao;
import iace.dao.option.OptionCooperateModeDao;
import iace.dao.option.OptionCountryDao;
import iace.dao.option.OptionDomainDao;
import iace.dao.option.OptionGrbDomainDao;
import iace.dao.option.OptionHadTecSrcDao;
import iace.dao.option.OptionHrstDao;
import iace.dao.option.OptionIndustryDao;
import iace.dao.option.OptionOrganizationClassDao;
import iace.dao.option.OptionOrganizationTypeDao;
import iace.dao.option.OptionSchoolDao;
import iace.dao.option.OptionSubjectDao;
import iace.dao.option.OptionSysActionDao;
import iace.dao.option.OptionSysNamespaceDao;
import iace.dao.option.OptionTrlDao;
import iace.dao.patent.IPatentDao;
import iace.dao.patent.PatentDao;
import iace.dao.qnrCooperateWay.IQnrCooperateWayDao;
import iace.dao.qnrCooperateWay.QnrCooperateWayDao;
import iace.dao.questionnaire.IQnrDao;
import iace.dao.questionnaire.IQnrTemplateDao;
import iace.dao.questionnaire.QnrDao;
import iace.dao.questionnaire.QnrTemplateDao;
import iace.dao.researchPlan.IResearchPlanDao;
import iace.dao.researchPlan.ITechnologyDao;
import iace.dao.researchPlan.ResearchPlanDao;
import iace.dao.researchPlan.TechnologyDao;
import iace.dao.sys.ISysLogDao;
import iace.dao.sys.ISysRoleDao;
import iace.dao.sys.ISysUserDao;
import iace.dao.sys.SysLogDao;
import iace.dao.sys.SysRoleDao;
import iace.dao.sys.SysUserDao;
import iace.dao.talentedPeople.ITalentedPeopleDao;
import iace.dao.talentedPeople.ITalentedPeopleMainProjectDao;
import iace.dao.talentedPeople.ITalentedPeopleRdResultDao;
import iace.dao.talentedPeople.ITalentedPeopleTransferCaseDao;
import iace.dao.talentedPeople.TalentedPeopleDao;
import iace.dao.talentedPeople.TalentedPeopleMainProjectDao;
import iace.dao.talentedPeople.TalentedPeopleRdResultDao;
import iace.dao.talentedPeople.TalentedPeopleTransferCaseDao;
import iace.dao.techField.ITechFieldDao;
import iace.dao.techField.TechFieldDao;
import iace.entity.option.OptionCompanyLocation;
import iace.entity.option.OptionConsult;
import iace.entity.option.OptionCooperateMode;
import iace.entity.option.OptionCountry;
import iace.entity.option.OptionDomain;
import iace.entity.option.OptionHadTecSrc;
import iace.entity.option.OptionHrst;
import iace.entity.option.OptionIndustry;
import iace.entity.option.OptionOrganizationClass;
import iace.entity.option.OptionOrganizationType;
import iace.entity.option.OptionSysAction;
import iace.entity.option.OptionSysNamespace;
import iace.entity.option.OptionTrl;

public class DaoFactory {
	
	private static ISysRoleDao sysRoleDao;
	private static ISysUserDao sysUserDao;
	private static ISysLogDao sysLogDao;
	
	private static IOptionDao<OptionCompanyLocation> optionCompanyLocationDao;
	private static IOptionDao<OptionConsult> optionConsultDao;
	private static IOptionDao<OptionCooperateMode> optionCooperateModeDao;
	private static IOptionDao<OptionCountry> optionCountryDao;
	private static IOptionGrbDomainDao optionGrbDomainDao;
	private static IOptionDao<OptionHadTecSrc> optionHadTecSrcDao;
	private static IOptionDao<OptionHrst> optionHrstDao;
	private static IOptionDao<OptionIndustry> optionIndustryDao;
	private static IOptionDao<OptionDomain> optionDomainDao;
	private static IOptionDao<OptionOrganizationClass> optionOrganizationClassDao;
	private static IOptionDao<OptionOrganizationType> optionOrganizationTypeDao;
	private static IOptionDao<OptionTrl> optionTrlDao;
	private static IOptionSubjectDao optionSubjectDao;
	private static IOptionSchoolDao optionSchoolDao;
	private static IOptionDao<OptionSysAction> optionSysActionDao;
	private static IOptionDao<OptionSysNamespace> optionSysNamespaceDao;
	
	private static ITechFieldDao techFieldDao;
	private static IPatentDao patentDao;
	
	private static IResearchPlanDao researchPlanDao;
	private static ITechnologyDao technologyDao;
	
	private static IConsultingDao consultingDao;
	
	private static IEnterpriseInfoDao enterpriseInfoDao;
	private static IEnterpriseRequireTechDao enterpriseRequireTechDao;
	private static IEnterpriseSituationDao enterpriseSituationDao;
	private static IEnterpriseAcademiaCoopDao enterpriseAcademiaCoopDao;
	
	private static IQnrTemplateDao qnrTemplateDao;
	private static IQnrDao qnrDao;
	
	private static IQnrCooperateWayDao qnrCooperateWayDao;
	
	private static ICoopExDao coopExDao;
	private static ICoopExImgDao coopExImgDao;
	private static ICoopExVideoDao coopExVideoDao;
	private static ICoopExAttachFileDao coopExAttachFileDao;
	
	private static ITalentedPeopleDao talentedPeopleDao;
	private static ITalentedPeopleRdResultDao talentedPeopleRdResultDao;
	private static ITalentedPeopleTransferCaseDao talentedPeopleTransferCaseDao;
	private static ITalentedPeopleMainProjectDao talentedPeopleMainProjectDao;
	
	private static IIncubationCenterDao incubationCenterDao;
	
	private static ILiteratureDao literatureDao;
	
	private static IAboutDao aboutDao;
	
	private static IFaqDao faqDao;
	
	private static INewsDao newsDao;
	private static INewsAttachDao newsAttachDao;
	
	private static IActivityDao activityDao;
	private static IActivityAttachDao activityAttachDao;
	private static IActivityVideoDao activityVideoDao;
	
	private static IMemberDao memberDao;
	
	private static IContactUsDao contactUsDao;
	
	public static ISysRoleDao getSysRoleDao() {
		if (sysRoleDao == null) {
			sysRoleDao = new SysRoleDao();
		}
		return sysRoleDao;
	}
	
	public static ISysUserDao getSysUserDao() {
		if (sysUserDao == null) {
			sysUserDao = new SysUserDao();
		}
		return sysUserDao;
	}
	
	
	public static ISysLogDao getSysLogDao() {
		if (sysLogDao == null) {
			sysLogDao = new SysLogDao();
		}
		return sysLogDao;
	}

	public static IOptionDao<OptionCompanyLocation> getOptionCompanyLocationDao() {
		if (optionCompanyLocationDao == null) {
			optionCompanyLocationDao = new OptionCompanyLocationDao();
		}
		return optionCompanyLocationDao;
	}
	
	public static IOptionDao<OptionConsult> getOptionConsultDao() {
		if (optionConsultDao == null) {
			optionConsultDao = new OptionConsultDao();
		}
		return optionConsultDao;
	}
	
	public static IOptionDao<OptionCooperateMode> getOptionCooperateModeDao() {
		if (optionCooperateModeDao == null) {
			optionCooperateModeDao = new OptionCooperateModeDao();
		}
		return optionCooperateModeDao;
	}
	
	public static IOptionDao<OptionCountry> getOptionCountryDao() {
		if (optionCountryDao == null) {
			optionCountryDao = new OptionCountryDao();
		}
		return optionCountryDao;
	}
	
	public static IOptionGrbDomainDao getOptionGrbDomainDao() {
		if (optionGrbDomainDao == null) {
			optionGrbDomainDao = new OptionGrbDomainDao();
		}
		return optionGrbDomainDao;
	}
	
	public static IOptionDao<OptionHadTecSrc> getOptionHadTecSrcDao() {
		if (optionHadTecSrcDao == null) {
			optionHadTecSrcDao = new OptionHadTecSrcDao();
		}
		return optionHadTecSrcDao;
	}
	
	public static IOptionDao<OptionHrst> getOptionHrstDao() {
		if (optionHrstDao == null) {
			optionHrstDao = new OptionHrstDao();
		}
		return optionHrstDao;
	}

	public static IOptionDao<OptionIndustry> getOptionIndustryDao() {
		if (optionIndustryDao == null) {
			optionIndustryDao = new OptionIndustryDao();
		}
		return optionIndustryDao;
	}
	
	public static IOptionDao<OptionDomain> getOptionDomainDao() {
		if (optionDomainDao == null) {
			optionDomainDao = new OptionDomainDao();
		}
		return optionDomainDao;
	}

	public static IOptionDao<OptionOrganizationClass> getOptionOrganizationClassDao() {
		if (optionOrganizationClassDao == null) {
			optionOrganizationClassDao = new OptionOrganizationClassDao();
		}
		return optionOrganizationClassDao;
	}

	public static IOptionDao<OptionOrganizationType> getOptionOrganizationTypeDao() {
		if (optionOrganizationTypeDao == null) {
			optionOrganizationTypeDao = new OptionOrganizationTypeDao();
		}
		return optionOrganizationTypeDao;
	}

	public static IOptionDao<OptionTrl> getOptionTrlDao() {
		if (optionTrlDao == null) {
			optionTrlDao = new OptionTrlDao();
		}
		return optionTrlDao;
	}
	
	public static IOptionSubjectDao getOptionSubjectDao() {
		if (optionSubjectDao == null) {
			optionSubjectDao = new OptionSubjectDao();
		}
		return optionSubjectDao;
	}
	
	public static IOptionSchoolDao getOptionSchoolDao() {
		if (optionSchoolDao == null) {
			optionSchoolDao = new OptionSchoolDao();
		}
		return optionSchoolDao;
	}
	
	public static IOptionDao<OptionSysAction> getOptionSysActionDao() {
		if (optionSysActionDao == null) {
			optionSysActionDao = new OptionSysActionDao();
		}
		return optionSysActionDao;
	}

	public static IOptionDao<OptionSysNamespace> getOptionSysNamespaceDao() {
		if (optionSysNamespaceDao == null) {
			optionSysNamespaceDao = new OptionSysNamespaceDao();
		}
		return optionSysNamespaceDao;
	}

	public static ITechFieldDao getTechFieldDao() {
		if (techFieldDao == null) {
			techFieldDao = new TechFieldDao();
		}		
		return techFieldDao;
	}
	
	public static IPatentDao getPatentDao() {
		if (patentDao == null) {
			patentDao = new PatentDao();
		}
		return patentDao;
	}

	public static IResearchPlanDao getResearchPlanDao() {
		if (researchPlanDao == null) {
			researchPlanDao = new ResearchPlanDao();
		}		
		return researchPlanDao;
	}
	
	public static ITechnologyDao getTechnologyDao() {
		if (technologyDao == null) {
			technologyDao = new TechnologyDao();
		}
		return technologyDao;
	}

	public static IConsultingDao getConsultingDao() {
		if (consultingDao == null) {
			consultingDao = new ConsultingDao();
		}
		return consultingDao;
	}
	
	public static IEnterpriseInfoDao getEnterpriseInfoDao() {
		if (enterpriseInfoDao == null) {
			enterpriseInfoDao = new EnterpriseInfoDao();
		}
		return enterpriseInfoDao;
	}
	
	public static IEnterpriseRequireTechDao getEnterpriseRequireTechDao() {
		if (enterpriseRequireTechDao == null) {
			enterpriseRequireTechDao = new EnterpriseRequireTechDao(); 
		}
		return enterpriseRequireTechDao;
	}
	
	public static IEnterpriseSituationDao getEnterpriseSituationDao() {
		if (enterpriseSituationDao == null) {
			enterpriseSituationDao = new EnterpriseSituationDao();
		}
		return enterpriseSituationDao;
	}
	
	public static IEnterpriseAcademiaCoopDao getEnterpriseAcademiaCoopDao() {
		if (enterpriseAcademiaCoopDao == null) {
			enterpriseAcademiaCoopDao = new EnterpriseAcademiaCoopDao();
		}
		return enterpriseAcademiaCoopDao;
	}

	public static IQnrTemplateDao getQnrTemplateDao() {
		if (qnrTemplateDao == null) {
			qnrTemplateDao = new QnrTemplateDao();
		}
		return qnrTemplateDao;
	}

	public static IQnrDao getQnrDao() {
		if (qnrDao == null) {
			qnrDao = new QnrDao();
		}
		return qnrDao;
	}
	
	public static IQnrCooperateWayDao getQnrCooperateWayDao() {
		if (qnrCooperateWayDao == null) {
			qnrCooperateWayDao = new QnrCooperateWayDao();
		}
		return qnrCooperateWayDao;
	}

	public static ICoopExDao getCoopExDao() {
		if (coopExDao == null) {
			coopExDao = new CoopExDao();
		}
		return coopExDao;
	}
	
	public static ICoopExImgDao getCoopExImgDao() {
		if (coopExImgDao == null) {
			coopExImgDao = new CoopExImgDao();
		}
		return coopExImgDao;
	}

	public static ICoopExVideoDao getCoopExVideoDao() {
		if (coopExVideoDao == null) {
			coopExVideoDao = new CoopExVideoDao();
		}
		return coopExVideoDao;
	}

	public static ICoopExAttachFileDao getCoopExAttachFileDao() {
		if (coopExAttachFileDao == null) {
			coopExAttachFileDao = new CoopExAttachFileDao();
		}
		return coopExAttachFileDao;
	}

	public static ITalentedPeopleDao getTalentedPeopleDao() {
		if (talentedPeopleDao == null) {
			talentedPeopleDao = new TalentedPeopleDao();
		}
		return talentedPeopleDao;
	}

	public static ITalentedPeopleRdResultDao getTalentedPeopleRdResultDao() {
		if (talentedPeopleRdResultDao == null) {
			talentedPeopleRdResultDao = new TalentedPeopleRdResultDao();
		}
		return talentedPeopleRdResultDao;
	}

	public static ITalentedPeopleTransferCaseDao getTalentedPeopleTransferCaseDao() {
		if (talentedPeopleTransferCaseDao == null) {
			talentedPeopleTransferCaseDao = new TalentedPeopleTransferCaseDao();
		}
		return talentedPeopleTransferCaseDao;
	}

	public static ITalentedPeopleMainProjectDao getTalentedPeopleMainProjectDao() {
		if (talentedPeopleMainProjectDao == null) {
			talentedPeopleMainProjectDao = new TalentedPeopleMainProjectDao();
		}
		return talentedPeopleMainProjectDao;
	}

	public static IIncubationCenterDao getIncubationCenterDao() {
		if (incubationCenterDao == null) {
			incubationCenterDao = new IncubationCenterDao();
		}
		return incubationCenterDao;
	}

	public static ILiteratureDao getLiteratureDao() {
		if (literatureDao == null) {
			literatureDao = new LiteratureDao();
		}
		return literatureDao;
	}

	public static IAboutDao getAboutDao() {
		if (aboutDao == null) {
			aboutDao = new AboutDao();
		}
		return aboutDao;
	}

	public static IFaqDao getFaqDao() {
		if (faqDao == null) {
			faqDao = new FaqDao();
		}
		return faqDao;
	}

	public static INewsDao getNewsDao() {
		if (newsDao == null) {
			newsDao = new NewsDao();
		}
		return newsDao;
	}

	public static INewsAttachDao getNewsAttachDao() {
		if (newsAttachDao == null) {
			newsAttachDao = new NewsAttachDao();
		}
		return newsAttachDao;
	}

	public static IActivityDao getActivityDao() {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao;
	}

	public static IActivityAttachDao getActivityAttachDao() {
		if (activityAttachDao == null) {
			activityAttachDao = new ActivityAttachDao();
		}
		return activityAttachDao;
	}

	public static IActivityVideoDao getActivityVideoDao() {
		if (activityVideoDao == null) {
			activityVideoDao = new ActivityVideoDao();
		}
		return activityVideoDao;
	}

	public static IMemberDao getMemberDao() {
		if (memberDao == null) {
			memberDao = new MemberDao();
		}
		return memberDao;
	}

	public static IContactUsDao getContactUsDao() {
		if (contactUsDao == null) {
			contactUsDao = new ContactUsDao();
		}
		return contactUsDao;
	}
	
	
}
