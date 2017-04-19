package iace.dao.activity;

import java.util.List;

import core.util.PagedList;
import iace.dao.IBaseIaceDao;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;

public interface IActivityDao extends IBaseIaceDao<Activity> {
	public PagedList<Activity> searchBy(ActivitySearchModel arg);
	
	public long queryTotalRecordsCount(ActivitySearchModel arg);
	
	public List<Activity> sampleForHomePage();
	
	public List<Activity> sampleForEpaper();
	
	/**
	 * Get most popular activities
	 * @param resultNum 
	 * @return
	 */
//	public List<Activity> popular(int resultNum);
//	
}
