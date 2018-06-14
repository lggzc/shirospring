package demo.model;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;  
	private String username;  
	private String password;
	private String salt;
	private Boolean locked = Boolean.FALSE; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getCredentialsSalt() {  
        return username + salt;  
    }  
  
    public Boolean getLocked() {  
        return locked;  
    }  
  
    public void setLocked(Boolean locked) {  
        this.locked = locked;  
    }  
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + username + ", password=" + password + "]";
	}  
	
	
}
