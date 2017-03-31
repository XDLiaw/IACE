package iace.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import iace.action.BaseIaceAction;
import iace.dao.DaoFactory;

public class ClickCountInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 3228274952133915197L;
	protected static Logger log = LogManager.getLogger(AbstractInterceptor.class);
	
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		try {
			return arg0.invoke();
		} catch (Exception e) {
			throw e;
		} finally {
			increaseClickNum(arg0);
		}
	}
	
	private void increaseClickNum(ActionInvocation arg0) {
		ActionContext actionContext = arg0.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		if ("showDetail".equals(arg0.getProxy().getActionName())) {
			BaseIaceAction action = (BaseIaceAction) arg0.getAction();
			Long id = Long.valueOf(request.getParameter("id"));
			Class<?> cls = action.getEntityClass();
			try {
				DaoFactory.getClickNumCounterDao().increaseClickNum(id, cls);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}
