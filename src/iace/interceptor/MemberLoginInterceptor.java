package iace.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import iace.entity.member.Member;

public class MemberLoginInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = -3809884103890574475L;
	protected static Logger log = LogManager.getLogger(AbstractInterceptor.class);

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		ActionContext actionContext = arg0.getInvocationContext();
		Map<String, Object> sessionMap = actionContext.getSession();
		Member member = (Member) sessionMap.get(SessionInterceptor.SESSION_KEY_MEMBER);
		if (member == null) {
			ActionSupport action = (ActionSupport) arg0.getAction();
			action.addActionMessage("您尚未登入會員!");
			 // 得到HttpServletRequest
            HttpServletRequest req = ServletActionContext.getRequest();
            // 得到完整的url
            String path=req.getRequestURL().toString();
            //String path = req.getRequestURI().substring(9);
            // 得到參數
            String queryString = req.getQueryString();
            // nullpointer
            if (queryString == null) {
                queryString = "";
            }
            String realPath = path + "?" + queryString;
            // 放進sessionMap
            sessionMap.put("urlBeforeLogin", realPath);
			return Action.LOGIN;
		} else {
			log.debug("Member login : "+member.getAccount());
			return arg0.invoke();
		}
	}

}
