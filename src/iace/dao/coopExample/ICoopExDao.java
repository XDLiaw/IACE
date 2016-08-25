package iace.dao.coopExample;

import core.util.PagedList;
import iace.dao.IBaseIaceDao;
import iace.entity.coopExample.CoopEx;
import iace.entity.coopExample.CoopExSearchModel;

public interface ICoopExDao extends IBaseIaceDao<CoopEx> {
	
	public boolean isProjNameExist(long currentId, String projName);
	
	public PagedList<CoopEx> searchBy(CoopExSearchModel arg);
}
