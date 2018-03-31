package com.quantech.service.RiskService;

import com.quantech.model.Risk;
import com.quantech.model.Ward;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface RiskService {

    List<Risk> getAllRisks();

    Risk getRisk(Long id);

    void saveRisk(Risk risk);

    void deleteRisk(Long id);

    void CheckValidity(BindingResult result, Risk risk);

}
