package iace.action;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.JDBCConnectionException;

import core.util.AESEncrypter;
import core.util.ExcelUtil;
import iace.entity.option.OptionSchool;
import iace.entity.qnrCooperateWay.QnrCooperateWay;
import iace.service.ServiceFactory;
import iace.service.option.OptionSchoolService;
import iace.service.qnrCooperateWay.QnrCooperateWayExcelService;
import iace.service.qnrCooperateWay.QnrCooperateWayService;

public class QnrCooperateWayAction extends BaseIaceAction {

	private static final long serialVersionUID = -8674132276568056185L;

	private OptionSchoolService schoolService = ServiceFactory.getOptionSchoolService(); 
	private QnrCooperateWayService qnrCooperateWayService = ServiceFactory.getQnrCooperateWayService();
	private QnrCooperateWayExcelService excelService = ServiceFactory.getQnrCooperateWayExcelService();

	private long schoolId;
	private String encryptSchoolId;
	private long qnrCooperateWayId;
	private QnrCooperateWay qnrCoopereateWay;
	
	private String qnrExcelFileName;
	private InputStream qrnExcelFileInputStream;
	
	public QnrCooperateWayAction() {
		super.setEntityClass(QnrCooperateWay.class);
		super.setTitle("精進大學產學合作發展機制調查問卷");
	}

	public String index() {
		try {
			OptionSchool school = this.schoolService.getByCode("TEST");
			this.encryptSchoolId = school.getUrlEncodeId();
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String downloadQnrLinksExcel() {
		try {
			List<OptionSchool> schools = this.schoolService.listAll();
			
			String currentUrl = ServletActionContext.getRequest().getRequestURL().toString();
			String qnrUrl = currentUrl.substring(0, currentUrl.lastIndexOf("/")) + "/fillInQnr";
			
			XSSFWorkbook wb = this.excelService.exportQnrLinksExcel(schools, qnrUrl);
			this.qrnExcelFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.qnrExcelFileName = "問卷連結.xlsx";
			this.qnrExcelFileName = new String(this.qnrExcelFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String downloadUnfillQnrLinksExcel() {
		try {
			List<OptionSchool> schools = this.schoolService.listUnfillQnrCooperateWay();
			
			String currentUrl = ServletActionContext.getRequest().getRequestURL().toString();
			String qnrUrl = currentUrl.substring(0, currentUrl.lastIndexOf("/")) + "/fillInQnr";
			
			XSSFWorkbook wb = this.excelService.exportQnrLinksExcel(schools, qnrUrl);
			this.qrnExcelFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.qnrExcelFileName = "問卷連結.xlsx";
			this.qnrExcelFileName = new String(this.qnrExcelFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String downloadQnrResultExcel() {
		try {
			List<QnrCooperateWay> qnrList = this.qnrCooperateWayService.listAll();
			XSSFWorkbook wb = this.excelService.exportQnrResulotExcel(qnrList);
			this.qrnExcelFileInputStream = ExcelUtil.workbookToInputStream(wb);
			this.qnrExcelFileName = "前三部分問卷結果.xlsx";
			this.qnrExcelFileName = new String(this.qnrExcelFileName.getBytes(), "ISO-8859-1"); // 解決中文檔名瀏覽器無法正常顯示問題
			
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String fillInQnr() {
		try {
			String decryptUrlId = URLDecoder.decode(this.encryptSchoolId, "UTF-8");
			decryptUrlId = decryptUrlId.replace(" ", "+");// after decode, "+" will become white-space, so need to change it back to "+"
			this.schoolId = Long.valueOf(AESEncrypter.decrypt(AESEncrypter.KEY, decryptUrlId));	
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateFillInQnrSubmit() {
		validateApplicantData();
	}
	
	public String fillInQnrSubmit() {
		try {
			OptionSchool school = this.schoolService.get(this.schoolId);
			this.qnrCoopereateWay.setSchool(school);
			for (int i=0; i<3; i++) {
				try {
					this.qnrCooperateWayService.create(this.qnrCoopereateWay);
					break;
				} catch (JDBCConnectionException e) {
					if (StringUtils.contains(e.getMessage(), "Unable to release JDBC Connection")) {
						log.warn("", e);
						Thread.sleep(1000);
						continue;
					} else {
						throw e;
					}
				}
			}
			
			return SUCCESS;	
		} catch (JDBCConnectionException e) {
			if (StringUtils.contains(e.getMessage(), "Unable to release JDBC Connection")) {
				String msg = "因為停滯時間過長，造成系統連結中斷，填答內容無法寫入資料庫。煩請點選信件中之[問卷連結]並重新填答問卷。造成不便，敬請見諒！";
				super.addActionError(msg);
				return ERROR;
			} else {
				super.showExceptionToPage(e);
				return ERROR;
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateApplicantData() {
		boolean isAllValid = true;
		if (this.qnrCoopereateWay.getAggreePDPL()) {
			isAllValid = super.validateNotBlankNLength(this.qnrCoopereateWay.getName(), 20, "qnrCoopereateWay.name") && isAllValid;
			if (super.validateNotBlankNLength(this.qnrCoopereateWay.getEmail(), 100, "qnrCoopereateWay.email")) {
				isAllValid = super.validateEmail(this.qnrCoopereateWay.getEmail(), "qnrCoopereateWay.email") && isAllValid;
			} else {
				isAllValid = false;
			}
			isAllValid = super.validateNotBlankNLength(this.qnrCoopereateWay.getAddress(), 200, "qnrCoopereateWay.address") && isAllValid;
		}
		
		if (!isAllValid) {
			this.addActionError("部分資料有問題，請重新檢查!");
		}
	}

	// =========================================================================
	
	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public String getEncryptSchoolId() {
		return encryptSchoolId;
	}

	public void setEncryptSchoolId(String encryptSchoolId) {
		this.encryptSchoolId = encryptSchoolId;
	}

	public long getQnrCooperateWayId() {
		return qnrCooperateWayId;
	}

	public void setQnrCooperateWayId(long qnrCooperateWayId) {
		this.qnrCooperateWayId = qnrCooperateWayId;
	}

	public QnrCooperateWay getQnrCoopereateWay() {
		return qnrCoopereateWay;
	}

	public void setQnrCoopereateWay(QnrCooperateWay qnrCoopereateWay) {
		this.qnrCoopereateWay = qnrCoopereateWay;
	}

	public String getQnrExcelFileName() {
		return qnrExcelFileName;
	}

	public void setQnrExcelFileName(String qnrExcelFileName) {
		this.qnrExcelFileName = qnrExcelFileName;
	}

	public InputStream getQrnExcelFileInputStream() {
		return qrnExcelFileInputStream;
	}

	public void setQrnExcelFileInputStream(InputStream qrnExcelFileInputStream) {
		this.qrnExcelFileInputStream = qrnExcelFileInputStream;
	}


}


