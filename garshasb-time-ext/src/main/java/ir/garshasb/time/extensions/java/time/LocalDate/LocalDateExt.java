package ir.garshasb.time.extensions.java.time.LocalDate;

import manifold.ext.rt.api.ComparableUsing;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Extension
public class LocalDateExt {

    public static Duration minus(@This LocalDate thiz, LocalDate other) {
        return Duration.between(thiz, other);
    }

    public static Duration plus(@This LocalDate thiz, LocalDate other) {
        return Duration.between(thiz, other);
    }

    /**
     * Manifold Operator overloading capability for LocalDateTime.
     * Activates >, <, >=, <=, ==, != for LocalDateTime
     *
     * @param thiz
     * @param that
     * @return
     */
    public static boolean compareToUsing(@This LocalDate thiz, LocalDate that, ComparableUsing.Operator op) {
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

    public static boolean compareToUsing(@This LocalDate thiz, LocalDateTime that, ComparableUsing.Operator op) {
        switch (op) {
            case LT:
                return thiz.isBefore(that.toLocalDate());
            case LE:
                return !thiz.isAfter(that.toLocalDate());
            case GT:
                return thiz.isAfter(that.toLocalDate());
            case GE:
                return !thiz.isBefore(that.toLocalDate());
            case EQ:
                return thiz.isEqual(that.toLocalDate());
            case NE:
                return !thiz.isEqual(that.toLocalDate());

            default:
                throw new IllegalStateException();
        }
    }
}
