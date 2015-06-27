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
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents a blog user.
 * 
 * @author Luka Skugor
 *
 */
@NamedQueries({
		@NamedQuery(name = "BlogUser.requestUser", query = "select u from BlogUser as u where u.nick=:givenNick"),
		@NamedQuery(name = "BlogUser.listUsers", query = "select u from BlogUser as u") })
@Entity
@Table(name = "blog_users")
// @Cacheable(true)
public class BlogUser {

	/**
	 * User's id.
	 */
	private Long id;
	/**
	 * User's first name.
	 */
	private String firstName;
	/**
	 * User's last name.
	 */
	private String lastName;
	/**
	 * User's nick.
	 */
	private String nick;
	/**
	 * User's e-mail.
	 */
	private String email;
	/**
	 * User's encrypted password.
	 */
	private String passwordHash;
	/**
	 * User's blog entries.
	 */
	private List<BlogEntry> entries = new ArrayList<>();

	/**
	 * Gets user's entries.
	 * 
	 * @return user's entries
	 */
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets user's entries.
	 * 
	 * @param entries
	 *            user's entries
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Gets user's id.
	 * 
	 * @return user's id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets user's id.
	 * 
	 * @param id
	 *            user's id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets user's first name.
	 * 
	 * @return user's first name
	 */
	@Column(nullable = false, length = 100)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets user's first name.
	 * 
	 * @param firstName
	 *            user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets user's last name.
	 * 
	 * @return user's last name
	 */
	@Column(nullable = false, length = 100)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets user's last name.
	 * 
	 * @param lastName
	 *            user's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets user's nick.
	 * 
	 * @return user's nick
	 */
	@Column(nullable = false, length = 100, unique = true)
	public String getNick() {
		return nick;
	}

	/**
	 * Sets user's nick.
	 * 
	 * @param nick
	 *            user's nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Gets user's e-mail.
	 * 
	 * @return user's e-mail
	 */
	@Column(nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	/**
	 * Sets user's e-mail.
	 * 
	 * @param email
	 *            user's e-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets user's encrypted password.
	 * 
	 * @return user's encrypted password
	 */
	@Column(nullable = false, length = 512)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets user's encrypted password.
	 * 
	 * @param passwordHash
	 *            user's encrypted password
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
