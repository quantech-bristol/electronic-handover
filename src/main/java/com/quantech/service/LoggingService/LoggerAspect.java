package com.quantech.service.LoggingService;

import com.quantech.misc.AuthFacade.Authenticationfacade;
import com.quantech.model.Log.LogOperations.AddUser;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserEntry;
import com.quantech.model.user.UserFormBackingObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
public class LoggerAspect
{
    @Autowired
    Authenticationfacade auth;

    @Autowired
    LogServiceImpl logService;

    @Pointcut("execution(public com.quantech.model.user.UserCore com.quantech.service.UserService.UserServiceImpl.createUser(com.quantech.model.user.UserFormBackingObject))")
    public void classMethod(){}

    @AfterReturning(value = "classMethod()",returning = "returnValue")
    public void logUserCreation(JoinPoint jp, UserCore returnValue)
    {
        UserEntry user = (UserEntry)auth.getAuthentication().getPrincipal();
        UserCore userAdded = returnValue;
        AddUser op = new AddUser(user.getUserCore().getId(), LocalDateTime.now(), userAdded.getId(),userAdded.getAuthorityStrings(), userAdded.getUsername(), userAdded.getEmail(), userAdded.getFirstName(),userAdded.getLastName(), userAdded.getTitle());
        logService.saveLog(op);
    }
}
