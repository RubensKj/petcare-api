package br.com.ipet.Controllers.Role;

import br.com.ipet.Models.Role;
import br.com.ipet.Models.RoleName;
import br.com.ipet.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000" })
@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/server/configuration/setroles")
    public ResponseEntity<?> setRoles() {
        roleRepository.save(new Role(RoleName.ROLE_USER));
        roleRepository.save(new Role(RoleName.ROLE_OWNER));
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        return new ResponseEntity<>("Role added", HttpStatus.OK);
    }

}
