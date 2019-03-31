package com.example.homework.repository;

import com.example.homework.entity.RelationStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RelationStoryRepository extends JpaRepository<RelationStory, UUID> {
    Optional<RelationStory> getRelationStoriesById(UUID id);
}
