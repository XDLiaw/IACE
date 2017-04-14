package iace.dao.httpRequestLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import core.dao.HibernateSessionFactory;
import iace.dao.BaseIaceDao;
import iace.entity.BaseEntity;
import iace.entity.about.About;
import iace.entity.activity.Activity;
import iace.entity.coopExample.CoopEx;
import iace.entity.httpRequestLog.HttpRequestLog;
import iace.entity.incubationCenter.IncubationCenter;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.literature.Literature;
import iace.entity.news.News;
import iace.entity.patent.Patent;
import iace.entity.rdFocus.RdFocus;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.videosArea.VideosArea;

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
			String hql = "SELECT HRL.showDetailId FROM " + HttpRequestLog.class.getSimpleName() + " HRL "
					+ "WHERE HRL.showDetailClassName = :cls " + "AND HRL.createTime > :start "
					+ "AND HRL.createTime < :end " + "AND HRL.isValid = :isValid " + "AND HRL.showDetailId IS NOT NULL "
					+ "GROUP BY HRL.showDetailId " + "ORDER BY count(HRL.id) desc";
			Query query = session.createQuery(hql);
			query.setString("cls", Activity.class.getName());
			query.setDate("start", start);
			query.setDate("end", end);
			query.setString("isValid", BaseEntity.TRUE);
			query.setMaxResults(maxResultNum);
			@SuppressWarnings("unchecked")
			List<Long> idList = query.list();
			return idList;
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateShowDetailId() {
		Transaction tran = null;
		try {
			Session session = HibernateSessionFactory.getSession();
			tran = session.beginTransaction();
			Query query = session.createQuery(
					"from HttpRequestLog a where ((a.queryString like '%id%') or (a.parameters like '%id%'))");
			List<HttpRequestLog> httpRequestLogList = query.list();
			for (HttpRequestLog h : httpRequestLogList) {
				if (h.getQueryString() != null && h.getQueryString().contains("id=") && (h.getId() != 10076)) {
					String temp = h.getQueryString();
					temp = temp.substring(temp.indexOf("id=") + 3);// 拿到id=之後的字串
					String[] AfterSplit = temp.split("&");// 切到&
					h.setShowDetailId(Long.parseLong(AfterSplit[0]));
				} else if (h.getParameters() != null && h.getParameters().contains("\"id\":[\"")) {
					String temp = h.getParameters();
					temp = temp.substring(temp.indexOf("\"id\":[\"") + 7);// 拿到id=之後的字串
					String[] AfterSplit = temp.split("\"]");// 切到&
					h.setShowDetailId(Long.parseLong(AfterSplit[0]));// 轉乘long丟入
				} else {
				}
			}
			tran.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateShowDetailClass() {
		Transaction tran = null;
		try {
			Session session = HibernateSessionFactory.getSession();
			tran = session.beginTransaction();
			Query query = session.createQuery(
					"from HttpRequestLog a where  a.actionName = 'showDetail')");
			List<HttpRequestLog> httpRequestLogList = query.list();
			
			for (HttpRequestLog h : httpRequestLogList) {
				
				if("showDetail".equals(h.getActionName())){
					if("/f2/about".equals(h.getNamespace())){
						h.setShowDetailClassName(About.class.getName());
					}
					else if("/f2/activity".equals(h.getNamespace())){
						h.setShowDetailClassName(Activity.class.getName());
				    }
					else if("/f2/coopEx".equals(h.getNamespace())){
						h.setShowDetailClassName(CoopEx.class.getName());
				    }
					else if("/f2/incubationCenter".equals(h.getNamespace())){
						h.setShowDetailClassName(IncubationCenter.class.getName());
				    }
					else if("/f2/industryInfo".equals(h.getNamespace())){
				    	h.setShowDetailClassName(IndustryInfo.class.getName());
				    }
					else if("/f2/literature".equals(h.getNamespace())){
						h.setShowDetailClassName(Literature.class.getName());
				    }
					else if("/f2/news".equals(h.getNamespace())){
						h.setShowDetailClassName(News.class.getName());
				    }
					else if("/f2/patent".equals(h.getNamespace())){
						h.setShowDetailClassName(Patent.class.getName());
				    }
					else if("/f2/policy".equals(h.getNamespace())){
						h.setShowDetailClassName(Literature.class.getName());
				    }
					else if("/f2/rdFocus".equals(h.getNamespace())){
						h.setShowDetailClassName(RdFocus.class.getName());
				    }
					else if("/f2/researchPlan".equals(h.getNamespace())){
						h.setShowDetailClassName(ResearchPlan.class.getName());
				    }
					else if("/f2/talentedPeople".equals(h.getNamespace())){
						h.setShowDetailClassName(TalentedPeople.class.getName());				    }
					else if("/f2/videosArea".equals(h.getNamespace())){
				    	h.setShowDetailClassName(VideosArea.class.getName());
				    }
				}				
			}
			
			tran.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			HibernateSessionFactory.closeSession();
		}
		
	}

}
