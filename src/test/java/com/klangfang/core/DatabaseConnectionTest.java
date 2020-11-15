package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.service.CompositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = "h2")
public class DatabaseConnectionTest {

    @Autowired
    private CompositionRepository compositionRepository;

    @Test
    public void testConnectionOfSuccess() {
        Sound sound = new Sound("Riot", 0,
                10000, 1, "S3_URL");
        Composition composition = new Composition("Composition in production", "Venom", List.of(sound), 4);
        Composition afterSave = compositionRepository.save(composition);

        assertThat(afterSave).isNotNull();
        assertThat(afterSave.getId()).isPositive();
    }
}
