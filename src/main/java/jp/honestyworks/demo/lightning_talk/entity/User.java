package jp.honestyworks.demo.lightning_talk.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	@CreatedDate
	private Date createdAt;

	private String email;

	private String sub;

	@Column(name="updated_at")
	@LastModifiedDate
	private Timestamp updatedAt;

	//bi-directional many-to-one association to Talk
	@OneToMany(mappedBy="user")
	private List<Talk> talks;

	public User() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Talk> getTalks() {
		return this.talks;
	}

	public void setTalks(List<Talk> talks) {
		this.talks = talks;
	}

	public Talk addTalk(Talk talk) {
		getTalks().add(talk);
		talk.setUser(this);

		return talk;
	}

	public Talk removeTalk(Talk talk) {
		getTalks().remove(talk);
		talk.setUser(null);

		return talk;
	}

}