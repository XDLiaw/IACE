package iace.entity.faq;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import core.util.StringUtil;
import iace.entity.BaseLinkiacEntity;
import iace.entity.option.BaseOption;

@Entity
@Table(name = "FAQ")
public class Faq extends BaseLinkiacEntity {
	private static final long serialVersionUID = -6496449791248146520L;
	
	private static List<BaseOption> categoryList = new ArrayList<BaseOption>();
	static {
		categoryList.add(new BaseOption("運用法人鏈結產學合作計畫", "運用法人鏈結產學合作計畫"));
		categoryList.add(new BaseOption("智財實務專業人才培訓", "智財實務專業人才培訓"));
		categoryList.add(new BaseOption("產學媒合服務團", "產學媒合服務團"));
	}
	
	private long id;
	private String category;
	private String title;
	private String source;
	private String content;

	@Id
	@Column(name = "ID", length = 19, unique = true, nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FAQ_ID")
	@SequenceGenerator(name = "SEQ_FAQ_ID", sequenceName = "SEQ_FAQ_ID", allocationSize = 1, initialValue = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "CATEGORY")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "SOURCE")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Transient
	public String getContentWithoutHTML() {
		return StringUtil.filterHtml(this.content);
	}

	@Transient
	public static List<BaseOption> getCategoryList() {
		return categoryList;
	}

}
