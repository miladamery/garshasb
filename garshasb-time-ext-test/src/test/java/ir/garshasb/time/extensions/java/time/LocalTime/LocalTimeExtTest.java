package ir.garshasb.time.extensions.java.time.LocalTime;

import manifold.test.api.ExtensionManifoldTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.*;
public class LocalTimeExtTest extends ExtensionManifoldTest {
    @Override
    public void testCoverage() {
    }

    public void testComparing() {
        var t1 = LocalTime.of(9, 0);
        var t2 = LocalTime.of(10, 0);

        assertThat(t1 >= t2).isFalse();
        assertThat(t1 <= t2).isTrue();
        assertThat(t1 > t2).isFalse();
        assertThat(t1 < t2).isTrue();
        assertThat(t1 == t2).isFalse();
        assertThat(t1 != t2).isTrue();
        assertThat(t1 == t1).isTrue();
        assertThat(t2 == t2).isTrue();
    }

    public void testMinus() {
        var t1 = LocalTime.of(10, 30);
        var t2 = LocalTime.of(12, 23);

        assertThat(t1 - t2).isEqualTo(Duration.parse("PT1H53M"));
        assertThat(t2 - t1).isEqualTo(Duration.parse("PT-1H-53M"));
    }

    public void xtestPlus() {
        var t1 = LocalTime.of(10, 30);
        var t2 = LocalTime.of(12, 23);

        assertThat(t1 + t2).isEqualTo(Duration.parse("PT1H53M"));
        System.out.println(t2 + t1);
        assertThat(t2 + t1).isEqualTo(Duration.parse("PT1H-53M"));
    }

}
