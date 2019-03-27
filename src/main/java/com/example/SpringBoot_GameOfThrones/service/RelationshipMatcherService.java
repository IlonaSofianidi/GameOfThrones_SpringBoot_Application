package com.example.SpringBoot_GameOfThrones.service;

import com.example.SpringBoot_GameOfThrones.entity.GameCharacter;
import com.example.SpringBoot_GameOfThrones.entity.RelationStory;
import com.example.SpringBoot_GameOfThrones.entity.Relationship;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RelationshipMatcherService {
    UUID findRelationshipBetweenCharacters(String name1, String name2);
    Relationship checkFamilyRelationships(GameCharacter character, GameCharacter character2);
    GameCharacter getCharacterInfo(String name) throws IOException;
    List<RelationStory> getFullRelationshipStory();

}
