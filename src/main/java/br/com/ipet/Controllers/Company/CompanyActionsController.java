package br.com.ipet.Controllers.Company;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000" })
@RestController
@RequestMapping("/api")
public class CompanyActionsController {

    // Here comes the actions like addProduct, and things like that.
}
