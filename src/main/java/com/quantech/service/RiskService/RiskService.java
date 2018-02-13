package com.quantech.service.RiskService;

import com.quantech.model.Risk;

import java.util.List;

public interface RiskService {

    public List<Risk> getAllRisks();

    public Risk getRisk(Long id);

    public void saveRisk(Risk risk);

    public void deleteRisk(Long id);
}
