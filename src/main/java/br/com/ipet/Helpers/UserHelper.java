package br.com.ipet.Helpers;

import br.com.ipet.Models.User;
import br.com.ipet.Payload.UserCompleteForm;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.UserService;

import javax.servlet.http.HttpServletRequest;

public class UserHelper {
    public static User updateValidation(User user, UserCompleteForm userJSON) {
        if(userJSON.getCompleteName() != null && !userJSON.getCompleteName().isEmpty()) {
            user.setCompleteName(userJSON.getCompleteName());
        }

        if(userJSON.getCpf() != null && !userJSON.getCpf().isEmpty()) {
            user.setCpf(userJSON.getCpf());
        }

        if(userJSON.getPhoneNumber() != null && !userJSON.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userJSON.getPhoneNumber());
        }

        if(userJSON.getAvatar() != null && !userJSON.getAvatar().isEmpty() || user.getAvatar().equals(userJSON.getEmail())) {
            user.setAvatar(userJSON.getAvatar());
        }

        return user;
    }

    public static User getUserLogged(HttpServletRequest request, UserService userService, JwtProvider jwtProvider) {
        String jwtToken = jwtProvider.getJwt(request);
        if(jwtToken.length() > 0) {
            String usernameUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
            return userService.findByEmail(usernameUserLogged);
        }
        return null;
    }
}
