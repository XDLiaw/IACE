package iace.action;

import java.util.List;

import core.util.PagedList;
import iace.entity.faq.Faq;
import iace.entity.faq.FaqSearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.faq.FaqService;

public class FaqAction extends BaseIaceAction {
	private static final long serialVersionUID = 718258371598631328L;

	private FaqService faqService = ServiceFactory.getFaqService();
	
	private FaqSearchModel searchCondition = new FaqSearchModel();
	private PagedList<Faq> faqPagedList;
	
	private long id;
	private Faq faq;
	
	private List<BaseOption> categoryList = Faq.getCategoryList();
	
	public FaqAction() {
		super.setTitle("常問集");
	}
	
	public String init() {
		return index();
	}

	public String index() {
		try {
			this.faqPagedList = this.faqService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			this.faq = this.faqService.get(this.id);
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
			this.faqService.create(this.faq, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.faq = this.faqService.get(this.id);
			if (this.faq == null) {
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
			this.faqService.update(this.faq);
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.faq = this.faqService.get(id);
			if (this.faq == null) {
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
			this.faqService.delete(this.id, false, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.faq.getTitle(), 200, "faq.title");
		super.validateTextMaxLength(this.faq.getMetaTitle(), 200, "about.metaTitle");
	}
	
	//==========================================================================

	public FaqSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(FaqSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Faq getFaq() {
		return faq;
	}

	public void setFaq(Faq faq) {
		this.faq = faq;
	}

	public PagedList<Faq> getFaqPagedList() {
		return faqPagedList;
	}

	public List<BaseOption> getCategoryList() {
		return categoryList;
	}
	
}
