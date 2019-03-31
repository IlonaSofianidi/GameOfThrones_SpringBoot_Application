package com.example.homework.service;

import com.example.homework.entity.GameCharacter;
import com.example.homework.entity.Relationship;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipUUIDResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RelationshipMatcherService {
    RelationShipUUIDResponse findRelationshipBetweenCharacters(String name1, String name2);

    Relationship checkFamilyRelationships(GameCharacter character, GameCharacter character2);

    GameCharacter getCharacterInfo(String name) throws IOException;

    List<GetRelationShipResponse> getFullRelationshipStory();

    GetRelationShipResponse getRelationShipStory(UUID id);

}
