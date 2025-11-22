package com.receipt.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReceiptData {

	private String receiptNumber;
	private String donorName;
	private String donorEmail;
	private String donorPAN;
	private String donorMobileNumber;
	private Long donationAmount;
	private String donationDate;
	private String volunteerEmail;
	private String volunteerName;
	private String newDonor;
	
	
	
	public ReceiptData() {
		super();
	}



	public ReceiptData(String donorName, String donorEmail, String donorPAN, String donorMobileNumber,
			Long donationAmount, String donationDate, String volunteerEmail, String volunteerName, String newDonor) {
		super();
		this.donorName = donorName;
		this.donorEmail = donorEmail;
		this.donorPAN = donorPAN;
		this.donorMobileNumber = donorMobileNumber;
		this.donationAmount = donationAmount;
		this.donationDate = donationDate;
		this.volunteerEmail = volunteerEmail;
		this.volunteerName = volunteerName;
		this.newDonor = newDonor;
	}


	

	public String getReceiptNumber() {
		return receiptNumber;
	}



	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}



	public String getDonorName() {
		return donorName;
	}



	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}



	public String getDonorEmail() {
		return donorEmail;
	}



	public void setDonorEmail(String donorEmail) {
		this.donorEmail = donorEmail;
	}



	public String getDonorPAN() {
		return donorPAN;
	}



	public void setDonorPAN(String donorPAN) {
		this.donorPAN = donorPAN;
	}



	public String getDonorMobileNumber() {
		return donorMobileNumber;
	}



	public void setDonorMobileNumber(String donorMobileNumber) {
		this.donorMobileNumber = donorMobileNumber;
	}



	public Long getDonationAmount() {
		return donationAmount;
	}



	public void setDonationAmount(Long donationAmount) {
		this.donationAmount = donationAmount;
	}



	public String getDonationDate() {
		return donationDate;
	}



	public void setDonationDate(String donationDate) {
		this.donationDate = donationDate;
	}



	public String getVolunteerEmail() {
		return volunteerEmail;
	}



	public void setVolunteerEmail(String volunteerEmail) {
		this.volunteerEmail = volunteerEmail;
	}



	public String getVolunteerName() {
		return volunteerName;
	}



	public void setVolunteerName(String volunteerName) {
		this.volunteerName = volunteerName;
	}



	public String getNewDonor() {
		return newDonor;
	}



	public void setNewDonor(String newDonor) {
		this.newDonor = newDonor;
	}


	@Override
	public String toString() {
		return "ReceiptData [receiptNumber=" + receiptNumber + ", donorName=" + donorName + ", donorEmail=" + donorEmail
				+ ", donorPAN=" + donorPAN + ", donorMobileNumber=" + donorMobileNumber + ", donationAmount="
				+ donationAmount + ", donationDate=" + donationDate + ", volunteerEmail=" + volunteerEmail
				+ ", volunteerName=" + volunteerName + ", newDonor=" + newDonor + "]";
	}
		
}
