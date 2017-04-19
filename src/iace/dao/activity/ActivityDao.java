package iace.dao.activity;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import core.dao.HibernateSessionFactory;
import core.util.PagedList;
import iace.dao.BaseIaceDao;
import iace.entity.BaseEntity;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;

public class ActivityDao extends BaseIaceDao<Activity> implements IActivityDao {

	public ActivityDao() {
		super(Activity.class);
	}
	
	@Override
	public Activity get(long id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(super.entityClass);
			criteria.add(Restrictions.eq("id", id));
			criteria.add(Restrictions.eq("isValid", BaseEntity.TRUE));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			Activity entity = (Activity) criteria.uniqueResult();
			Hibernate.initialize(entity);
			Hibernate.initialize(entity.getAttachList());
			Hibernate.initialize(entity.getVideoList());
			
			return entity;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}
	
	@Override
	protected List<Order> getDefaultOrderList() {
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.desc("sort"));
		orderList.add(Order.desc("updateTime"));
		return orderList;
	}

	@Override
	public PagedList<Activity> searchBy(ActivitySearchModel arg) {
		long totalItemCount = queryTotalRecordsCount(arg);
		PagedList<Activity> results = new PagedList<Activity>(totalItemCount, arg.getPageSize(), arg.getPageIndex());
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(super.entityClass);
			addCriteriaRestrictionsForSearch(arg, criteria);
			super.addDefaultOrder(criteria);
			
			criteria.setFirstResult(results.getItemStart()-1);
			criteria.setMaxResults(arg.getPageSize());
			
			@SuppressWarnings("unchecked")
			List<Activity> list = criteria.list();
			results.setList(list);
			return results;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	@Override
	public long queryTotalRecordsCount(ActivitySearchModel arg) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(super.entityClass);
			addCriteriaRestrictionsForSearch(arg, criteria);

			criteria.setProjection(Projections.rowCount());
			Object count = criteria.uniqueResult();
			return (long) count;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	private void addCriteriaRestrictionsForSearch(ActivitySearchModel arg, Criteria criteria) {
		if (StringUtils.isNotBlank(arg.getSearchText())) {
			criteria.add(Restrictions.like("title", arg.getSearchText(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (StringUtils.isNotBlank(arg.getCategory())) {
			criteria.add(Restrictions.eq("category", arg.getCategory()));
		}
		if (arg.getYear() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			try {
				Date dateS = new Date( sdf.parse(arg.getYear().toString()).getTime() );
				Date dateE = new Date( sdf.parse(String.valueOf(arg.getYear()+1)).getTime() );
				criteria.add(Restrictions.ge("postDate", dateS));
				criteria.add(Restrictions.lt("postDate", dateE));
			} catch (ParseException e) {
				log.warn("", e);
			}
		}
		criteria.add(Restrictions.eq("isValid", BaseEntity.TRUE));
	}

	@Override
	public List<Activity> sampleForHomePage() {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(super.entityClass);
			criteria.add(Restrictions.eq("homeDisplayStatus", true));
			criteria.add(Restrictions.eq("isValid", BaseEntity.TRUE));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("id"), "id");
			projectionList.add(Projections.property("category"), "category");
			projectionList.add(Projections.property("title"), "title");
			projectionList.add(Projections.property("postDate"), "postDate");
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers.aliasToBean(super.entityClass));
			
			super.addDefaultOrder(criteria);
			criteria.setMaxResults(5);
			
			@SuppressWarnings("unchecked")
			List<Activity> list = criteria.list();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}
	
	

	@Override
	public List<Activity> sampleForEpaper() {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(super.entityClass);
			criteria.add(Restrictions.eq("isValid", BaseEntity.TRUE));
			super.addDefaultOrder(criteria);
			criteria.setMaxResults(2);
			
			@SuppressWarnings("unchecked")
			List<Activity> list = criteria.list();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

//	@Override
//	public List<Activity> popular(int resultNum) {
//		try {
//			Session session = HibernateSessionFactory.getSession();
//			Criteria criteria = session.createCriteria(super.entityClass);
//			
//			ProjectionList projectionList = Projections.projectionList();
//			projectionList.add(Projections.property("id"), "id");
//			projectionList.add(Projections.property("category"), "category");
//			projectionList.add(Projections.property("title"), "title");
//			projectionList.add(Projections.property("postDate"), "postDate");
//			criteria.setProjection(projectionList);
//			criteria.setResultTransformer(Transformers.aliasToBean(super.entityClass));
//			
//			criteria.addOrder(Order.desc("clickNum"));
//			criteria.setMaxResults(resultNum);
//			
//			@SuppressWarnings("unchecked")
//			List<Activity> list = criteria.list();
//			return list;
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			HibernateSessionFactory.closeSession();
//		}
//	}
	
}
