package com.quantech.model.Log;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Log
{


    @Id
    @GeneratedValue
    Long id;
    @NotNull
    Long originatingUser;
    @NotNull
    OperationTypes operation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDateTime dateOfEvent;

    @Transient
    LoggableEvent loggedEvent;

    byte[] serializedObject;

    public Log()
    {

    }
    public Log(LoggableEvent event)
    {
        originatingUser = event.getOriginatingId();
        operation = event.getOperation();
        dateOfEvent = event.getTimeOfOperation();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(event);
            out.flush();
            serializedObject = bos.toByteArray();
        }
        catch (Exception ex)
        {
            System.out.print(ex.toString());
        }
        finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public LoggableEvent returnLoggedOperation()
    {
        if (loggedEvent != null){return loggedEvent;}
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
            loggedEvent = (LoggableEvent)o;
        }
        catch (Exception ex)
        {
            System.out.print(ex.toString());
        }
        finally {
            try {
                if (in != null) {
                    in.close();

                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return loggedEvent;

    }
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

    public LocalDateTime getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(LocalDateTime dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public byte[] getSerializedObject() {
        return serializedObject;
    }

    public void setSerializedObject(byte[] serializedObject) {
        this.serializedObject = serializedObject;
    }

    @Override
    public String toString()
    {
        if (loggedEvent == null){returnLoggedOperation();}
        return loggedEvent.toString();
    }
}
