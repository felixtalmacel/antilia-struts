package com.antilia.struts2.entities;

// Generated Apr 23, 2008 5:11:37 PM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Asignment generated by hbm2java
 */
@Entity
@Table(name = "asignment", uniqueConstraints = @UniqueConstraint(columnNames = {
		"task", "employee" }))
public class Asignment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task", nullable = false)	
	private Task task;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee", nullable = false)
	private Employee employee;
	
	@Column(name = "notes", length=200)
	private String notes;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "startdate", nullable = false, length = 13)
	private Date startdate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "enddate", length = 13)
	private Date enddate;
	
	@Column(name = "estimatedhours", precision = 3)
	private BigDecimal estimatedhours;

	public Asignment() {
	}

	public Asignment(long id, Task task, Employee employee, Date startdate) {
		this.id = id;
		this.task = task;
		this.employee = employee;
		this.startdate = startdate;
	}

	public Asignment(long id, Task task, Employee employee, Date startdate,
			Date enddate, BigDecimal estimatedhours) {
		this.id = id;
		this.task = task;
		this.employee = employee;
		this.startdate = startdate;
		this.enddate = enddate;
		this.estimatedhours = estimatedhours;
	}

	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	
	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}


	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	
	public BigDecimal getEstimatedhours() {
		return this.estimatedhours;
	}

	public void setEstimatedhours(BigDecimal estimatedhours) {
		this.estimatedhours = estimatedhours;
	}
	
	public String getNotes() {
		return notes;
	}

	
	public void setNotes(String notes) {
		this.notes = notes;
	}

}