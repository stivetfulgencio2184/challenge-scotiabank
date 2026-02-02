package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Getter
@Component
public abstract class UriFactory {

    @Value("${api.version}")
    private String apiVersion;

    public abstract URI uriLocation(String path);
}
