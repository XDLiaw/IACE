package iace.entity.httpRequestLog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import iace.entity.BaseEntity;
import iace.entity.member.Member;

@Entity
@Table(name = "HTTP_REQUEST_LOG")
public class HttpRequestLog extends BaseEntity {
	private static final long serialVersionUID = -1206502073534210282L;
	
	private long id;
	
	private String namespace;
	private String actionName;
	
	private String httpRequestMethod;
	private String url;
	private String queryString;
	private String parameters;
	
	private String clientIP;
	
	private Member member;
	
	private String showDetailClassName;
	private Long showDetailId;
	
	@Id
	@Column(name = "ID", length = 19, unique = true, nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HTTP_REQUEST_LOG_ID")
	@SequenceGenerator(name = "SEQ_HTTP_REQUEST_LOG_ID", sequenceName = "SEQ_HTTP_REQUEST_LOG_ID", allocationSize = 1, initialValue = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "NAMESPACE")
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Column(name = "ACTION_NAME")
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Column(name = "HTTP_REQUEST_METHOD")
	public String getHttpRequestMethod() {
		return httpRequestMethod;
	}

	public void setHttpRequestMethod(String httpRequestMethod) {
		this.httpRequestMethod = httpRequestMethod;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "QUERY_STRING")
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@Transient
	public String getFullUrl() {
		return this.url + (this.queryString==null ? "" : ("?" + this.queryString));
	}
	
	@Column(name = "PARAMETERS")
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Column(name = "CLIENT_IP")
	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	@ManyToOne
	@JoinColumn(name="MEMBER_ID", referencedColumnName= "ID")
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Column(name="SHOW_DETAIL_CLASS_NAME")
	public String getShowDetailClassName() {
		return showDetailClassName;
	}

	public void setShowDetailClassName(String showDetailClassName) {
		this.showDetailClassName = showDetailClassName;
	}

	@Column(name="SHOW_DETAIL_ID")
	public Long getShowDetailId() {
		return showDetailId;
	}

	public void setShowDetailId(Long showDetailId) {
		this.showDetailId = showDetailId;
	}

	@Override
	public String toString() {
		return "FrontendViewLog [id=" + id + ", namespace=" + namespace + ", actionName=" + actionName + ", httpRequestMethod=" + httpRequestMethod + ", url=" + url + ", queryString=" + queryString + ", parameters=" + parameters + ", clientIP=" + clientIP + ", member=" + member + "]";
	}

	
}
