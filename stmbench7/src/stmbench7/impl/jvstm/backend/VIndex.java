package stmbench7.impl.jvstm.backend;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jvstm.VBox;
import jvstm.util.RedBlackTree;
import stmbench7.backend.Index;
import stmbench7.backend.IndexKey;
import stmbench7.core.RuntimeError;

public class VIndex<K extends IndexKey,V> implements Index<K,V> {

	@SuppressWarnings("unchecked")
	protected final VBox<RedBlackTree<Entry<K,V>>> index = new VBox<RedBlackTree<Entry<K,V>>>(RedBlackTree.EMPTY);

	public VIndex() {
	}

	public void put(K key, V value) {
		if (value == null) {
			throw new RuntimeError("VIndex does not support null values!");
		}
		index.put(index.get().put(new Entry<K,V>(key, value)));
	}

	public V putIfAbsent(K key, V value) {
		if (value == null) {
			throw new RuntimeError("VIndex does not support null values!");
		}

		Entry<K,V> newEntry = new Entry<K,V>(key, value);
		Entry<K,V> oldVal = index.get().get(newEntry);
		if (oldVal != null) {
			return oldVal.value;
		}

		index.put(index.get().put(newEntry));
		return null;
	}

	public V get(K key) {
		Entry<K,V> entry = new Entry<K,V>(key, null);
		Entry<K,V> oldVal = index.get().get(entry);
		if (oldVal != null) {
			return oldVal.value;
		} else {
			return null;
		}
	}

	public Iterable<V> getRange(K minKey, K maxKey) {
		final Entry<K,V> entryMin = new Entry<K,V>(minKey, null);
		final Entry<K,V> entryMax = new Entry<K,V>(maxKey, null);

		return new Iterable<V>() {
			public Iterator<V> iterator() {
				return new IndexIterator(index.get().iterator(entryMin, entryMax));
			}
		};
	}

	public boolean remove(K key) {
		Entry<K,V> entry = new Entry<K,V>(key, null);
		Entry<K,V> existing = index.get().get(entry);
		index.put(index.get().put(entry));
		return (existing != null);
	}

	public Iterator<V> iterator() {
		return new IndexIterator(index.get().iterator());
	}

	class IndexIterator implements Iterator<V> {
		private Iterator<Entry<K,V>> iter;
		private Entry<K,V> lastReturned;
		private Entry<K,V> next;

		IndexIterator(Iterator<Entry<K,V>> iter) {
			this.iter = iter;
			updateNext();
		}

		private void updateNext() {
			while (iter.hasNext()) {
				Entry<K,V> nextEntry = iter.next();
				if (nextEntry.value != null) {
					next = nextEntry;
					return;
				}
			}
			next = null;
		}

		public boolean hasNext() {
			return next != null;
		}

		public V next() {
			if (next == null) {
				throw new NoSuchElementException();
			} else {
				V result = next.value;
				lastReturned = next;
				updateNext();
				return result;
			}
		}

		public void remove() {
			if (lastReturned == null) {
				throw new IllegalStateException();
			}

			VIndex.this.remove(lastReturned.key);
			lastReturned = null;
		}
	}

	static class Entry<K extends IndexKey,V> implements Comparable<Entry<K,V>> {
		private K key;
		private V value;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public int compareTo(Entry<K,V> other) {
			return this.key.compareTo(other.key);
		}
	}
}
