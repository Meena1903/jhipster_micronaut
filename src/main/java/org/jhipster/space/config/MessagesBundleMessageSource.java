package org.jhipster.space.config;

import io.micronaut.context.i18n.ResourceBundleMessageSource;
import jakarta.inject.Singleton;

@Singleton
public class MessagesBundleMessageSource extends ResourceBundleMessageSource {

    public MessagesBundleMessageSource() {
        super("i18n.messages");
    }
}
