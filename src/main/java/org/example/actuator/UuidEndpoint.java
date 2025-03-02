package org.example.actuator;

import java.util.UUID;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "uuid")
public class UuidEndpoint {
    @ReadOperation
    public UUID uuid() {
        return UUID.randomUUID();
    }
}