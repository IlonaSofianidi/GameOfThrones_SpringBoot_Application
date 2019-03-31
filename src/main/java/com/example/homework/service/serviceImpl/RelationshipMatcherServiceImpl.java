package com.example.homework.service.serviceImpl;

import com.example.homework.entity.GameCharacter;
import com.example.homework.entity.RelationStory;
import com.example.homework.entity.Relationship;
import com.example.homework.exception.GameCharacterNotFoundException;
import com.example.homework.exception.RelationShipStoryNotFound;
import com.example.homework.repository.RelationStoryRepository;
import com.example.homework.service.RelationshipMatcherService;
import com.example.homework.util.OKHttpGameOfThronesClient;
import com.example.homework.util.utilImpl.OKHttpGameOfThronesClientImpl;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipUUIDResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@CacheConfig(cacheNames = {"storyRequests"})
public class RelationshipMatcherServiceImpl implements RelationshipMatcherService {

    private static final String EXTRA_URL = "https://anapioficeandfire.com/api/characters";
    private RelationStoryRepository relationStoryRepository;
    private OKHttpGameOfThronesClient client;

    @Autowired
    public void setRelationStoryRepository(RelationStoryRepository relationStoryRepository) {
        this.relationStoryRepository = relationStoryRepository;
    }

    @Autowired
    public void setOKHttpGameOfThronesClient(OKHttpGameOfThronesClientImpl client) {
        this.client = client;
    }
    @Cacheable
    @Override
    public RelationShipUUIDResponse findRelationshipBetweenCharacters(String name1, String name2) {
        GameCharacter gameCharacter = getCharacterInfo(name1);
        GameCharacter gameCharacter2 = getCharacterInfo(name2);
        Relationship relationship = checkFamilyRelationships(gameCharacter, gameCharacter2);
        String msg = String.format("GameCharacter %s and GameCharacter %s RelationShip: %s", gameCharacter.getName(), gameCharacter2.getName(), relationship);
        log.info(msg);
        RelationStory relationStory = createRelationStory(gameCharacter, gameCharacter2, relationship);
        RelationStory save = relationStoryRepository.save(relationStory);
        return RelationShipUUIDResponse.builder()
                .characterName1(name1)
                .characterName2(name2)
                .id(save.getId()).build();
    }

    @Override
    public GetRelationShipResponse getRelationShipStory(UUID id) {
        Optional<RelationStory> relationStoriesById = relationStoryRepository.getRelationStoriesById(id);
        if (!relationStoriesById.isPresent()) {
            throw new RelationShipStoryNotFound("UUID: " + id.toString());
        }
        RelationStory relationStory = relationStoriesById.get();
        String story = createStoryMessage(relationStory);
        log.info("RelationStory: " + story);
        return GetRelationShipResponse.builder().storyMessage(story).build();
    }

    private RelationStory createRelationStory(GameCharacter gameCharacter, GameCharacter gameCharacter2, Relationship relationship) {
        RelationStory relationStory = new RelationStory();
        int firstCharacterId = parseUrlId(gameCharacter.getUrl());
        int secondCharacterId = parseUrlId(gameCharacter2.getUrl());
        relationStory.setIdCharacter1(firstCharacterId);
        relationStory.setIdCharacter2(secondCharacterId);
        relationStory.setCharacterName1(gameCharacter.getName());
        relationStory.setCharacterName2(gameCharacter2.getName());
        relationStory.setRelationship(relationship);
        return relationStory;
    }

    @Override
    public List<GetRelationShipResponse> getFullRelationshipStory() {
        List<RelationStory> all = relationStoryRepository.findAll();
        List<GetRelationShipResponse> responses = new ArrayList<>();
        for (RelationStory story : all) {
            String storyMsg = createStoryMessage(story);
            log.info("RelationStory: " + story);
            responses.add(GetRelationShipResponse.builder().storyMessage(storyMsg).build());
        }
        return responses;
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
        String userRequest = createUserRequest(name);
        String fetchCharacterData = null;
        try {
            fetchCharacterData = client.fetchCharacterData(userRequest);
        } catch (IOException e) {
            log.info("Http request fail " + e);
        }
        GameCharacter gameCharacter = parseJson(fetchCharacterData);
        if (gameCharacter == null) {
            throw new GameCharacterNotFoundException("name: " + name);
        }
        log.info("Get character info: " + gameCharacter.toString());
        return gameCharacter;
    }

    private String createStoryMessage(RelationStory story) {
        String msg = null;
        if (story.getRelationship() == Relationship.NONE) {
            msg = String.format("GameCharacter %s(%d) and GameCharacter%s(%d) no relationship found [ UUID: %s]", story.getCharacterName1(), story.getIdCharacter1(), story.getCharacterName2(),
                    story.getIdCharacter2(), story.getId().toString());
        } else {
            msg = String.format("GameCharacter %s(%d) is %s to GameCharacter%s(%d) [UUID:%s ]", story.getCharacterName1(),
                    story.getIdCharacter1(), story.getRelationship().toString(),
                    story.getCharacterName2(), story.getIdCharacter2(), story.getId().toString());
        }
        return msg;
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

    private int parseUrlId(String url) {
        int result = 0;
        Pattern id = Pattern.compile("characters/(\\d+)");
        Matcher m = id.matcher(url);
        if (m.find()) {
            result = Integer.parseInt(m.group(1));
        }
        return result;
    }
}