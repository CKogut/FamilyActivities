package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Activity getActivitySample1() {
        return new Activity().id(1L).description("description1").cost("cost1").minParticipants(1).maxParticipants(1).location("location1");
    }

    public static Activity getActivitySample2() {
        return new Activity().id(2L).description("description2").cost("cost2").minParticipants(2).maxParticipants(2).location("location2");
    }

    public static Activity getActivityRandomSampleGenerator() {
        return new Activity()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .cost(UUID.randomUUID().toString())
            .minParticipants(intCount.incrementAndGet())
            .maxParticipants(intCount.incrementAndGet())
            .location(UUID.randomUUID().toString());
    }
}
