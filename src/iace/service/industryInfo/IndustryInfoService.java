package iace.service.industryInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import core.util.PagedList;
import core.util.StringUtil;
import iace.dao.industryInfo.IIndustryInfoDao;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.industryInfo.IndustryInfoSearchModel;
import iace.entity.sys.SysLog;
import iace.entity.sys.SysUser;
import iace.service.BaseIaceService;
import iace.webservice.linkiacClient.IndustryInfoWSResult;
import iace.webservice.linkiacClient.LinkIacWSSoapProxy;

public class IndustryInfoService extends BaseIaceService<IndustryInfo> {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private IIndustryInfoDao dao;
	
	public IndustryInfoService(IIndustryInfoDao dao) {
		super(dao);
		this.dao = dao;
	}
	
	public PagedList<IndustryInfo> searchBy(IndustryInfoSearchModel arg) {
		return this.dao.searchBy(arg);
	}

	public List<IndustryInfo> sampleForHomePage(String category) {
		return this.dao.sampleForHomePage(category);
	}
	
	public List<IndustryInfo> sampleForEpaper() {
		return this.dao.sampleForEpaper();
	}
	
	public synchronized void syncData(SysUser user, boolean indexing, SysLog sysLog) throws DocumentException, ParseException, IOException, SQLException, org.apache.lucene.queryparser.classic.ParseException {
		LinkIacWSSoapProxy proxy = new LinkIacWSSoapProxy();
		//新聞雷達
		{
			int pageIndex = 1;
			IndustryInfoWSResult res;
			do {
				String parm = String.format("<root><wsusr>linkiac</wsusr><wspwd>0527f3a3</wspwd><page_idx>%d</page_idx></root>", pageIndex);
				String resXml = proxy.getPatentInfo(parm);
				res = convertToIndustryInfo(resXml, "新聞雷達");
				batchCreateOrUpdate(res.getIndustryInfoList(), user, indexing, sysLog);
				pageIndex++;
			} while (pageIndex <= Math.ceil((float)res.getTotalCount()/20));
		}
		//產業評析
		{
			int pageIndex = 1;
			IndustryInfoWSResult res;
			do {
				String parm = String.format("<root><wsusr>linkiac</wsusr><wspwd>0527f3a3</wspwd><page_idx>%d</page_idx></root>", pageIndex);
				String resXml = proxy.getIEKView(parm);
				res = convertToIndustryInfo(resXml, "產業評析");
				batchCreateOrUpdate(res.getIndustryInfoList(), user, indexing, sysLog);
				pageIndex++;
			} while (pageIndex <= Math.ceil((float)res.getTotalCount()/20));
		}
	}
	
	private void batchCreateOrUpdate(List<IndustryInfo> industryInfoList, SysUser user, boolean indexing, SysLog sysLog) throws IOException, SQLException, org.apache.lucene.queryparser.classic.ParseException {
		for (IndustryInfo newII : industryInfoList) {
			IndustryInfo oldII = this.dao.getByOid(newII.getOid());
			if (oldII == null) {
				super.create(newII, user, indexing, sysLog);
			} else {
				oldII.setCategory(newII.getCategory());
				oldII.setPostDate(newII.getPostDate());
				oldII.setTitle(newII.getTitle());
				oldII.setSource(newII.getSource());
				oldII.setLink(newII.getLink());
				super.update(oldII, user, indexing, sysLog);
			}
		}
	}
	
	private static IndustryInfoWSResult convertToIndustryInfo(String xmlText, String category) throws DocumentException, ParseException {
		IndustryInfoWSResult res = new IndustryInfoWSResult();
		
//		xmlText = StringEscapeUtils.unescapeHtml4(xmlText);
		Document document = DocumentHelper.parseText(xmlText);
		Element root = document.getRootElement();
		Attribute attr = root.attribute("status");
		res.setStatus(Boolean.parseBoolean(attr.getValue()));
		if (res.getStatus()) {
			Element eResult = root.element("Result");
			res.setTotalCount(Integer.valueOf(eResult.attribute("count").getValue()));
			
			@SuppressWarnings("unchecked")
			List<Element> eRecList = eResult.elements();
			for (Element eRec : eRecList) {
				IndustryInfo info = new IndustryInfo();
				info.setCategory(category);
				info.setOid(eRec.selectSingleNode("field[@name='id']").getText());
				Date time = sdf.parse(eRec.selectSingleNode("field[@name='time']").getText());
				info.setPostDate(new java.sql.Date(time.getTime()));
				String title = eRec.selectSingleNode("field[@name='title']").getText();
				info.setTitle(StringUtil.filterHtml(title));
				info.setSource(eRec.selectSingleNode("field[@name='author']").getText());
				info.setLink(eRec.selectSingleNode("field[@name='link']").getText());
				res.addIndustryInfoToList(info);
			}
		}
		
		return res;
	}
}
