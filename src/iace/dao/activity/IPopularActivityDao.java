package iace.dao.activity;

import java.util.List;

import iace.dao.IBaseIaceDao;
import iace.entity.activity.PopularActivity;

public interface IPopularActivityDao extends IBaseIaceDao<PopularActivity> {
	/**
	 * 列出所有置頂的活動人培
	 * @return
	 */
	public List<PopularActivity> listAllPinned();
	
	/**
	 * 刪除非置頂的活動人培
	 */
	public void deleteAllNotPinned();
}
