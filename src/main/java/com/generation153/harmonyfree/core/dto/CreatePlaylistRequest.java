package com.generation153.harmonyfree.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlaylistRequest {

    private Long userId;
    private String title;
    private String description;
}