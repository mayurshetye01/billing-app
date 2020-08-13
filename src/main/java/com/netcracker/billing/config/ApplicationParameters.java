package com.netcracker.billing.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Component
public class ApplicationParameters {
    @Value("${parcel-charges}")
    private BigDecimal parcelCharges;

    @Value("${system-username}")
    private String systemUsername;

    @Value("${system-password}")
    private String systemPassword;

}
