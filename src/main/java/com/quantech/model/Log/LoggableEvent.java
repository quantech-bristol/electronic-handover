package com.quantech.model.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LoggableEvent extends Serializable
{
    public Long getOriginatingId();
    public OperationTypes getOperation();
    public LocalDateTime getTimeOfOperation();
}
