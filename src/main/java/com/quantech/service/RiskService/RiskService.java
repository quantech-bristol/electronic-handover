package com.quantech.service.RiskService;

import com.quantech.model.Risk;

import java.util.List;

public interface RiskService {

    List<Risk> getAllRisks();

    Risk getRisk(Long id);

    void saveRisk(Risk risk);

    void deleteRisk(Long id);
}
