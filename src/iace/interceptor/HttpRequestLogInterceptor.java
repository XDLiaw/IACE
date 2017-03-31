package iace.interceptor;

import java.net.URLDecoder;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import iace.action.BaseIaceAction;
import iace.entity.httpRequestLog.HttpRequestLog;
import iace.entity.member.Member;
import iace.service.ServiceFactory;
import iace.service.httpRequestLog.HttpRequestLogService;

public class HttpRequestLogInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 3856585448131138500L;
	protected static Logger log = LogManager.getLogger(AbstractInterceptor.class);

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		try {
			return arg0.invoke();
		} catch (Exception e) {
			throw e;
		} finally {
			createHttpRequestLogToDb(arg0, currentTime);
		}
	}

	private void createHttpRequestLogToDb(ActionInvocation arg0, Timestamp createTime) {
		ActionContext actionContext = arg0.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		HttpRequestLog viewLog = new HttpRequestLog();

		try {
			viewLog.setNamespace(arg0.getProxy().getNamespace());
			viewLog.setActionName(arg0.getProxy().getActionName());
			viewLog.setHttpRequestMethod(request.getMethod());
			viewLog.setUrl(request.getRequestURL().toString());
			if (StringUtils.isNotBlank(request.getQueryString())) {
				viewLog.setQueryString(URLDecoder.decode(request.getQueryString(), "UTF-8"));
			}
			viewLog.setParameters(new Gson().toJson(actionContext.getParameters()));
			viewLog.setClientIP(request.getRemoteAddr());
			viewLog.setMember((Member) actionContext.getSession().get(SessionInterceptor.SESSION_KEY_MEMBER));
			viewLog.setCreateTime(createTime);
			viewLog.setUpdateTime(createTime);

			if ("showDetail".equals(viewLog.getActionName())) {
				BaseIaceAction action = (BaseIaceAction) arg0.getAction();
				Long showDetailId = Long.valueOf(request.getParameter("id"));
				Class<?> showDetailClass = action.getEntityClass();
				if (showDetailClass != null && showDetailId != null) {
					viewLog.setShowDetailClassName(showDetailClass.getName());
					viewLog.setShowDetailId(showDetailId);
				}
			}

			HttpRequestLogService service = ServiceFactory.getHttpRequestLogService();
			service.create(viewLog);
			log.debug(viewLog);
		} catch (Exception e) {
			log.warn("LOG插入資料庫時發生錯誤，並未記錄 " + viewLog.toString(), e);
		}
	}

}
