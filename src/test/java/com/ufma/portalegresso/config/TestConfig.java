package com.ufma.portalegresso.config;

import com.ufma.portalegresso.infra.SenhaEncoderFake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public SenhaEncoderFake senhaEncoderFake() {
        return new SenhaEncoderFake();
    }
}
