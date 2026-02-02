package org.alpha.omega.student_microservice.infrastructure.adapter.in.web.uri;

import lombok.Getter;
import org.alpha.omega.student_microservice.domain.exception.InvalidUriException;
import org.alpha.omega.student_microservice.infrastructure.adapter.in.web.constant.WebMessages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Getter
@Component
public class UserUriFactory extends UriFactory {

    @Value("${resource.users}")
    private String resourceUsers;

    public URI uriLocation(String path) {
        try {
            return new URI(getApiVersion() + this.resourceUsers + WebMessages.PATH_DELIMITER + path);
        } catch (URISyntaxException e) {
            throw new InvalidUriException("Invalid user location URI");
        }
    }
}
