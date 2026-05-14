package com.fieldcode.tesseract.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexedSortedSet<E extends Comparable<E>> implements SortedSet<E> {

  private final SortedSet<E> set;
  private final IndexLookup<E> index;

  private IndexedSortedSet(SortedSet<E> set) {
    this.set = set;
    this.index = IndexLookup.of(set);
  }

  public static <T extends Comparable<T>> IndexedSortedSet<T> of(SortedSet<T> set) {

    if (set instanceof IndexedSortedSet) {
      return (IndexedSortedSet<T>) set;
    }

    return new IndexedSortedSet<>(set);
  }

  @Override
  public Comparator<? super E> comparator() {return set.comparator();}

  @Override
  public SortedSet<E> subSet(E fromElement, E toElement) {return set.subSet(fromElement, toElement);}

  @Override
  public SortedSet<E> headSet(E toElement) {return set.headSet(toElement);}

  @Override
  public SortedSet<E> tailSet(E fromElement) {return set.tailSet(fromElement);}

  @Override
  public E first() {return set.first();}

  @Override
  public E last() {return set.last();}

  @Override
  public Spliterator<E> spliterator() {return set.spliterator();}

  @Override
  public int size() {return set.size();}

  @Override
  public boolean isEmpty() {return set.isEmpty();}

  @Override
  public boolean contains(Object o) {return set.contains(o);}

  @Override
  public Iterator<E> iterator() {return set.iterator();}

  @Override
  public Object[] toArray() {return set.toArray();}

  @Override
  public <T1> T1[] toArray(T1[] a) {return set.toArray(a);}

  @Override
  public boolean add(E e) {return set.add(e);}

  @Override
  public boolean remove(Object o) {return set.remove(o);}

  @Override
  public boolean containsAll(Collection<?> c) {return set.containsAll(c);}

  @Override
  public boolean addAll(Collection<? extends E> c) {return set.addAll(c);}

  @Override
  public boolean retainAll(Collection<?> c) {return set.retainAll(c);}

  @Override
  public boolean removeAll(Collection<?> c) {return set.removeAll(c);}

  @Override
  public void clear() {set.clear();}

  @Override
  public boolean removeIf(Predicate<? super E> filter) {return set.removeIf(filter);}

  @Override
  public Stream<E> stream() {return set.stream();}

  @Override
  public Stream<E> parallelStream() {return set.parallelStream();}

  @Override
  public void forEach(Consumer<? super E> action) {set.forEach(action);}

  public int indexOf(E element) {
    return index.indexOf(element);
  }

  public List<Integer> allIndicesOf(Collection<E> elements) {
    return elements.stream()
        .map(this::indexOf)
        .collect(Collectors.toList());
  }

}
