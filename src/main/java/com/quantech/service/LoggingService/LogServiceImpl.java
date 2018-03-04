package com.quantech.service.LoggingService;

import com.quantech.model.Log.Log;
import com.quantech.model.Log.LogOperations.LogFilterBackingObject;
import com.quantech.model.Log.LoggableEvent;
import com.quantech.repo.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("logService")
public class LogServiceImpl
{
    @Autowired
    LogRepository logRepository;

    public void saveLog(LoggableEvent logEvent)
    {
        logRepository.save(new Log(logEvent));
    }

    public List<Log> returnMatchingLogs(LogFilterBackingObject lo, List<Long> validIds)
    {
        List<Log> logs;
        if (lo.getOperation() == null){logs = logRepository.getAllByOriginatingUserInAndDateOfEventBetween(validIds,lo.getAfterDate(), lo.getBeforeDate());}
        else {logs = logRepository.getAllByOriginatingUserInAndDateOfEventBetweenAndOperation(validIds,lo.getAfterDate(),lo.getBeforeDate(),lo.getOperation());}
     return logs;
    }
}
