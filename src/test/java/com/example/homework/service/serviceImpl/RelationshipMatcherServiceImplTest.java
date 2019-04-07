package com.example.homework.service.serviceImpl;

import com.example.homework.entity.GameCharacter;
import com.example.homework.entity.RelationStory;
import com.example.homework.entity.Relationship;
import com.example.homework.exception.GameCharacterNotFoundException;
import com.example.homework.exception.RelationShipStoryNotFound;
import com.example.homework.repository.RelationStoryRepository;
import com.example.homework.util.OKHttpGameOfThronesClient;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipUUIDResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipMatcherServiceImplTest {

    @Mock
    RelationStoryRepository repository;
    @Mock
    OKHttpGameOfThronesClient client;
    @Spy
    @InjectMocks
    RelationshipMatcherServiceImpl unit;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(unit, "extraUrl", "https://anapioficeandfire.com/api/characters");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenCheckFamilyRelationshipsMatchReturnSpouse() {
        GameCharacter[] list3 = createOneElementArray3();
        GameCharacter[] list2 = createOneElementArray2();
        List<RelationStory> list = unit.checkFamilyRelationships(list3, list2);
        Assert.assertThat(list.get(0).getRelationship(), is(Relationship.SPOUSE));
    }

    @Test
    public void whenCheckFamilyRelationshipsNothingMatchReturnNone() {
        GameCharacter[] list1 = createOneElementArray1();
        GameCharacter[] list2 = createOneElementArray2();

        List<RelationStory> list = unit.checkFamilyRelationships(list1, list2);
        Assert.assertThat(list.get(0).getRelationship(), is(Relationship.NONE));
    }

    @Test
    public void whenGetCharacterInfoReturnGameCharacterArray() throws IOException {
        String fetchData = "[{\"url\":\"https://anapioficeandfire.com/api/characters/1\",\"name\":\"Aemma Arryn\",\"gender\":\"Female\",\"culture\":\"\",\"born\":\"In 82 AC\",\"died\":\"In 105 AC\",\"titles\":[\"Queen\"],\"aliases\":[\"\"],\"father\":\"\",\"mother\":\"\",\"spouse\":\"https://anapioficeandfire.com/api/characters/2\",\"allegiances\":[\"https://anapioficeandfire.com/api/houses/7\",\"https://anapioficeandfire.com/api/houses/378\"],\"books\":[\"https://anapioficeandfire.com/api/books/9\",\"https://anapioficeandfire.com/api/books/10\",\"https://anapioficeandfire.com/api/books/11\"],\"povBooks\":[],\"tvSeries\":[\"\"],\"playedBy\":[\"\"]}]";
        when(client.fetchCharacterData(anyString())).thenReturn(fetchData);
        GameCharacter[] list1 = createOneElementArray1();
        GameCharacter[] actual = unit.getCharacterInfo("Aemma Arryn");
        Assert.assertThat(actual, is(list1));
    }

    @Test(expected = GameCharacterNotFoundException.class)
    public void whenGetCharacterInfoInvalidRequestThrowCustomException() throws IOException {
        when(client.fetchCharacterData(any())).thenReturn("[]");
        unit.getCharacterInfo("Aemma Arryn");
    }

    @Test
    public void whenFindRelationshipBetweenCharactersReturnUUIDResponse() {
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
        List<RelationShipUUIDResponse> actual = unit.findRelationshipBetweenCharacters(characterName1, character2Name);
        verify(unit, times(2)).getCharacterInfo(anyString());
        Assert.assertThat(actual, is(listExpected));
        Assert.assertThat(actual.get(0).getId(), is(listExpected.get(0).getId()));

    }

    @Test
    public void whenGetFullRelationshipStoryTestReturnPageRelationStory() {
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

        Page<RelationStory> page = new PageImpl<>(list);

        when(repository.findAllBy(any(Pageable.class))).thenReturn(page);
        Page<RelationStory> actual = unit.getFullRelationshipStoryTest(PageRequest.of(0, 5));
        Assert.assertThat(actual, is(page));
    }

    @Test
    public void whenGetRelationShipStoryByUUIDReturnRelationStoryResponse() {
        UUID id = UUID.randomUUID();
        RelationStory relationStory = createRelationStory();
        relationStory.setId(id);
        Optional<RelationStory> story = Optional.of(relationStory);

        when(repository.getRelationStoriesById(any(UUID.class))).thenReturn(story);

        GetRelationShipResponse expected = GetRelationShipResponse.builder()
                .storyMessage("GameCharacter Aemma Arryn(1) and GameCharacterViserys I(3) no relationship found")
                .id(id)
                .build();
        Assert.assertThat(unit.getRelationShipStory(id),is(expected));
    }
    @Test(expected = RelationShipStoryNotFound.class)
    public void whenGetRelationShipStoryByUUIDThrowRelationStoryNotFoundException() {
        UUID id = UUID.randomUUID();
        Optional<RelationStory> story = Optional.empty();

        when(repository.getRelationStoriesById(any(UUID.class))).thenReturn(story);

        unit.getRelationShipStory(id);

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

    private GameCharacter[] createOneElementArray3() {
        GameCharacter gameCharacter2 = new GameCharacter();
        gameCharacter2.setName("Darin I");
        gameCharacter2.setUrl("https://anapioficeandfire.com/api/characters/6");
        gameCharacter2.setMother("https://anapioficeandfire.com/api/characters/7");
        gameCharacter2.setFather("https://anapioficeandfire.com/api/characters/8");
        gameCharacter2.setSpouse("https://anapioficeandfire.com/api/characters/9");
        GameCharacter[] list2 = new GameCharacter[1];
        list2[0] = gameCharacter2;
        return list2;
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

}