package com.csvsim.wrapper;

public class Period {
	java.lang.Integer years;
	java.lang.Integer months;
	java.lang.Integer weeks;
	java.lang.Integer days;
	java.lang.Integer hours;
	java.lang.Integer minutes;
	java.lang.Integer seconds;
	java.lang.Integer millis;

	public Period() {
		this.years = 0;
		this.months = 0;
		this.weeks = 0;
		this.days = 0;
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
		this.millis = 0;
	}

	public Period(int hours, int minutes, int seconds) {
		this.years = 0;
		this.months = 0;
		this.weeks = 0;
		this.days = 0;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.millis = 0;
	}	
	
	public java.lang.Integer getYears() {
		return this.years;
	}

	public void setYears(java.lang.Integer years) {
		this.years = years;
	}

	public java.lang.Integer getMonths() {
		return this.months;
	}

	public void setMonths(java.lang.Integer months) {
		this.months = months;
	}

	public java.lang.Integer getWeeks() {
		return this.weeks;
	}

	public void setWeeks(java.lang.Integer weeks) {
		this.weeks = weeks;
	}

	public java.lang.Integer getDays() {
		return this.days;
	}

	public void setDays(java.lang.Integer days) {
		this.days = days;
	}

	public java.lang.Integer getHours() {
		return this.hours;
	}

	public void setHours(java.lang.Integer hours) {
		this.hours = hours;
	}

	public java.lang.Integer getMinutes() {
		return this.minutes;
	}

	public void setMinutes(java.lang.Integer minutes) {
		this.minutes = minutes;
	}

	public java.lang.Integer getSeconds() {
		return this.seconds;
	}

	public void setSeconds(java.lang.Integer seconds) {
		this.seconds = seconds;
	}

	public java.lang.Integer getMillis() {
		return this.millis;
	}

	public void setMillis(java.lang.Integer millis) {
		this.millis = millis;
	}

	public org.joda.time.Period getJodaPeriod() {
		return new org.joda.time.Period(years, months, weeks, days, hours, minutes, seconds, millis);

	}
}
