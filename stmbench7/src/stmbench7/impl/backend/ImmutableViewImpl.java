package stmbench7.impl.backend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import stmbench7.backend.ImmutableCollection;

public class ImmutableViewImpl<E> implements ImmutableCollection<E>, Cloneable {

	private final LinkedList<E> elements;

	public ImmutableViewImpl(LinkedList<E> elements) {
		this.elements = elements;
	}

	public boolean contains(E element) {
		return elements.contains(element);
	}

	public int size() {
		return elements.size();
	}

	public Iterator<E> iterator() {
		return elements.iterator();
	}

	@SuppressWarnings("unchecked")
	public ImmutableCollection<E> clone() {
		return new ImmutableViewImpl<E>((LinkedList<E>) elements.clone());
	}
}