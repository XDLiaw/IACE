package iace.action;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;

import core.util.PagedList;
import iace.entity.member.Member;
import iace.entity.member.MemberSearchModel;
import iace.entity.option.BaseOption;
import iace.entity.option.OptionDomain;
import iace.entity.option.OptionIndustry;
import iace.interceptor.SessionInterceptor;
import iace.service.ServiceFactory;
import iace.service.member.MemberService;

public class MemberAction extends BaseIaceAction {
	private static final long serialVersionUID = 1548716018154460586L;

	private MemberService memberService = ServiceFactory.getMemberService();

	private MemberSearchModel searchCondition = new MemberSearchModel();
	private PagedList<Member> memberPagedList;

	private Long id;
	private Member member;

	private List<BaseOption> industryList;
	private List<OptionIndustry> optIndustryList;
	private List<BaseOption> jobTypeList;
	private List<OptionDomain> optDomainList;
	
	private String captchaCode;

	public MemberAction() {
		super.setTitle("會員");
	}

	public String init() {
		return SUCCESS;
	}

	public String index() {
		try {
			this.memberPagedList = this.memberService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String showDetail() {
		try {
			this.member = this.memberService.get(this.id);
			if (this.member == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String create() {
		return SUCCESS;
	}

	public void validateCreateSubmit() {
		if (super.validateNotBlankNLength(this.member.getAccount(), 50, "member.account")) {
			if (this.memberService.isAccountExist(this.member.getAccount())) {
				super.addFieldError("member.account", "帳號已存在");
			}
		}
		validateBeforeSubmit();
	}

	public String createSubmit() {
		try {
			this.memberService.create(this.member);
			super.addActionMessage("CREATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String register() {
		return SUCCESS;
	}
	
	public void validateRegisterSubmit() {
		if (super.validateNotBlankNLength(this.member.getAccount(), 50, "member.account")) {
			if (this.memberService.isAccountExist(this.member.getAccount())) {
				super.addFieldError("member.account", "帳號已存在");
			}
		}
		validateBeforeSubmit();
		validateCaptcha();
	}
	
	public String registerSubmit() {
		try {
			this.memberService.create(this.member);
			super.session.put(SessionInterceptor.SESSION_KEY_MEMBER, this.member);
			super.addActionMessage("註冊成功");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String update() {
		try {
			this.member = this.memberService.get(this.id);
			if (this.member == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public void validateUpdateSubmit() {
		validateBeforeSubmit();
	}

	public String updateSubmit() {
		try {
			this.memberService.update(this.member, super.getCurrentSysUser(), false, super.getSysLog());
			super.addActionMessage("UPDATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String selfUpdate() {
		try {
			this.member = (Member) super.session.get(SessionInterceptor.SESSION_KEY_MEMBER);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateSelfUpdateSubmit() {
		Member member = (Member) super.session.get(SessionInterceptor.SESSION_KEY_MEMBER);
		if (this.member.getId() != member.getId()) {
			super.addFieldError("member.id", "無法修他人的資料");
		}
		validateBeforeSubmit();
	}
	
	public String selfUpdateSubmit() {
		try {
			this.memberService.update(this.member, super.getCurrentSysUser(), false, super.getSysLog());
			super.session.put(SessionInterceptor.SESSION_KEY_MEMBER, this.member);
			super.addActionMessage("資料更新成功");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String delete() {
		try {
			this.member = this.memberService.get(this.id);
			if (this.member == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String deleteSubmit() {
		try {
			this.memberService.delete(this.id, false, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String memberCenter() {
		try {
			if (super.session.get(SessionInterceptor.SESSION_KEY_MEMBER) != null) {
				this.member = (Member) super.session.get(SessionInterceptor.SESSION_KEY_MEMBER);
				return SUCCESS;
			} else {
				return LOGIN;
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String loginSubmit() {
		try {
			Member member = this.memberService.getBy(this.member.getAccount(), this.member.getPassword());
			if (member == null) {
				super.addActionError("帳號或密碼錯誤!");
				return INPUT;
			} else {
				this.member = member;
				super.session.put(SessionInterceptor.SESSION_KEY_MEMBER, this.member);

				return SUCCESS;
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String logout() {
		try {
			this.session.clear();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateForgetPasswordSubmit() {
		try {
			this.member = this.memberService.getByAccount(this.member.getAccount());
			if (this.member == null) {
				super.addFieldError("member.account", "帳號不存在!");
			} else if (member.getName().equals(this.member.getName()) == false) {
				super.addFieldError("member.name", "姓名錯誤!");
			}
		} catch (Exception e) {
			log.error("", e);
		}
		
		validateCaptcha();
	}

	public String forgetPasswordSubmit() {
		try {
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader().getResourceAsStream("configs/mail.smtp.properties"));
			javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null); // 取得與SMTP

			MimeMessage msg = new MimeMessage(session); // 取得一Mime的Message
			msg.setHeader("Content-Type", "text/plain; charset=UTF-8");
			// set FROM
			msg.setFrom(new InternetAddress(props.getProperty("mail.default.from"), props.getProperty("mail.default.sender"), "UTF-8"));
			// set TO
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(this.member.getEmail()));
			// Set Subject: header field
			msg.setSubject("IACE會員密碼");
			// Now set the actual message
			msg.setText("您的密碼為 [" + member.getPassword() + "]");

			Transport.send(msg);
			super.addActionMessage("信件已發送，請至您的信箱收取補發密碼");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateBeforeSubmit() {
		super.validateTextLength(this.member.getPassword(), 6, 20, "member.password");
		if (super.validateNotBlank(this.member.getEmail(), "member.email")) {
			super.validateEmail(this.member.getEmail(), "member.email");
		}
		super.validateNotBlankNLength(this.member.getName(), 50, "member.name");
		super.validateNotBlankNLength(this.member.getOrg(), 50, "member.org");
		super.validateNotBlank(this.member.getIndustry(), "member.industry", "請選擇");
		super.validateNotBlank(this.member.getJobType(), "member.jobType", "請選擇");
		super.validateNotBlankNLength(this.member.getAddress(), 500, "member.address");
		super.validateNotBlankNLength(this.member.getTel(), 30, "member.tel");
	}
	
	public void validateCaptcha() {
		String correctCaptchaCode = (String)session.remove(SessionInterceptor.SESSION_KEY_CAPTCHA_CODE);
		if (StringUtils.equalsIgnoreCase(this.captchaCode, correctCaptchaCode) == false) {
			super.addFieldError("captchaCode", "驗證碼錯誤");
		}
	}
	

	// =========================================================================

	public MemberSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(MemberSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public PagedList<Member> getMemberPagedList() {
		return memberPagedList;
	}

	public List<BaseOption> getIndustryList() {
		if (industryList == null) {
			industryList = Member.getIndustryList();
		}
		return industryList;
	}

	public List<OptionIndustry> getOptIndustryList() {
		if (optIndustryList == null) {
			optIndustryList = ServiceFactory.getOptionIndustryService().listAll();
		}
		return optIndustryList;
	}

	public List<BaseOption> getJobTypeList() {
		if (jobTypeList == null) {
			jobTypeList = Member.getJobTypeList();
		}
		return jobTypeList;
	}

	public List<OptionDomain> getOptDomainList() {
		if (optDomainList == null) {
			optDomainList = ServiceFactory.getOptionDomainService().listAll();
		}
		return optDomainList;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}

	
}
