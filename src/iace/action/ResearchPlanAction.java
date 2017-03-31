package iace.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import core.util.ExcelUtil;
import core.util.PagedList;
import iace.entity.option.BaseOption;
import iace.entity.option.OptionGrbDomain;
import iace.entity.option.OptionTrl;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.researchPlan.ResearchPlanSearchModel;
import iace.entity.researchPlan.Technology;
import iace.service.ServiceFactory;
import iace.service.option.OptionGrbDomainService;
import iace.service.option.OptionTrlService;
import iace.service.researchPlan.ResearchPlanExcelService;
import iace.service.researchPlan.ResearchPlanService;
import iace.service.researchPlan.TechnologyService;

public class ResearchPlanAction extends BaseIaceAction {

	private static final long serialVersionUID = 1798627207055671857L;
	
	private ResearchPlanService researchPlanService = ServiceFactory.getResearchPlanService();
	private ResearchPlanExcelService researchPlanExcelService = ServiceFactory.getResearchPlanExcelService();
	private TechnologyService technologyService = ServiceFactory.getTechnologyService();
	private OptionGrbDomainService optionGrbDomainService = ServiceFactory.getOptionGrbDomainService();
	private OptionTrlService optionTrlService = ServiceFactory.getOptionTrlService();
	
	private ResearchPlanSearchModel searchCondition = new ResearchPlanSearchModel();
	private List<OptionGrbDomain> optionGrbDomainList;
	private List<OptionTrl> optionTrlList;
	private List<BaseOption> yearList;
	private PagedList<ResearchPlan> researchPlanPagedList;
	
	private Boolean fromHomePage;
	
	private Long id;
	private ResearchPlan researchPlan;	
	
	private long technologyId;
	private Technology technology;
	
	private Map<String, Object> ajaxResult;
	
	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	
	private String downloadFileName;
	private InputStream downloadFileInputStream;
	
	public ResearchPlanAction() {
		super.setEntityClass(ResearchPlan.class);
		super.setTitle("研發成果");
	}

	public String init() {
		return SUCCESS;
	}
	
	public String index() {
		try {
			this.researchPlanPagedList = this.researchPlanService.searchBy(searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String showDetail() {
		try {
			this.researchPlan = this.researchPlanService.get(this.id);
			if (this.researchPlan == null) {
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
		if (this.researchPlanService.planNoExist(this.researchPlan.getPlanNo())) {
			super.addFieldError("researchPlan.planNo", "計畫編號已存在，不可重複");
		}
	}
	
	public String createSubmit() {
		try {
			this.researchPlanService.create(this.researchPlan, super.getCurrentSysUser(), true, super.getSysLog());
			this.id = this.researchPlan.getId();
			this.researchPlan = this.researchPlanService.get(this.id);
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String update() {
		try {
			this.researchPlan = this.researchPlanService.get(this.id);
			if (this.researchPlan == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}

			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	@Deprecated
	public String updateSubmit() {
		try {
			this.id = this.researchPlan.getId();
			ResearchPlan origin = this.researchPlanService.get(this.id);
			this.researchPlan.setTechnologies(origin.getTechnologies());

			this.researchPlanService.update(this.researchPlan);
			
			this.researchPlan = this.researchPlanService.get(this.id);
			List<ResearchPlan> plans = new ArrayList<ResearchPlan>();
			plans.add(this.researchPlan);
			this.researchPlanPagedList = new PagedList<ResearchPlan>(plans, 1, 5, 0);
			
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	@Deprecated
	public String delete() {
		try {
			this.researchPlan = this.researchPlanService.get(this.id);
			if (this.researchPlan == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	@Deprecated
	public String deleteSubmit() {
		try {
			this.researchPlanService.delete(this.id);
			this.addActionMessage("DELETE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	@Deprecated
	public String showTechnologyDetail() {
		try {
			this.technology = this.technologyService.get(this.technologyId);
			if (this.technology == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String createTechnology() {
		this.researchPlan = this.researchPlanService.get(this.id);
		if (this.researchPlan == null) {
			super.addActionError("找不到選擇的資料紀錄!");
			return INPUT;
		}
		return SUCCESS;
	}
	
	public void validateCreateTechnology() {
		//TODO
	}
	
	public String createTechnologySubmit() {
		try {
			this.researchPlan = this.researchPlanService.get(this.id);
			this.researchPlan.addTechnology(this.technology);
			this.technology.setResearchPlan(this.researchPlan);
			this.researchPlanService.update(this.researchPlan, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String updateTechnology() {
		try {
			this.researchPlan = this.researchPlanService.get(this.id);
			if (this.researchPlan == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			this.technology = this.technologyService.get(this.technologyId);
			if (this.technology == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}		
	}

	public void validateUpdateTechnology() {
		//TODO
	}
	
	public String updateTechnologySubmit() {
		try {
			this.researchPlan = this.researchPlanService.get(this.id);
			this.technology.setResearchPlan(this.researchPlan);
			this.technologyService.update(this.technology, super.getCurrentSysUser(), true, super.getSysLog());
			
			// 成功後重新抓取研究計畫資料
			this.researchPlan = this.researchPlanService.get(this.id);
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return INPUT;
		}
	}
	
	public String deleteTechnologySubmit() {
		try {
			this.technologyService.delete(this.technologyId, true, super.getSysLog());
			// 成功後重新抓取研究計畫資料
			this.researchPlan = this.researchPlanService.get(this.id);
			this.addActionMessage("DELETE SUCCESS!");
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
			List<ResearchPlan> entities = this.researchPlanExcelService.excelToResearchPlans(this.uploadFile);
			List<String> errMsgs = this.researchPlanService.createAll(entities);
			if (errMsgs.size() > 0) {
				super.addActionError("----- 匯入檔案中含有下列錯誤，批次匯入失敗，請修正後重新上傳 -----");
				for (String errMsg : errMsgs) {
					super.addActionError(errMsg);
				}
				return INPUT;
			} else {
				super.addActionMessage("成功新增"+entities.size()+"筆研究計畫資料");
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
			this.downloadFileName = "技術資料匯入_sample.xlsx";
			String filePath = context.getRealPath("/files/"+this.downloadFileName);
			log.debug(filePath);
			downloadFileInputStream = new FileInputStream(new File(filePath));
			this.downloadFileName = new String(this.downloadFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String exportRawData() {
		try {
			XSSFWorkbook wb = this.researchPlanService.exportRawData(this.searchCondition);
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "raw_data.xlsx";
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	//==========================================================================

	public ResearchPlanSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(ResearchPlanSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<ResearchPlan> getResearchPlanPagedList() {
		return researchPlanPagedList;
	}

	public List<OptionGrbDomain> getOptionGrbDomainList() {
		if (this.optionGrbDomainList == null) {
			this.optionGrbDomainList = this.optionGrbDomainService.listForResearchPlan();
		}
		return this.optionGrbDomainList;
	}

	public List<OptionTrl> getOptionTrlList() {
		if (this.optionTrlList == null) {
			this.optionTrlList = this.optionTrlService.listAll();
		}
		return optionTrlList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResearchPlan getResearchPlan() {
		return researchPlan;
	}

	public void setResearchPlan(ResearchPlan researchPlan) {
		this.researchPlan = researchPlan;
	}

	public long getTechnologyId() {
		return technologyId;
	}

	public void setTechnologyId(long technologyId) {
		this.technologyId = technologyId;
	}

	public Technology getTechnology() {
		return technology;
	}

	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	public Map<String, Object> getAjaxResult() {
		return ajaxResult;
	}

	public void setAjaxResult(Map<String, Object> ajaxResult) {
		this.ajaxResult = ajaxResult;
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

	public Boolean getFromHomePage() {
		return fromHomePage;
	}

	public void setFromHomePage(Boolean fromHomePage) {
		this.fromHomePage = fromHomePage;
	}

	public List<BaseOption> getYearList() {
		if (this.yearList == null) {
			this.yearList = new ArrayList<BaseOption>();
			List<Integer> yearList = this.researchPlanService.getYearList();
			for (int year : yearList) {
				String strYear = String.valueOf(year);
				this.yearList.add(new BaseOption(strYear, strYear+"年"));
			}
		}
		return this.yearList;
	}
	
	
}
