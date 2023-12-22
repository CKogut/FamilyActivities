package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RelationshipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Relationship getRelationshipSample1() {
        return new Relationship().id(1L);
    }

    public static Relationship getRelationshipSample2() {
        return new Relationship().id(2L);
    }

    public static Relationship getRelationshipRandomSampleGenerator() {
        return new Relationship().id(longCount.incrementAndGet());
    }
}
