package rs.ac.uns.ftn.e2.onee2team.security.pki.auth;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.UserType;

public class UserTokenState {
	 private String accessToken;
	    private Long expiresIn;
	    private UserType userType;
	    private String email;

	    public UserType getUserType() {
			return userType;
		}

		public void setUserType(UserType userType) {
			this.userType = userType;
		}

		public String getAccessToken() {
	        return accessToken;
	    }

	    public void setAccessToken(String accessToken) {
	        this.accessToken = accessToken;
	    }

	    public Long getExpiresIn() {
	        return expiresIn;
	    }

	    public UserTokenState() {
			super();
		}

		public UserTokenState(String accessToken, Long expiresIn, UserType userType, String email) {
			super();
			this.accessToken = accessToken;
			this.expiresIn = expiresIn;
			this.userType = userType;
			this.email = email;
		}

		public void setExpiresIn(Long expiresIn) {
	        this.expiresIn = expiresIn;
	    }

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
}
