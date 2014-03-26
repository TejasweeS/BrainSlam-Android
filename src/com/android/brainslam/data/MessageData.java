package com.android.brainslam.data;


public class MessageData {
	String messageTitle, messageTime, message; 
	//messageHeader;
//	public String getMessageHeader() {
//		return messageHeader;
//	}
//
//
//	public void setMessageHeader(String messageHeader) {
//		this.messageHeader = messageHeader;
//	}
	int imageDrawable;
//	boolean isTypeHeader;
	boolean isColapsed;
	
	public MessageData(String messageTitle,String messageTime,String message,
	int imageDrawable,

	boolean isColapsed)
	{
//		this.messageHeader =messageHeader;
		this.messageTime = messageTime;
		this.messageTitle = messageTitle;
		this.message = message;
		this.imageDrawable = imageDrawable;
		this.isColapsed = isColapsed;
//		this.isTypeHeader = isTypeHeader;
	}
	
	
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getImageDrawable() {
		return imageDrawable;
	}
	public void setImageDrawable(int imageDrawable) {
		this.imageDrawable = imageDrawable;
	}
//	public boolean isTypeHeader() {
//		return isTypeHeader;
//	}
//	public void setTypeHeader(boolean isTypeHeader) {
//		this.isTypeHeader = isTypeHeader;
//	}
	public boolean isColapsed() {
		return isColapsed;
	}
	public void setColapsed(boolean isColapsed) {
		this.isColapsed = isColapsed;
	}
	
}
