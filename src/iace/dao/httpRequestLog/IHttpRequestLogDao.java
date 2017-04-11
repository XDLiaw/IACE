package iace.dao.httpRequestLog;

import java.util.Date;
import java.util.List;

import iace.dao.IBaseIaceDao;
import iace.entity.httpRequestLog.HttpRequestLog;

public interface IHttpRequestLogDao extends IBaseIaceDao<HttpRequestLog> {
	public List<Long> getPopularActivityId(int maxResultNum, Date start, Date end);
	public void updateShowDetailId();
	public void updateShowDetailClass();
}
