package br.com.ipet.Helpers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class AuthMethods {
    public static ResponseEntity<?> logoutMethod(HttpServletRequest req) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        try {
            if (req.getHeader("Authorization") != null) {
                req.getHeader("Authorization").replace("", "");
                req.logout();
            }
        } catch (ServletException e) {
            return ResponseEntity.ok("false");
        }
        return ResponseEntity.ok("true");
    }
}
