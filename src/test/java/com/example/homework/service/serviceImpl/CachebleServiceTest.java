package com.example.homework.service.serviceImpl;

import com.example.homework.entity.GameCharacter;
import com.example.homework.entity.RelationStory;
import com.example.homework.entity.Relationship;
import com.example.homework.repository.RelationStoryRepository;
import com.example.homework.wire.RelationShipUUIDResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CachebleServiceTest {
    @Autowired
    @SpyBean
    RelationshipMatcherServiceImpl unit;
    @MockBean
    RelationStoryRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFindRelationShipWithTheSameParametresTwiceShouldCallRepositoryOnce() {
        GameCharacter[] list1 = createOneElementArray1();
        GameCharacter[] list2 = createOneElementArray2();
        String characterName1 = list1[0].getName();
        String character2Name = list2[0].getName();

        UUID id = UUID.fromString("8efb4522-5393-11e9-8647-d663bd873d93");

        List<RelationStory> relationStories = new ArrayList<>();
        relationStories.add(createRelationStory());

        RelationStory afterSave = new RelationStory();
        afterSave.setCharacterName1("Aemma Arryn");
        afterSave.setCharacterName2("Viserys I");
        afterSave.setIdCharacter1(1);
        afterSave.setIdCharacter2(3);
        afterSave.setRelationship(Relationship.NONE);
        afterSave.setId(id);

        Mockito.doReturn(list1).when(unit).getCharacterInfo(characterName1);
        Mockito.doReturn(list2).when(unit).getCharacterInfo(character2Name);

        when(unit.checkFamilyRelationships(list1, list2)).thenReturn(relationStories);
        when(repository.save(any(RelationStory.class))).thenReturn(afterSave);

        List<RelationShipUUIDResponse> listExpected = new ArrayList<>();

        RelationShipUUIDResponse expected = RelationShipUUIDResponse.builder()
                .characterName1(characterName1 + (" (id:1)"))
                .characterName2(character2Name + (" (id:3)"))
                .id(id).build();

        listExpected.add(expected);
        unit.findRelationshipBetweenCharacters(characterName1, character2Name);
        unit.findRelationshipBetweenCharacters(characterName1, character2Name);

        Mockito.verify(repository, Mockito.times(1)).save(any(RelationStory.class));
    }

    private RelationStory createRelationStory() {
        RelationStory relationStory = new RelationStory();
        relationStory.setCharacterName1("Aemma Arryn");
        relationStory.setCharacterName2("Viserys I");
        relationStory.setIdCharacter1(1);
        relationStory.setIdCharacter2(3);
        relationStory.setRelationship(Relationship.NONE);
        return relationStory;
    }

    private GameCharacter[] createOneElementArray1() {
        GameCharacter gameCharacter1 = new GameCharacter();
        gameCharacter1.setName("Aemma Arryn");
        gameCharacter1.setMother("");
        gameCharacter1.setFather("");
        gameCharacter1.setUrl("https://anapioficeandfire.com/api/characters/1");
        gameCharacter1.setSpouse("https://anapioficeandfire.com/api/characters/2");
        GameCharacter[] list1 = new GameCharacter[1];
        list1[0] = gameCharacter1;
        return list1;
    }

    private GameCharacter[] createOneElementArray2() {
        GameCharacter gameCharacter2 = new GameCharacter();
        gameCharacter2.setName("Viserys I");
        gameCharacter2.setUrl("https://anapioficeandfire.com/api/characters/3");
        gameCharacter2.setMother("https://anapioficeandfire.com/api/characters/4");
        gameCharacter2.setFather("https://anapioficeandfire.com/api/characters/5");
        gameCharacter2.setSpouse("https://anapioficeandfire.com/api/characters/6");
        GameCharacter[] list2 = new GameCharacter[1];
        list2[0] = gameCharacter2;
        return list2;
    }

}
