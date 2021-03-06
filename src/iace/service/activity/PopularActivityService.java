package iace.service.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import iace.dao.activity.IActivityDao;
import iace.dao.activity.IPopularActivityDao;
import iace.dao.httpRequestLog.IHttpRequestLogDao;
import iace.entity.activity.Activity;
import iace.entity.activity.PopularActivity;
import iace.service.BaseIaceService;

public class PopularActivityService extends BaseIaceService<PopularActivity> {
	
	private IPopularActivityDao popularActivityDao;
	private IActivityDao activityDao;
	private IHttpRequestLogDao httpRequestLogDao;

	public PopularActivityService(IPopularActivityDao popularActivityDao, IActivityDao activityDao, IHttpRequestLogDao httpRequestLogDao) {
		super(popularActivityDao);
		this.popularActivityDao = popularActivityDao;
		this.activityDao = activityDao;
		this.httpRequestLogDao = httpRequestLogDao;
	}

	public void reflashMonthlyPopularActivity() {
		this.popularActivityDao.deleteAllNotPinned();
		
		Date end = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(end);
		c.add(Calendar.DATE, -30);
		Date start = c.getTime();
		List<Long> popularActivityIds = this.httpRequestLogDao.getPopularActivityId(20, start, end);
		for (int i=0; i<popularActivityIds.size(); i++) {
			Long id = popularActivityIds.get(i);
			PopularActivity pa = new PopularActivity();
			pa.setActivityId(id);
			pa.setPinned(false);
			pa.setPriority((float)i+1);
			this.popularActivityDao.create(pa);
		}
	}
	
	public List<Activity> hot20() {
		List<PopularActivity> pas = this.popularActivityDao.listAll();
		List<Long> ids = new ArrayList<Long>();
		for (PopularActivity pa : pas) {
			if (ids.contains(pa.getActivityId()) == false) {
				ids.add(pa.getActivityId());
			}
			if (ids.size() == 20) {
				break;
			}
		}
		List<Activity> res = new ArrayList<Activity>();
		for (Long id : ids) {
			Activity a = this.activityDao.get(id);
			res.add(a);
		}
		return res;
	}
}
