package iace.service.ePaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import core.util.EmailUtil;
import core.util.PagedList;
import core.util.Validator;
import iace.dao.ePaper.IEPaperDao;
import iace.dao.ePaper.IEPaperSubscriberDao;
import iace.dao.member.IMemberDao;
import iace.dao.sys.ISysParameterDao;
import iace.entity.activity.Activity;
import iace.entity.ePaper.EPaper;
import iace.entity.ePaper.EPaperProduceTemplate;
import iace.entity.ePaper.EPaperSearchModel;
import iace.entity.faq.Faq;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.news.News;
import iace.entity.patent.Patent;
import iace.entity.rdFocus.RdFocus;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.sys.SysLog;
import iace.entity.sys.SysParameter;
import iace.entity.sys.SysUser;
import iace.service.BaseIaceService;

public class EPaperService extends BaseIaceService<EPaper> {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy 年 M 月 d 日");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

	private IEPaperDao dao;
	private IEPaperSubscriberDao subscriberDao;
	private IMemberDao memberDao;
	private ISysParameterDao sysParameterDao;
	
	private String epaperBackupFolder;
	
	public EPaperService(IEPaperDao dao, IEPaperSubscriberDao subscriberDao, IMemberDao memberDao, ISysParameterDao sysParameterDao) {
		super(dao);
		this.dao = dao;
		this.subscriberDao = subscriberDao;
		this.memberDao = memberDao;
		this.sysParameterDao = sysParameterDao;
		
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("configs/iace.properties"));
			this.epaperBackupFolder = prop.getProperty("epaperBackupFolder");
		} catch (IOException e) {
			log.fatal("", e);
		}
	}

	public PagedList<EPaper> searchBy(EPaperSearchModel arg) {
		return this.dao.searchBy(arg);
	}

	public void sendTestEmail(long id, String to, File emailToF) throws IOException, MessagingException {
		// get all email
		Set<String> emailSet = new HashSet<String>();
		String errMsg = "必須是有效的Email格式，並請檢查是否含有[空白]、[特殊符號]、[非英數字元]";
		if (StringUtils.isNotBlank(to)) {
			to = StringUtils.replace(to, "；", ";");
			for (String e : StringUtils.split(to, ";")) {
				if (Validator.isValidEmail(e) == false) {
					throw new IllegalArgumentException(errMsg);
				}
				emailSet.add(e.trim());
			}
		}
		if (emailToF != null) {
			// read email from uploadFile
			try (FileInputStream fis = new FileInputStream(emailToF);) {
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				XSSFSheet sheet = wb.getSheetAt(0);

				XSSFRow row;
				XSSFCell cell;

				for (int r = 1; r <= sheet.getLastRowNum(); r++) {
					int c = -1;
					row = sheet.getRow(r);
					cell = row.getCell(++c);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String cellValue = cell.getStringCellValue();
					cellValue = StringUtils.replace(cellValue, "；", ";");
					String[] emails = StringUtils.split(cellValue, ";");
					for (String e : emails) {
						if (Validator.isValidEmail(e) == false) {
							String msg = String.format("%d列%d欄 → %s", r + 1, c + 1, errMsg);
							throw new IllegalArgumentException(msg);
						}
						emailSet.add(e.trim());
					}
				}
			} catch (IOException e) {
				throw e;
			}
		}
		emailSet.removeAll(Collections.singleton(null));
		List<String> emailList = new ArrayList<String>();
		emailList.addAll(emailSet);
		
		// start send email
		EPaper epaper = get(id);
		List<String> failEmails = batchSendEpaperMail(epaper, emailList);
		sendEmailResult(epaper.getTitle(), emailSet, failEmails);
	}
	
	public void publish(long id, SysUser user, SysLog syslog) throws IOException, SQLException, ParseException, MessagingException {
		Set<String> emails = new HashSet<String>();
		emails.addAll(this.subscriberDao.allEmailList());
		emails.addAll(this.memberDao.allEmailList());
		emails.removeAll(Collections.singleton(null));
		List<String> emailList = new ArrayList<String>();
		emailList.addAll(emails);
		
		EPaper epaper = get(id);
		epaper.setPostDate(new Date(System.currentTimeMillis()));
		epaper.setPublishState(true);
		List<String> failEmails = batchSendEpaperMail(epaper, emailList);
		sendEmailResult(epaper.getTitle(), emails, failEmails);
		
		update(epaper, user, false, syslog);
	}
	
	private List<String> batchSendEpaperMail(EPaper epaper, List<String> emailList) {
		String content = readEpaperContent(epaper);
		content = content.replaceAll("openedEpaperId=0", "openedEpaperId="+epaper.getId());
		String from = "linkiac2@gmail.com";
		String senderName = "科技部鏈結產學合作計畫辦公室";
		
		List<String> failEmails = new ArrayList<String>();
		for (int i=0; i<emailList.size(); i++) {
			String email = emailList.get(i);
			try {
				String text = content + getHiddenEpaperOpenCountImageForCountent(epaper.getId(), email);
				EmailUtil.send(epaper.getTitle(), text, null, from, senderName, email);
			} catch (Exception e1) {
				failEmails.add(email);
			}
			
			if (i%100 == 0) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
			}
		}
		
		return failEmails;
	}
	
	private String getHiddenEpaperOpenCountImageForCountent(long epaperId, String email) {
		String url = ServletActionContext.getRequest().getRequestURL().toString();
		String namespace = ServletActionContext.getActionMapping().getNamespace();
		String urlDomainName = url.substring(0, url.indexOf(namespace));
		String res = "<img src=\""+urlDomainName+"/f2/ePaper/openEpaperMail?epaperId="+epaperId+"&email="+email+"\" style=\"display:none\">";
		return res;
	}
	
	/**
	 * 將批次發送郵件的結果寄一封信給管理員
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	private void sendEmailResult(String epaperTitle, Set<String> allEmails, List<String> failEmails) throws MessagingException, IOException {
		SysParameter epaperAdminEmail = this.sysParameterDao.getByKey("epaperAdminEmail");
		int finishCount = allEmails.size() - failEmails.size();
		int failCount = failEmails.size();
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		sb.append("	<tr>");
		sb.append("		<th><strong>電子報標題</strong></th>");
		sb.append("		<td>" + epaperTitle + "</td>");
		sb.append("	</tr>");
		sb.append("	<tr>");
		sb.append("		<th><strong>成功寄送Email數</strong></th>");
		sb.append("		<td>" + finishCount + "</td>");
		sb.append("	</tr>");
		sb.append("	<tr>");
		sb.append("		<th><strong>寄送失敗數</strong></th>");
		sb.append("		<td>" + failCount + "</td>");
		sb.append("	</tr>");
		sb.append("</table>");
		
		if (failCount > 0) {
			sb.append("<br>");
			sb.append("<table>");
			sb.append("	<tr>");
			sb.append("		<th><strong>寄送失敗Email清單</strong></th>");
			sb.append("	</tr>");
			for (String e : failEmails) {
				sb.append("	<tr>");
				sb.append("		<td>" + e + "</td>");
				sb.append("	</tr>");
			}
			sb.append("</table>");
		}

		
		String subject = "IACE 電子報Email發送結果";
		String content = sb.toString();
		String from = "linkiac2@gmail.com";
		String senderName = "科技部鏈結產學合作計畫辦公室";
		String to = epaperAdminEmail.getValue();
		
		EmailUtil.send(subject, content, null, from, senderName, to);
	}
	
	private String readEpaperContent(EPaper epaper) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(epaper.getFilePath()));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		return content;
	}

	public EPaper create(EPaperProduceTemplate template) throws IOException {		
		String fileName = sdf.format(System.currentTimeMillis())+".html";
		produceEpaperFile(template, fileName);
		EPaper entity = new EPaper();
		entity.setFileName(fileName);
		entity.setTitle(template.getTitle());
		entity.setNo(template.getNo());
		entity.setPostDate(new Date(System.currentTimeMillis()));
		entity.setPublishState(false);
		this.dao.create(entity);
		
		return entity;
	}
	
	public String createPreview(EPaperProduceTemplate template) throws IOException {
		String fileName = "preview_"+sdf.format(System.currentTimeMillis())+".html";
		produceEpaperFile(template, fileName);
		return fileName;
	}
	
	private void produceEpaperFile(EPaperProduceTemplate template, String fileName) throws IOException {
		String url = ServletActionContext.getRequest().getRequestURL().toString();
		String namespace = ServletActionContext.getActionMapping().getNamespace();
		String urlDomainName = url.substring(0, url.indexOf(namespace));

		String filePath = ServletActionContext.getServletContext().getRealPath("/ePapers/" + fileName);
		String content = createEpaperContentString(template, urlDomainName);
		File epaperFile = new File(filePath);
		FileUtils.writeStringToFile(epaperFile, content, "UTF-8", false);
		FileUtils.copyFileToDirectory(epaperFile, new File(this.epaperBackupFolder), true);
		log.info("產生電子報檔案 --> "+filePath);
		log.info("電子報檔案大小 : "+FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(epaperFile)));
//		System.out.println(FileUtils.readFileToString(epaperFile, "UTF-8"));
		
		// wait until e-paper resource ready 
		{
			URL u = new URL (urlDomainName+"/ePapers/"+fileName);
			int responseCode = 0;
			int timeout = 30*1000;
			do {
				HttpURLConnection huc = (HttpURLConnection) u.openConnection();
				huc.setRequestMethod("GET");
				huc.connect();
				responseCode = huc.getResponseCode();
				try {
					Thread.sleep(100);
					timeout -= 100;
				} catch (InterruptedException e) {
				}
			} while (responseCode != 200 && timeout > 0);
		}
	}
	
	private String createEpaperContentString(EPaperProduceTemplate template, String urlDomainName) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">").append("\r\n");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">").append("\r\n");
		sb.append("<head>").append("\r\n");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />").append("\r\n");
		sb.append("<title>"+template.getTitle()+"</title>").append("\r\n");
		sb.append("</head>").append("\r\n");
		sb.append("<body>").append("\r\n");
		sb.append("	<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 940px; border: 1px solid #ddd;\">").append("\r\n");
		sb.append("		<tr>").append("\r\n");
		sb.append("			<td align=\"center\">").append("\r\n");
		
		//頂端LOGO區
		sb.append("				<!-- 頂端LOGO區 -->").append("\r\n");
		sb.append("				<table width=\"94%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td align=\"left\">").append("\r\n");
		sb.append("							<img src=\""+urlDomainName+"/ePapers/images/elogo.gif\" width=\"495\" height=\"77\" />").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("						<td align=\"left\" style=\"font-family: Verdana; color: #16285e;\">").append("\r\n");
		sb.append("							<span style=\"font-size: 12px; margin-right: 5px;\">"+sdf1.format(template.getPostDate())+"</span> <strong style=\"font-size: 48px;\">"+template.getNo()+"</strong>").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("						<td width=\"103\">").append("\r\n");
		sb.append("							<img src=\""+urlDomainName+"/ePapers/images/addon.gif\" width=\"109\" height=\"35\" usemap=\"#Map\" border=\"0\" />").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("				</table>").append("\r\n");
		
		sb.append("\r\n");
		
		//Banner
		sb.append("				<!-- Banner -->").append("\r\n");
		sb.append("				<img src=\""+urlDomainName+"/ePapers/images/main.jpg\" width=\"940\" height=\"152\" usemap=\"#Map3\" border=\"0\" /> ").append("\r\n");
		sb.append("				<map name=\"Map3\" id=\"Map3\">").append("\r\n");
		sb.append("					<area shape=\"rect\" coords=\"348,28,448,126\" href=\""+urlDomainName+"/f2/member/registerPolicy\" target=\"_blank\" alt=\"加入會員\" />").append("\r\n");
		sb.append("					<area shape=\"rect\" coords=\"29,29,130,126\" href=\""+urlDomainName+"/f2/matchIntro/init\" target=\"_blank\" alt=\"媒合專區\" />").append("\r\n");
		sb.append("					<area shape=\"rect\" coords=\"135,29,236,126\" href=\""+urlDomainName+"/f2/consulting/create\" target=\"_blank\" alt=\"我要諮詢\" />").append("\r\n");
		sb.append("					<area shape=\"rect\" coords=\"242,29,342,127\" href=\""+urlDomainName+"/f2/coopEx/init?searchCondition.type=商品化\" target=\"_blank\" alt=\"產學合作案例\" />").append("\r\n");
		sb.append("				</map>").append("\r\n");
		
		sb.append("\r\n");
		
		// 公告訊息 & 活動人培
		sb.append("				<!-- 公告訊息 & 活動人培 -->").append("\r\n");
		sb.append("				<table width=\"835\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("					<tr><td>&nbsp;</td></tr>").append("\r\n");
		sb.append("					<tr><td>&nbsp;</td></tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td>").append("\r\n");
		sb.append("							<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/yellow-s-l.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"65\" align=\"center\" style=\"background: #ffb346;\">").append("\r\n");
		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/topic-ico-4.png\" width=\"41\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td align=\"left\" style=\"font-size: 25px; font-family: 微軟正黑體, Arial, Helvetica; color: #fff; background: #ffb346;\">").append("\r\n");
		sb.append("										<strong>公告訊息</strong>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"30\" bgcolor=\"#ffb346\">").append("\r\n");
		sb.append("										<a href=\""+urlDomainName+"/f2/news/init?searchCondition.category="+URLEncoder.encode("一般公告", "UTF-8")+"&openedEpaperId=0\" target=\"_blank\">").append("\r\n");
		sb.append("											<img src=\""+urlDomainName+"/ePapers/images/more-w.gif\" width=\"30\" height=\"20\" border=\"0\" />").append("\r\n");
		sb.append("										</a>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/yellow-s-r.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("							</table>").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td><img src=\""+urlDomainName+"/ePapers/images/space.png\" width=\"1\" height=\"10\" /></td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td align=\"center\">").append("\r\n");
		
		// 公告訊息
		sb.append("							<table width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		for (News news : template.getNewsList()) {
			String url = urlDomainName + "/f2/news/showDetail?id="+news.getId()+"&searchCondition.category="+URLEncoder.encode(news.getCategory(), "UTF-8")+"&openedEpaperId=0";
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"2%\" align=\"center\" valign=\"top\">").append("\r\n");
			sb.append("										<img src=\""+urlDomainName+"/ePapers/images/icon-1.gif\" width=\"8\" height=\"23\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td width=\"13%\" align=\"left\" valign=\"top\" style=\"font-size: 16px; color: #e6bc00; font-family: 微軟正黑體, Arial, Helvetica; padding-top: 6px; line-height: 25px\">").append("\r\n");
			sb.append("										<strong>"+sdf2.format(news.getPostDate())+"</strong>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<a href=\""+url+"\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 25px\">").append("\r\n");
			sb.append("											<strong>"+news.getTitle()+"</strong>").append("\r\n");
			sb.append("										</a>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
		}
		sb.append("							</table>").append("\r\n");
		sb.append("							<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td height=\"30\"><img src=\""+urlDomainName+"/ePapers/images/line-2.gif\" width=\"820\" height=\"1\" /></td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("							</table>").append("\r\n");
		
		//活動人培
		for (Activity activity : template.getActivityList()) {
			String imgSrc = urlDomainName + "/images/frontend-v2/noimage-2.gif";
			if (activity.getThumbnail() != null && activity.getThumbnail().length > 0) {
				String fileSubPath = "images/"+UUID.randomUUID().toString()+".jpg";
				File f = new File(this.epaperBackupFolder, fileSubPath);
				try (OutputStream out = new FileOutputStream(f)) {
					out.write(activity.getThumbnail());
					out.flush();
					out.close();
					imgSrc = urlDomainName + "/file/downloadFile?folderConfigKey=epaperBackupFolder&downloadFileSubPath="+fileSubPath;
				} catch (IOException e) {
					imgSrc = urlDomainName + "/images/frontend-v2/noimage-2.gif";
				}
			}
			String url = urlDomainName + "/f2/activity/showDetail?id="+activity.getId()+"&searchCondition.category="+URLEncoder.encode(activity.getCategory(), "UTF-8")+"&openedEpaperId=0";
			sb.append("							<table width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"115\" align=\"left\" valign=\"top\">").append("\r\n");
			sb.append("										<img src=\""+imgSrc+"\" width=\"100\" height=\"70\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td align=\"left\" style=\"font-size: 16px; color: #000; font-family: 微軟正黑體, Arial, Helvetica;\">").append("\r\n");
			sb.append("										<a href=\""+url+"\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 30px\">").append("\r\n");
			sb.append("											<strong>"+activity.getTitle()+"</strong>").append("\r\n");
			sb.append("										</a><br/>").append("\r\n");
			sb.append("										日期 ： "+StringUtils.defaultIfBlank(activity.getActDate(), "")+" <br/>").append("\r\n");
			sb.append("										地點 ： "+StringUtils.defaultIfBlank(activity.getActAddress(), "")).append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
			sb.append("							</table>").append("\r\n");
			sb.append("							<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td height=\"30\"><img src=\""+urlDomainName+"/ePapers/images/line-2.gif\" width=\"820\" height=\"1\" /></td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
			sb.append("							</table>").append("\r\n");
		}
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
//		sb.append("					<tr>").append("\r\n");
//		sb.append("						<td><img src=\""+urlDomainName+"/ePapers/images/space.png\" width=\"1\" height=\"15\" /></td>").append("\r\n");
//		sb.append("					</tr>").append("\r\n");
		sb.append("				</table>").append("\r\n");
		
		//研發焦點
		sb.append("				<!-- 研發焦點-->").append("\r\n");
		sb.append("				<table width=\"835\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("					<tr><td>&nbsp;</td></tr>").append("\r\n");
		sb.append("					<tr><td>&nbsp;</td></tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td>").append("\r\n");
		sb.append("							<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/yellow-s-l.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"65\" align=\"center\" style=\"background: #014c8f;\">").append("\r\n");
		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/topic-ico-3.png\" width=\"41\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td align=\"left\" style=\"font-size: 25px; font-family: 微軟正黑體, Arial, Helvetica; color: #fff; background: #014c8f;\">").append("\r\n");
		sb.append("										<strong>研發焦點</strong>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"30\" bgcolor=\"#014c8f\">").append("\r\n");
		sb.append("										<a href=\""+urlDomainName+"/f2/rdFocus/init?searchCondition.category="+URLEncoder.encode("國際前瞻趨勢", "UTF-8")+"&openedEpaperId=0\" target=\"_blank\">").append("\r\n");
		sb.append("											<img src=\""+urlDomainName+"/ePapers/images/more-w.gif\" width=\"30\" height=\"20\" border=\"0\" />").append("\r\n");
		sb.append("										</a>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/yellow-s-r.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("							</table>").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td><img src=\""+urlDomainName+"/ePapers/images/space.png\" width=\"1\" height=\"10\" /></td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td align=\"center\">").append("\r\n");
		sb.append("							<table width=\"97%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		for (RdFocus rdFocus : template.getRdFocusList()) {
			String url = urlDomainName + "/f2/rdFocus/showDetail?id="+rdFocus.getId()+"&searchCondition.category="+URLEncoder.encode(rdFocus.getCategory(), "UTF-8")+"&openedEpaperId=0";
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"2%\" align=\"center\" valign=\"top\">").append("\r\n");
			sb.append("										<img src=\""+urlDomainName+"/ePapers/images/icon-1.gif\" width=\"8\" height=\"23\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td width=\"13%\" align=\"left\" valign=\"top\" style=\"font-size: 16px; color: #013c7f; font-family: 微軟正黑體, Arial, Helvetica; padding-top: 6px; line-height: 25px\">").append("\r\n");
			sb.append("										<strong>"+sdf2.format(rdFocus.getPostDate())+"</strong>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<a href=\""+url+"\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 25px\">").append("\r\n");
			sb.append("											<strong>"+rdFocus.getTitle()+"</strong>").append("\r\n");
			sb.append("										</a>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
		}
		sb.append("							</table>").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td><img src=\""+urlDomainName+"/ePapers/images/space.png\" width=\"1\" height=\"15\" /></td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("				</table>").append("\r\n");
		sb.append("				<br/>").append("\r\n");
		
		sb.append("\r\n");
		
		// 學界研發成果 & 專利
		sb.append("				<!-- 學界研發成果 & 專利 -->").append("\r\n");
		sb.append("				<table width=\"835\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td align=\"center\">").append("\r\n");
		sb.append("							<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/blue-s-l.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"65\" align=\"center\" style=\"background: #46c0ff;\">").append("\r\n");
		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/topic-ico-2.png\" width=\"41\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td align=\"left\" style=\"font-size: 25px; font-family: 微軟正黑體, Arial, Helvetica; color: #fff; background: #46c0ff;\">").append("\r\n");
		sb.append("										<strong>學界研發成果/專利</strong>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"30\" bgcolor=\"#46c0ff\">").append("\r\n");
		sb.append("										<a href=\""+urlDomainName+"/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.Technology&openedEpaperId=0\" target=\"_blank\">").append("\r\n");
		sb.append("											<img src=\""+urlDomainName+"/ePapers/images/more-w.gif\" alt=\"更多學界研發成果\" width=\"30\" height=\"20\" border=\"0\" />").append("\r\n");
		sb.append("										</a>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/blue-s-r.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("							</table>").append("\r\n");
		
		//研發成果
		sb.append("							<table width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		for (ResearchPlan rp : template.getResearchPlanList()) {
			String url = urlDomainName + "/f2/researchPlan/showDetail?id="+rp.getId()+"&openedEpaperId=0";
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"2%\" align=\"center\" valign=\"top\">").append("\r\n");
			sb.append("										<img src=\""+urlDomainName+"/ePapers/images/icon-1.gif\" width=\"8\" height=\"23\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td width=\"21%\" align=\"left\" valign=\"top\" style=\"font-size: 16px; color: #0099cc; font-family: 微軟正黑體, Arial, Helvetica; padding-top: 6px; line-height: 25px\">").append("\r\n");
			sb.append("										<strong>"+rp.getGrbDomain1().getName()+"</strong>").append("\r\n");
			sb.append("									</td>").append("\r\n");
		    sb.append("									<td style=\"border:none; width:75px;\">").append("\r\n");
		    sb.append("									<span style=\"background-color: #5bc0de; display: inline;  padding: .2em .6em .3em;  font-size: 75%;  font-weight: bold;  line-height: 1;  color: #fff;  text-align: center;  white-space: nowrap;  vertical-align: baseline;  border-radius: .25em; \"><strong>研發成果</strong></span>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<a href=\""+url+"\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 25px\">").append("\r\n");
			sb.append("											<strong>"+rp.getName()+"</strong>").append("\r\n");
			sb.append("										</a>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
		}
		sb.append("							</table>").append("\r\n");
		
		//專利
		sb.append("							<table width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		for (Patent patent : template.getPatentList()) {
			String url = urlDomainName + "/f2/patent/showDetail?id="+patent.getId()+"&openedEpaperId=0";
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"2%\" align=\"center\" valign=\"top\">").append("\r\n");
			sb.append("										<img src=\""+urlDomainName+"/ePapers/images/icon-1.gif\" width=\"8\" height=\"23\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td width=\"21%\" align=\"left\" valign=\"top\" style=\"font-size: 16px; color: #0099cc; font-family: 微軟正黑體, Arial, Helvetica; padding-top: 6px; line-height: 25px\">").append("\r\n");
			sb.append("										<strong>"+patent.getTechField().getName()+"</strong>").append("\r\n");
			sb.append("									</td>").append("\r\n");
		    sb.append("									<td style=\"border:none; width:75px;\">").append("\r\n");
		    sb.append("									<span style=\"background-color: #64a614; display: inline;  padding: .2em .6em .3em;  font-size: 75%;  font-weight: bold;  line-height: 1;  color: #fff;  text-align: center;  white-space: nowrap;  vertical-align: baseline;  border-radius: .25em; \"><strong>專利</strong></span>").append("\r\n");
			sb.append("									</td>").append("\r\n");			
			sb.append("									<td align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<a href=\""+url+"\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 25px\">").append("\r\n");
			sb.append("											<strong>"+patent.getName()+"</strong>").append("\r\n");
			sb.append("										</a>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
		}
		sb.append("							</table>").append("\r\n");
		
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("				</table>").append("\r\n");
		sb.append("				<br/>").append("\r\n");
		
		sb.append("\r\n");
		
		// 產業評析／新聞
		sb.append("				<!-- 產業評析／新聞 -->").append("\r\n");
		sb.append("				<table width=\"835\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td align=\"center\">").append("\r\n");
		sb.append("							<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/purple-s-l.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"65\" align=\"center\" style=\"background: #8a62ff;\">").append("\r\n");
		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/topic-ico-5.png\" width=\"43\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td align=\"left\" style=\"font-size: 25px; font-family: 微軟正黑體, Arial, Helvetica; color: #fff; background: #8a62ff;\">").append("\r\n");
		sb.append("										<strong>產業評析/新聞</strong>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"30\" bgcolor=\"#8a62ff\">").append("\r\n");
		sb.append("										<a href=\""+urlDomainName+"/f2/industryInfo/init?searchCondition.category="+URLEncoder.encode("新聞雷達", "UTF-8")+"&openedEpaperId=0\" target=\"_blank\">").append("\r\n");
		sb.append("											<img src=\""+urlDomainName+"/ePapers/images/more-w.gif\" alt=\"更多產業評析／新聞\" width=\"30\" height=\"20\" border=\"0\" />").append("\r\n");
		sb.append("										</a>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/purple-s-r.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td colspan=\"5\"><img src=\""+urlDomainName+"/ePapers/images/space.png\" width=\"1\" height=\"10\" /></td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("							</table>").append("\r\n");
		sb.append("							<table width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		for (IndustryInfo info : template.getIndustryInfoList()) {
			String url = urlDomainName + "/f2/industryInfo/showDetail?id="+info.getId()+"&openedEpaperId=0";
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"2%\" align=\"center\" valign=\"top\">").append("\r\n");
			sb.append("										<img src=\""+urlDomainName+"/ePapers/images/icon-1.gif\" width=\"8\" height=\"23\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td width=\"21%\" align=\"left\" valign=\"top\" style=\"font-size: 16px; color: #8a62ff; font-family: 微軟正黑體, Arial, Helvetica; padding-top: 6px; line-height: 25px;\">").append("\r\n");
			sb.append("										<strong>"+sdf2.format(info.getPostDate())+"</strong>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("									<td align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<a href=\""+url+"\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 25px;\">").append("\r\n");
			sb.append("											<strong>"+info.getTitle()+"</strong>").append("\r\n");
			sb.append("										</a>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
		}
		sb.append("							</table>").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("				</table>").append("\r\n");
		sb.append("				<br/>").append("\r\n");
		
		sb.append("\r\n");
		
		// 常問集
		sb.append("				<!-- 常問集 -->").append("\r\n");
		sb.append("				<table width=\"835\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("					<tr>").append("\r\n");
		sb.append("						<td align=\"center\">").append("\r\n");
		sb.append("							<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/green-s-l.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"65\" align=\"center\" style=\"background: #64a614;\">").append("\r\n");
		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/topic-ico-6.png\" width=\"41\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td align=\"left\" style=\"font-size: 25px; font-family: 微軟正黑體, Arial, Helvetica; color: #fff; background: #64a614;\">").append("\r\n");
		sb.append("										<strong>常問集</strong>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"30\" bgcolor=\"#64a614\">").append("\r\n");
		sb.append("										<a href=\""+urlDomainName+"/f2/faq/init?searchCondition.category="+URLEncoder.encode("運用法人鏈結產學合作計畫", "UTF-8")+"&openedEpaperId=0\" target=\"_blank\">").append("\r\n");
		sb.append("											<img src=\""+urlDomainName+"/ePapers/images/more-w.gif\" alt=\"更多常問集\" width=\"30\" height=\"20\" border=\"0\" />").append("\r\n");
		sb.append("										</a>").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("									<td width=\"10\">").append("\r\n");
//		sb.append("										<img src=\""+urlDomainName+"/ePapers/images/green-s-r.png\" width=\"10\" height=\"42\" />").append("\r\n");
		sb.append("									</td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("								<tr>").append("\r\n");
		sb.append("									<td colspan=\"5\"><img src=\""+urlDomainName+"/ePapers/images/space.png\" width=\"1\" height=\"10\" /></td>").append("\r\n");
		sb.append("								</tr>").append("\r\n");
		sb.append("							</table>").append("\r\n");
		sb.append("							<table width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">").append("\r\n");
		for (Faq faq : template.getFaqList()) {
			sb.append("								<tr>").append("\r\n");
			sb.append("									<td width=\"4%\" align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<img src=\""+urlDomainName+"/ePapers/images/q-icon.png\" width=\"22\" height=\"22\" />").append("\r\n");
			sb.append("									</td>").append("\r\n");
			if(faq.getCategory().equals("運用法人鏈結產學合作計畫")){
				sb.append("									<td style=\"border:none; width:75px;\">").append("\r\n");
			    sb.append("									<span style=\"background-color: #f1a35b; display: inline;  padding: .2em .6em .3em;  font-size: 75%;  font-weight: bold;  line-height: 1;  color: #fff;  text-align: center;  white-space: nowrap;  vertical-align: baseline;  border-radius: .25em; \"><strong>&nbsp;&nbsp;鏈結計畫&nbsp;&nbsp;</strong></span>").append("\r\n");
				sb.append("									</td>").append("\r\n");		
			}
			else if(faq.getCategory().equals("智財實務專業人才培訓")){
				sb.append("									<td style=\"border:none; width:75px;\">").append("\r\n");
			    sb.append("									<span style=\"background-color: #6693ea; display: inline;  padding: .2em .6em .3em;  font-size: 75%;  font-weight: bold;  line-height: 1;  color: #fff;  text-align: center;  white-space: nowrap;  vertical-align: baseline;  border-radius: .25em; \"><strong>&nbsp;&nbsp;人才培訓&nbsp;&nbsp;</strong></span>").append("\r\n");
				sb.append("									</td>").append("\r\n");		
			}
			else if(faq.getCategory().equals("產學媒合服務團")){
				sb.append("									<td style=\"border:none; width:75px;\">").append("\r\n");
			    sb.append("									<span style=\"background-color: #5df820; display: inline;  padding: .2em .6em .3em;  font-size: 75%;  font-weight: bold;  line-height: 1;  color: #fff;  text-align: center;  white-space: nowrap;  vertical-align: baseline;  border-radius: .25em; \"><strong>媒合服務團</strong></span>").append("\r\n");
				sb.append("									</td>").append("\r\n");		
			}
			else{
				
			}
			sb.append("									<td align=\"left\" valign=\"top\" style=\"padding-top: 6px\">").append("\r\n");
			sb.append("										<a href=\""+urlDomainName+"/f2/faq/init?searchCondition.category="+URLEncoder.encode(faq.getCategory(), "UTF-8")+"&openedEpaperId=0\" target=\"_blank\" style=\"font-size: 17px; color: #000; font-family: 微軟正黑體, Arial, Helvetica; line-height: 25px;\">").append("\r\n");
			sb.append("											<strong>"+faq.getTitle()+"</strong>").append("\r\n");
			sb.append("										</a>").append("\r\n");
			sb.append("									</td>").append("\r\n");
			sb.append("								</tr>").append("\r\n");
		}
		sb.append("							</table>").append("\r\n");
		sb.append("						</td>").append("\r\n");
		sb.append("					</tr>").append("\r\n");
		sb.append("				</table>").append("\r\n");
		
		sb.append("			</td>").append("\r\n");
		sb.append("		</tr>").append("\r\n");
		sb.append("		<tr><td height=\"20\"></td></tr>").append("\r\n");
		sb.append("	</table>").append("\r\n");
		
		sb.append("\r\n");
		
		// footer begin
		sb.append("	<!-- footer begin -->").append("\r\n");
		sb.append("	<table align=\"center\" style=\"width: 940px; margin: 22px auto 22px auto;\">").append("\r\n");
		sb.append("		<tr>").append("\r\n");
		sb.append("			<td align=\"center\" style=\"font-size: 14px; color: #888; font-family: 微軟正黑體, Arial, Helvetica; line-height: 23px; text-align: center\">").append("\r\n");
		sb.append("				版權所有 © 2017 科技部產學及園區業務司<br />").append("\r\n");
		sb.append("				Department of Academia-Industry Collaboration and Science Park Affairs, Academy and Science Park Affairs <br /> ").append("\r\n");
		sb.append("				<img src=\""+urlDomainName+"/ePapers/images/service-icon-tel.png\" width=\"15\" height=\"15\" style=\"vertical-align: middle;\" />&nbsp;計畫諮詢專線：886-2-2737-7373&nbsp;&nbsp;").append("\r\n");
		sb.append("				<img src=\""+urlDomainName+"/ePapers/images/service-icon-service.png\" width=\"15\" height=\"15\" style=\"vertical-align: middle;\" />&nbsp;系統客服專線：886-2-2737-7796&nbsp;&nbsp;").append("\r\n");
		sb.append("				<img src=\""+urlDomainName+"/ePapers/images/service-icon-time.png\" width=\"15\" height=\"15\" style=\"vertical-align: middle;\" />&nbsp;上班時間：每週一到週五8:30 至 17:30").append("\r\n");
		sb.append("			</td>").append("\r\n");
		sb.append("			<td width=\"120\"><img src=\""+urlDomainName+"/images/qrCode.jpg\" width=\"77\" height=\"77\" alt=\"QRcode\" /></td>").append("\r\n");
		sb.append("		</tr>").append("\r\n");
		sb.append("	</table>").append("\r\n");
		sb.append("	<!-- footer end -->").append("\r\n");
		// footer end
		
		sb.append("\r\n");
		
		sb.append("	<map name=\"Map\" id=\"Map\">").append("\r\n");
		sb.append("		<area shape=\"rect\" coords=\"0,0,108,18\" href=\""+urlDomainName+"/f2/home/init\" target=\"_blank\" />").append("\r\n");
		sb.append("		<area shape=\"rect\" coords=\"0,20,108,36\" href=\""+urlDomainName+"/f2/ePaper/subscribe\" target=\"_blank\" />").append("\r\n");
		sb.append("	</map>").append("\r\n");
		
		sb.append("</body>").append("\r\n");
		sb.append("</html>").append("\r\n");
		return sb.toString();
	}
}
