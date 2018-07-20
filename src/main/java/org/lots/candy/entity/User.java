package org.lots.candy.entity;

public class User {
	
	private String userId;
	private String username;
	private String password;
	private String wallet;
	private String email;
	private String twitterAccount;
	private String facebookAccount;
	private String telegramAccount;
	private String inviteCode;
	private String superInviteCode;
	private String status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPasssword(String password){
		this.password = password;
	}
	
	public String getWallet(){
		return wallet;
	}
	
	public void setWallet(String wallet){
		this.wallet = wallet;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getTwitterAccount(){
		return twitterAccount;
	}
	
	public void setTwitterAccount(String twitterAccount){
		this.twitterAccount = twitterAccount;
	}
	
	public String getFacebookAccount(){
		return facebookAccount;
	}
	
	public void setFacebookAccount(String facebookAccount){
		this.facebookAccount = facebookAccount;
	}
	
	public String getTelegramAccount() {
		return telegramAccount;
	}

	public void setTelegramAccount(String telegramAccount) {
		this.telegramAccount = telegramAccount;
	}

	public String getInviteCode(){
		return inviteCode;
	}
	
	public void setInviteCode(String inviteCode){
		this.inviteCode = inviteCode;
	}
	
	public String getSuperInviteCode(){
		return superInviteCode;
	}
	
	public void setSuperInviteCode(String superInviteCode){
		this.superInviteCode = superInviteCode;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	 
	

}
