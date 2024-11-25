package hello.em.secu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	// Ánh xạ username đến ID
	private final Map<String, String> userToIdMap = new HashMap<>() {
		{
			put("phuong", "admin"); // Admin không cần ID
			put("baophuong", "002"); // Ánh xạ username baophuong -> ID 002
		}
	};

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String redirectUrl = "/";
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				redirectUrl = "/customer/all";
				break;
			} else if (authority.getAuthority().equals("ROLE_USER")) {
				User user = (User) authentication.getPrincipal();
				String userId = userToIdMap.getOrDefault(user.getUsername(), "unknown");
				redirectUrl = "/customer/" + userId;
				break;
			}
		}
		response.sendRedirect(redirectUrl);
	}
}
