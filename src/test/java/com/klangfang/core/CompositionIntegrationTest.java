package com.klangfang.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "h2")
public class CompositionIntegrationTest {

    @Autowired
    private CompositionRepository compositionRepository;

    @Test
    public void testPersistenceOfSuccess() {
        byte[] data = new byte[]{0,0,0,1};
        Sound sound = new Sound("SoundTitle", "soundxy.3gp", "Riot", 0,
                10000, data);
        Track track = new Track(Arrays.asList(sound));
        Composition composition = new Composition("CompositionTitle", "Venom", Arrays.asList(track));
        Composition afterSave = compositionRepository.save(composition);

        assertThat(afterSave).isNotNull();
        assertThat(afterSave.getId()).isPositive();


        List<Composition> venomsCompositions = compositionRepository.findByCreatorname("Venom");

        //assertThat(venomsCompositions).hasSize(1);
        Composition venomsComposition = venomsCompositions.get(0);
        assertThat(venomsComposition.getTracks()).hasSize(1);
        assertThat(venomsComposition.getTracks().get(0).getSounds()).hasSize(1);
        assertThat(venomsComposition.getFiles()).isNotEmpty();
        //assertThat(venomsComposition.getFiles()).contains(data);
    }
}
