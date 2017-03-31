package iace.action;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import core.util.PagedList;
import iace.entity.BaseBatchImportResult;
import iace.entity.literature.Literature;
import iace.entity.literature.LiteratureSearchModel;
import iace.service.ServiceFactory;
import iace.service.literature.LiteratureService;

public class LiteratureAction extends BaseIaceAction {

	private static final long serialVersionUID = 727897246820033912L;
	private LiteratureService literatureService = ServiceFactory.getLiteratureService();
	
	private LiteratureSearchModel searchCondition = new LiteratureSearchModel();
	private PagedList<Literature> literaturePagedList;
	
	private Long id;
	private Literature literature;
	
	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	private BaseBatchImportResult<Literature> batchImportResult;
	
	public LiteratureAction() {
		super.setEntityClass(Literature.class);
		super.setTitle("文獻與法規政策");
	}

	private void settingTittle() {
		if (StringUtils.isNotBlank(this.searchCondition.getCategory())) {
			super.setTitle(this.searchCondition.getCategory());
		}
	}
	
	public String init() {
		settingTittle();
		return SUCCESS;
	}
	
	public String index() {
		settingTittle();
		try {
			this.literaturePagedList = this.literatureService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		settingTittle();
		try {
			this.literature = this.literatureService.get(this.id);
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
			this.batchImportResult = this.literatureService.batchImport(this.uploadFile);
			if (this.batchImportResult.getErrMsgs().size() > 0) {
				this.addActionError("部分或全部匯入資料有誤，請看下方錯誤列表");
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	// =========================================================================
	
	public LiteratureSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(LiteratureSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<Literature> getLiteraturePagedList() {
		return literaturePagedList;
	}

	public void setLiteraturePagedList(PagedList<Literature> literaturePagedList) {
		this.literaturePagedList = literaturePagedList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Literature getLiterature() {
		return literature;
	}

	public void setLiterature(Literature literature) {
		this.literature = literature;
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

	public BaseBatchImportResult<Literature> getBatchImportResult() {
		return batchImportResult;
	}

	public void setBatchImportResult(BaseBatchImportResult<Literature> batchImportResult) {
		this.batchImportResult = batchImportResult;
	}

	
}
