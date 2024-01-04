package com.raisin.task.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FixtureKind {

    JOINED("joined"), ORPHANED("orphaned"), DEFECTIVE("defective");

    private final String name;
}
