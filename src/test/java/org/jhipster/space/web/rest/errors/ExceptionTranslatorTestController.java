package org.jhipster.space.web.rest.errors;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import org.jhipster.space.web.rest.errors.handlers.ProblemHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Controller("/test")
@Secured(SecurityRule.IS_ANONYMOUS)
public class ExceptionTranslatorTestController {

    @Post("/method-argument")
    public void methodArgument(@Valid @Body TestDTO testDTO) {}

    @Get("/missing-servlet-request-part")
    public void missingServletRequestPartException(@Part String part) {}

    @Get("/missing-servlet-request-parameter")
    public void missingServletRequestParameterException(@QueryValue String param) {}

    @Get("/access-denied")
    @Secured("ROLE_ADMIN")
    public void accessdenied() {}

    @Get("/unauthorized")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void unauthorized() {}

    @Get("/response-status")
    public void exceptionWithResponseStatus() {
        throw new TestResponseStatusException();
    }

    @Get("/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }

    @Error(exception = TestResponseStatusException.class)
    public HttpResponse<Problem> errorResponse(HttpRequest request, TestResponseStatusException exception) {
        return ProblemHandler.create(Problem.builder().withStatus(Status.BAD_REQUEST).build(), request, exception);
    }

    @SuppressWarnings("serial")
    public static class TestResponseStatusException extends RuntimeException {}
}
