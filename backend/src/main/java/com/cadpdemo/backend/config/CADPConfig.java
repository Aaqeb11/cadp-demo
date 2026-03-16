package com.cadpdemo.backend.config;

import com.centralmanagement.CentralManagementProvider;
import com.centralmanagement.RegisterClientParameters;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CADPConfig {

    @Value("${cadp.server}")
    private String server;

    @Value("${cadp.registration-token}")
    private String registrationToken;

    @PostConstruct
    public void registerCADPClient() {
        try {
            RegisterClientParameters params =
                new RegisterClientParameters.Builder(
                    server,
                    registrationToken.toCharArray()
                ).build();
            new CentralManagementProvider(params).addProvider();
            System.out.println("CADP client registered successfully.");
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to register CADP client: " + e.getMessage(),
                e
            );
        }
    }
}
