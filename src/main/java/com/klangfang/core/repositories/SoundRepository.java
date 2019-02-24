package com.klangfang.core.repositories;

import com.klangfang.core.entities.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoundRepository extends JpaRepository<Sound, Long> {
}
