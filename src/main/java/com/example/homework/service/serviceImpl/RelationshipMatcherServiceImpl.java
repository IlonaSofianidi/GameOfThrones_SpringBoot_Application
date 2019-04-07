package com.example.homework.service.serviceImpl;

import com.example.homework.config.WebProperties;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@CacheConfig(cacheNames = {"storyRequests"})
public class RelationshipMatcherServiceImpl implements RelationshipMatcherService {

    private String extraUrl;
    private WebProperties webProperties;
    private RelationStoryRepository relationStoryRepository;
    private OKHttpGameOfThronesClient client;

    @Autowired
    public void setWebProperties(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Autowired
    public void setRelationStoryRepository(RelationStoryRepository relationStoryRepository) {
        this.relationStoryRepository = relationStoryRepository;
    }

    @Autowired
    public void setOKHttpGameOfThronesClient(OKHttpGameOfThronesClientImpl client) {
        this.client = client;
    }

    @PostConstruct
    public void initProp() {
        extraUrl = webProperties.gameOfThronesUrl;
    }

    @Cacheable
    @Override
    public List<RelationShipUUIDResponse> findRelationshipBetweenCharacters(String name1, String name2) {
        List<RelationShipUUIDResponse> responses = new ArrayList<>();
        GameCharacter[] gameCharacterList = getCharacterInfo(name1);
        GameCharacter[] gameCharacter2List = getCharacterInfo(name2);
        List<RelationStory> relationStories = checkFamilyRelationships(gameCharacterList, gameCharacter2List);
        //save all relation stories to db
        for (RelationStory story : relationStories) {
            RelationStory save = relationStoryRepository.save(story);
            String characterNameResponse1 = String.format("%s (id:%d)", story.getCharacterName1(), story.getIdCharacter1());
            String characterNameResponse2 = String.format("%s (id:%d)", story.getCharacterName2(), story.getIdCharacter2());
            String msg = String.format("GameCharacter %s and GameCharacter %s RelationShip: %s", characterNameResponse1, characterNameResponse2, story.getRelationship());
            log.info(msg);
            responses.add(RelationShipUUIDResponse.builder()
                    .characterName1(characterNameResponse1)
                    .characterName2(characterNameResponse2)
                    .id(save.getId()).build());
        }

        return responses;
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
        return GetRelationShipResponse.builder().storyMessage(story).id(relationStory.getId()).build();
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

    public Page<RelationStory> getFullRelationshipStoryTest(Pageable pageable) {
        Page<RelationStory> allBy = relationStoryRepository.findAllBy(pageable);
        return allBy;

    }

    public List<RelationStory> checkFamilyRelationships(GameCharacter[] gameCharacter, GameCharacter[] gameCharacter2) {
        List<RelationStory> list = new ArrayList<>();
        //take all characters with the same name from list 1
        for (GameCharacter character1 : gameCharacter) {
            //get url of each characters
            String urlCharacter = character1.getUrl();
            //match relationship with each character with the same name from list 2
            for (GameCharacter character2 : gameCharacter2) {
                if (character2.getFather().equals(urlCharacter)) {
                    list.add(createRelationStory(character1, character2, Relationship.FATHER));
                } else if (character2.getMother().equals(urlCharacter)) {
                    list.add(createRelationStory(character1, character2, Relationship.MOTHER));
                } else if (character2.getSpouse().equals(urlCharacter)) {
                    list.add(createRelationStory(character1, character2, Relationship.SPOUSE));
                } else {
                    list.add(createRelationStory(character1, character2, Relationship.NONE));
                }
            }
        }
        return list;
    }

    @Override
    public GameCharacter[] getCharacterInfo(String name) {
        String userRequest = createUserRequest(name);
        String fetchCharacterData = null;
        try {
            fetchCharacterData = client.fetchCharacterData(userRequest);
        } catch (IOException e) {
            log.info("Http request fail " + e);
        }
        GameCharacter[] gameCharacters = parseJson(fetchCharacterData);
        if (gameCharacters == null || gameCharacters.length == 0) {
            throw new GameCharacterNotFoundException("name: " + name);
        }
        log.info("Get character info,fetch characters: " + Arrays.toString(gameCharacters));
        return gameCharacters;
    }

    private String createStoryMessage(RelationStory story) {
        String msg = null;
        if (story.getRelationship() == Relationship.NONE) {
            msg = String.format("GameCharacter %s(%d) and GameCharacter%s(%d) no relationship found", story.getCharacterName1(), story.getIdCharacter1(), story.getCharacterName2(),
                    story.getIdCharacter2());
        } else {
            msg = String.format("GameCharacter %s(%d) is %s to GameCharacter%s(%d)", story.getCharacterName1(),
                    story.getIdCharacter1(), story.getRelationship().toString(),
                    story.getCharacterName2(), story.getIdCharacter2());
        }
        return msg;
    }

    private String createUserRequest(String name) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(extraUrl).newBuilder();
        urlBuilder.addQueryParameter("name", name);
        String url = urlBuilder.build().toString();
        log.info("UserRequest: " + url);
        return url;
    }

    private GameCharacter[] parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        GameCharacter[] gameCharacter = null; //if there are some characters with the same name
        try {
            gameCharacter = objectMapper.readValue(json, GameCharacter[].class);
        } catch (IOException e) {
            log.info("Fail to parse json " + e);
        }
        return gameCharacter;
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