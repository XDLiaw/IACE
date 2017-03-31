package iace.action;

import org.apache.commons.lang3.StringUtils;

import core.util.PagedList;
import iace.entity.ePaper.EPaperSubscriber;
import iace.entity.ePaper.EPaperSubscriberSearchModel;
import iace.interceptor.SessionInterceptor;
import iace.service.ServiceFactory;
import iace.service.ePaper.EPaperSubscriberService;

public class EPaperSubscriberAction extends BaseIaceAction {
	private static final long serialVersionUID = -918979258359988603L;

	private EPaperSubscriberService epaperSubscriberService = ServiceFactory.getEpaperSubscriberService();
	
	private EPaperSubscriberSearchModel searchCondition = new EPaperSubscriberSearchModel();
	private PagedList<EPaperSubscriber> subscriberPagedList;
	
	private Long id;
	private EPaperSubscriber subscriber;
	
	private String captchaCode;
	
	public EPaperSubscriberAction() {
		super.setEntityClass(EPaperSubscriber.class);
		super.setTitle("電子報訂閱者");
	}

	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.subscriberPagedList = this.epaperSubscriberService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			this.subscriber = this.epaperSubscriberService.get(this.id);
			if (this.subscriber == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String subscribe() {
		return SUCCESS;
	}
	
	public void validateSubscribeSubmit() {
		validateBeforeSubmit();
		validateCaptcha();
	}
	
	public String subscribeSubmit() {
		try {
			EPaperSubscriber old = this.epaperSubscriberService.getByEmail(this.subscriber.getEmail());
			if (old == null) {
				if (this.subscriber.getIsSubscribe() == false) {
					super.addActionMessage("您沒有訂閱過電子報，無須退訂");
					return INPUT;
				}
				this.epaperSubscriberService.create(this.subscriber, super.getCurrentSysUser(), false, this.getSysLog());
			} else {
				old.setIsSubscribe(this.subscriber.getIsSubscribe());
				this.epaperSubscriberService.update(old, super.getCurrentSysUser(), false, this.getSysLog());
			}
			super.addActionMessage(this.subscriber.getIsSubscribe() ? "訂閱成功" : "退閱成功");
			
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
		validateBeforeSubmit();
	}
	
	public String updateSubmit() {
		try {
			this.epaperSubscriberService.update(this.subscriber, super.getCurrentSysUser(), false, super.getSysLog());
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.subscriber.getName(), 100, "subscriber.name");
		super.validateTextMaxLength(this.subscriber.getOrgName(), 200, "subscriber.orgName");
		super.validateTextMaxLength(this.subscriber.getTel(), 50, "subscriber.tel");
		if (super.validateNotBlank(this.subscriber.getEmail(), "subscriber.email")) {
			super.validateEmail(this.subscriber.getEmail(), "subscriber.email");
		}
	}
	
	public String delete() {
		return showDetail();
	}
	
	public String deleteSubmit() {
		try {
			this.epaperSubscriberService.delete(this.id, false, super.getSysLog());
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateCaptcha() {
		String correctCaptchaCode = (String)session.remove(SessionInterceptor.SESSION_KEY_CAPTCHA_CODE);
		if (StringUtils.equalsIgnoreCase(this.captchaCode, correctCaptchaCode) == false) {
			super.addFieldError("captchaCode", "驗證碼錯誤");
		}
	}
	
	//==========================================================================

	public EPaperSubscriberSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(EPaperSubscriberSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EPaperSubscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(EPaperSubscriber subscriber) {
		this.subscriber = subscriber;
	}

	public PagedList<EPaperSubscriber> getSubscriberPagedList() {
		return subscriberPagedList;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}
}
