package ir.garshasb.time.extensions.java.time.LocalTime;

import manifold.ext.rt.api.ComparableUsing;
import manifold.ext.rt.api.ComparableUsing.Operator;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Extension
public class LocalTimeExt {

    @Extension
    public static LocalTime truncatedNow(ChronoUnit chronoUnit) {
        return LocalTime.now().truncatedTo(chronoUnit);
    }

    @Extension
    public static LocalTime truncatedNowToMinutes() {
        return LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @Extension
    public static LocalTime truncatedNowToSeconds() {
        return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static Duration minus(@This LocalTime thiz, LocalTime other) {
        return Duration.between(thiz, other);
    }

    public static Duration plus(@This LocalTime thiz, LocalTime other) {
        return Duration.between(thiz, other);
    }

    public static boolean compareToUsing(@This LocalTime thiz, LocalTime that, Operator op) {
        switch (op) {
            case LT:
                return thiz.isBefore(that);
            case LE:
                return !thiz.isAfter(that);
            case GT:
                return thiz.isAfter(that);
            case GE:
                return !thiz.isBefore(that);
            case EQ:
                return Objects.equals(thiz, that);
            case NE:
                return !Objects.equals(thiz, that);

            default:
                throw new IllegalStateException();
        }
    }

    public static boolean compareToUsing(@This LocalTime thiz, LocalDateTime that, Operator op) {
        switch (op) {
            case LT:
                return thiz.isBefore(that.toLocalTime());
            case LE:
                return !thiz.isAfter(that.toLocalTime());
            case GT:
                return thiz.isAfter(that.toLocalTime());
            case GE:
                return !thiz.isBefore(that.toLocalTime());
            case EQ:
                return Objects.equals(thiz, that.toLocalTime());
            case NE:
                return !Objects.equals(thiz, that.toLocalTime());

            default:
                throw new IllegalStateException();
        }
    }

    public static void main(String[] args) {
        var t1 = LocalTime.of(9, 0);
        var t2 = LocalTime.of(11, 0);
        var t3 = LocalTime.of(16, 0);
        var t4 = LocalTime.of(19, 0);

        System.out.println(LocalTimeExt.compareToUsing(t1, t3, Operator.GE));
    }
}
