package iace.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import core.util.PagedList;
import iace.entity.option.BaseOption;
import iace.entity.option.BaseOptionSearchModel;
import iace.entity.option.BatchImportOptionResult;
import iace.service.option.BaseOptionService;

public class BaseOptionAction<OptionEntity extends BaseOption> extends BaseIaceAction {

	private static final long serialVersionUID = -3645672145100849569L;

	protected BaseOptionService<OptionEntity> optionService;

	protected PagedList<OptionEntity> optionPagedList;
	protected BaseOptionSearchModel searchCondition = new BaseOptionSearchModel();

	protected Long id;
	protected OptionEntity option;

	protected File uploadFile;
	protected String uploadFileContentType;
	protected String uploadFileFileName;

	protected BatchImportOptionResult batchImportResult;

	protected String downloadFileName;
	protected InputStream sampleFileInputStream;

	protected BaseOptionAction(String title, BaseOptionService<OptionEntity> optionService) {
		super.setTitle(title);
		this.optionService = optionService;
	}

	public String index() {
		try {
			this.optionPagedList = this.optionService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String create() {
		return INPUT;
	}

	public void validateCreateSubmit() {
		// code
		if (super.validateNotBlankNLength(this.option.getCode(), 10, "option.code")) {
			if (this.optionService.isCodeExist(this.option.getCode())) {
				this.addFieldError("option.code", "代碼已存在!");
			}
		}

		// name
		super.validateNotBlankNLength(this.option.getName(), 500, "option.name");
	}

	public String createSubmit() {
		String rtnStr;
		try {
			this.optionService.create(this.option);
			rtnStr = SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			rtnStr = ERROR;
		}
		this.index();
		return rtnStr;
	}

	public String update() {
		try {
			this.option = this.optionService.get(this.id);
			return INPUT;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public void validateUpdateSubmit() {
		super.validateNotBlankNLength(this.option.getCode(), 10, "option.code");
		super.validateNotBlankNLength(this.option.getName(), 500, "option.name");
	}

	public String updateSubmit() {
		String rtnStr;
		try {
			this.optionService.update(this.option);
			rtnStr = SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			rtnStr = INPUT;
		}
		this.index();
		return rtnStr;
	}

	public String deleteSubmit() {
		String rtnStr = SUCCESS;
		try {
			this.optionService.delete(this.id);
			rtnStr = SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			rtnStr = ERROR;
		}
		this.index();
		return rtnStr;
	}

	public String batchImport() {
		return SUCCESS;
	}
	
	public String batchImportSubmit() {
		try {
			this.batchImportResult = this.optionService.batchImport(this.uploadFile);
			if (this.batchImportResult.getErrMsgs().size() > 0) {
				this.addActionError("部分或全部匯入資料有誤，請看下方錯誤列表");
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
			this.downloadFileName = "代碼資料匯入範例格式.xlsx";
			String filePath = context.getRealPath("/files/" + this.downloadFileName);
			log.debug(filePath);
			sampleFileInputStream = new FileInputStream(new File(filePath));
			this.downloadFileName = new String(this.downloadFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	// =========================================================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OptionEntity getOption() {
		return option;
	}

	public void setOption(OptionEntity option) {
		this.option = option;
	}

	public BaseOptionSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(BaseOptionSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<OptionEntity> getOptionPagedList() {
		return optionPagedList;
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
	

	public BatchImportOptionResult getBatchImportResult() {
		return batchImportResult;
	}
	

	public void setBatchImportResult(BatchImportOptionResult batchImportResult) {
		this.batchImportResult = batchImportResult;
	}
	

	public String getDownloadFileName() {
		return downloadFileName;
	}
	

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	

	public InputStream getSampleFileInputStream() {
		return sampleFileInputStream;
	}
	

	public void setSampleFileInputStream(InputStream sampleFileInputStream) {
		this.sampleFileInputStream = sampleFileInputStream;
	}
	

	public void setOptionPagedList(PagedList<OptionEntity> optionPagedList) {
		this.optionPagedList = optionPagedList;
	}

}
