package iace.action;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import core.dao.HibernateSessionFactory;
import core.util.PagedList;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;
import iace.entity.activity.PopularActivity;
import iace.entity.httpRequestLog.HttpRequestLog;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.activity.ActivityService;
import iace.service.activity.PopularActivityService;

public class PopularActivityAction extends BaseIaceAction {
	private static final long serialVersionUID = -4743494482790169370L;
	private PopularActivityService popularActivityService = ServiceFactory.getPopularActivityService();
	private ActivityService activityService = ServiceFactory.getActivityService();
	private ActivitySearchModel searchCondition = new ActivitySearchModel();
	
	private List<Activity> activitydList;              //byAllSearchActivity
	private PagedList<Activity> activityPagedList;
	private List<PopularActivity> popularActivityList; //byChoose

	private Long id;
	private Activity activity;
	private List<BaseOption> categoryList = Activity.getCategoryList();

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
		// TODO hot20初始進入點，需列出現有置頂的活動人培
		try {
			//this.activitydList = this.popularActivityService.hot20Pinned();
			this.activityPagedList = this.activityService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
		
	}

	public String index() {
		//TODO 進入activity的搜尋畫面，用來勾選需要置頂的activity
		try {
			this.activityPagedList = this.activityService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String createSubmit() {
		//TODO 將勾選的activity加入置頂的hot20，然後回到init()的頁面顯示所有置頂的activity
		try {
			this.popularActivityService.updatePinned();
			//-----------此行增加jsp有被勾選的activity
			this.addActionMessage("CREATE SUCCESS!");
			return init();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String updateHttpRequestLog() {
		// --用來新增DB裡HTTP_REQUEST_LOG的欄位所屬SHOW_DETAIL_CLASS_NAME與SHOW_DETAIL_ID
		// --以QUERY_STRING或PARAMETERS來找到id 以NAMESPACE和ACTION_NAME找到class
		this.popularActivityService.reflashShowDetail();		
		return SUCCESS;
	}
}
