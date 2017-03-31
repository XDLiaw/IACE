package iace.action;

import java.io.InputStream;
import java.util.List;

import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.coopExample.CoopEx;
import iace.entity.coopExample.CoopExSearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.coopExample.CoopExService;

public class CoopExAction extends BaseIaceAction {

	private static final long serialVersionUID = -1853598489369057497L;
	
	private CoopExService coopExService = ServiceFactory.getCoopExService();
	
	private CoopExSearchModel searchCondition = new CoopExSearchModel();
	private PagedList<CoopEx> coopExPagedList;
	
	private List<BaseOption> typeList = CoopEx.getTypeList();
	
	private Long id;
	private CoopEx coopEx;
	
	private Long imgId;
	private Long videoId;
	private Long attachFileId;
	private String downloadFileName;
	private InputStream downloadFileInputStream;

	public CoopExAction() {
		super.setTitle("產學合作案例");
	}
	
	public String init() {
		return index();
	}

	public String index() {
		try {
			this.coopExPagedList = this.coopExService.searchBy(searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, CoopEx.class);
			this.coopEx = this.coopExService.get(this.id);
			if (this.coopEx == null) {
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
		validateBeforeSubmit();
	}
	
	public String createSubmit() {
		try {
			this.coopExService.create(this.coopEx, super.getCurrentSysUser(), true, super.getSysLog());
			super.addActionMessage("CREATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.coopEx = this.coopExService.get(this.id);
			if (this.coopEx == null) {
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
			this.coopExService.update(this.coopEx, super.getCurrentSysUser(), true, super.getSysLog());
			super.addActionMessage("UPDATE SUCCESS");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;			
		}
	}
	
	public String delete() {
		try {
			this.coopEx = this.coopExService.get(this.id);
			if (this.coopEx == null) {
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
			this.coopExService.delete(this.id, true, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateBeforeSubmit() {
		if (super.validateNotBlankNLength(this.coopEx.getProjName(), 1000, "coopEx.projName")) {
			boolean isExist = this.coopExService.isProjNameExist(this.coopEx.getId(), this.coopEx.getProjName());
			if (isExist) {
				super.addFieldError("coopEx.projName", "案名已存在");
			}
		}
		super.validateNotNull(this.coopEx.getYear(), "coopEx.year");
		super.validateNotBlankNLength(this.coopEx.getRdTeam(), 1000, "coopEx.rdTeam");
		super.validateNotBlankNLength(this.coopEx.getAssisTeam(), 1000, "coopEx.assisTeam");
	}
	
	// ========================================================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CoopEx getCoopEx() {
		return coopEx;
	}

	public void setCoopEx(CoopEx coopEx) {
		this.coopEx = coopEx;
	}

	public Long getAttachFileId() {
		return attachFileId;
	}

	public void setAttachFileId(Long attachFileId) {
		this.attachFileId = attachFileId;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public InputStream getDownloadFileInputStream() {
		return downloadFileInputStream;
	}

	public void setDownloadFileInputStream(InputStream downloadFileInputStream) {
		this.downloadFileInputStream = downloadFileInputStream;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public CoopExSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(CoopExSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<CoopEx> getCoopExPagedList() {
		return coopExPagedList;
	}

	public void setCoopExPagedList(PagedList<CoopEx> coopExPagedList) {
		this.coopExPagedList = coopExPagedList;
	}

	public List<BaseOption> getTypeList() {
		return typeList;
	}
	
	
}
