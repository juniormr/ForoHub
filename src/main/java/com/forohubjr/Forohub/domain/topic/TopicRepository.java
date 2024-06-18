package com.forohubjr.Forohub.domain.topic;

import jakarta.persistence.NamedNativeQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Modifying
    @Query(value = "delete from topics t where t.id=:id", nativeQuery = true)
    void deleteTopic(Long id);


}
