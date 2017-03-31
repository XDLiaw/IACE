package iace.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import core.util.ExcelUtil;
import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.option.OptionCountry;
import iace.entity.option.OptionTrl;
import iace.entity.patent.Patent;
import iace.entity.patent.PatentSearchModel;
import iace.entity.patent.TechField;
import iace.service.ServiceFactory;
import iace.service.option.OptionCountryService;
import iace.service.option.OptionTrlService;
import iace.service.patent.PatentExcelService;
import iace.service.patent.PatentService;
import iace.service.patent.TechFieldService;

public class PatentAction extends BaseIaceAction {

	private static final long serialVersionUID = 6073541012565178740L;

	private PatentService patentService = ServiceFactory.getPatentService();
	private PatentExcelService patentExcelService = ServiceFactory.getPatentExcelService();
	private TechFieldService techFieldService = ServiceFactory.getTechFieldService();
	private OptionCountryService optionCountryService = ServiceFactory.getOptionCountryService();
	private OptionTrlService optionTrlService = ServiceFactory.getOptionTrlService();

	private PatentSearchModel searchCondition = new PatentSearchModel();
	private PagedList<Patent> patentPagedList;

	private List<TechField> techFieldList;
	private List<OptionCountry> optionCountryList;
	private List<OptionTrl> optionTrlList;

	private Long id;
	private Patent patent;

	private File uploadPatentImg;
	private String uploadPatentImgContentType;
	private String uploadPatentImgFileName;
	
	private String reportFileName;
	private InputStream reportInputStream;
	
	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	
	private String downloadFileName;
	private InputStream downloadFileInputStream;

	public PatentAction() {
		super.setTitle("專利資料");
	}

	public String init() {
		return SUCCESS;
	}

	public String index() {
		try {
			this.patentPagedList = this.patentService.searchBy(this.searchCondition);
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return SUCCESS;
		}
	}

	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, Patent.class);
			this.patent = this.patentService.get(this.id);
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
		validateBeforeSubmit();
	}

	public String createSubmit() {
		try {
			setUploadFileToEntity();

			this.patentService.create(this.patent, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}

	public String update() {
		try {
			this.patent = this.patentService.get(this.id);
			if (this.patent == null) {
				super.addActionError("找不到選擇的資料紀錄!");
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
			setUploadFileToEntity();
			this.patentService.update(this.patent, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			
			this.patentPagedList = this.patentService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}

	public String delete() {
		try {
			this.patent = this.patentService.get(this.id);
			if (this.patent == null) {
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
			this.patentService.delete(this.id, true, super.getSysLog());
			this.addActionMessage("DELETE SUCCESS!");
			
			this.patentPagedList = this.patentService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	@Override
	public void validate() {
		String actionName = ActionContext.getContext().getName();
		if ("showDetail".equals(actionName) || "update".equals(actionName) || "delete".equals(actionName)) {
			validateIdExist();
		}
	}

	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.patent.getName(), 300, "patent.name");
		super.validateNotBlankNLength(this.patent.getAssignee(), 500, "patent.assignee");
		super.validateNotBlankNLength(this.patent.getInvertor(), 500, "patent.invertor");
//		super.validateNotBlankNLength(this.patent.getCountry(), 10, "patent.country");
		super.validateNotBlankNLength(this.patent.getAppliactionNo(), 100, "patent.appliactionNo");
		super.validateNotNull(this.patent.getApplicationDate(), "patent.applicationDate");
		super.validateTextMaxLength(this.patent.getOpenNo(), 100, "patent.openNo");
		super.validateTextMaxLength(this.patent.getPublicationNo(), 100, "patent.publicationNo");
		super.validateNotBlankNLength(this.patent.getCategory(), 100, "patent.category");
		super.validateNotBlankNLength(this.patent.getPatentStatus(), 500, "patent.patentStatus");
		super.validateNotBlankNLength(this.patent.getFamilyNo(), 2000, "patent.familyNo");
		super.validateNotBlankNLength(this.patent.getIpc(), 200, "patent.ipc");
		super.validateNotBlank(this.patent.getTechAbstract(), "patent.techAbstract");
//		super.validateNotBlankNLength(this.patent.getImportantPictureCode(), 100, "patent.importantPictureCode");
		super.validateNotBlankNLength(this.patent.getTechField().getName(), 500, "patent.techField.name");
		super.validateTextMaxLength(this.patent.getUsage(), 500, "patent.usage");
		if (this.patent.getTrl() != null && StringUtils.isNotBlank(this.patent.getTrl().getCode())) {
			try {
				if (this.optionTrlService.isCodeExist(this.patent.getTrl().getCode()) == false) {
					super.addFieldError("patent.trl.code", "發展階段代碼不存在");
				}
			} catch (Exception e) {
				super.addActionError(e.getMessage());
			}
		}
		super.validateTextMaxLength(this.patent.getTrlDesc(), 2000, "patent.trlDesc");

		try {
			if (this.patentService.checkUK(this.patent) == false) {
				super.addFieldError("patent.appliactionNo", "申請號和專業技術領域合起來必須唯一");
				super.addFieldError("patent.techField.name", "申請號和專業技術領域合起來必須唯一");
			}
		} catch (Exception e) {
			super.addActionError(e.getMessage());
		}
	}

	private void validateIdExist() {
		try {
			this.patent = this.patentService.get(id);
			if (this.patent == null) {
				this.addActionError("找不到資料");
			}
		} catch (Exception e) {
			this.addActionError(e.getMessage());
		}
	}

	private void setUploadFileToEntity() throws IOException {
		if (StringUtils.isNotBlank(this.uploadPatentImgFileName)) {
			Path path = Paths.get(this.uploadPatentImg.getAbsolutePath());
			byte[] data = Files.readAllBytes(path);
			this.patent.setImportantPatentPicture(data);
			this.patent.setImportantPicturePath(this.uploadPatentImgFileName);
		} else {
			if (this.patent.getId() != 0) {
				Patent oldP = this.patentService.get(this.patent.getId());
				this.patent.setImportantPatentPicture(oldP.getImportantPatentPicture());
				this.patent.setImportantPicturePath(oldP.getImportantPicturePath());
			}
		}
	}
	
	public String printReport() {
		try {
			this.reportInputStream = this.patentService.printReport(this.id);
			this.reportFileName = "patent.pdf";
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String batchImport() {
		return SUCCESS;
	}
	
	public String batchImportSubmit() {
		try {
//			String filePath = "D:\\SYSVIN\\2016\\i-ACE鏈結產學媒合平台";
//			File fileToCreate = new File(filePath, this.uploadFileFileName);
//			FileUtils.copyFile(this.uploadFile, fileToCreate);
			List<Patent> patentList = this.patentExcelService.excelToPatents(uploadFile);
			List<String> errMsgs = this.patentService.createAll(patentList);
			if (errMsgs.size() > 0) {
				super.addActionError("----- 匯入檔案中含有下列錯誤，批次匯入失敗，請修正後重新上傳 -----");
				for (String errMsg : errMsgs) {
					super.addActionError(errMsg);
				}
				return INPUT;
			} else {
				super.addActionMessage("成功新增"+patentList.size()+"筆專利資料");
				return SUCCESS;
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String downloadBatchSample() {
		try {
			ServletContext context = ServletActionContext.getServletContext();
			this.downloadFileName = "專利資料匯入_sample.xlsx";
			String filePath = context.getRealPath("/files/"+this.downloadFileName);
			log.debug(filePath);
			this.downloadFileInputStream = new FileInputStream(new File(filePath));
			this.downloadFileName = new String(this.downloadFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}	
	}
	
	public String exportRawData() {
		try {
			XSSFWorkbook wb = this.patentService.exportRawData(searchCondition);
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "patent_raw_data.xlsx";
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	// ==========================================================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public List<OptionCountry> getOptionCountryList() {
		if (optionCountryList == null) {
			this.optionCountryList = this.optionCountryService.listAll();
		}
		return optionCountryList;
	}

	public List<OptionTrl> getOptionTrlList() {
		if (optionTrlList == null) {
			this.optionTrlList = this.optionTrlService.listAll();
		}
		return optionTrlList;
	}

	public List<TechField> getTechFieldList() {
		if (techFieldList == null) {
			this.techFieldList = this.techFieldService.listAll();
		}
		return techFieldList;
	}

	public PagedList<Patent> getPatentPagedList() {
		return patentPagedList;
	}

	public File getUploadPatentImg() {
		return uploadPatentImg;
	}

	public void setUploadPatentImg(File uploadPatentImg) {
		this.uploadPatentImg = uploadPatentImg;
	}

	public String getUploadPatentImgContentType() {
		return uploadPatentImgContentType;
	}

	public void setUploadPatentImgContentType(String uploadPatentImgContentType) {
		this.uploadPatentImgContentType = uploadPatentImgContentType;
	}

	public String getUploadPatentImgFileName() {
		return uploadPatentImgFileName;
	}

	public void setUploadPatentImgFileName(String uploadPatentImgFileName) {
		this.uploadPatentImgFileName = uploadPatentImgFileName;
	}

	public PatentSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(PatentSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public InputStream getReportInputStream() {
		return reportInputStream;
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

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileInputStream() {
		return downloadFileInputStream;
	}



	
}
