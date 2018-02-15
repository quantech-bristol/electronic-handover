package com.quantech.service.RiskService;

import com.quantech.model.Risk;
import com.quantech.repo.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RiskServiceImpl implements RiskService {

    @Autowired
    RiskRepository riskRepository;

    @Override
    public List<Risk> getAllRisks() {
        List<Risk> risks = new ArrayList<>();
        riskRepository.findAll().forEach(risks::add);
        return risks;
    }

    @Override
    public Risk getRisk(Long id) {
        return riskRepository.findOne(id);
    }

    @Override
    public void saveRisk(Risk risk) {
        riskRepository.save(risk);
    }

    @Override
    public void deleteRisk(Long id) {
        riskRepository.delete(id);
    }
}
