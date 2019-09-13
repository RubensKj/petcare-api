package br.com.ipet.Services;

import br.com.ipet.Models.Evaluation;
import br.com.ipet.Repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    public void save(Evaluation evaluation) { evaluationRepository.save(evaluation); }

    public void removeById(Long id) { evaluationRepository.deleteById(id); }

    public Evaluation findById(Long id) { return evaluationRepository.findById(id).get(); }

    public Page<Evaluation> findByIds(Set<Long> ids, Pageable pageable) { return evaluationRepository.findByIdIn(ids, pageable); }

    public Set<Evaluation> findAllEvaluationFromIds(Set<Long> ids) { return evaluationRepository.findEvaluationByIdIn(ids); }
}
