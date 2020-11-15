package com.klangfang.core;

import com.klangfang.core.service.CompositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "h2")
public class CompositionIntegrationTest {

    @Autowired
    private CompositionRepository compositionRepository;

    @Test
    public void testPersistenceOfSuccess() {
        /*Sound sound = new Sound("SoundTitle", "soundxy.3gp", "Riot", 0,
                10000);
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
    */}
}
