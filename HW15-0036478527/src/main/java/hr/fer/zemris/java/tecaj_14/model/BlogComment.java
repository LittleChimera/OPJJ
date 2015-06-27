package hr.fer.zemris.java.tecaj_14.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity which represents a blog comment.
 * 
 * @author Luka Skugor
 *
 */
@Entity
@Table(name = "blog_comments")
@Cacheable(true)
public class BlogComment {

	/**
	 * Comment id.
	 */
	private Long id;
	/**
	 * Reference to blog entry on which comment was made.
	 */
	private BlogEntry blogEntry;
	/**
	 * E-mail of the user which made the comment.
	 */
	private String usersEMail;
	/**
	 * Comment content.
	 */
	private String message;
	/**
	 * Time when comment was posted.
	 */
	private Date postedOn;

	/**
	 * Gets comment's id.
	 * @return comment's id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets comment's id.
	 * @param id comment's id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets blog entry on which comment was made.
	 * @return blog entry on which comment was made
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Sets blog entry on which comment was made.
	 * @param blogEntry blog entry on which comment was made
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Gets e-mail of the user which made the comment.
	 * @return e-mail of the user which made the comment
	 */
	@Column(nullable = false, length = 100)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets e-mail of the user which made the comment.
	 * @param usersEMail e-mail of the user which made the comment
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Gets comment's message.
	 * @return comment's message
	 */
	@Column(nullable = false, length = 4096)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets comment's message.
	 * @param message comment's message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets time when comment was made.
	 * @return time when comment was made
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets time when comment was made.
	 * @param postedOn time when comment was made
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
