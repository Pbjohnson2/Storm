package com.swerve.storm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(suppressConstructorProperties=true)
@Getter
public class StormContact implements Model {
    private final String fullName;
    private final String avatarLink;
}
