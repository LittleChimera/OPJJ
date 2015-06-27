package hr.fer.zemris.java.tecaj_14.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.QueryHint;

/**
 * This entity represents a blog entry.
 * 
 * @author Luka Skugor
 *
 */
@NamedQueries({
		@NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "BlogEntry.requestById", query = "select b from BlogEntry as b where b.id=:eId") })
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {

	/**
	 * Entrie's id.
	 */
	private Long id;
	/**
	 * List of comments to the entry.
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * Creation time.
	 */
	private Date createdAt;
	/**
	 * Time when last edit was made.
	 */
	private Date lastModifiedAt;
	/**
	 * Entrie's title.
	 */
	private String title;
	/**
	 * Entrie's body text.
	 */
	private String text;
	/**
	 * Entrie's creator.
	 */
	private BlogUser creator;

	/**
	 * Gets entrie's creator.
	 * @return entrie's creator
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Sets entrie's creator.
	 * @param creator entrie's creator
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	/**
	 * Gets entrie's id.
	 * @return entrie's id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets entrie's id.
	 * @param id entrie's id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets entrie's comments.
	 * @return entrie's comments
	 */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Sets entrie's comments.
	 * @param comments entrie's comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Gets entry's creation time.
	 * @return entry's creation time
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets entry's creation time.
	 * @param createdAt entry's creation time
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets time of last modification.
	 * @return time of last modification
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets time of last modification.
	 * @param lastModifiedAt time of last modification
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Gets entry's title.
	 * @return entry's title
	 */
	@Column(nullable = false, length = 100)
	public String getTitle() {
		return title;
	}

	/**
	 * Sets entry's title.
	 * @param title entry's title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets entry's body text.
	 * @return entry's body text
	 */
	@Column(nullable = false, length = 4096)
	public String getText() {
		return text;
	}

	/**
	 * Sets entry's body text.
	 * @param text entry's body text
	 */
	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
