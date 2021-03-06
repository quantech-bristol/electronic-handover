package com.quantech.service.RiskService;

import com.quantech.model.Risk;
import com.quantech.model.Ward;
import com.quantech.repo.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service("riskService")
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

    @Override
    public void CheckValidity(BindingResult result, Risk risk) {
        if (risk.getName().equals("")) {
            result.rejectValue("name","name.risk","Risk must have a name.");
        }
        if (risk.getAcronym().equals("")) {
            result.rejectValue("acronym","name.acronym","Please enter an acronym for the new risk.");
        }
    }

}
