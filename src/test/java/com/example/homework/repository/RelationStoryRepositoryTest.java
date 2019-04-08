package com.example.homework.repository;

import com.example.homework.entity.RelationStory;
import com.example.homework.entity.Relationship;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RelationStoryRepositoryTest {
    @Autowired
    RelationStoryRepository unit;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenGetRelationStoryByIdReturnRelationStory() {
        RelationStory relationStory = new RelationStory();
        relationStory.setCharacterName1("Alice");
        relationStory.setCharacterName2("Bob");
        relationStory.setRelationship(Relationship.SPOUSE);
        relationStory.setIdCharacter1(12);
        relationStory.setIdCharacter2(34);
        RelationStory persist = entityManager.persist(relationStory);
        Optional<RelationStory> actual = unit.getRelationStoriesById(persist.getId());
        Assert.assertThat(actual.get(), is(relationStory));


    }
    @Test
    public void whenGetRelationStoryByUnknownIdReturnEmptyOptional() {
        UUID id = UUID.fromString("8efb4522-5393-11e9-8647-d663bd873d93");
        Optional<RelationStory> actual = unit.getRelationStoriesById(id);
        Assert.assertThat(actual.isPresent(),is(false));


    }

    @Test
    public void whenRepositoryIsEmptyReturnNoRelationships() {
        List<RelationStory> actual = unit.findAll();
        Assert.assertThat(actual, is(empty()));

    }
    @Test
    public void whenSaveReturnUUID(){
        RelationStory relationStory = new RelationStory();
        relationStory.setCharacterName1("Alice");
        relationStory.setCharacterName2("Bob");
        relationStory.setRelationship(Relationship.SPOUSE);
        relationStory.setIdCharacter1(12);
        relationStory.setIdCharacter2(34);

        RelationStory actual = unit.save(relationStory);

        Assert.assertNotNull(actual.getId());
        Assert.assertThat(actual.getId(), Matchers.is(any(UUID.class)));

    }
}