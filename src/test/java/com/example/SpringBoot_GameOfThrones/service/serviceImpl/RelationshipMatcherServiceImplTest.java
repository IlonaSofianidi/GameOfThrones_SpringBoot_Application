package com.example.SpringBoot_GameOfThrones.service.serviceImpl;

import com.example.SpringBoot_GameOfThrones.entity.GameCharacter;
import com.example.SpringBoot_GameOfThrones.entity.Relationship;
import com.example.SpringBoot_GameOfThrones.util.utilImpl.OKHttpGameOfThronesClientImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class RelationshipMatcherServiceImplTest {
    @Mock
    OKHttpGameOfThronesClientImpl client;
    @InjectMocks
    RelationshipMatcherServiceImpl unit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findRelationshipBetweenCharacters() {
        String chatracter1Name = "Aemma Arryn";
        String character2Name = "Viserys I";
        //TODO mock db request
        UUID relationshipBetweenCharacters = unit.findRelationshipBetweenCharacters(chatracter1Name, character2Name);

    }

    @Test
    public void getFullRelationshipStory() {
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

    @Test
    public void whenGetCharacterInfoInvalidRequestThrowCustomException() {

    }

}