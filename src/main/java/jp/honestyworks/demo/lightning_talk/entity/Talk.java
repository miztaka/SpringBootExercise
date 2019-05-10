package jp.honestyworks.demo.lightning_talk.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jp.honestyworks.demo.lightning_talk.model.TalkStatus;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the talks database table.
 * 
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="talks")
@NamedQuery(name="Talk.findAll", query="SELECT t FROM Talk t")
public class Talk implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String browser;

	@Lob
	@Column(columnDefinition="TEXT")
	private String description;

	private String hostname;

	private String ipaddr;

	private String os;

	private boolean prized;

	@Temporal(TemporalType.DATE)
	@Column(name="session_date")
	private Date sessionDate;

	@Column(columnDefinition="ENUM")
	@Enumerated(EnumType.STRING)
	private TalkStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_at")
	@CreatedDate
	private Date submittedAt;

	private String topic;

	@Column(name="updated_at")
	@LastModifiedDate
	private Timestamp updatedAt;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Talk() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrowser() {
		return this.browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIpaddr() {
		return this.ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getOs() {
		return this.os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public boolean isPrized() {
		return this.prized;
	}

	public void setPrized(boolean prized) {
		this.prized = prized;
	}

	public Date getSessionDate() {
		return this.sessionDate;
	}

	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
	}

	public TalkStatus getStatus() {
		return this.status;
	}

	public void setStatus(TalkStatus status) {
		this.status = status;
	}

	public Date getSubmittedAt() {
		return this.submittedAt;
	}

	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}