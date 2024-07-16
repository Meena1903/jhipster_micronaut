package org.jhipster.space.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.annotation.Property;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.jhipster.space.config.Constants;
import org.jhipster.space.domain.Authority;
import org.jhipster.space.domain.User;
import org.jhipster.space.repository.AuthorityRepository;
import org.jhipster.space.repository.UserRepository;
import org.jhipster.space.security.AuthoritiesConstants;
import org.jhipster.space.service.dto.AdminUserDTO;
import org.junit.jupiter.api.*;

/**
 * Integration tests for {@link UserService}.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIT {

    private static final String DEFAULT_ID = "id1";

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String DEFAULT_LANGKEY = "en";

    @Inject
    private UserRepository userRepository;

    @Inject
    AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setId(DEFAULT_ID);
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        List<Authority> authorities = authorityRepository.findAll();
        if (authorities.isEmpty()) {
            // Set up expected authorities, ADMIN and USER
            Authority admin = new Authority();
            admin.setName(AuthoritiesConstants.ADMIN);
            authorityRepository.save(admin);
            Authority user = new Authority();
            user.setName(AuthoritiesConstants.USER);
            authorityRepository.save(user);
        }
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void assertThatAnonymousUserIsNotGet() {
        user.setLogin(Constants.ANONYMOUS_USER);
        if (!userRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userRepository.saveAndFlush(user);
        }
        final Pageable pageable = Pageable.from(0, (int) userRepository.count());
        final Page<AdminUserDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream().noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
    }
}
