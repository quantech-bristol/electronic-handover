package com.quantech.service.LoggingService;

import com.quantech.model.Log.Log;
import com.quantech.model.Log.LogOperations.LogFilterBackingObject;
import com.quantech.model.Log.LoggableEvent;

import java.util.List;
import java.util.Set;

public interface LogService {
    void saveLog(LoggableEvent logEvent);

    List<Log> returnMatchingLogs(LogFilterBackingObject lo, Set<Long> validIds);
}
