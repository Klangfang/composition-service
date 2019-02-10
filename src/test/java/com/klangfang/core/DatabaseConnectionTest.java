package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.entities.Track;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "h2")
public class DatabaseConnectionTest {

    @Autowired
    private CompositionRepository compositionRepository;

    @Test
    public void testConnectionOfSuccess() {
        Sound sound = new Sound("SoundTitle", "soundxy.3gp", "Riot", 0,
                10000, new byte[]{});
        Track track = new Track(Arrays.asList(sound));
        Composition composition = new Composition("Composition in production", "Venom", Arrays.asList(track));
        Composition afterSave = compositionRepository.save(composition);

        assertThat(afterSave).isNotNull();
        assertThat(afterSave.getId()).isPositive();
    }
}
