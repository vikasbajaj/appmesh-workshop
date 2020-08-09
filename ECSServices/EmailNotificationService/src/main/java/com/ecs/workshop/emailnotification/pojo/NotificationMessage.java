package com.ecs.workshop.emailnotification.pojo;

public class NotificationMessage {

	private String subject;
	private String notificationText;
	private String notificationSentToEmailAddress;
	private String addressNotificationTo;
	
	@SuppressWarnings("unused")
	private NotificationMessage() {
	}
	public NotificationMessage(String subject, String notificationText, String notificationSentToEmailAddress,
			String addressNotificationTo) {
		super();
		this.subject = subject;
		this.notificationText = notificationText;
		this.notificationSentToEmailAddress = notificationSentToEmailAddress;
		this.addressNotificationTo = addressNotificationTo;
	}
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public String getNotificationSentToEmailAddress() {
		return notificationSentToEmailAddress;
	}

	public void setNotificationSentToEmailAddress(String notificationSentToEmailAddress) {
		this.notificationSentToEmailAddress = notificationSentToEmailAddress;
	}

	public String getAddressNotificationTo() {
		return addressNotificationTo;
	}

	public void setAddressNotificationTo(String addressNotificationTo) {
		this.addressNotificationTo = addressNotificationTo;
	}
}
