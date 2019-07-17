package br.com.ipet.Services;

import br.com.ipet.Models.User;
import br.com.ipet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public void remove(User user) {
        userRepository.delete(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public User findById(Long longID) {
        return userRepository.findById(longID).get();
    }

}
