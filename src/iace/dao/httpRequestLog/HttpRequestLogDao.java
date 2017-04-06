package iace.dao.httpRequestLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import core.dao.HibernateSessionFactory;
import iace.dao.BaseIaceDao;
import iace.entity.BaseEntity;
import iace.entity.activity.Activity;
import iace.entity.httpRequestLog.HttpRequestLog;

public class HttpRequestLogDao extends BaseIaceDao<HttpRequestLog> implements IHttpRequestLogDao {

	public HttpRequestLogDao() {
		super(HttpRequestLog.class);
	}

	@Override
	protected List<Order> getDefaultOrderList() {
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.desc("createTime"));
		return orderList;
	}

	@Override
	public List<Long> getPopularActivityId(int maxResultNum, Date start, Date end) {
		try {
			Session session = HibernateSessionFactory.getSession();
			String hql = "SELECT HRL.showDetailId FROM " + HttpRequestLog.class.getSimpleName() +" HRL "
					+ "WHERE HRL.showDetailClassName = :cls "
					+ "AND HRL.createTime > :start "
					+ "AND HRL.createTime < :end "
					+ "AND HRL.isValid = :isValid "
					+ "GROUP BY HRL.showDetailId "
					+ "ORDER BY count(hrl.id) desc";
			Query query = session.createQuery(hql);
			query.setString("cls", Activity.class.getName());
			query.setDate("start", start);
			query.setDate("end", end);
			query.setString("isValid", BaseEntity.TRUE);
			query.setMaxResults(maxResultNum);
			List<Long> idList = query.list();
			return idList;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}
}
