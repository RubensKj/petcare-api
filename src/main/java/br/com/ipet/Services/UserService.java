package br.com.ipet.Services;

import br.com.ipet.Models.User;
import br.com.ipet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public User findById(Long longID) {
        return userRepository.findById(longID).get();
    }

    public List<User> findAll() { return userRepository.findAll(); }

    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }
}
