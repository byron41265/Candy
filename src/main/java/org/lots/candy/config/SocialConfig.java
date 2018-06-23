package org.lots.candy.config;

import java.util.UUID;

import org.lots.candy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;

import javax.sql.DataSource;

@Configuration
@EnableSocial
@Order(1)
public class SocialConfig extends SocialConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Override
	public UserIdSource getUserIdSource() {
		return new SessionIdUserIdSource();
	}

	private static final class SessionIdUserIdSource implements UserIdSource {
		@Override
		public String getUserId() {
			RequestAttributes request = RequestContextHolder.currentRequestAttributes();
			User user = (User) request.getAttribute(Constant.USER_SESSION_NAME,	RequestAttributes.SCOPE_SESSION);
			String userid = user.getUserId();
			if (userid == null) {
				userid = UUID.randomUUID().toString();
				request.setAttribute(Constant.USER_SESSION_NAME,	userid, RequestAttributes.SCOPE_SESSION);
			}
			return userid;
		}
	}
	
	    @Override
	    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
	        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,  connectionFactoryLocator, Encryptors.noOpText());
//	        if (myConnectionSignUp != null) {
//	            repository.setConnectionSignUp(myConnectionSignUp);
//	        }
	        return repository;
	    }

}
