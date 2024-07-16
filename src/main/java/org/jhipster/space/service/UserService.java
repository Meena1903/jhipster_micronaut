package org.jhipster.space.service;

import io.micronaut.cache.CacheManager;
import io.micronaut.cache.interceptor.ParametersKey;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import org.jhipster.space.config.Constants;
import org.jhipster.space.domain.Authority;
import org.jhipster.space.domain.User;
import org.jhipster.space.repository.AuthorityRepository;
import org.jhipster.space.repository.UserRepository;
import org.jhipster.space.security.SecurityUtils;
import org.jhipster.space.service.dto.AdminUserDTO;
import org.jhipster.space.service.dto.UserDTO;
import org.jhipster.space.web.rest.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for managing users.
 */
@Singleton
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public User syncUserWithIdP(User user) {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
        Collection<String> dbAuthorities = getAuthorities();
        Collection<String> userAuthorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        for (String authority : userAuthorities) {
            if (!dbAuthorities.contains(authority)) {
                log.debug("Saving authority '{}' in local database", authority);
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            log.debug("Updating user '{}' in local database", user.getLogin());
            updateUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getLangKey(), user.getImageUrl());
        } else {
            log.debug("Saving user '{}' in local database", user.getLogin());
            userRepository.save(user);
            this.clearUserCaches(user);
        }
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                user = userRepository.update(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @ReadOnly
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findByLoginNotEqual(Constants.ANONYMOUS_USER, pageable);
        return Page.of(
            userPage.getContent().stream().map(AdminUserDTO::new).collect(Collectors.toList()),
            pageable,
            userPage.getTotalSize()
        );
    }

    @ReadOnly
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findByLoginNotEqual(Constants.ANONYMOUS_USER, pageable);
        return Page.of(userPage.getContent().stream().map(UserDTO::new).collect(Collectors.toList()), pageable, userPage.getTotalSize());
    }

    @ReadOnly
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    @ReadOnly
    public Optional<User> getUserWithAuthorities(String id) {
        return userRepository.findOneById(id);
    }

    @ReadOnly
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_CACHE)).invalidate(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_CACHE)).invalidate(user.getEmail());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_CACHE)).invalidate(
            new ParametersKey(user.getLogin(), user.getEmail())
        );
    }

    public Optional<String> getCurrentUserLogin() {
        return SecurityUtils.getCurrentUserLogin();
    }
}
