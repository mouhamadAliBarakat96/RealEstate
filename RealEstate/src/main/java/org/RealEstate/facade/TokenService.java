package org.RealEstate.facade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.RealEstate.model.TokenEntity;
import org.RealEstate.model.User;

@Stateless
public class TokenService extends AbstractFacade<TokenEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	// private static final int TOKEN_LENGTH = 32;
	private static final int EXPIRATION_TIME_MINUTES = 1000;

	public TokenService() {
		super(TokenEntity.class);
	}

	public String generateToken(User user) throws Exception {
		// byte[] randomBytes = new byte[TOKEN_LENGTH];
		// new SecureRandom().nextBytes(randomBytes);
		// Base64.getEncoder().encodeToString(randomBytes);
		String tokenValue = user.getUserName().concat(UUID.randomUUID().toString());

		TokenEntity userToken = findTokenByUser(user);

		if (userToken == null) {
			userToken = new TokenEntity();
			userToken.setUser(user);
			userToken.setExpirationDate(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MINUTES * 60000));
		}

		userToken.setTokenValue(tokenValue);
		userToken = save(userToken);

		return userToken.getTokenValue();
	}

	public TokenEntity findTokenByUser(User user) {
		try {
			TypedQuery<TokenEntity> query = em.createNamedQuery(TokenEntity.FIND_BY_USER, TokenEntity.class);
			query.setParameter("pUser", user);
			List<TokenEntity> tokens = query.getResultList();
			if (tokens.isEmpty()) {
				return null;
			} else {
				return tokens.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public User validateToken(String tokenValue) {
		TokenEntity token = findByToken(tokenValue);
		if (token != null) {// && token.getExpirationDate().after(new Date())
			return token.getUser();
		}
		return null;
	}

	private TokenEntity findByToken(String tokenValue) {
		try {
			TypedQuery<TokenEntity> query = em.createNamedQuery(TokenEntity.FIND_BY_TOKEN, TokenEntity.class);
			query.setParameter("pTokenValue", tokenValue);
			List<TokenEntity> tokens = query.getResultList();
			if (tokens.isEmpty()) {
				return null;
			} else {
				return tokens.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void invalidateToken(String tokenValue) throws Exception {
		TokenEntity token = findByToken(tokenValue);
		if (token != null) {
			remove(token);
		}
	}

	public List<TokenEntity> findAllTokenByUser(User user) {
		try {
			TypedQuery<TokenEntity> query = em.createNamedQuery(TokenEntity.FIND_BY_USER, TokenEntity.class);
			query.setParameter("pUser", user);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void deleteByUserId(User user) {
		try {
			List<TokenEntity> tokens = findAllTokenByUser(user);
			if (tokens != null && tokens.size() > 0) {
				remove(tokens);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
