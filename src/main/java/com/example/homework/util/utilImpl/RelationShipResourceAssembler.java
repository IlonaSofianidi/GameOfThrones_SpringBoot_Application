package com.example.homework.util.utilImpl;

import com.example.homework.controller.MainController;
import com.example.homework.entity.RelationStory;
import com.example.homework.wire.RelationShipResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RelationShipResourceAssembler extends ResourceAssemblerSupport<RelationStory, RelationShipResource> {

    public RelationShipResourceAssembler() {
        super(MainController.class, RelationShipResource.class);
    }

    @Override
    public RelationShipResource toResource(RelationStory entity) {
        RelationShipResource relationShipResource = super.createResourceWithId(entity.getId(), entity);
        relationShipResource.setCharacter1(entity.getCharacterName1());
        relationShipResource.setCharacter2(entity.getCharacterName2());
        relationShipResource.setUuid(entity.getId());
        return relationShipResource;
    }
}
