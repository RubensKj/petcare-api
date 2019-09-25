package br.com.ipet.Controllers.Evaluation;

import br.com.ipet.Models.Company;
import br.com.ipet.Models.Evaluation;
import br.com.ipet.Models.Order;
import br.com.ipet.Payload.EvaluationWithCNPJForm;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.EvaluationService;
import br.com.ipet.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000", "https://aw-petcare-client.herokuapp.com/", "https://aw-petcare-business.herokuapp.com/"})
@RestController
@RequestMapping("/api")
public class EvaluationCrudController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/evaluation-create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> saveEvaluation(@Valid @RequestBody EvaluationWithCNPJForm evaluationPayLoad, HttpServletRequest req) {
        System.out.println(evaluationPayLoad.getCnpj());
        // Token from user
        String jwtToken = jwtProvider.getJwt(req);
        if (!evaluationPayLoad.getCnpj().isEmpty() && !jwtToken.isEmpty()) {
            if (!(evaluationPayLoad.getRate() < 0 || evaluationPayLoad.getRate() > 5)) {
                // Find company of evaluation
                Company company = companyService.findByCnpj(evaluationPayLoad.getCnpj());
                // Order's evaluation
                Order order = orderService.findById(evaluationPayLoad.getIdOfOrder());
                Evaluation newEvaluation = new Evaluation(evaluationPayLoad.getIdOfOrder(), evaluationPayLoad.getIdFromUserEvaluated(), evaluationPayLoad.getNameOfUser(), evaluationPayLoad.getRate(), evaluationPayLoad.getDescription());
                evaluationService.save(newEvaluation);

                double totalRates = evaluationPayLoad.getRate();
                double rateCalculated = 0;

                if (company != null) {
                    // Finding all evaluation from company
                    Set<Evaluation> allEvaluationFromIds = evaluationService.findAllEvaluationFromIds(company.getEvaluations());
                    // Summing all rates
                    double rateFromAllEvaluation = allEvaluationFromIds.stream().mapToDouble(Evaluation::getRate).sum();
                    // rate plus the new evaluation's rate
                    totalRates = totalRates + rateFromAllEvaluation;
                    // Finally calculated
                    rateCalculated = totalRates / (company.getEvaluations().size() + 1);

                }

                BigDecimal bd = new BigDecimal(rateCalculated).setScale(1, RoundingMode.HALF_UP);
                double rateCalcAndFormatted = bd.doubleValue();

                // Setting all
                company.setRate(rateCalcAndFormatted);
                company.getEvaluations().add(newEvaluation.getId());
                order.setEvaluated(true);

                // Saving to edit
                companyService.save(company);
                orderService.save(order);
                return ResponseEntity.ok("Evaluation was made successfully");
            }
        }
        return ResponseEntity.ok("Something happen during the evaluation");
    }

    @GetMapping("/evaluations/{page}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public Page<Evaluation> getAllEvaluationFromCompany(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        String jwt = jwtProvider.getJwt(req);
        if (jwt != null) {
            String emailFromCompanyOwner = jwtProvider.getEmailFromJwtToken(jwt);
            Company company = companyService.findByOwnerEmail(emailFromCompanyOwner);
            Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdEvaluationAt").descending());
            return evaluationService.findByIds(company.getEvaluations(), pageable);
        } else {
            return null;
        }
    }
}
