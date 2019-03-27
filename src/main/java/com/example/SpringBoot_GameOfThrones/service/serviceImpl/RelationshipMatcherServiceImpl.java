package com.example.SpringBoot_GameOfThrones.service.serviceImpl;

import com.example.SpringBoot_GameOfThrones.entity.GameCharacter;
import com.example.SpringBoot_GameOfThrones.entity.RelationStory;
import com.example.SpringBoot_GameOfThrones.entity.Relationship;
import com.example.SpringBoot_GameOfThrones.service.RelationshipMatcherService;
import com.example.SpringBoot_GameOfThrones.util.utilImpl.OKHttpGameOfThronesClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
public class RelationshipMatcherServiceImpl implements RelationshipMatcherService {
    private static final String EXTRA_URL = "https://anapioficeandfire.com/api/characters";

    @Override
    public UUID findRelationshipBetweenCharacters(String name1, String name2) {
        GameCharacter gameCharacter = getCharacterInfo(name1);
        GameCharacter gameCharacter2 = getCharacterInfo(name2);
        Relationship relationship = checkFamilyRelationships(gameCharacter, gameCharacter2);
        String msg = String.format("GameCharacter %s and GameCharacter %s RelationShip: %s", gameCharacter.getName(),
                gameCharacter2.getName(), relationship);
        log.info(msg);
        //TODO save relationship to db ,return id
        return null;
    }

    @Override
    public List<RelationStory> getFullRelationshipStory() {
        //TODO get all relationships from db
        return null;
    }

    @Override
    public Relationship checkFamilyRelationships(GameCharacter gameCharacter, GameCharacter gameCharacter2) {
        String urlCharacter = gameCharacter.getUrl();
        if (gameCharacter2.getFather().equals(urlCharacter)) {
            return Relationship.FATHER;
        } else if (gameCharacter2.getMother().equals(urlCharacter)) {
            return Relationship.MOTHER;
        } else if (gameCharacter2.getSpouse().equals(urlCharacter)) {
            return Relationship.SPOUSE;
        } else {
            return Relationship.NONE;
        }
    }

    @Override
    public GameCharacter getCharacterInfo(String name) {
        OKHttpGameOfThronesClientImpl client = new OKHttpGameOfThronesClientImpl();
        String userRequest = createUserRequest(name);
        String fetchCharacterData = null;
        try {
            fetchCharacterData = client.fetchCharacterData(userRequest);
        } catch (IOException e) {
            log.info("Http request fail " + e);
        }
        GameCharacter gameCharacter = parseJson(fetchCharacterData);
        if (gameCharacter == null) {
            //TODO throw new CustomException ;
        }
        log.info("Get character info: " + gameCharacter.toString());
        return gameCharacter;
    }

    private String createUserRequest(String name) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(EXTRA_URL).newBuilder();
        urlBuilder.addQueryParameter("name", name);
        String url = urlBuilder.build().toString();
        log.info("UserRequest: " + url);
        return url;
    }

    private GameCharacter parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        GameCharacter[] gameCharacter = null; //if there are some characters return first
        try {
            gameCharacter = objectMapper.readValue(json, GameCharacter[].class);
        } catch (IOException e) {
            log.info("Fail to parse json " + e);
        }
        if (gameCharacter != null && gameCharacter.length > 0) {
            return gameCharacter[0];
        } else {
            return null;
        }

    }
}