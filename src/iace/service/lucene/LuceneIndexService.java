package iace.service.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;

import core.util.PagedList;
import iace.dao.activity.IActivityDao;
import iace.dao.coopExample.ICoopExDao;
import iace.dao.incubationCenter.IIncubationCenterDao;
import iace.dao.industryInfo.IIndustryInfoDao;
import iace.dao.literature.ILiteratureDao;
import iace.dao.news.INewsDao;
import iace.dao.patent.IPatentDao;
import iace.dao.researchPlan.IResearchPlanDao;
import iace.dao.researchPlan.ITechnologyDao;
import iace.dao.talentedPeople.ITalentedPeopleDao;
import iace.entity.activity.Activity;
import iace.entity.activity.ActivitySearchModel;
import iace.entity.coopExample.CoopEx;
import iace.entity.coopExample.CoopExSearchModel;
import iace.entity.incubationCenter.IncubationCenter;
import iace.entity.incubationCenter.IncubationCenterSearchModel;
import iace.entity.industryInfo.IndustryInfo;
import iace.entity.industryInfo.IndustryInfoSearchModel;
import iace.entity.literature.Literature;
import iace.entity.literature.LiteratureSearchModel;
import iace.entity.news.News;
import iace.entity.news.NewsSearchModel;
import iace.entity.patent.Patent;
import iace.entity.patent.PatentSearchModel;
import iace.entity.researchPlan.ResearchPlan;
import iace.entity.researchPlan.ResearchPlanSearchModel;
import iace.entity.researchPlan.Technology;
import iace.entity.researchPlan.TechnologySearchModel;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;
import lucene.integrationSearch.IntegrationIndexer;
import lucene.integrationSearch.IntegrationSearchModel;
import lucene.integrationSearch.IntegrationSearchResult;

public class LuceneIndexService {
	protected static Logger log = LogManager.getLogger(LuceneIndexService.class);

	private IResearchPlanDao researchPlanDao;
	private ITechnologyDao technologyDao;
	private IPatentDao patentDao;
	private ITalentedPeopleDao talentedPeopleDao;
	private ICoopExDao coopExDao;
	private ILiteratureDao literautureDao;
	private IIncubationCenterDao incubationCenterDao;
	private IActivityDao activityDao;
	private IIndustryInfoDao industryInfoDao;
	private INewsDao newsDao;

	private String integrationSearchIndexFolder;

	public LuceneIndexService(
			IResearchPlanDao researchPlanDao,
			ITechnologyDao technologyDao, 
			IPatentDao patentDao, 
			ITalentedPeopleDao talentedPeopleDao,
			ICoopExDao coopExDao,
			ILiteratureDao literautureDao,
			IIncubationCenterDao incubationCenterDao,
			IActivityDao activityDao,
			IIndustryInfoDao industryInfoDao,
			INewsDao newsDao) {
		this.researchPlanDao = researchPlanDao;
		this.technologyDao = technologyDao;
		this.patentDao = patentDao;
		this.talentedPeopleDao = talentedPeopleDao;
		this.coopExDao = coopExDao;
		this.literautureDao = literautureDao;
		this.incubationCenterDao = incubationCenterDao;
		this.activityDao = activityDao;
		this.industryInfoDao = industryInfoDao;
		this.newsDao = newsDao;

		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("configs/iace.properties"));
			this.integrationSearchIndexFolder = prop.getProperty("luceneIndexFolder") + File.separator +"IntegrationSearch";
		} catch (IOException e) {
			log.fatal("", e);
		}
	}

	// rebuild the whole index
	public synchronized void rebuildIndex() throws IOException {
		synchronized (IntegrationIndexer.lock) {
			Directory indexDirectory = IntegrationIndexer.openDirectory(this.integrationSearchIndexFolder);
			IndexWriter writer = IntegrationIndexer.createIndexWriter(indexDirectory);
			try {
				writer.deleteAll(); writer.commit();
				createResearchPlanIndex(writer);
//				createTechnologyIndex(writer);
				createPatentIndex(writer);
				createTalentedPeopleIndex(writer);
				createCoopExIndex(writer);
				createLiteratureIndex(writer);
				createIncubationCenterIndex(writer);
				createActivityIndex(writer);
				createIndustryInfoIndex(writer);
				createNewsInfoIndex(writer);
			} catch (IOException e) {
				throw e;
			} finally {
				writer.close();
				indexDirectory.close();
			}
		}
	}
	
	private void createResearchPlanIndex(IndexWriter writer) throws IOException {
		ResearchPlanSearchModel arg = new ResearchPlanSearchModel();
		long totalRecordCount = this.researchPlanDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<ResearchPlan> pagedList = this.researchPlanDao.searchBy(arg);
			for (ResearchPlan entity : pagedList.getList()) {
				entity = this.researchPlanDao.get(entity.getId());
				StringBuilder sb = new StringBuilder();
				sb.append(entity.toLunceneContent()+ " ");
				for (Technology tec : entity.getTechnologies()) {
					sb.append(tec.toLunceneContent() + " ");
				}
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), sb.toString());
			}
		}
	}

	@SuppressWarnings("unused")
	private void createTechnologyIndex(IndexWriter writer) throws IOException {
		TechnologySearchModel arg = new TechnologySearchModel();
		long totalRecordCount = this.technologyDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<Technology> pagedList = this.technologyDao.searchBy(arg);
			for (Technology entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent()+entity.getResearchPlan().toLunceneContent());
			}
		}
	}
	
	private void createPatentIndex(IndexWriter writer) throws IOException {
		PatentSearchModel arg = new PatentSearchModel();
		long totalRecordCount = this.patentDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<Patent> pagedList = this.patentDao.searchBy(arg);
			for (Patent entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}
	}
	
	private void createTalentedPeopleIndex(IndexWriter writer) throws IOException {
		TalentedPeopleSearchModel arg = new TalentedPeopleSearchModel();
		arg.setAgreePDPL(true);
		long totalRecordCount = this.talentedPeopleDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<TalentedPeople> pagedList = this.talentedPeopleDao.searchBy(arg, true);
			for (TalentedPeople entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}		
	}
	
	private void createCoopExIndex(IndexWriter writer) throws IOException {
		CoopExSearchModel arg = new CoopExSearchModel();
		long totalRecordCount = this.coopExDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<CoopEx> pagedList = this.coopExDao.searchBy(arg);
			for (CoopEx entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}
	}
	
	private void createLiteratureIndex(IndexWriter writer) throws IOException {
		{
			LiteratureSearchModel arg = new LiteratureSearchModel();
			arg.setCategory("文獻");
			long totalRecordCount = this.literautureDao.queryTotalRecordsCount(arg);
			int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
			for (int i = 0; i < pageCount; i++) {
				arg.setPageIndex(i);
				PagedList<Literature> pagedList = this.literautureDao.searchBy(arg);
				for (Literature entity : pagedList.getList()) {
					IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
				}
			}
		}
		{
			LiteratureSearchModel arg = new LiteratureSearchModel();
			arg.setCategory("法規政策");
			long totalRecordCount = this.literautureDao.queryTotalRecordsCount(arg);
			int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
			for (int i = 0; i < pageCount; i++) {
				arg.setPageIndex(i);
				PagedList<Literature> pagedList = this.literautureDao.searchBy(arg);
				for (Literature entity : pagedList.getList()) {
					IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
				}
			}
		}
	}
	
	private void createIncubationCenterIndex(IndexWriter writer) throws IOException {
		IncubationCenterSearchModel arg = new IncubationCenterSearchModel();
		long totalRecordCount = this.incubationCenterDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<IncubationCenter> pagedList = this.incubationCenterDao.searchBy(arg);
			for (IncubationCenter entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}
	}
	
	private void createActivityIndex(IndexWriter writer) throws IOException {
		ActivitySearchModel arg = new ActivitySearchModel();
		long totalRecordCount = this.activityDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<Activity> pagedList = this.activityDao.searchBy(arg);
			for (Activity entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}
	}
	
	private void createIndustryInfoIndex(IndexWriter writer) throws IOException {
		IndustryInfoSearchModel arg = new IndustryInfoSearchModel();
		long totalRecordCount = this.industryInfoDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<IndustryInfo> pagedList = this.industryInfoDao.searchBy(arg);
			for (IndustryInfo entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}
	}
	
	private void createNewsInfoIndex(IndexWriter writer) throws IOException {
		NewsSearchModel arg = new NewsSearchModel();
		long totalRecordCount = this.newsDao.queryTotalRecordsCount(arg);
		int pageCount = (int) Math.ceil(totalRecordCount / (double) arg.getPageSize());
		for (int i = 0; i < pageCount; i++) {
			arg.setPageIndex(i);
			PagedList<News> pagedList = this.newsDao.searchBy(arg);
			for (News entity : pagedList.getList()) {
				IntegrationIndexer.addDoc(writer, entity.getId(), entity.getClass(), entity.toLunceneContent());
			}
		}
	}	

	public PagedList<IntegrationSearchResult> integrationSearch(IntegrationSearchModel arg) throws IOException, ParseException {
		Directory indexDirectory = IntegrationIndexer.openDirectory(this.integrationSearchIndexFolder);
		IndexReader reader = IntegrationIndexer.createIndexReader(indexDirectory);
		try {
			PagedList<Document> docs = IntegrationIndexer.searchBy(reader, arg);
			
			List<IntegrationSearchResult> list = new ArrayList<IntegrationSearchResult>();
			for (Document doc : docs.getList()) {
				String className = doc.get(IntegrationIndexer.FIELD_CLASS_NAME);
				long id = Long.valueOf(doc.get(IntegrationIndexer.FIELD_ID));
				log.debug(className + "\t" + id);
				
				IntegrationSearchResult r = new IntegrationSearchResult();
				r.setType(className);
				if (ResearchPlan.class.getName().equals(className)) {
					r.setResearchPlan(this.researchPlanDao.get(id));
				} else if (Technology.class.getName().equals(className)) {
					r.setTechnology(this.technologyDao.get(id));
				} else if (Patent.class.getName().equals(className)) {
					r.setPatent(this.patentDao.get(id));
				} else if (TalentedPeople.class.getName().equals(className)) {
					r.setTalentedPeople(this.talentedPeopleDao.get(id));
				} else if (CoopEx.class.getName().equals(className)) {
					r.setCoopEx(this.coopExDao.get(id));
				} else if (Literature.class.getName().equals(className)) {
					r.setLiterature(this.literautureDao.get(id));
				} else if (IncubationCenter.class.getName().equals(className)) {
					r.setIncubationCenter(this.incubationCenterDao.get(id));
				} else if (Activity.class.getName().equals(className)) {
					r.setActivity(this.activityDao.get(id));
				} else if (IndustryInfo.class.getName().equals(className)) {
					r.setIndustryInfo(this.industryInfoDao.get(id));
				} else if (News.class.getName().equals(className)) {
					r.setNews(this.newsDao.get(id));
				} else {
					throw new IllegalArgumentException("No such type!");
				}
				list.add(r);
			}
			
			PagedList<IntegrationSearchResult> resultList = 
					new PagedList<IntegrationSearchResult>(list, docs.getTotatlItemCount(), arg.getPageSize(), arg.getPageIndex());
			return resultList;
		} catch (Exception e) {
			throw e;
		} finally {
			reader.close();
			indexDirectory.close();
		}
	}
}
