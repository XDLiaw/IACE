package iace.action;

import java.util.List;

import iace.entity.marquee.Marquee;
import iace.service.ServiceFactory;
import iace.service.marquee.MarqueeService;

public class MarqueeAction extends BaseIaceAction {
	private static final long serialVersionUID = -8510728626029959991L;

	private MarqueeService marqueeService = ServiceFactory.getMarqueeService();
	
	private List<Marquee> marqueeList;
	
	private Long id;
	private Marquee marquee;
	
	public MarqueeAction() {
		super.setTitle("首頁跑馬燈");
	}

	public String index() {
		try {
			this.marqueeList = this.marqueeService.listAll();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			this.marquee = this.marqueeService.get(this.id);
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
			this.marqueeService.create(this.marquee, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		return showDetail();
	}
	
	public void validateUpdateSubmit() {
		validateBeforeSubmit();
	}
	
	public String updateSubmit() {
		try {
			this.marqueeService.update(this.marquee, super.getCurrentSysUser(), false, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String deleteSubmit() {
		try {
			this.marqueeService.delete(this.id);
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.marquee.getText(), 100, "marquee.text");
		super.validateTextMaxLength(this.marquee.getUrl(), 500, "marquee.url");
	}
	
	//==========================================================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Marquee getMarquee() {
		return marquee;
	}

	public void setMarquee(Marquee marquee) {
		this.marquee = marquee;
	}

	public List<Marquee> getMarqueeList() {
		return marqueeList;
	}
	
	
}
