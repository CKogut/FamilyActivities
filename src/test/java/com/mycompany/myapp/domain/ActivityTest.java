package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActivityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = getActivitySample1();
        Activity activity2 = new Activity();
        assertThat(activity1).isNotEqualTo(activity2);

        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);

        activity2 = getActivitySample2();
        assertThat(activity1).isNotEqualTo(activity2);
    }
}
