package ir.garshasb.time.extensions.java.time.LocalDateTime;

import manifold.ext.rt.api.ComparableUsing;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Extension
public class LocalDateTimeExt {

    @Extension
    public static LocalDateTime truncatedNow(ChronoUnit chronoUnit) {
        return LocalDateTime.now().truncatedTo(chronoUnit);
    }

    @Extension
    public static LocalDateTime truncatedNowToMinutes() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @Extension
    public static LocalDateTime truncatedNowToSeconds() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Manifold Operator overloading capability for LocalDateTime.
     * Activates >, <, >=, <=, ==, != for LocalDateTime
     *
     * @param thiz
     * @param that
     * @return
     */
    public static boolean compareToUsing(@This LocalDateTime thiz, LocalDateTime that, ComparableUsing.Operator op) {
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
                return thiz.isEqual(that);
            case NE:
                return !thiz.isEqual(that);

            default:
                throw new IllegalStateException();
        }
    }
}
