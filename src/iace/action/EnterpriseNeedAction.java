package iace.action;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import core.util.ExcelUtil;
import core.util.PagedList;
import iace.entity.enterpriseNeed.EnterpriseInfo;
import iace.entity.enterpriseNeed.EnterpriseNeedSearchModel;
import iace.entity.option.OptionCompanyLocation;
import iace.entity.option.OptionCooperateMode;
import iace.entity.option.OptionHadTecSrc;
import iace.entity.option.OptionDomain;
import iace.service.ServiceFactory;
import iace.service.enterpriseNeed.EnterpriseInfoService;
import iace.service.option.OptionCompanyLocationService;
import iace.service.option.OptionCooperateModeService;
import iace.service.option.OptionHadTecSrcService;
import iace.service.option.OptionDomainService;

public class EnterpriseNeedAction extends BaseIaceAction {

	private static final long serialVersionUID = -4591959836078338744L;

	private EnterpriseInfoService enterpriseInfoService = ServiceFactory.getEnterpriseInfoService();
	private OptionDomainService optionDomainService = ServiceFactory.getOptionDomainService();
	private OptionCompanyLocationService optionCompanyLocationService = ServiceFactory.getOptionCompanyLocationService();
	private OptionHadTecSrcService optionHadTecSrcService = ServiceFactory.getOptionHadTecSrcService();
	private OptionCooperateModeService optionCooperateModeService = ServiceFactory.getOptionCooperateModeService();
	
	private List<OptionDomain> optionDomainList;
	private List<OptionCompanyLocation> optionCompanyLocationList;
	private List<OptionHadTecSrc> optionHadTecSrcList;
	private List<OptionCooperateMode> optionCooperateModeList;
	
	private Long id;
	private EnterpriseInfo enterpriseInfo;
	private EnterpriseNeedSearchModel searchCondition = new EnterpriseNeedSearchModel();
	private PagedList<EnterpriseInfo> enterpriseInfoPagedList;
	
	private String downloadFileName;
	private InputStream downloadFileInputStream;
	
	private String reportFileName;
	private InputStream reportInputStream;
	
	public EnterpriseNeedAction() {
		super.setEntityClass(EnterpriseInfo.class);
		super.setTitle("企業需求單");
	}
	
	public String init() {
		return SUCCESS;
	}
	
	public String index() {
		try {
			this.enterpriseInfoPagedList = this.enterpriseInfoService.searchBy(searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			this.enterpriseInfo = this.enterpriseInfoService.get(this.id);
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
			this.enterpriseInfoService.create(this.enterpriseInfo, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.enterpriseInfo = this.enterpriseInfoService.get(this.id);
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
			this.enterpriseInfoService.update(this.enterpriseInfo, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.enterpriseInfo = this.enterpriseInfoService.get(this.id);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String deleteSubmit() {
		try {
			this.enterpriseInfoService.delete(this.id, false, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS!");
			
			this.enterpriseInfoPagedList = this.enterpriseInfoService.searchBy(searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateBeforeSubmit() {
		//TODO
		double hadTecSrcRation = this.enterpriseInfo.getEnterpriseSituation().getHadTecSrcRation();
		validateNumberRange(hadTecSrcRation, 1, 0, "enterpriseInfo.enterpriseSituation.hadTecSrcRation");
	}
	
	public String exportRawData() {
		try {
			XSSFWorkbook wb = this.enterpriseInfoService.exportRawData(this.searchCondition);
			this.downloadFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.downloadFileName = "raw_data.xlsx";
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String printReport() {
		try {
			this.reportInputStream = this.enterpriseInfoService.printReport(this.id);
			this.reportFileName = "print.pdf";
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	//==========================================================================

	public EnterpriseInfo getEnterpriseInfo() {
		return enterpriseInfo;
	}

	public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
		this.enterpriseInfo = enterpriseInfo;
	}

	public List<OptionDomain> getOptionDomainList() {
		if (optionDomainList == null) {
			optionDomainList = this.optionDomainService.listAll();
		}
		return optionDomainList;
	}

	public List<OptionCompanyLocation> getOptionCompanyLocationList() {
		if (optionCompanyLocationList == null) {
			optionCompanyLocationList = this.optionCompanyLocationService.listAll();
		}
		return optionCompanyLocationList;
	}

	public List<OptionHadTecSrc> getOptionHadTecSrcList() {
		if (optionHadTecSrcList == null) {
			optionHadTecSrcList = this.optionHadTecSrcService.listAll();
		}
		return optionHadTecSrcList;
	}

	public List<OptionCooperateMode> getOptionCooperateModeList() {
		if (optionCooperateModeList == null) {
			optionCooperateModeList = this.optionCooperateModeService.listAll();
		}
		return optionCooperateModeList;
	}

	public EnterpriseNeedSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(EnterpriseNeedSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PagedList<EnterpriseInfo> getEnterpriseInfoPagedList() {
		return enterpriseInfoPagedList;
	}

	public void setEnterpriseInfoPagedList(PagedList<EnterpriseInfo> enterpriseInfoPagedList) {
		this.enterpriseInfoPagedList = enterpriseInfoPagedList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	
	
	
	
	
	
	
	
	

}
