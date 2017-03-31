package iace.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.JDBCConnectionException;

import core.util.ExcelUtil;
import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.BaseBatchImportResult;
import iace.entity.option.OptionDomain;
import iace.entity.sys.SysRole;
import iace.entity.sys.SysUser;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.talentedPeople.TalentedPeoplePDPL;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;
import iace.interceptor.SessionInterceptor;
import iace.service.ServiceFactory;
import iace.service.sys.SysRoleService;
import iace.service.sys.SysUserService;
import iace.service.talentedPeople.TalentedPeoplePDPLService;
import iace.service.talentedPeople.TalentedPeopleService;

public class TalentedPeopleAction extends BaseIaceAction {

	private static final long serialVersionUID = 570851154374907844L;

	private TalentedPeopleService talentedPeopleService = ServiceFactory.getTalentedPeopleService();
	private TalentedPeoplePDPLService talentedPeoplePDPLService = ServiceFactory.getTalentedPeoplePDPLService();
	private SysUserService sysUserService = ServiceFactory.getSysUserService();
	private SysRoleService sysRoleService = ServiceFactory.getSysRoleService();

	private TalentedPeopleSearchModel searchCondition = new TalentedPeopleSearchModel();
	private PagedList<TalentedPeople> talentedPeoplePagedList;

	private Long id;
	private Long sysRoleId;
	private TalentedPeople talentedPeople;
	private TalentedPeoplePDPL talentedPeoplePDPL;
	private SysUser sysUser;

	private List<OptionDomain> mainDomainList;
	private List<SysRole> sysRoleList;

	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	private BaseBatchImportResult<TalentedPeople> batchImportResult;

	private String downloadFileName;
	private InputStream downloadFileInputStream;

	private String reportFileName;
	private InputStream reportInputStream;

	private String scrollTo;

	public TalentedPeopleAction() {
		super.setEntityClass(TalentedPeople.class);
		super.setTitle("產學合作人才資料");
	}

	public String init() {
		return SUCCESS;
	}

	public String index() {
		try {
			this.talentedPeoplePagedList = this.talentedPeopleService.searchBy(this.searchCondition);

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, TalentedPeople.class);
			this.talentedPeople = this.talentedPeopleService.get(this.id);
			if (this.talentedPeople == null) {
				super.addActionError("找不到資料!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	@Deprecated
	public String create() {
		return SUCCESS;
	}

	@Deprecated
	public void validateCreateSubmit() {
		validateBeforeSubmit();
	}

	@Deprecated
	public String createSubmit() {
		try {
			boolean isIndexing = this.talentedPeoplePDPLService.isIndexing(this.talentedPeople.getId());
			this.talentedPeopleService.create(this.talentedPeople, super.getCurrentSysUser(), isIndexing, super.getSysLog());

			super.addActionMessage("CREATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String update() {
		try {
			this.talentedPeople = this.talentedPeopleService.get(this.id);
			if (this.talentedPeople == null) {
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
			boolean isIndexing = this.talentedPeoplePDPLService.isIndexing(this.talentedPeople.getId());
			TalentedPeople talentedPeopleOld = this.talentedPeopleService.get(this.talentedPeople.getId());
			this.talentedPeople.setRdResults(talentedPeopleOld.getRdResults());
			this.talentedPeople.setTransferCases(talentedPeopleOld.getTransferCases());
			this.talentedPeople.setMainProjects(talentedPeopleOld.getMainProjects());
			this.talentedPeopleService.update(this.talentedPeople, super.getCurrentSysUser(), isIndexing, super.getSysLog());
			this.talentedPeople = this.talentedPeopleService.get(this.talentedPeople.getId());

			super.addActionMessage("UPDATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String delete() {
		try {
			this.talentedPeople = this.talentedPeopleService.get(this.id);
			if (this.talentedPeople == null) {
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
			boolean isIndexing = this.talentedPeoplePDPLService.isIndexing(this.talentedPeople.getId());
			this.talentedPeopleService.delete(this.id, isIndexing, super.getSysLog());

			super.addActionMessage("DELETE SUCCESS");
			return index();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.talentedPeople.getNameCh(), 100, "talentedPeople.nameCh");
		super.validateNotBlankNLength(this.talentedPeople.getNameEn(), 100, "talentedPeople.nameEn");
		super.validateNotBlankNLength(this.talentedPeople.getTel(), 200, "talentedPeople.tel");
		if (super.validateNotBlankNLength(this.talentedPeople.getEmail(), 200, "talentedPeople.email")) {
			String emails = this.talentedPeople.getEmail().trim().replace(";", "；");
			StringBuilder sb = new StringBuilder();
			for (String email : emails.split("；")) {
				if (super.validateEmail(email.trim(), "talentedPeople.email")) {
					sb.append(email.trim()+"；");
				}
			}
			this.talentedPeople.setEmail(sb.toString().substring(0, sb.length()-1));
		}
		super.validateTextMaxLength(this.talentedPeople.getWorkOrg(), 100, "talentedPeople.workOrg");
		super.validateTextMaxLength(this.talentedPeople.getJob(), 100, "talentedPeople.job");
		super.validateTextMaxLength(this.talentedPeople.getUrl(), 1000, "talentedPeople.url");
		super.validateTextMaxLength(this.talentedPeople.getSpecialty(), 1000, "talentedPeople.specialty");
	}

	public String batchImport() {
		return SUCCESS;
	}

	public String batchImportSubmit() {
		try {
			this.batchImportResult = this.talentedPeopleService.batchImport(this.uploadFile, this.sysRoleId);
			if (this.batchImportResult.getInsertList().size() > 0) {
				this.addActionMessage("已成功匯入" + this.batchImportResult.getInsertList().size() + "筆資料");
			}
			if (this.batchImportResult.getErrMsgs().size() > 0) {
				this.addActionError("匯入資料有誤，請看下方錯誤列表");
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String downloadBatchSample() {
		try {
			ServletContext context = ServletActionContext.getServletContext();
			this.downloadFileName = "產學人才資料庫基本資料_批次匯入格式.xlsx";
			String filePath = context.getRealPath("/files/" + this.downloadFileName);
			log.debug(filePath);
			downloadFileInputStream = new FileInputStream(new File(filePath));
			this.downloadFileName = new String(this.downloadFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String printReport() {
		try {
			this.reportInputStream = this.talentedPeopleService.printReport(this.id);
			this.reportFileName = "print.pdf";
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String exportRawData() {
		try {
			XSSFWorkbook wb = this.talentedPeopleService.exportRawData(this.searchCondition);
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "raw_data.xlsx";

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String exportEmail() {
		return SUCCESS;
	}

	public String exportAllEmail() {
		try {
			XSSFWorkbook wb = this.talentedPeopleService.exportAllEmailList();
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "emailList.xlsx";

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String exportNotAgreePDPLYetEmail() {
		try {
			XSSFWorkbook wb = this.talentedPeopleService.exportNotAgreePDPLYetEmailList();
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "emailList.xlsx";

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String exportPDPLList() {
		try {
			XSSFWorkbook wb = this.talentedPeoplePDPLService.exportList();
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "PDPL_List.xlsx";

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String exportPDPLSummary() {
		try {
			XSSFWorkbook wb = this.talentedPeoplePDPLService.exportSummary();
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "PDPL_Summary.xlsx";

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String PDPL() {
		try {
			this.talentedPeoplePDPL = this.talentedPeoplePDPLService.getByTalentedPeopleID(this.id);
			if (this.talentedPeoplePDPL == null) {
				this.talentedPeople = this.talentedPeopleService.get(this.id);
				if (this.talentedPeople == null) {
					super.addActionError("找不到資料!");
					return ERROR;
				}
				this.talentedPeoplePDPL = new TalentedPeoplePDPL();
				this.talentedPeoplePDPL.setTalentedPeople(this.talentedPeople);
				this.talentedPeoplePDPLService.create(this.talentedPeoplePDPL);
				return SUCCESS; // TO pdpl.jsp
			} else {
				if (this.talentedPeoplePDPL.getAgreePDPL() == null || this.talentedPeoplePDPL.getAgreePDPL() == false) {
					return SUCCESS; // TO pdpl.jsp
				} else {
					return LOGIN; // skip pdpl.jsp and redirect to login.jsp
				}
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String PDPLSubmit() {
		try {
			boolean agreePDPL = this.talentedPeoplePDPL.getAgreePDPL();
			this.talentedPeoplePDPL = this.talentedPeoplePDPLService.get(this.talentedPeoplePDPL.getId());
			this.talentedPeoplePDPL.setAgreePDPL(agreePDPL);
			this.talentedPeoplePDPL.setIp(ServletActionContext.getRequest().getRemoteAddr());
			this.talentedPeoplePDPLService.update(this.talentedPeoplePDPL);

			if (this.talentedPeoplePDPL.getAgreePDPL()) {
				return LOGIN;
			} else {
				return "close";
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String login() {
		return SUCCESS;
	}

	public String loginSubmit() {
		try {
			if (this.sysUserService.loginCheck(this.sysUser.getAccount(), this.sysUser.getPassword())) {
				this.sysUser = this.sysUserService.getBy(this.sysUser.getAccount(), this.sysUser.getPassword());
				this.talentedPeople = this.talentedPeopleService.get(this.sysUser);
				if (this.talentedPeople == null) {
					super.addActionError("此帳號不是產學人才");
					return INPUT;
				}
				
				super.session.put(SessionInterceptor.SESSION_KEY_SYS_USER, this.sysUser);

				// sysLog
				super.getSysLog().setSysUser(this.sysUser);
				super.getSysLog().setEnableLog(true);

				return SUCCESS;
			} else {
				super.addActionError("帳號或密碼錯誤!");
				return INPUT;
			}
		} catch (JDBCConnectionException e) {
			log.error("資料庫連線錯誤，請重新嘗試!", e);
			super.addActionError("資料庫連線錯誤，請重新嘗試!");
			return INPUT;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String selfUpdate() {
		try {
			this.talentedPeople = this.talentedPeopleService.get(super.getCurrentSysUser());
			if (this.talentedPeople == null) {
				super.addActionError("此帳號不是產學人才");
				this.session.clear();
				return INPUT;
			}

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public void validateSelfUpdateSubmit() {
		validateBeforeSubmit();
	}
	
	public String selfUpdateSubmit() {
		if (super.getCurrentSysUser().getId() != this.talentedPeople.getSysUser().getId()) {
			super.addActionError("您只能維護自己的資料");
			return ERROR;
		}
		return updateSubmit();
	}

	public String batchResizeHeadshot() {
		String sysRoleName = super.getCurrentSysUser().getSysRole().getName();
		if (StringUtils.equals(sysRoleName, "系統開發人員") == false) {
			super.addActionError("沒有權限");
			return INPUT;
		}

		try {
			Date d1 = new Date();
			this.talentedPeopleService.resizeAllHeadShot();
			Date d2 = new Date();
			long spendSec = (d2.getTime() - d1.getTime()) / 1000;
			this.addActionMessage("處理完成! 用時" + spendSec + "秒.");

			this.setTitle("開發者專屬功能");

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	// =========================================================================

	public TalentedPeopleSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(TalentedPeopleSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<TalentedPeople> getTalentedPeoplePagedList() {
		return talentedPeoplePagedList;
	}

	public void setTalentedPeoplePagedList(PagedList<TalentedPeople> talentedPeoplePagedList) {
		this.talentedPeoplePagedList = talentedPeoplePagedList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TalentedPeople getTalentedPeople() {
		return talentedPeople;
	}

	public void setTalentedPeople(TalentedPeople talentedPeople) {
		this.talentedPeople = talentedPeople;
	}

	public List<OptionDomain> getMainDomainList() {
		if (mainDomainList == null) {
			mainDomainList = ServiceFactory.getOptionDomainService().listAll();
		}
		return mainDomainList;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BaseBatchImportResult<TalentedPeople> getBatchImportResult() {
		return batchImportResult;
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

	public Long getSysRoleId() {
		return sysRoleId;
	}

	public void setSysRoleId(Long sysRoleId) {
		this.sysRoleId = sysRoleId;
	}

	public List<SysRole> getSysRoleList() {
		if (sysRoleList == null) {
			sysRoleList = this.sysRoleService.listAll();
		}
		return sysRoleList;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public TalentedPeoplePDPL getTalentedPeoplePDPL() {
		return talentedPeoplePDPL;
	}

	public void setTalentedPeoplePDPL(TalentedPeoplePDPL talentedPeoplePDPL) {
		this.talentedPeoplePDPL = talentedPeoplePDPL;
	}


	public String getScrollTo() {
		return scrollTo;
	}
	

	public void setScrollTo(String scrollTo) {
		this.scrollTo = scrollTo;
	}

}
