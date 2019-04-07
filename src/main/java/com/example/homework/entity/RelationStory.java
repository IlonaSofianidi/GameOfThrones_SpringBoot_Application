package com.example.homework.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@RequiredArgsConstructor
public class RelationStory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String characterName1;
    private String characterName2;
    private int idCharacter1;
    private int idCharacter2;
    private Relationship relationship;

}
