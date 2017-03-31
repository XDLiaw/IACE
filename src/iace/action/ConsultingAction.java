package iace.action;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import core.util.ExcelUtil;
import core.util.PagedList;
import iace.entity.consulting.Consulting;
import iace.entity.consulting.ConsultingSearchModel;
import iace.entity.option.OptionConsult;
import iace.entity.option.OptionIndustry;
import iace.entity.option.OptionOrganizationType;
import iace.interceptor.SessionInterceptor;
import iace.service.ServiceFactory;
import iace.service.consulting.ConsultingService;
import iace.service.option.OptionConsultService;
import iace.service.option.OptionIndustryService;
import iace.service.option.OptionOrganizationTypeService;

public class ConsultingAction extends BaseIaceAction {

	private static final long serialVersionUID = 7983348708278484405L;
	
	private ConsultingService consultingService = ServiceFactory.getConsultingService();
	private OptionOrganizationTypeService optionOrgTypeService = ServiceFactory.getOptionOrganizationTypeService();
	private OptionConsultService optionConsultService = ServiceFactory.getOptionConsultService();
	private OptionIndustryService optionIndustryService = ServiceFactory.getOptionIndustryService();
	
	private ConsultingSearchModel searchCondition = new ConsultingSearchModel();	
	private PagedList<Consulting> consultingPagedList;
	
	private List<OptionOrganizationType> optionOrganizationTypeList;
	private List<OptionConsult> optionConsultList;
	private List<OptionIndustry> optionIndustryList;
	
	private Long id;
	private Consulting consulting;
	private boolean beenHandled;
	
	private String downloadFileName;
	private InputStream downloadFileInputStream;
	
	private String reportFileName;
	private InputStream reportInputStream;
	
	private String captchaCode;

	public ConsultingAction() {
		super.setTitle("諮詢服務表");
	}
	
	public String init() {
		return SUCCESS;
	}
	
	public String index() {
		try {
			this.consultingPagedList = this.consultingService.searchBy(searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String showDetail() {
		try {
			this.consulting = this.consultingService.get(this.id);
			if (this.consulting == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;			
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}	
	}
	
	public String create() {
		return SUCCESS;
	}
	
	public void validateCreateSubmit() {
		validateBeforeSubmit();
		validateCaptcha();
	}
	
	public void validateCaptcha() {
		String correctCaptchaCode = (String)session.remove(SessionInterceptor.SESSION_KEY_CAPTCHA_CODE);
		if (correctCaptchaCode != null) { //後台不需要驗證碼所以會是null，此時就不做檢查
			if (StringUtils.equalsIgnoreCase(this.captchaCode, correctCaptchaCode) == false) {
				super.addFieldError("captchaCode", "驗證碼錯誤");
			}
		}
	}
	
	public String createSubmit() {
		try {
			this.consultingService.create(this.consulting, super.getCurrentSysUser(), false, super.getSysLog());
			this.consultingService.sendNotificationEmail(this.consulting);
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String update() {
		try {
			this.consulting = this.consultingService.get(this.id);
			if(this.consulting == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			
			return SUCCESS;
		} catch(Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public void validateUpdateSubmit() {
		validateBeforeSubmit();
	}
	
	public String updateSubmit() {
		try {
			this.consultingService.update(this.consulting, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String updateHandledStatus() {
		try {
			this.consulting = this.consultingService.get(id);
			if (this.consulting == null) {
				super.addActionError("沒有對應的資料");
			}
			
			this.consulting.setBeenHandled(this.beenHandled);
			this.consultingService.update(this.consulting, this.getCurrentSysUser(), false, super.getSysLog());
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.consulting = this.consultingService.get(this.id);
			if (this.consulting == null) {
				super.addActionError("找不到選擇的資料紀錄!");
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
			this.consultingService.delete(this.id, false, super.getSysLog());
			this.addActionMessage("DELETE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.consulting.getName(), 100, "consulting.name");
		super.validateNotBlankNLength(this.consulting.getOrganization(), 500, "consulting.organization");
		super.validateNotBlankNLength(this.consulting.getPhone(), 100, "consulting.phone");
		if (super.validateNotBlank(this.consulting.getEmail(), "consulting.email")) {
			super.validateEmail(this.consulting.getEmail(), "consulting.email");
		}
		super.validateNotBlank(this.consulting.getContent(), "consulting.content");
	}
	
	public String exportRawData() {
		try {
			XSSFWorkbook wb = this.consultingService.exportRawData(searchCondition);
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "consulting_raw_data.xlsx";
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String printReport() {
		try {
			this.reportInputStream = this.consultingService.printReport(this.id);
			this.reportFileName = "print.pdf";
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	//==========================================================================
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Consulting getConsulting() {
		return consulting;
	}

	public void setConsulting(Consulting consulting) {
		this.consulting = consulting;
	}

	public PagedList<Consulting> getConsultingPagedList() {
		return consultingPagedList;
	}

	public List<OptionOrganizationType> getOptionOrganizationTypeList() {
		if (optionOrganizationTypeList == null) {
			optionOrganizationTypeList = this.optionOrgTypeService.listAll(); 
		}
		return optionOrganizationTypeList;
	}

	public List<OptionConsult> getOptionConsultList() {
		if(optionConsultList == null) {
			optionConsultList = this.optionConsultService.listAll();
		}
		return optionConsultList;
	}

	public List<OptionIndustry> getOptionIndustryList() {
		if (optionIndustryList == null) {
			optionIndustryList = this.optionIndustryService.listAll();
		}
		return optionIndustryList;
	}

	public ConsultingSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(ConsultingSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileInputStream() {
		return downloadFileInputStream;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public InputStream getReportInputStream() {
		return reportInputStream;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}

	public boolean getBeenHandled() {
		return beenHandled;
	}

	public void setBeenHandled(boolean beenHandled) {
		this.beenHandled = beenHandled;
	}
	
	
}
