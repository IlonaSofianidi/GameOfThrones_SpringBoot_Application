package com.example.homework.wire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

@Getter
@Setter
public class RelationShipResource extends ResourceSupport {
    String character1;
    String character2;
    UUID uuid;
}
