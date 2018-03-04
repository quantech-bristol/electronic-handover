package com.quantech.model.Log.LogOperations;

import com.quantech.model.Log.OperationTypes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class LogFilterBackingObject {
    private Long id;
    private Long originatingUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginatingUser() {
        return originatingUser;
    }

    public void setOriginatingUser(Long originatingUser) {
        this.originatingUser = originatingUser;
    }

    public OperationTypes getOperation() {
        return operation;
    }

    public void setOperation(OperationTypes operation) {
        this.operation = operation;
    }


    private OperationTypes operation;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime beforeDate;

    public LocalDateTime getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(LocalDateTime beforeDate) {
        this.beforeDate = beforeDate;
    }

    public LocalDateTime getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(LocalDateTime afterDate) {
        this.afterDate = afterDate;
    }
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime afterDate;

}
