package ir.garshasb.collections.extensions.java.lang.Iterable;

import ir.garshasb.UnitTestRequired;
import ir.garshasb.collections.extensions.java.util.List.GarshasbListCollectionExt;
import ir.garshasb.collections.extensions.java.util.stream.Stream.GarshasbStreamCollectionsExt;
import manifold.ext.rt.api.*;
import manifold.ext.rt.extensions.manifold.rt.api.Array.ManArrayExt;
import manifold.rt.api.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static ir.garshasb.collections.extensions.java.util.Collection.GarshasbCollectionExt.addAll;
import static ir.garshasb.collections.extensions.java.util.List.GarshasbListCollectionExt.optimizeReadOnlyList;
import static java.util.Collections.emptyList;

@Extension
public class GarshasbIterableExt {

    /**
     * Returns a list containing all elements of the original collection and then all elements of the given [elements] collection.
     */
    public static <T> List<T> plus(@This Iterable<T> thiz, Iterable<T> elements) {
        List<T> result = new ArrayList<T>();
        thiz.forEach(result::add);
        elements.forEach(result::add);
        return result;
    }

    /**
     * Returns a list containing all elements of the original collection and then the given [element].
     */
    public static <T> List<T> plus(@This Iterable<T> thiz, T element) {
        List<T> result = new ArrayList<T>();
        thiz.forEach(result::add);
        result.add(element);
        return result;
    }

    /**
     * Returns a list containing all elements of the original collection and then all elements of the given [elements] array.
     */
    public static <T> List<T> plus(@This Iterable<T> thiz, T[] elements) {
        List<T> result = new ArrayList<T>();
        thiz.forEach(result::add);
        result.addAll(Arrays.asList(elements));
        return result;
    }

    /**
     * Returns a list containing all elements of the original collection except the elements contained in the given [elements] array.
     */
    public static <T> Iterable<T> minus(@This Iterable<T> thiz, T[] elements) {
        if (elements.isEmpty()) return thiz.toList();
        Set<T> other = GarshasbStreamCollectionsExt.toSet(Arrays.stream(elements));
        return thiz.filterNotToList(other::contains);
    }

    /**
     * Returns a list containing all elements of the original collection except the elements contained in the given [elements] collection.
     */
    public static <T> Iterable<T> minus(@This Iterable<T> thiz, Iterable<T> elements) {
        Set<T> other = elements.toSet();
        if (other.isEmpty())
            return thiz.toList();
        return thiz.filterNotToList(other::contains);
    }

    /**
     * Returns a [Map] containing key-value pairs provided by [transform] function
     * applied to elements of the given collection.
     * <p>
     * If any of two pairs would have the same key the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original collection.
     */
    public static <T, K, V> Map<K, V> associate(@This Iterable<T> thiz, Function<T, Pair<K, V>> transform) {
        Map<K, V> result = new LinkedHashMap<>(16);
        thiz.forEach(t -> {
            Pair<K, V> p = transform.apply(t);
            result.put(p.getFirst(), p.getSecond());
        });
        return result;
    }

    /**
     * Returns a [Map] containing the elements from the given collection indexed by the key
     * returned from [keySelector] function applied to each element.
     * <p>
     * If any two elements would have the same key returned by [keySelector] the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original collection.
     */
    public static <T, K> Map<K, T> associateBy(@This Iterable<T> thiz, Function<T, K> keySelector) {
        Map<K, T> result = new LinkedHashMap<>(16);
        thiz.forEach(t -> result.put(keySelector.apply(t), t));
        return result;
    }

    /**
     * Returns a [Map] containing the values provided by [valueTransform] and indexed by [keySelector] functions applied to elements of the given collection.
     * <p>
     * If any two elements would have the same key returned by [keySelector] the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original collection.
     */
    public static <T, K, V> Map<K, V> associateBy(@This Iterable<T> thiz, Function<T, K> keySelector, Function<T, V> valueTransform) {
        Map<K, V> result = new LinkedHashMap<>(16);
        thiz.forEach(t -> result.put(keySelector.apply(t), valueTransform.apply(t)));
        return result;
    }

    /**
     * Populates and returns the [destination] mutable map with key-value pairs,
     * where key is provided by the [keySelector] function applied to each element of the given collection
     * and value is the element itself.
     * <p>
     * If any two elements would have the same key returned by [keySelector] the last one gets added to the map.
     */
    public static <T, K, M extends Map<K, T>> M associateByTo(@This Iterable<T> thiz, M destination, Function<T, K> keySelector) {
        thiz.forEach(t -> destination.put(keySelector.apply(t), t));
        return destination;
    }

    /**
     * Populates and returns the [destination] mutable map with key-value pairs,
     * where key is provided by the [keySelector] function and
     * and value is provided by the [valueTransform] function applied to elements of the given collection.
     * <p>
     * If any two elements would have the same key returned by [keySelector] the last one gets added to the map.
     */
    public static <T, K, V, M extends Map<K, V>> M associateByTo(@This Iterable<T> thiz, M destination, Function<T, K> keySelector, Function<T, V> valueTransform) {
        thiz.forEach(t -> destination.put(keySelector.apply(t), valueTransform.apply(t)));
        return destination;
    }

    /**
     * Populates and returns the [destination] mutable map with key-value pairs
     * provided by [transform] function applied to each element of the given collection.
     * <p>
     * If any of two pairs would have the same key the last one gets added to the map.
     */
    public static <T, K, V, M extends Map<K, V>> M associateTo(@This Iterable<T> thiz, M destination, Function<T, Pair<K, V>> transform) {
        thiz.forEach(t -> {
            Pair<K, V> p = transform.apply(t);
            destination.put(p.getFirst(), p.getSecond());
        });
        return destination;
    }

    /**
     * Returns first element.
     *
     * @throws NoSuchElementException if the collection is empty.
     */
    public static <T> T first(@This Iterable<T> thiz) {
        Iterator<T> iterator = thiz.iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        return iterator.next();
    }

    /**
     * Returns the first element matching the given {@code predicate}.
     *
     * @throws {@code NoSuchElementException} if no such element is found.
     */
    public static <T> T first(@This Iterable<T> thiz, Predicate<T> predicate) {
        for (T element : thiz) {
            if (predicate.test(element)) {
                return element;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    /**
     * Returns index of the first element matching the given {@code predicate}, or -1 if the collection does not contain such element.
     */
    public static <T> int indexOfFirst(@This Iterable<T> thiz, Predicate<T> predicate) {
        int index = 0;
        for (T item : thiz) {
            if (predicate.test(item)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Returns the first element, or null if the collection is empty.
     */
    public static <T> T firstOrNull(@This Iterable<T> thiz) {
        if (thiz instanceof List) {
            if (((List<T>) thiz).isEmpty()) {
                return null;
            } else {
                return ((List<T>) thiz).get(0);
            }
        } else {
            Iterator<T> iterator = thiz.iterator();
            if (!iterator.hasNext()) {
                return null;
            }
            return iterator.next();
        }
    }

    /**
     * Returns the first element matching the given {@code predicate}, or {@code null} if element was not found.
     */
    public static <T> T firstOrNull(@This Iterable<T> thiz, Predicate<T> predicate) {
        for (T element : thiz) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Returns the last element.
     *
     * @throws NoSuchElementException if the collection is empty.
     */
    public static <T> T last(@This Iterable<T> thiz) {
        Iterator<T> iterator = thiz.iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        T last = iterator.next();
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return last;
    }

    /**
     * Returns the last element matching the given {@code predicate}.
     *
     * @throws NoSuchElementException if no such element is found.
     */
    public static <T> T last(@This Iterable<T> thiz, Predicate<T> predicate) {
        T last = null;
        boolean found = false;
        for (T element : thiz) {
            if (predicate.test(element)) {
                last = element;
                found = true;
            }
        }
        if (!found) {
            throw new NoSuchElementException("Collection contains no element matching the predicate.");
        }
        return last;
    }

    /**
     * Returns index of the last element matching the given {@code predicate}, or -1 if the collection does not contain such element.
     */
    public static <T> int indexOfLast(@This Iterable<T> thiz, Predicate<T> predicate) {
        int lastIndex = -1;
        int index = 0;
        for (T item : thiz) {
            if (predicate.test(item)) {
                lastIndex = index;
            }
            index++;
        }
        return lastIndex;
    }

    /**
     * Returns the last element, or {@code null} if the collection is empty.
     */
    public static <T> T lastOrNull(@This Iterable<T> thiz) {
        if (thiz instanceof List) {
            return ((List<T>) thiz).isEmpty() ? null : ((List<T>) thiz).get(((List) thiz).size() - 1);
        } else {
            Iterator<T> iterator = thiz.iterator();
            if (!iterator.hasNext()) {
                return null;
            }
            T last = iterator.next();
            while (iterator.hasNext()) {
                last = iterator.next();
            }
            return last;
        }
    }

    /**
     * Returns the last element matching the given {@code predicate}, or {@code null} if no such element was found.
     */
    public static <T> T lastOrNull(@This Iterable<T> thiz, Predicate<T> predicate) {
        T last = null;
        for (T element : thiz) {
            if (predicate.test(element)) {
                last = element;
            }
        }
        return last;
    }

    /**
     * Returns the single element, or throws an exception if the collection is empty or has more than one element.
     */
    public static <T> T single(@This Iterable<T> thiz) {
        Iterator<T> iterator = thiz.iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        T single = iterator.next();
        if (iterator.hasNext()) {
            throw new IllegalArgumentException("Collection has more than one element.");
        }
        return single;
    }

    /**
     * Returns the single element matching the given {@code predicate}, or throws exception if there is no or more than one matching element.
     */
    public static <T> T single(@This Iterable<T> thiz, Predicate<T> predicate) {
        T single = null;
        boolean found = false;
        for (T element : thiz) {
            if (predicate.test(element)) {
                if (found) {
                    throw new IllegalArgumentException("Collection contains more than one matching element.");
                }
                single = element;
                found = true;
            }
        }
        if (!found) {
            throw new NoSuchElementException("Collection contains no element matching the predicate.");
        }
        return single;
    }

    /**
     * Returns single element, or {@code null} if the collection is empty or has more than one element.
     */
    public static <T> T singleOrNull(@This Iterable<T> thiz) {
        if (thiz instanceof List) {
            return ((List<T>) thiz).size() == 1 ? ((List<T>) thiz).get(0) : null;
        } else {
            Iterator<T> iterator = thiz.iterator();
            if (!iterator.hasNext()) {
                return null;
            }
            T single = iterator.next();
            if (iterator.hasNext()) {
                return null;
            }
            return single;
        }
    }

    /**
     * Returns the single element matching the given {@code predicate}, or {@code null} if element was not found or more than one element was found.
     */
    public static <T> T singleOrNull(@This Iterable<T> thiz, Predicate<T> predicate) {
        T single = null;
        boolean found = false;
        for (T element : thiz) {
            if (predicate.test(element)) {
                if (found) {
                    return null;
                }
                single = element;
                found = true;
            }
        }
        if (!found) {
            return null;
        }
        return single;
    }

    /**
     * Returns a list containing all elements matching the given {@code predicate}.
     */
    public static <T> List<T> filterToList(@This Iterable<T> thiz, Predicate<T> predicate) {
        return filterTo(thiz, new ArrayList<>(), predicate);
    }

    /**
     * Appends all elements matching the given {@code predicate} to the given {@code destination}.
     */
    public static <T, C extends Collection<? super T>> C filterTo(@This Iterable<T> thiz, C destination, Predicate<T> predicate) {
        for (T element : thiz) {
            if (predicate.test(element)) {
                destination.add(element);
            }
        }
        return destination;
    }

    /**
     * Returns a list containing only elements matching the given {@code predicate}.
     *
     * @param predicate function that takes the index of an element and the element itself
     *                  and returns the result of predicate evaluation on the element.
     */
    public static <T> List<T> filterIndexedToList(@This Iterable<T> thiz, IndexedPredicate<T> predicate) {
        return filterIndexedTo(thiz, new ArrayList<T>(), predicate);
    }

    /**
     * Appends all elements matching the given {@code predicate} to the given {@code destination}.
     *
     * @param predicate function that takes the index of an element and the element itself
     *                  and returns the result of predicate evaluation on the element.
     */
    public static <T, C extends Collection<? super T>> C filterIndexedTo(@This Iterable<T> thiz, C destination, IndexedPredicate<T> predicate) {
        forEachIndexed(thiz, (index, element) ->
        {
            if (predicate.test(index, element)) {
                destination.add(element);
            }
        });
        return destination;
    }

    /**
     * Returns a list containing all elements not matching the given {@code predicate}.
     */
    public static <T> List<T> filterNotToList(@This Iterable<T> thiz, Predicate<T> predicate) {
        return filterNotTo(thiz, new ArrayList<T>(), predicate);
    }

    /**
     * Appends all elements not matching the given {@code predicate} to the given {@code destination}.
     */
    public static <T, C extends Collection<? super T>> C filterNotTo(@This Iterable<T> thiz, C destination, Predicate<T> predicate) {
        for (T element : thiz) {
            if (!predicate.test(element)) {
                destination.add(element);
            }
        }
        return destination;
    }

    /**
     * Returns a collection with elements in reversed order.
     * <p/>
     * Note, this method expires with JDK 21 because it interferes with new methods: {@code SequencedCollection#reversed()}
     * and {@code List#reversed()}.These new methods provide the same functionality, but only for SequencedCollection.
     */
    @Expires(21)
    public static <T> Collection<T> reversed(@This Iterable<T> thiz) {
        List<T> list = toList(thiz);
        if (list.size() <= 1) {
            return list;
        }
        GarshasbListCollectionExt.reverse(list);
        return list;
    }

    /**
     * Returns a {@code List} containing all elements.
     */
    public static <T> List<T> toList(@This Iterable<T> thiz) {
        ArrayList<T> list = new ArrayList<>();
        for (T elem : thiz) {
            list.add(elem);
        }
        return list;
    }

    /**
     * Returns a {@code Set} containing all unique elements.
     * <p>
     * The returned set preserves the element iteration order of the original collection.
     */
    public static <T> Set<T> toSet(@This Iterable<T> thiz) {
        LinkedHashSet<T> set = new LinkedHashSet<>();
        for (T elem : thiz) {
            set.add(elem);
        }
        return set;
    }

    /**
     * Returns a single list of all elements yielded from results of {@code transform} function being invoked on each element of original collection.
     */
    public static <T, R> List<R> flatMap(@This Iterable<T> thiz, Function<T, Iterable<R>> transform) {
        return flatMapTo(thiz, new ArrayList<R>(), transform);
    }

    /**
     * Appends all elements yielded from results of {@code transform} function being invoked on each element of original collection, to the given {@code destination}.
     */
    public static <T, R, C extends Collection<R>> C flatMapTo(@This Iterable<T> thiz, C destination, Function<T, Iterable<R>> transform) {
        for (T element : thiz) {
            Iterable<R> list = transform.apply(element);
            addAll(destination, list);
        }
        return destination;
    }

    /**
     * Returns a list containing only distinct elements from the given collection.
     * <p>
     * The elements in the resulting list are in the same order as they were in the source collection.
     */
    public static <T> List<T> distinctList(@This Iterable<T> thiz) {
        return toList(toSet(thiz));
    }

    /**
     * Returns a list containing only elements from the given collection
     * having distinct keys returned by the given {@code selector} function.
     * <p>
     * The elements in the resulting list are in the same order as they were in the source collection.
     */
    public static <T, K> List<T> distinctBy(@This Iterable<T> thiz, Function<T, K> selector) {
        HashSet<K> set = new HashSet<>();
        ArrayList<T> list = new ArrayList<>();
        for (T e : thiz) {
            K key = selector.apply(e);
            if (set.add(key)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a set containing all elements that are contained by both thiz set and the specified collection.
     * <p>
     * The returned set preserves the element iteration order of the original collection.
     */
    public static <T> Set<T> intersect(@This Iterable<T> thiz, Iterable<T> other) {
        Set<T> set = toSet(thiz);
        set.retainAll(coerceToUniqueCollection(other));
        return set;
    }

    /**
     * Returns a set containing all elements that are contained by thiz collection and not contained by the specified collection.
     * <p>
     * The returned set preserves the element iteration order of the original collection.
     */
    public static <T> Set<T> subtract(@This Iterable<T> thiz, Iterable<T> other) {
        Set<T> set = toSet(thiz);
        set.removeAll(coerceToUniqueCollection(other));
        return set;
    }

    /**
     * Returns a set containing all distinct elements from both collections.
     * <p>
     * The returned set preserves the element iteration order of the original collection.
     * Those elements of the {@code other} collection that are unique are iterated in the end
     * in the order of the {@code other} collection.
     */
    public static <T> Set<T> union(@This Iterable<T> thiz, Iterable<T> other) {
        Set<T> set = toSet(thiz);
        addAll(set, other);
        return set;
    }

    private static <T> Collection<T> coerceToUniqueCollection(Iterable<T> source) {
        if (source instanceof Collection && ((Collection) source).size() <= 1) {
            return (Collection<T>) source;
        }
        HashSet<T> set = new HashSet<>();
        for (T elem : source) {
            set.add(elem);
        }
        return set;
    }

    /**
     * Returns the number of elements in thiz collection.
     */
    public static <T> int count(@This Iterable<T> thiz) {
        if (thiz instanceof Collection) {
            return ((Collection<T>) thiz).size();
        }

        int count = 0;
        for (T element : thiz) {
            count++;
        }
        return count;
    }

    /**
     * Returns the number of elements matching the given {@code predicate}.
     */
    public static <T> int count(@This Iterable<T> thiz, Predicate<T> predicate) {
        int count = 0;
        for (T element : thiz) {
            if (predicate.test(element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the first element having the largest value according to the provided {@code comparator} or {@code null} if there are no elements.
     */
    public static <T> T maxWith(@This Iterable<T> thiz, Comparator<T> comparator) {
        Iterator<T> iterator = thiz.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        T max = iterator.next();
        while (iterator.hasNext()) {
            T e = iterator.next();
            if (comparator.compare(max, e) < 0) {
                max = e;
            }
        }
        return max;
    }

    /**
     * Returns the first element having the smallest value according to the provided {@code comparator} or {@code null} if there are no elements.
     */
    public static <T> T minWith(@This Iterable<T> thiz, Comparator<T> comparator) {
        Iterator<T> iterator = thiz.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        T min = iterator.next();
        while (iterator.hasNext()) {
            T e = iterator.next();
            if (comparator.compare(min, e) > 0) {
                min = e;
            }
        }
        return min;
    }

    /**
     * Splits the original collection into pair of lists,
     * where <i>first</i> list contains elements for which {@code predicate} yielded {@code true},
     * while <i>second</i> list contains elements for which {@code predicate} yielded {@code false}.
     */
    public static <T> Pair<List<T>, List<T>> partition(@This Iterable<T> thiz, Predicate<T> predicate) {
        ArrayList<T> first = new ArrayList<T>();
        ArrayList<T> second = new ArrayList<T>();
        for (T element : thiz) {
            if (predicate.test(element)) {
                first.add(element);
            } else {
                second.add(element);
            }
        }
        return new Pair<>(first, second);
    }

    private static <T> int collectionSizeOrDefault(Iterable<T> thiz, int def) {
        return thiz instanceof Collection ? ((Collection<T>) thiz).size() : def;
    }

    /**
     * Returns a list containing the results of applying the given {@code transform} function
     * to each element in the original collection.
     */
    public static <T, R> List<R> mapToList(@This Iterable<T> thiz, Function<T, R> transform) {
        return mapTo(thiz, new ArrayList<R>(collectionSizeOrDefault(thiz, 10)), transform);
    }

    /**
     * Returns a list containing the results of applying the given {@code transform} function
     * to each element and its index in the original collection.
     *
     * @param transform function that takes the index of an element and the element itself
     *                  and returns the result of the transform applied to the element.
     * @deprecated Use {@link #mapIndexedToList(Iterable, IndexedFunction)} instead.
     */
    @Deprecated
    public static <T, R> List<R> mapIndexed(@This Iterable<T> thiz, IndexedFunction<T, R> transform) {
        return mapIndexedTo(thiz, new ArrayList<R>(collectionSizeOrDefault(thiz, 10)), transform);
    }

    /**
     * Returns a list containing the results of applying the given {@code transform} function
     * to each element and its index in the original collection.
     *
     * @param transform function that takes the index of an element and the element itself
     *                  and returns the result of the transform applied to the element.
     */
    public static <T, R> List<R> mapIndexedToList(@This Iterable<T> thiz, IndexedFunction<T, R> transform) {
        return mapIndexedTo(thiz, new ArrayList<R>(collectionSizeOrDefault(thiz, 10)), transform);
    }

    /**
     * Returns a list containing only the non-null results of applying the given {@code transform} function
     * to each element and its index in the original collection.
     *
     * @param transform function that takes the index of an element and the element itself
     *                  and returns the result of the transform applied to the element.
     * @deprecated Use {@link #mapIndexedNotNullToList(Iterable, IndexedFunction)} instead.
     */
    @Deprecated
    public static <T, R> List<R> mapIndexedNotNull(@This Iterable<T> thiz, IndexedFunction<T, R> transform) {
        return mapIndexedNotNullTo(thiz, new ArrayList<R>(), transform);
    }

    /**
     * Returns a list containing only the non-null results of applying the given {@code transform} function
     * to each element and its index in the original collection.
     *
     * @param transform function that takes the index of an element and the element itself
     *                  and returns the result of the transform applied to the element.
     */
    public static <T, R> List<R> mapIndexedNotNullToList(@This Iterable<T> thiz, IndexedFunction<T, R> transform) {
        return mapIndexedNotNullTo(thiz, new ArrayList<R>(), transform);
    }

    /**
     * Applies the given {@code transform} function to each element and its index in the original collection
     * and appends only the non-null results to the given {@code destination}.
     *
     * @param transform function that takes the index of an element and the element itself
     *                  and returns the result of the transform applied to the element.
     */
    public static <T, R, C extends Collection<? super R>> C mapIndexedNotNullTo(@This Iterable<T> thiz, C destination, IndexedFunction<T, R> transform) {
        forEachIndexed(thiz, (index, element) ->
        {
            R result = transform.apply(index, element);
            if (result != null) {
                destination.add(result);
            }
        });
        return destination;
    }

    /**
     * Applies the given {@code transform} function to each element and its index in the original collection
     * and appends the results to the given {@code destination}.
     *
     * @param transform function that takes the index of an element and the element itself
     *                  and returns the result of the transform applied to the element.
     */
    public static <T, R, C extends Collection<? super R>> C mapIndexedTo(@This Iterable<T> thiz, C destination, IndexedFunction<T, R> transform) {
        int index = 0;
        for (T item : thiz) {
            destination.add(transform.apply(index++, item));
        }
        return destination;
    }

    /**
     * Returns a list containing only the non-null results of applying the given {@code transform} function
     * to each element in the original collection.
     *
     * @deprecated Use {@link #mapNotNullToList(Iterable, Function)} instead.
     */
    @Deprecated
    public static <T, R> List<R> mapNotNull(@This Iterable<T> thiz, Function<T, R> transform) {
        return mapNotNullTo(thiz, new ArrayList<>(), transform);
    }

    /**
     * Returns a list containing only the non-null results of applying the given {@code transform} function
     * to each element in the original collection.
     */
    public static <T, R> List<R> mapNotNullToList(@This Iterable<T> thiz, Function<T, R> transform) {
        return mapNotNullTo(thiz, new ArrayList<>(), transform);
    }

    /**
     * Applies the given {@code transform} function to each element in the original collection
     * and appends only the non-null results to the given {@code destination}.
     */
    public static <T, R, C extends Collection<? super R>> C mapNotNullTo(@This Iterable<T> thiz, C destination, Function<T, R> transform) {
        thiz.forEach(element ->
        {
            R result = transform.apply(element);
            if (result != null) {
                destination.add(result);
            }
        });
        return destination;
    }

    /**
     * Applies the given {@code transform} function to each element of the original collection
     * and appends the results to the given {@code destination}.
     */
    public static <T, R, C extends Collection<? super R>> C mapTo(@This Iterable<T> thiz, C destination, Function<T, R> transform) {
        for (T item : thiz) {
            destination.add(transform.apply(item));
        }
        return destination;
    }

    /**
     * Returns a list containing all the elmeents from {@code fromIndex} (inclusive)
     */
    public static <T> List<T> subList(@This Iterable<T> thiz, int fromIndex) {
        return subList(thiz, fromIndex, -1);
    }

    /**
     * Returns a list containing the elmeents {@code fromIndex} (inclusive) to {@code toIndex} (exclusive)
     */
    public static <T> List<T> subList(@This Iterable<T> thiz, int fromIndex, int toIndex) {
        if (thiz instanceof Collection && ((Collection<T>) thiz).isEmpty()) {
            return emptyList();
        }
        boolean toEnd = toIndex < 0;
        if (thiz instanceof List) {
            //noinspection unchecked
            return ((List<T>) thiz).subList(fromIndex, !toEnd ? toIndex : ((List<T>) thiz).size());
        }
        ArrayList<T> list = new ArrayList<>();
        Iterator<T> iter = thiz.iterator();
        for (int i = 0; (toEnd || i < toIndex) && iter.hasNext(); i++) {
            T elem = iter.next();
            if (i >= fromIndex) {
                list.add(elem);
            }
        }
        return optimizeReadOnlyList(list);
    }

    /**
     * Performs the given {@code action} on each element, providing sequential index with the element.
     *
     * @param action function that takes the index of an element and the element itself
     *               and performs the desired action on the element.
     */
    public static <T> void forEachIndexed(@This Iterable<T> thiz, IndexedConsumer<T> action) {
        int index = 0;
        for (T item : thiz) {
            action.accept(index++, item);
        }
    }

    /**
     * Join the elements together in a String separated by {@code separator}.
     */
    public static <T> String joinToString(@This Iterable<T> thiz, CharSequence separator) {
        return joinTo(thiz, new StringBuilder(), separator).toString();
    }

    /**
     * Append the elements to {@code buffer} separated by {@code separator}.
     */
    public static <T, A extends Appendable> A joinTo(@This Iterable<T> thiz, A buffer, CharSequence separator) {
        int count = 0;
        try {
            for (T e : thiz) {
                if (count++ > 0) {
                    buffer.append(separator);
                }
                buffer.append(e.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return buffer;
    }

    /**
     * Accumulates value starting with {@code demo} value and applying {@code operation} from left to right to current accumulator value and each element.
     * <p>
     * The operation is <i>terminal</i>.
     */
    public static <T, R> R fold(@This Iterable<T> thiz, R initial, BiFunction<R, T, R> operation) {
        R accumulator = initial;
        for (T element : thiz) {
            accumulator = operation.apply(accumulator, element);
        }
        return accumulator;
    }
}
