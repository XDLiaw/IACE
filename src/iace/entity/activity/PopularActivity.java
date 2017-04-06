package iace.entity.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import iace.entity.BaseEntity;

@Entity
@Table(name = "POPULAR_ACTIVITY")
public class PopularActivity extends BaseEntity {
	private static final long serialVersionUID = 847712809953822453L;
	private long id;
	private long activityId;
	private boolean pinned; // 是否置頂
	private Float priority; // 小到大排序

	@Id
	@Column(name = "ID", length = 19, unique = true, nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACTIVITY_ID")
	@SequenceGenerator(name = "SEQ_ACTIVITY_ID", sequenceName = "SEQ_ACTIVITY_ID", allocationSize = 1, initialValue = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "ACTIVITY_ID")
	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	@Column(name = "PINNED")
	@Type(type="true_false")
	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	@Column(name = "PRIORITY")
	public Float getPriority() {
		return priority;
	}

	public void setPriority(Float priority) {
		this.priority = priority;
	}

}
