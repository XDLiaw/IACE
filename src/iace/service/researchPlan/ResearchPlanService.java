package iace.service.researchPlan;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import core.util.ExcelUtil;
import core.util.PagedList;
import iace.dao.option.IOptionDao;
import iace.dao.researchPlan.IResearchPlanDao;
import iace.entity.option.OptionGrbDomain;
import iace.entity.option.OptionTrl;
import iace.entity.researchPlan.ResearchPlanManagerSearchResult;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.researchPlan.ResearchPlanSearchModel;
import iace.entity.researchPlan.Technology;
import iace.entity.sys.SysLog;
import iace.entity.sys.SysUser;
import iace.service.BaseIaceService;
import lucene.integrationSearch.IntegrationSearchModel;
import lucene.researchPlan.ResearchPlanIndexer;

public class ResearchPlanService extends BaseIaceService<ResearchPlan> {

	private IResearchPlanDao researchPlanDao;
	private IOptionDao<OptionGrbDomain> optionGrbDomainDao;
	private IOptionDao<OptionTrl> optionTrlDao;
	
	private String indexFolder;
	
	public ResearchPlanService(IResearchPlanDao researchPlanDao, 
			IOptionDao<OptionGrbDomain> optionGrbDomainDao, 
			IOptionDao<OptionTrl> optionTrlDao) {
		super(researchPlanDao);
		this.researchPlanDao = researchPlanDao;
		this.optionGrbDomainDao = optionGrbDomainDao;
		this.optionTrlDao = optionTrlDao;
		
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("configs/iace.properties"));
			this.indexFolder = prop.getProperty("luceneIndexFolder") + File.separator +"ResearchPlan";
		} catch (IOException e) {
			log.fatal("", e);
		}
	}

	public boolean planNoExist(String planNo) {
		return this.researchPlanDao.planNoExist(planNo);
	}
	
	public List<ResearchPlan> sampleForEpaper() {
		return this.researchPlanDao.sampleForEpaper();
	}

	public PagedList<ResearchPlan> searchBy(ResearchPlanSearchModel arg) {
		PagedList<ResearchPlan> res = this.researchPlanDao.searchBy(arg);
		return res;
	}
	
	public List<ResearchPlanManagerSearchResult> searchManagerFromResearchPlanIndex(IntegrationSearchModel arg) throws IOException, ParseException {
		Directory indexDirectory = ResearchPlanIndexer.openDirectory(this.indexFolder);
		IndexReader reader = ResearchPlanIndexer.createIndexReader(indexDirectory);
		try {
			List<Document> docList = ResearchPlanIndexer.search(reader, arg.getResearchPlanSearchModel().getKeyword());
			HashMap<String, ResearchPlanManagerSearchResult> map = new HashMap<String, ResearchPlanManagerSearchResult>();
			for (Document doc : docList) {
				long id = Long.valueOf(doc.get(ResearchPlanIndexer.FIELD_ID));
				String manager = doc.get(ResearchPlanIndexer.FIELD_MANAGER);
				if (StringUtils.isNotBlank(arg.getResearchPlanManager())) {
					if (StringUtils.equals(manager, arg.getResearchPlanManager()) == false) {
						continue;
					}
				}
				ResearchPlanManagerSearchResult res = map.containsKey(manager) ? map.get(manager) : new ResearchPlanManagerSearchResult();
				res.setManager(manager);
				res.addResearchPlanId(id);
				map.put(manager, res);
			}
			
			// 將Map轉換成List
			List<ResearchPlanManagerSearchResult> resList = new ArrayList<ResearchPlanManagerSearchResult>();
			for (Map.Entry<String, ResearchPlanManagerSearchResult> entry : map.entrySet()) {
				resList.add(entry.getValue());
			}
			Collections.sort(resList);
			
			return resList;
		} catch (ParseException | IOException e) {
			throw e;
		} finally {
			reader.close();
			indexDirectory.close();
		}
	}
	
	@Override
	public void create(ResearchPlan entity, SysUser user, boolean indexing, SysLog sysLog) throws IOException, SQLException {
		super.create(entity, user, indexing, sysLog);
		if (indexing) {
			createDocToResearchPlanIndex(entity);
		}
	}

	private void createDocToResearchPlanIndex(ResearchPlan entity) throws IOException {
		synchronized (ResearchPlanIndexer.lock) {
			Directory indexDirectory = ResearchPlanIndexer.openDirectory(this.indexFolder);
			IndexWriter writer = ResearchPlanIndexer.createIndexWriter(indexDirectory);
			try {
				if (entity.getKeyword() != null) {
					String[] keywords = entity.getKeyword().replace("；", ";").split(";");
					ResearchPlanIndexer.addDoc(writer, entity.getId(), entity.getManager(), keywords);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				writer.close();
				indexDirectory.close();
			}
		}
	}
	
	@Override
	public void update(ResearchPlan entity, SysUser user, boolean indexing, SysLog sysLog) throws IOException, SQLException, ParseException {
		super.update(entity, user, indexing, sysLog);
		if (indexing) {
			updateDocToResearchPlanIndex(entity);
		}
	}
	
	private void updateDocToResearchPlanIndex(ResearchPlan entity) throws IOException, ParseException {
		synchronized (ResearchPlanIndexer.lock) {
			Directory indexDirectory = ResearchPlanIndexer.openDirectory(this.indexFolder);
			IndexWriter writer = ResearchPlanIndexer.createIndexWriter(indexDirectory);
			try {
				if (entity.getKeyword() != null) {
					String[] keywords = entity.getKeyword().replace("；", ";").split(";");
					ResearchPlanIndexer.updateDoc(writer, entity.getId(), entity.getManager(), keywords);

				}
			} catch (ParseException | IOException e) {
				throw e;
			} finally {
				writer.close();
				indexDirectory.close();
			}
		}
	}

	@Override
	public void delete(ResearchPlan entity, boolean indexing, SysLog sysLog) throws IOException, ParseException, SQLException {
		super.delete(entity, indexing, sysLog);
		deteteDocFromResearchPlanIndex(entity.getId());
	}

	@Override
	public void delete(long id, boolean indexing, SysLog sysLog) throws IOException, ParseException, SQLException {
		super.delete(id, indexing, sysLog);
		deteteDocFromResearchPlanIndex(id);
	}
	
	private void deteteDocFromResearchPlanIndex(Long id) throws ParseException, IOException {
		synchronized (ResearchPlanIndexer.lock) {
			Directory indexDirectory = ResearchPlanIndexer.openDirectory(this.indexFolder);
			IndexWriter writer = ResearchPlanIndexer.createIndexWriter(indexDirectory);
			try {
				ResearchPlanIndexer.deleteDoc(writer, id);
			} catch (ParseException | IOException e) {
				throw e;
			} finally {
				writer.close();
				indexDirectory.close();
			}
		}
	}

	public List<String> createAll(List<ResearchPlan> entities) {
		List<String> errMsgs = new ArrayList<String>();
		Map<String, OptionGrbDomain> grbDomains = this.optionGrbDomainDao.mapAll();
		Map<String, OptionTrl> trls = this.optionTrlDao.mapAll();
		for (ResearchPlan rp : entities) {
			rp.getTechnologies().forEach(t -> t.create());
			try {				
				boolean planExist = this.researchPlanDao.planNoExist(rp.getPlanNo());
				if (planExist) {
					validateTechnology(rp.getTechnologies(), trls);
					ResearchPlan rpOrigin = this.researchPlanDao.getByPlanNo(rp.getPlanNo());
					rpOrigin.addTechnology(rp.getTechnologies());
					rp.getTechnologies().forEach(v -> v.setResearchPlan(rpOrigin));
					this.researchPlanDao.update(rpOrigin);
					super.updateDocToIndex(rpOrigin);
					updateDocToResearchPlanIndex(rpOrigin);
				} else {
					validateResearchPlan(rp, grbDomains, trls);
					rp.getTechnologies().forEach(v -> v.setResearchPlan(rp));
					this.researchPlanDao.create(rp);
					super.addDocToIndex(rp);
					createDocToResearchPlanIndex(rp);
				}
			} catch (Exception e) {
				log.warn("", e);
				errMsgs.add(e.getMessage());
			}
		}
		return errMsgs;
	}
	
	private void validateResearchPlan(ResearchPlan rp, Map<String, OptionGrbDomain> grbDomains, Map<String, OptionTrl> trls) {
		//TODO validate some field
		
		if (rp.getGrbDomainCodes() != null) {
			for (String code : rp.getGrbDomainCodes()) {
				if (grbDomains.containsKey(code) == false) {
					String msg = "GrbDomain: ["+code+"] isn't a legal code!";
					throw new IllegalArgumentException(msg);								
				}				
			}
		}
		if (rp.getTrl() != null && trls.containsKey(rp.getTrl().getCode()) == false) {
			String msg = "trlCode: ["+rp.getTrl().getCode()+"] doesn't exit!";
			throw new IllegalArgumentException(msg);
		}
		validateTechnology(rp.getTechnologies(), trls);
	}
	
	private void validateTechnology(List<Technology> technologies, Map<String, OptionTrl> trls) {
		if (technologies != null) {
			for (Technology tech:technologies) {
				//TODO validate some field
				
				
				if (tech.getOptionTrlCodes() != null) {
					for (String code : tech.getOptionTrlCodes()) {
						if (trls.containsKey(code) == false) {
							String msg = "trlCode: ["+code+"] doesn't exit!";
							throw new IllegalArgumentException(msg);
						}
					}
				}

			}			
		}
	}

	public XSSFWorkbook exportRawData(ResearchPlanSearchModel arg) {
		List<ResearchPlan> list = this.researchPlanDao.listAll(arg);
		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		int r = -1;
		int c = -1;
		XSSFRow row = null;
		
		{//title row
			row = sheet.createRow(++r);
			row.createCell(++c).setCellValue("年度");
			row.createCell(++c).setCellValue("計畫編號");
			row.createCell(++c).setCellValue("計畫名稱");
			row.createCell(++c).setCellValue("計畫主持人");
			row.createCell(++c).setCellValue("研究領域");
			row.createCell(++c).setCellValue("關鍵字");
			row.createCell(++c).setCellValue("計畫發展階段");
			row.createCell(++c).setCellValue("GRB計畫編號");
			row.createCell(++c).setCellValue("成果報告ID");
			row.createCell(++c).setCellValue("技術名稱");
			row.createCell(++c).setCellValue("技術簡述");
			row.createCell(++c).setCellValue("技術發展階段");
			row.createCell(++c).setCellValue("技術發展階段說明");
		}		
		
		// data part
		for (ResearchPlan rp : list) {
			if (rp.getTechnologies() == null || rp.getTechnologies().size() == 0) {
				row = sheet.createRow(++r);
				c = -1;
				ExcelUtil.createNSetCellValue(row, ++c, rp.getYear());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getPlanNo());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getName());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getManager());
				ExcelUtil.createNSetCellValue(row, ++c, rp.grbDomainCodeString());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getKeyword());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getTrlCode());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getProjkey());
				ExcelUtil.createNSetCellValue(row, ++c, rp.getGrb05Id());
			} else {
				for (Technology t : rp.getTechnologies()) {
					row = sheet.createRow(++r);
					c = -1;
					ExcelUtil.createNSetCellValue(row, ++c, rp.getYear());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getPlanNo());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getName());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getManager());
					ExcelUtil.createNSetCellValue(row, ++c, rp.grbDomainCodeString());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getKeyword());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getTrlCode());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getProjkey());
					ExcelUtil.createNSetCellValue(row, ++c, rp.getGrb05Id());
					ExcelUtil.createNSetCellValue(row, ++c, t.getName());
					ExcelUtil.createNSetCellValue(row, ++c, t.getDescriptoin());
					ExcelUtil.createNSetCellValue(row, ++c, t.getOptionTrlCodesString());
					ExcelUtil.createNSetCellValue(row, ++c, t.getTrlDesc());
				}
			}
		}
		
		for (int i=0; i<=c; i++) {
			sheet.autoSizeColumn(i);
		}
		sheet.createFreezePane(0, 1);
		
		return wb;
	}
	
	public List<Integer> getYearList() {
		return this.researchPlanDao.getYearList();
	}

	public void rebuildResearchPlanIndex() throws IOException {
		synchronized (ResearchPlanIndexer.lock) {
			Directory indexDirectory = ResearchPlanIndexer.openDirectory(this.indexFolder);
			IndexWriter writer = ResearchPlanIndexer.createIndexWriter(indexDirectory);
			try {
				writer.deleteAll(); writer.commit();
				
				ResearchPlanSearchModel arg = new ResearchPlanSearchModel();
				long totalRecordCount = this.researchPlanDao.queryTotalRecordsCount(arg);
				int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
				for (int i = 0; i < pageCount; i++) {
					arg.setPageIndex(i);
					PagedList<ResearchPlan> pagedList = this.researchPlanDao.searchBy(arg);
					for (ResearchPlan rp : pagedList.getList()) {
						if (rp.getKeyword() != null) {
							String[] keywords = rp.getKeyword().replace("；", ";").split(";");
							ResearchPlanIndexer.addDoc(writer, rp.getId(), rp.getManager(), keywords);
						}
					}
				}
			} catch (IOException e) {
				throw e;
			} finally {
				writer.close();
				indexDirectory.close();
			}
		}
	}

}
