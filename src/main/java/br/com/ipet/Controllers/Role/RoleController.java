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

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://aw-petcare-client.herokuapp.com",
        "https://aw-petcare-business.herokuapp.com",
        "http://aw-petcare-client.herokuapp.com",
        "http://aw-petcare-business.herokuapp.com",
        "https://petcare-client.now.sh/",
        "https://petcare-client.now.sh",
        "https://petcare-business.now.sh"
})
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
