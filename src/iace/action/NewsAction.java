package iace.action;

import java.util.List;

import core.util.PagedList;
import iace.dao.ClickNumCounterDao;
import iace.entity.DbFile;
import iace.entity.news.News;
import iace.entity.news.NewsSearchModel;
import iace.entity.option.BaseOption;
import iace.service.ServiceFactory;
import iace.service.news.NewsService;

public class NewsAction extends BaseIaceAction {
	private static final long serialVersionUID = 5568302832472926677L;
	
	private NewsService newsService = ServiceFactory.getNewsService();
	
	private NewsSearchModel searchCondition = new NewsSearchModel();
	private PagedList<News> newsPagedList;
	
	private Long id;
	private News news;
	
	private List<BaseOption> categoryList = News.getCategoryList();
	private List<BaseOption> fileTypeList = DbFile.getFileTypeList();
	
	public NewsAction() {
		super.setTitle("公告訊息");
	}
	
	public String init() {
		return index();
	}
	
	public String index() {
		try {
			this.newsPagedList = this.newsService.searchBy(this.searchCondition);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String showDetail() {
		try {
			new ClickNumCounterDao().increaseClickNum(this.id, News.class);
			this.news = this.newsService.get(this.id);
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String create() {
		return SUCCESS;
	}
	
	public void validateCreateSubmit() {
		validateBeforeSubmit();
	}
	
	public String createSubmit() {
		try {
			this.newsService.create(this.news, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("CREATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String update() {
		try {
			this.news = this.newsService.get(id);
			if (this.news == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public void validateUpdateSubmit() {
		validateBeforeSubmit();
	}
	
	public String updateSubmit() {
		try {
			this.newsService.update(this.news, super.getCurrentSysUser(), true, super.getSysLog());
			this.addActionMessage("UPDATE SUCCESS!");
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			this.news = this.newsService.get(id);
			if (this.news == null) {
				super.addActionError("找不到選擇的資料紀錄!");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	public String deleteSubmit() {
		try {
			this.newsService.delete(this.id, true, super.getSysLog());
			super.addActionMessage("DELETE SUCCESS");
			index();
			return SUCCESS;
		} catch (Exception e) {
			super.showExceptionToPage(e);
			return ERROR;
		}
	}
	
	private void validateBeforeSubmit() {
		super.validateNotBlankNLength(this.news.getTitle(), 200, "news.title");
		super.validateNotNull(this.news.getPostDate(), "news.postDate");
		super.validateTextMaxLength(this.news.getMetaTitle(), 200, "news.metaTitle");
	}
	
	//==========================================================================

	public NewsSearchModel getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(NewsSearchModel searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public PagedList<News> getNewsPagedList() {
		return newsPagedList;
	}

	public List<BaseOption> getCategoryList() {
		return categoryList;
	}
	
	public List<BaseOption> getFileTypeList() {
		return fileTypeList;
	}

	
}
