package com.github.superz97.tracker.repository;

import com.github.superz97.tracker.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByUserIdAndExerciseId(Long userId, Long exerciseId);

    List<UserProgress> findByUserId(Long userId);

    @Query("""
    SELECT up FROM UserProgress up WHERE up.user.id = :userId
    ORDER BY up.totalVolume DESC LIMIT :limit
""")
    List<UserProgress> findTopExercisesByVolume(
            @Param("userId") Long userId,
            @Param("limit") int limit
    );

}
