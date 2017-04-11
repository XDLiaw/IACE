package iace.entity.sysAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;

import iace.entity.sysAuth.sysApplication.SysApp;
import iace.entity.sysAuth.sysApplication.SysAppAbout;
import iace.entity.sysAuth.sysApplication.SysAppActivity;
import iace.entity.sysAuth.sysApplication.SysAppBatchSendEmail;
import iace.entity.sysAuth.sysApplication.SysAppConsulting;
import iace.entity.sysAuth.sysApplication.SysAppContactUs;
import iace.entity.sysAuth.sysApplication.SysAppCoopEx;
import iace.entity.sysAuth.sysApplication.SysAppEPaper;
import iace.entity.sysAuth.sysApplication.SysAppEPaperSubscriber;
import iace.entity.sysAuth.sysApplication.SysAppEnterpriseNeed;
import iace.entity.sysAuth.sysApplication.SysAppFaq;
import iace.entity.sysAuth.sysApplication.SysAppFile;
import iace.entity.sysAuth.sysApplication.SysAppIncubationCenter;
import iace.entity.sysAuth.sysApplication.SysAppLiterature;
import iace.entity.sysAuth.sysApplication.SysAppMarquee;
import iace.entity.sysAuth.sysApplication.SysAppMember;
import iace.entity.sysAuth.sysApplication.SysAppNews;
import iace.entity.sysAuth.sysApplication.SysAppOptionManage;
import iace.entity.sysAuth.sysApplication.SysAppPatent;
import iace.entity.sysAuth.sysApplication.SysAppPopularActivity;
import iace.entity.sysAuth.sysApplication.SysAppQnrCoopWay;
import iace.entity.sysAuth.sysApplication.SysAppRdFocus;
import iace.entity.sysAuth.sysApplication.SysAppResearchPlan;
import iace.entity.sysAuth.sysApplication.SysAppSysLog;
import iace.entity.sysAuth.sysApplication.SysAppSysRole;
import iace.entity.sysAuth.sysApplication.SysAppSysUser;
import iace.entity.sysAuth.sysApplication.SysAppTalentedPeople;
import iace.entity.sysAuth.sysApplication.SysAppVideosArea;

public class SysAuth {
	private transient boolean enableAuth = true;
	
	private SysAppOptionManage optionManage = new SysAppOptionManage();
	
	private SysAppQnrCoopWay qnrCoopWay = new SysAppQnrCoopWay();
	private SysAppResearchPlan researchPlan = new SysAppResearchPlan();
	private SysAppPatent patent = new SysAppPatent();
	private SysAppConsulting consulting = new SysAppConsulting();
	private SysAppEnterpriseNeed enterpriseNeed = new SysAppEnterpriseNeed();
	private SysAppCoopEx coopEx = new SysAppCoopEx();
	private SysAppTalentedPeople talentedPeople = new SysAppTalentedPeople();
	private SysAppIncubationCenter incubationCenter = new SysAppIncubationCenter();
	private SysAppLiterature literature = new SysAppLiterature();
	
	private SysAppSysLog sysLog = new SysAppSysLog();
	private SysAppSysUser sysUser = new SysAppSysUser();
	private SysAppSysRole sysRole = new SysAppSysRole();
	
	private SysAppAbout about = new SysAppAbout();
	private SysAppFaq faq = new SysAppFaq();
	private SysAppNews news = new SysAppNews();
	private SysAppRdFocus rdFocus = new SysAppRdFocus();
	private SysAppActivity activity = new SysAppActivity();
	
	private SysAppMember member = new SysAppMember();
	
	private SysAppContactUs contactUs =  new SysAppContactUs();
	
	private SysAppBatchSendEmail batchSendEmail = new SysAppBatchSendEmail();
	
	private SysAppVideosArea videosArea = new SysAppVideosArea();
	
	private SysAppFile file = new SysAppFile();
	
	private SysAppEPaper epaper = new SysAppEPaper();
	private SysAppEPaperSubscriber epaperSubscriber = new SysAppEPaperSubscriber();
	
	private SysAppMarquee marquee = new SysAppMarquee();
	
	private SysAppPopularActivity popularActivity= new SysAppPopularActivity();
	
	public SysAuth() {
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("configs/iace.properties"));
			String enableAuthStr = prop.getProperty("enableAuth");
			this.enableAuth = Boolean.valueOf(enableAuthStr);
		} catch (IOException e) {
			System.err.println("can't read value from config file, set [enableAuth] to true");
			this.enableAuth = true;
		}
	}
	
	private List<SysApp> getSysAppList() {
		List<SysApp> sysApps = new ArrayList<SysApp>();
		sysApps.add(this.optionManage);
		sysApps.add(this.qnrCoopWay);
		sysApps.add(this.researchPlan);
		sysApps.add(this.patent);
		sysApps.add(this.consulting);
		sysApps.add(this.enterpriseNeed);
		sysApps.add(this.coopEx);
		sysApps.add(this.talentedPeople);
		sysApps.add(this.incubationCenter);
		sysApps.add(this.literature);
		sysApps.add(this.sysLog);
		sysApps.add(this.sysUser);
		sysApps.add(this.sysRole);
		sysApps.add(this.about);
		sysApps.add(this.faq);
		sysApps.add(this.news);
		sysApps.add(this.rdFocus);
		sysApps.add(this.activity);
		sysApps.add(this.member);
		sysApps.add(this.contactUs);
		sysApps.add(this.batchSendEmail);
		sysApps.add(this.videosArea);
		sysApps.add(this.file);
		sysApps.add(this.epaper);
		sysApps.add(this.epaperSubscriber);
		sysApps.add(this.marquee);
		sysApps.add(this.popularActivity);
		return sysApps;
	}

	public String toJsonString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static SysAuth fromJsonString(String jsonString) {
		Gson gson = new Gson();
		SysAuth auth = gson.fromJson(jsonString, SysAuth.class);
		return auth;
	}
	
	public boolean hasEnableNamespace(String namespace) {
		if (this.enableAuth) {
			for (SysApp sysApp : getSysAppList()) {
				if (sysApp.hasEnableNamespace(namespace)) 
					return true;
			}
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasEnableNamespaceStartWith(String namespace) {
		if (this.enableAuth) {
			for (SysApp sysApp : getSysAppList()) {
				if (sysApp.hasEnableNamespaceStartWith(namespace)) 
					return true;
			}
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasAuth(String namespace, String action) {
		if (this.enableAuth) {
			for (SysApp sysApp : getSysAppList()) {
				if (sysApp.hasAuth(namespace, action)) return true;
			}
			return false;
		} else {
			return true;
		}
	}
	
	//==========================================================================

	public SysAppOptionManage getOptionManage() {
		return optionManage;
	}

	public SysAppQnrCoopWay getQnrCoopWay() {
		return qnrCoopWay;
	}

	public SysAppResearchPlan getResearchPlan() {
		return researchPlan;
	}

	public SysAppPatent getPatent() {
		return patent;
	}

	public SysAppConsulting getConsulting() {
		return consulting;
	}

	public SysAppEnterpriseNeed getEnterpriseNeed() {
		return enterpriseNeed;
	}

	public SysAppCoopEx getCoopEx() {
		return coopEx;
	}

	public SysAppTalentedPeople getTalentedPeople() {
		return talentedPeople;
	}

	public SysAppIncubationCenter getIncubationCenter() {
		return incubationCenter;
	}

	public SysAppLiterature getLiterature() {
		return literature;
	}

	public SysAppSysLog getSysLog() {
		return sysLog;
	}

	public SysAppSysUser getSysUser() {
		return sysUser;
	}

	public SysAppSysRole getSysRole() {
		return sysRole;
	}

	public boolean isEnableAuth() {
		return enableAuth;
	}

	public SysAppAbout getAbout() {
		return about;
	}

	public SysAppFaq getFaq() {
		return faq;
	}

	public SysAppNews getNews() {
		return news;
	}
	
	public SysAppRdFocus getRdFocus() {
		return rdFocus;
	}

	public SysAppActivity getActivity() {
		return activity;
	}

	public SysAppPopularActivity getPopularActivity() {
		return popularActivity;
	}

	public SysAppMember getMember() {
		return member;
	}

	public SysAppContactUs getContactUs() {
		return contactUs;
	}

	public SysAppBatchSendEmail getBatchSendEmail() {
		return batchSendEmail;
	}

	public SysAppVideosArea getVideosArea() {
		return videosArea;
	}

	public SysAppFile getFile() {
		return file;
	}
	
	public SysAppEPaper getEpaper() {
		return epaper;
	}

	public SysAppEPaperSubscriber getEpaperSubscriber() {
		return epaperSubscriber;
	}

	public SysAppMarquee getMarquee() {
		return marquee;
	}
	
	
}
