package iace.action;

import iace.entity.activity.PopularActivity;
import iace.service.ServiceFactory;
import iace.service.activity.PopularActivityService;

public class PopularActivityAction extends BaseIaceAction {
	private static final long serialVersionUID = -4743494482790169370L;
	private PopularActivityService popularActivityService = ServiceFactory.getPopularActivityService();

	public PopularActivityAction() {
		super.setEntityClass(PopularActivity.class);
		super.setTitle("HOT20");
	}

	public String reflashMonthlyPopularActivity() {
		try {
			this.popularActivityService.reflashMonthlyPopularActivity();
			super.addActionMessage("HOT20更新完成!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String init() {
		//TODO hot20初始進入點，需列出現有置頂的活動人培
		return null;
	}
	
	public String index() {
		//TODO 進入activity的搜尋畫面，用來勾選需要置頂的activity
		return null;
	}
	
	public String createSubmit() {
		//TODO 將勾選的activity加入置頂的hot20，然後回到init()的頁面顯示所有置頂的activity
		return null;
	}
}
