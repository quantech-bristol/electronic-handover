package com.quantech.service.LoggingService;

import com.quantech.model.Log.Log;
import com.quantech.model.Log.LogOperations.LogFilterBackingObject;
import com.quantech.model.Log.LoggableEvent;
import com.quantech.repo.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("logService")
public class LogServiceImpl implements LogService {
    @Autowired
    LogRepository logRepository;

    @Override
    public void saveLog(LoggableEvent logEvent)
    {
        logRepository.save(new Log(logEvent));
    }

    @Override
    public List<Log> returnMatchingLogs(LogFilterBackingObject lo, Set<Long> validIds)
    {
        List<Log> logs;
        if (lo.getOperation() == null){logs = logRepository.getAllByOriginatingUserInAndDateOfEventBetween(validIds,lo.getAfterDate(), lo.getBeforeDate());}
        else {logs = logRepository.getAllByOriginatingUserInAndDateOfEventBetweenAndOperation(validIds,lo.getAfterDate(),lo.getBeforeDate(),lo.getOperation());}
     return logs;
    }
}
