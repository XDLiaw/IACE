package iace.dao.activity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import core.dao.HibernateSessionFactory;
import iace.dao.BaseIaceDao;
import iace.entity.BaseEntity;
import iace.entity.activity.PopularActivity;

public class PopularActivityDao extends BaseIaceDao<PopularActivity> implements IPopularActivityDao {

	public PopularActivityDao() {
		super(PopularActivity.class);
	}

	@Override
	protected List<Order> getDefaultOrderList() {
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.desc("pinned"));
		orderList.add(Order.asc("priority"));
		return orderList;
	}
	
	@Override
	public List<PopularActivity> listAllPinned() {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(super.entityClass);
			criteria.add(Restrictions.eq("pinned", true));
			criteria.add(Restrictions.eq("isValid", BaseEntity.TRUE));
			criteria.addOrder(Order.asc("priority"));
			@SuppressWarnings("unchecked")
			List<PopularActivity> res = criteria.list();
			return res;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	@Override
	public void deleteAllNotPinned() {
		Transaction tran = null;
		try {
			Session session = HibernateSessionFactory.getSession();
			tran = session.beginTransaction();
			Query query = session.createQuery("delete from "+super.entityClass.getSimpleName()+" where pinned = :pinned");
			query.setBoolean("pinned", false);
			query.executeUpdate();
			tran.commit();
		} catch (HibernateException e) {
			if (tran != null) {
				tran.rollback();
			}
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}
	
	@Override
	public void deleteAllPinned() {
		Transaction tran = null;
		try {
			Session session = HibernateSessionFactory.getSession();
			tran = session.beginTransaction();
			Query query = session.createQuery("delete from "+super.entityClass.getSimpleName()+" where pinned = :pinned");
			query.setBoolean("pinned", true);
			query.executeUpdate();
			tran.commit();
		} catch (HibernateException e) {
			if (tran != null) {
				tran.rollback();
			}
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

}
