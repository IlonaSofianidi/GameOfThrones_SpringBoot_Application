package com.example.homework.service;

import com.example.homework.entity.GameCharacter;
import com.example.homework.entity.RelationStory;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipUUIDResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RelationshipMatcherService {
    List<RelationShipUUIDResponse> findRelationshipBetweenCharacters(String name1, String name2);

    List<RelationStory> checkFamilyRelationships(GameCharacter[] character, GameCharacter[] character2);

    GameCharacter[] getCharacterInfo(String name) throws IOException;


    GetRelationShipResponse getRelationShipStory(UUID id);

    Page<RelationStory> getFullRelationshipStoryTest(Pageable pageable);
}
