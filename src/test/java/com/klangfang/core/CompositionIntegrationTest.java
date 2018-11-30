package com.klangfang.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompositionIntegrationTest {

    @Autowired
    private CompositionRepository compositionRepository;

    @Test
    public void testPersistenceOfSuccess() {
        Sound sound = new Sound(0, 10000, "soundxy", "Riot");
        Track track = new Track(Arrays.asList(sound));
        Composition composition = new Composition("Venom", Arrays.asList(track));
        Composition afterSave = compositionRepository.save(composition);

        assertThat(afterSave).isNotNull();
        assertThat(afterSave.getId()).isPositive();


        List<Composition> venomsCompositions = compositionRepository.findByCreatorname("Venom");

        assertThat(venomsCompositions).hasSize(1);
        Composition venomsComposition = venomsCompositions.get(0);
        assertThat(venomsComposition.getTracks()).hasSize(1);
        assertThat(venomsComposition.getTracks().get(0).getSounds()).hasSize(1);
    }
}
