package com.ufma.portalegresso.config;

import com.ufma.portalegresso.infra.SenhaEncoderFake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@Configuration
public class TestConfig {
    @Bean(name = "senhaEncoderFake")
    public SenhaEncoderFake senhaEncoderFake() {
        return new SenhaEncoderFake();
    }
}
