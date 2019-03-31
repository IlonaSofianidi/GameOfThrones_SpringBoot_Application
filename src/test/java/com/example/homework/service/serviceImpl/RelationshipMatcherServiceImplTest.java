package com.example.homework.service.serviceImpl;

import com.example.homework.entity.GameCharacter;
import com.example.homework.entity.RelationStory;
import com.example.homework.entity.Relationship;
import com.example.homework.exception.GameCharacterNotFoundException;
import com.example.homework.repository.RelationStoryRepository;
import com.example.homework.util.utilImpl.OKHttpGameOfThronesClientImpl;
import com.example.homework.wire.RelationShipUUIDResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class RelationshipMatcherServiceImplTest {
    @Mock
    RelationStoryRepository repository;
    @Mock
    OKHttpGameOfThronesClientImpl client;
    @InjectMocks
    @Spy
    RelationshipMatcherServiceImpl unit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findRelationshipBetweenCharacters() {
        String chatracter1Name = "Aemma Arryn";
        String character2Name = "Viserys I";
        GameCharacter gameCharacter1 = new GameCharacter();
        gameCharacter1.setName("Aemma Arryn");
        gameCharacter1.setUrl("https://anapioficeandfire.com/api/characters/49");
        gameCharacter1.setSpouse("https://anapioficeandfire.com/api/characters/1076");
        GameCharacter gameCharacter2 = new GameCharacter();
        gameCharacter2.setName("Viserys I");
        gameCharacter2.setUrl("https://anapioficeandfire.com/api/characters/1076");
        gameCharacter2.setMother("https://anapioficeandfire.com/api/characters/109");
        gameCharacter2.setFather("https://anapioficeandfire.com/api/characters/157");
        gameCharacter2.setSpouse("https://anapioficeandfire.com/api/characters/49");
        RelationStory relationStory = new RelationStory();
        relationStory.setCharacterName1("Aemma Arryn");
        relationStory.setCharacterName2("Viserys I");
        relationStory.setIdCharacter1(49);
        relationStory.setIdCharacter2(1076);
        relationStory.setRelationship(Relationship.SPOUSE);
        UUID id = UUID.fromString("8efb4522-5393-11e9-8647-d663bd873d93");
        RelationStory afterSave = new RelationStory();
        afterSave.setCharacterName1("Aemma Arryn");
        afterSave.setCharacterName2("Viserys I");
        afterSave.setIdCharacter1(49);
        afterSave.setIdCharacter2(1076);
        afterSave.setRelationship(Relationship.SPOUSE);
        afterSave.setId(id);

        when(unit.getCharacterInfo(chatracter1Name)).thenReturn(gameCharacter1);
        when(unit.getCharacterInfo(character2Name)).thenReturn(gameCharacter2);
        when(repository.save(relationStory)).thenReturn(afterSave);
        RelationShipUUIDResponse expected = RelationShipUUIDResponse.builder()
                .characterName1(chatracter1Name)
                .characterName2(character2Name)
                .id(id).build();
        Assert.assertThat(unit.findRelationshipBetweenCharacters(chatracter1Name, character2Name), is(expected));

    }

    @Test
    public void getFullRelationshipStory() {
        List<RelationStory> list = new ArrayList<>();
        RelationStory relationStory = new RelationStory();
        relationStory.setCharacterName1("Aemma Arryn");
        relationStory.setCharacterName2("Viserys I");
        relationStory.setIdCharacter1(49);
        relationStory.setIdCharacter2(1076);
        UUID id = UUID.fromString("8efb4522-5393-11e9-8647-d663bd873d93");
        relationStory.setId(id);
        relationStory.setRelationship(Relationship.SPOUSE);
        list.add(relationStory);
        RelationStory relationStory2 = new RelationStory();
        relationStory2.setCharacterName1("Jon Snow");
        relationStory2.setCharacterName2("Drogo");
        relationStory2.setIdCharacter1(101);
        relationStory2.setIdCharacter2(1077);
        UUID id2 = UUID.fromString("8efb4522-5393-11e9-8647-d663bd113d93");
        relationStory2.setId(id2);
        relationStory2.setRelationship(Relationship.NONE);
        list.add(relationStory2);

        when(repository.findAll()).thenReturn(list);

        Assert.assertThat(unit.getFullRelationshipStory(), hasSize(2));

    }

    @Test
    public void whenCheckFamilyRelationshipsMatchReturnSpouse() {
        GameCharacter gameCharacter1 = new GameCharacter();
        gameCharacter1.setName("Aemma Arryn");
        gameCharacter1.setUrl("https://anapioficeandfire.com/api/characters/49");
        gameCharacter1.setSpouse("https://anapioficeandfire.com/api/characters/1076");
        GameCharacter gameCharacter2 = new GameCharacter();
        gameCharacter2.setName("Viserys I");
        gameCharacter2.setUrl("https://anapioficeandfire.com/api/characters/1076");
        gameCharacter2.setMother("https://anapioficeandfire.com/api/characters/109");
        gameCharacter2.setFather("https://anapioficeandfire.com/api/characters/157");
        gameCharacter2.setSpouse("https://anapioficeandfire.com/api/characters/49");
        Relationship relationship = unit.checkFamilyRelationships(gameCharacter1, gameCharacter2);
        Assert.assertThat(relationship, is(Relationship.SPOUSE));
    }

    @Test
    public void whenCheckFamilyRelationshipsNothingMatchReturnNone() {
        GameCharacter gameCharacter1 = new GameCharacter();
        gameCharacter1.setName("Aemma Arryn");
        gameCharacter1.setUrl("https://anapioficeandfire.com/api/characters/1");
        gameCharacter1.setSpouse("https://anapioficeandfire.com/api/characters/2");
        GameCharacter gameCharacter2 = new GameCharacter();
        gameCharacter2.setName("Viserys I");
        gameCharacter2.setUrl("https://anapioficeandfire.com/api/characters/3");
        gameCharacter2.setMother("https://anapioficeandfire.com/api/characters/4");
        gameCharacter2.setFather("https://anapioficeandfire.com/api/characters/5");
        gameCharacter2.setSpouse("https://anapioficeandfire.com/api/characters/6");
        Relationship relationship = unit.checkFamilyRelationships(gameCharacter1, gameCharacter2);
        Assert.assertThat(relationship, is(Relationship.NONE));
    }

    @Test
    public void whenGetCharacterInfoReturnGameCharacter() throws IOException {
        String userRequest = "https://anapioficeandfire.com/api/characters?name=Aemma%20Arryn";
        String fetchData = "[{\"url\":\"https://anapioficeandfire.com/api/characters/49\",\"name\":\"Aemma Arryn\",\"gender\":\"Female\",\"culture\":\"\",\"born\":\"In 82 AC\",\"died\":\"In 105 AC\",\"titles\":[\"Queen\"],\"aliases\":[\"\"],\"father\":\"\",\"mother\":\"\",\"spouse\":\"https://anapioficeandfire.com/api/characters/1076\",\"allegiances\":[\"https://anapioficeandfire.com/api/houses/7\",\"https://anapioficeandfire.com/api/houses/378\"],\"books\":[\"https://anapioficeandfire.com/api/books/9\",\"https://anapioficeandfire.com/api/books/10\",\"https://anapioficeandfire.com/api/books/11\"],\"povBooks\":[],\"tvSeries\":[\"\"],\"playedBy\":[\"\"]}]";
        when(client.fetchCharacterData(userRequest)).thenReturn(fetchData);
        GameCharacter expected = new GameCharacter();
        expected.setName("Aemma Arryn");
        expected.setUrl("https://anapioficeandfire.com/api/characters/1");
        expected.setSpouse("https://anapioficeandfire.com/api/characters/2");
        GameCharacter actual = unit.getCharacterInfo("Aemma Arryn");
    }

    @Test(expected = GameCharacterNotFoundException.class)
    public void whenGetCharacterInfoInvalidRequestThrowCustomException() throws IOException {
        String userRequest = "https://anapioficeandfire.com/api/characters?name=Aemma%20Arryn";
        when(client.fetchCharacterData(userRequest)).thenReturn("[]");
        unit.getCharacterInfo("Aemma Arryn");
    }

}