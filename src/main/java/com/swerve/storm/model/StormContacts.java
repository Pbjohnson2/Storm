package com.swerve.storm.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(suppressConstructorProperties=true)
@Getter
public class StormContacts {
    private final List<StormContact> contacts;
    private final long lastRetrieved;
}
