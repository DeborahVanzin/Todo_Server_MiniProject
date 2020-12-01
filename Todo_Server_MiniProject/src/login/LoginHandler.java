package login;

import java.util.HashMap;
import java.util.Map;

import database.IToDoDatabase;
import model.Token;
import model.User;

public class LoginHandler implements ILoginHandler {
	private final int EXPIRY_TIME_MINUTES = 1;
	
	private IToDoDatabase toDoDatabase; //Dependency Injection
	private Map<String, Token> loggedUsersTokens = new HashMap<>();
	
	public LoginHandler(IToDoDatabase toDoDatabase) {
		this.toDoDatabase = toDoDatabase;
	}

	public Token login(String email, String password) {
		User user = toDoDatabase.getUserByEmail(email);
		if(user == null) {
			return null;
		}
		
		if(!user.getPassword().equals(password)) {
			return null;
		}
		
		Token token = new Token(EXPIRY_TIME_MINUTES);
		loggedUsersTokens.put(email, token);
		return token;
	}

	@Override
	public String validateToken(String token) {
		for(Map.Entry<String, Token> entry : loggedUsersTokens.entrySet()) {
			String entryEmail = entry.getKey();
			Token entryToken = entry.getValue();
			if(entryToken.toString().equals(token)) {
				if(entryToken.isValid()) {
					return entryEmail;
				}
			}
		}
		
		return null;
	}
}
