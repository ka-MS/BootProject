package com.example.bootproject.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationYamlRead {
    @Getter
    static int modifiableDateValue;
    @Getter
    static int modificationLimitValue;

    @Value("${constant-data.modifiable-date-value}")
    public void setModifiedDate(int modifiableDateValue){
        ApplicationYamlRead.modifiableDateValue = modifiableDateValue;

    }
    @Value("${constant-data.modification-limit-value}")
    public void setModificationLimit(int modificationLimitValue){
        ApplicationYamlRead.modificationLimitValue = modificationLimitValue;
    }
}
