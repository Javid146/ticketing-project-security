package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    public void onPrePersist(BaseEntity baseEntity){

        //SecurityContextHolder give login user info
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.insertDateTime = LocalDateTime.now();
        baseEntity.lastUpdateDateTime = LocalDateTime.now();

        //if user is authenticated while clicking login btn, then assign its userId to baseEntity userId
        if(authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.insertUserId = ((UserPrincipal) principal).getId();
            baseEntity.lastUpdateUserId =((UserPrincipal) principal).getId();
        }
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.lastUpdateDateTime = LocalDateTime.now();

        if(authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.lastUpdateUserId =((UserPrincipal) principal).getId();
        }
    }
}
