package com.swerve.storm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Credentials implements Model{
    private final String userId;
    private final String stormKey;

    public Credentials(final String userId, final String stormKey) {
        this.userId = userId;
        this.stormKey = userId;
    }
}
