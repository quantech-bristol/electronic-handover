package com.quantech.repo;

import com.quantech.model.Log.Log;
import com.quantech.model.Log.OperationTypes;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface LogRepository extends CrudRepository<Log, Long>
{
     List<Log> getAllByOriginatingUserInAndDateOfEventBetweenAndOperation(Set<Long> ids, LocalDateTime before, LocalDateTime after, OperationTypes op);
    List<Log> getAllByOriginatingUserInAndDateOfEventBetween(Set<Long> ids, LocalDateTime before, LocalDateTime after);

}
