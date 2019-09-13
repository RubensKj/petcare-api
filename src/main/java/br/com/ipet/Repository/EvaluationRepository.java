package br.com.ipet.Repository;

import br.com.ipet.Models.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface EvaluationRepository extends PagingAndSortingRepository<Evaluation, Long> {
    Page<Evaluation> findByIdIn(Set<Long> ids, Pageable pageable);
    Set<Evaluation> findEvaluationByIdIn(Set<Long> ids);
}
