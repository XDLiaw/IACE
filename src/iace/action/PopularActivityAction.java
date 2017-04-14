package iace.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import core.util.AESEncrypter;
import core.util.PagedList;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;
import iace.entity.activity.PopularActivity;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.activity.ActivityService;
import iace.service.activity.PopularActivityService;

public class PopularActivityAction extends BaseIaceAction {
	private static final long serialVersionUID = -4743494482790169370L;
	private PopularActivityService popularActivityService = ServiceFactory.getPopularActivityService();
	private ActivityService activityService = ServiceFactory.getActivityService();
	private ActivitySearchModel searchCondition = new ActivitySearchModel();
	
	private List<Activity> activityList;              //byAllSearchActivity
	private PagedList<Activity> activityPagedList;
	private List<PopularActivity> popularActivityList;
    private Set<Long> activityIds=new HashSet<Long>(); //byChoose to create jsp
    private List<Float> prioritys;

	private Long id;//for activity
	private Float priority;
	private Activity activity;
	private PopularActivity popularActivity;
	private List<BaseOption> categoryList = Activity.getCategoryList();
	private String keyForSyncData;

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

	public String reflashMonthlyPopularActivityWithoutLogin() {
		try {
			String decryptKey = AESEncrypter.decrypt("iace@!QAZ", this.keyForSyncData);
			if ("sysvin".equals(decryptKey) == false) {
				throw new Exception("key值錯誤");
			} else {
				return reflashMonthlyPopularActivity();
			}
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String init() {
		// TODO hot20初始進入點，需列出現有置頂的活動人培
		try {
			this.activityList = this.popularActivityService.hot20Pinned();//給出hot20
			this.popularActivityList=this.popularActivityService.getPinnedPopularActivityList();
			Set<Long> activityIds = new HashSet<Long>();
			for (Activity a : this.getActivityList()) {
				activityIds.add(a.getId());
			}
			this.setActivityIds(activityIds);
		    //從activityList抓出ID紀錄
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String index() {
		//TODO 進入activity的搜尋畫面，用來勾選需要置頂的activity
		try {
			this.activityPagedList=activityService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}

	public String indexSubmit() {
		//TODO 將勾選的activity加入置頂的hot20，然後回到init()的頁面顯示所有置頂的activity
		try {
			if(activityIds.size()>0){
			    this.popularActivityService.updatePinned(activityIds);
			}
			//-----------此行增加jsp有被勾選的activity
			this.addActionMessage("確認更改!");
			return init();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update(){
		//即時更新pinned
		try {
			this.popularActivity = this.popularActivityService.get(id);
			if (this.popularActivity == null) {
				super.addActionError("沒有對應的資料");
			}
			
			this.popularActivity.setPriority(this.priority);
			this.popularActivityService.update(this.popularActivity);
			return init();
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete(){
		//即時更新pinned
		try {
			this.popularActivity = this.popularActivityService.get(id);
			if (this.popularActivity == null) {
				super.addActionError("沒有對應的資料");
			}
			this.popularActivityService.delete(this.popularActivity);
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
	
	//=========================================================================================================
	
	
	public ActivitySearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(ActivitySearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public List<BaseOption> getCategoryList() {
		return categoryList;
	}

	public PagedList<Activity> getActivityPagedList() {
		return activityPagedList;
	}

	public Set<Long> getActivityIds() {
		return activityIds;
	}

	public void setActivityIds(Set<Long> activityIds) {
		this.activityIds = activityIds;
	}
		
	public List<Float> getPrioritys() {
		return prioritys;
	}

	public void setPrioritys(List<Float> prioritys) {
		this.prioritys = prioritys;
	}
	
	public List<PopularActivity> getPopularActivityList() {
		return popularActivityList;
	}

	public void setPopularActivityList(List<PopularActivity> popularActivityList) {
		this.popularActivityList = popularActivityList;
	}

	public Float getPriority() {
		return priority;
	}

	public void setPriority(Float priority) {
		this.priority = priority;
	}
	

	public String getKeyForSyncData() {
		return keyForSyncData;
	}

	public void setKeyForSyncData(String keyForSyncData) {
		this.keyForSyncData = keyForSyncData;
	}
}
