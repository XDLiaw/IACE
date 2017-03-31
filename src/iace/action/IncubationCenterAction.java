package iace.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.BaseBatchImportResult;
import iace.entity.incubationCenter.IncubationCenter;
import iace.entity.incubationCenter.IncubationCenterSearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.incubationCenter.IncubationCenterService;

public class IncubationCenterAction extends BaseIaceAction {

	private static final long serialVersionUID = -4582348899545320156L;

	private IncubationCenterService incubationCenterService = ServiceFactory.getIncubationCenterService();

	private IncubationCenterSearchModel searchCondition = new IncubationCenterSearchModel();
	private PagedList<IncubationCenter> incubationCenterPagedList;

	private Long id;
	private IncubationCenter incubationCenter;

	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	private BaseBatchImportResult<IncubationCenter> batchImportResult;

	private String downloadFileName;
	private InputStream sampleFileInputStream;
	
	private List<BaseOption> attributeList = IncubationCenter.getAttributeList();

	public IncubationCenterAction() {
		super.setTitle("育成中心");
	}
	
	public String init() {
		return SUCCESS;
	}
	
	public String index() {
		try {
			this.incubationCenterPagedList = this.incubationCenterService.searchBy(searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, IncubationCenter.class);
			this.incubationCenter = this.incubationCenterService.get(this.id);
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
			this.batchImportResult = this.incubationCenterService.batchImport(this.uploadFile);
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
			this.downloadFileName = "育成中心匯入範例格式.xlsx";
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

	public IncubationCenterSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(IncubationCenterSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<IncubationCenter> getIncubationCenterPagedList() {
		return incubationCenterPagedList;
	}

	public void setIncubationCenterPagedList(PagedList<IncubationCenter> incubationCenterPagedList) {
		this.incubationCenterPagedList = incubationCenterPagedList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IncubationCenter getIncubationCenter() {
		return incubationCenter;
	}

	public void setIncubationCenter(IncubationCenter incubationCenter) {
		this.incubationCenter = incubationCenter;
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

	public BaseBatchImportResult<IncubationCenter> getBatchImportResult() {
		return batchImportResult;
	}

	public void setBatchImportResult(BaseBatchImportResult<IncubationCenter> batchImportResult) {
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

	public List<BaseOption> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<BaseOption> attributeList) {
		this.attributeList = attributeList;
	}
	
	

}
