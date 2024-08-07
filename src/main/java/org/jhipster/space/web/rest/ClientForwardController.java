package org.jhipster.space.web.rest;

import io.micronaut.context.env.Environment;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.Optional;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class ClientForwardController {

    @Inject
    Environment environment;

    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     * @return forward to client {@code index.html}.
     */
    @Get("/{path:[^\\.]*}")
    public Optional<StreamedFile> forward(String path) {
        return environment.getResource("classpath:static/index.html").map(StreamedFile::new);
    }
}
