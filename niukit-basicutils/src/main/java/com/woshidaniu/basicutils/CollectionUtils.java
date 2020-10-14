package com.woshidaniu.basicutils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.woshidaniu.basicutils.map.MultiValueMap;
/**
 * 
 * @className: CollectionUtils
 * @description: 扩展Apache基础工具对象CollectionUtils
 * @author : kangzhidong
 * @date : 上午8:51:18 2014-10-25
 * @modify by:
 * @modify date :
 * @modify description :
 * @see org.apache.commons.collections.CollectionUtils
 */
@SuppressWarnings("unchecked")
public abstract class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

	public static boolean isArray(Class<?> aClass) {
		return aClass.isArray();
	}

	/**
	 * Class类的isAssignableFrom(Class cls)方法，如果调用这个方法的class或接口 与 参数cls表示的类或接口相同，或者是参数cls表示的类或接口的父类，则返回true。 
	 * 形象地：自身类.class.isAssignableFrom(自身类或子类.class)  返回true 
	 */
	public static boolean isCollection(Class<?> aClass) {
		return Collection.class.isAssignableFrom(aClass);
	}

	public static boolean isList(Class<?> aClass) {
		return List.class.isAssignableFrom(aClass);
	}

	public static boolean isSet(Class<?> aClass) {
		return Set.class.isAssignableFrom(aClass);
	}

	public static boolean isPrimitiveArray(Class<?> aClass) {
		return aClass.isArray() && aClass.getComponentType().isPrimitive();
	}

	public static int getLengthOfCollection(Object value) {
		if (isArray(value.getClass())) {
			return Array.getLength(value);
		} else {
			return ((Collection<?>) value).size();
		}
	}

	public static Object getValueFromCollection(Object collection, int index) {
		if (isArray(collection.getClass())) {
			return Array.get(collection, index);
		} else {
			return ((Collection<?>) collection).toArray()[index];
		}
	}
	
	public static <T> List<T> findAll(Collection<T> inputCollection, Predicate predicate) {
        if (inputCollection != null && predicate != null) {
        	List<T> outputCollection = new ArrayList<T>();
        	select(inputCollection, predicate, outputCollection);
            return outputCollection;
        }
        return null;
    }
  
	public static <T> List<T> killNull(List<T> collection) {
        if (collection != null) {
			CollectionUtils.filter(collection, PredicateUtils.notNullPredicate());
        }
        return collection;
    }
	
	public static <T> List<T> killNotNull(List<T> collection) {
        if (collection != null) {
			CollectionUtils.filter(collection, PredicateUtils.nullPredicate());
			return collection;
        }
        return new ArrayList<T>();
    }
	
	/**
	 * 将数组转换成List。
	 * 
	 * @param objs
	 * @return
	 */
	public static List arrayToList(Object[] objs) {
		List ret = new ArrayList();
		if (objs == null || objs.length <= 0) {
			return ret;
		}

		for (int i = 0; i < objs.length; i++) {
			ret.add(objs[i]);
		}
		return ret;
	}

	public static List stringArrayToLongList(String[] objs) {
		List ret = new ArrayList();
		if (objs == null || objs.length <= 0) {
			return ret;
		}

		for (int i = 0; i < objs.length; i++) {
			Long longobj = new Long(objs[i]);
			ret.add(longobj);
		}
		return ret;
	}

	public static List defaultIfNull(List list) {
		if (list == null) {
			return new ArrayList();
		}

		return list;
	}

	/**
	 * 把一个<code>elmLst</code>从<code>orgLst</code>中除去
	 * 
	 * @param elmLst
	 *            the List 要被除去的部分
	 * @param orgLst
	 *            the List 原始list
	 * @return 剩余的list
	 */
	public static List removeList(List elmLst, List orgLst) {
		List rst = new ArrayList();

		if (orgLst != null && elmLst != null && elmLst.size() > 0) {
			for (int i = 0; i < orgLst.size(); i++) {
				if (!elmLst.contains(orgLst.get(i))) {
					rst.add(orgLst.get(i));
				}
			}
			return rst;
		} else if (elmLst != null && elmLst.size() == 0) {
			return orgLst;
		}
		return rst;
	}

	/**
	 * 将Set转换为List
	 * 
	 * @param set
	 * @return
	 */
	public static List setToList(Set set) {
		List list = new ArrayList();
		list.addAll(set);
		return list;
	}
	
	/**
	 * Return {@code true} if the supplied Map is {@code null} or empty.
	 * Otherwise, return {@code false}.
	 * @param map the Map to check
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * Convert the supplied array into a List. A primitive array gets converted
	 * into a List of the appropriate wrapper type.
	 * <p><b>NOTE:</b> Generally prefer the standard {@link Arrays#asList} method.
	 * This {@code arrayToList} method is just meant to deal with an incoming Object
	 * value that might be an {@code Object[]} or a primitive array at runtime.
	 * <p>A {@code null} source value will be converted to an empty List.
	 * @param source the (potentially primitive) array
	 * @return the converted List result
	 * @see ObjectUtils#toObjectArray(Object)
	 * @see Arrays#asList(Object[])
	 */
	@SuppressWarnings("rawtypes")
	public static List arrayToList(Object source) {
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	/**
	 * Merge the given array into the given Collection.
	 * @param array the array to merge (may be {@code null})
	 * @param collection the target Collection to merge the array into
	 */
	public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection must not be null");
		}
		Object[] arr = ObjectUtils.toObjectArray(array);
		for (Object elem : arr) {
			collection.add((E) elem);
		}
	}

	/**
	 * Merge the given Properties instance into the given Map,
	 * copying all properties (key-value pairs) over.
	 * <p>Uses {@code Properties.propertyNames()} to even catch
	 * default properties linked into the original Properties instance.
	 * @param props the Properties instance to merge (may be {@code null})
	 * @param map the target Map to merge the properties into
	 */
	public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.getProperty(key);
				if (value == null) {
					// Potentially a non-String value...
					value = props.get(key);
				}
				map.put((K) key, (V) value);
			}
		}
	}


	/**
	 * Check whether the given Iterator contains the given element.
	 * @param iterator the Iterator to check
	 * @param element the element to look for
	 * @return {@code true} if found, {@code false} else
	 */
	public static boolean contains(Iterator<?> iterator, Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Enumeration contains the given element.
	 * @param enumeration the Enumeration to check
	 * @param element the element to look for
	 * @return {@code true} if found, {@code false} else
	 */
	public static boolean contains(Enumeration<?> enumeration, Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Collection contains the given element instance.
	 * <p>Enforces the given instance to be present, rather than returning
	 * {@code true} for an equal element as well.
	 * @param collection the Collection to check
	 * @param element the element to look for
	 * @return {@code true} if found, {@code false} else
	 */
	public static boolean containsInstance(Collection<?> collection, Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return the first element in '{@code candidates}' that is contained in
	 * '{@code source}'. If no element in '{@code candidates}' is present in
	 * '{@code source}' returns {@code null}. Iteration order is
	 * {@link Collection} implementation specific.
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return the first present object, or {@code null} if not found
	 */
	public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return null;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return (E) candidate;
			}
		}
		return null;
	}

	/**
	 * Find a single value of the given type in the given Collection.
	 * @param collection the Collection to search
	 * @param type the type to look for
	 * @return a value of the given type found if there is a clear match,
	 * or {@code null} if none or more than one such value found
	 */
	public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
		if (isEmpty(collection)) {
			return null;
		}
		T value = null;
		for (Object element : collection) {
			if (type == null || type.isInstance(element)) {
				if (value != null) {
					// More than one value found... no clear single value.
					return null;
				}
				value = (T) element;
			}
		}
		return value;
	}

	/**
	 * Find a single value of one of the given types in the given Collection:
	 * searching the Collection for a value of the first type, then
	 * searching for a value of the second type, etc.
	 * @param collection the collection to search
	 * @param types the types to look for, in prioritized order
	 * @return a value of one of the given types found if there is a clear match,
	 * or {@code null} if none or more than one such value found
	 */
	public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
		if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
			return null;
		}
		for (Class<?> type : types) {
			Object value = findValueOfType(collection, type);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Determine whether the given Collection only contains a single unique object.
	 * @param collection the Collection to check
	 * @return {@code true} if the collection contains a single reference or
	 * multiple references to the same instance, {@code false} else
	 */
	public static boolean hasUniqueObject(Collection<?> collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Object elem : collection) {
			if (!hasCandidate) {
				hasCandidate = true;
				candidate = elem;
			}
			else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Find the common element type of the given Collection, if any.
	 * @param collection the Collection to check
	 * @return the common element type, or {@code null} if no clear
	 * common type has been found (or the collection was empty)
	 */
	public static Class<?> findCommonElementType(Collection<?> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		Class<?> candidate = null;
		for (Object val : collection) {
			if (val != null) {
				if (candidate == null) {
					candidate = val.getClass();
				}
				else if (candidate != val.getClass()) {
					return null;
				}
			}
		}
		return candidate;
	}

	/**
	 * Marshal the elements from the given enumeration into an array of the given type.
	 * Enumeration elements must be assignable to the type of the given array. The array
	 * returned will be a different instance than the array given.
	 */
	public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
		ArrayList<A> elements = new ArrayList<A>();
		while (enumeration.hasMoreElements()) {
			elements.add(enumeration.nextElement());
		}
		return elements.toArray(array);
	}

	/**
	 * Adapt an enumeration to an iterator.
	 * @param enumeration the enumeration
	 * @return the iterator
	 */
	public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
		return new EnumerationIterator<E>(enumeration);
	}

	/**
	 * Adapt a {@code Map<K, List<V>>} to an {@code MultiValueMap<K, V>}.
	 * @param map the original map
	 * @return the multi-value map
	 * @since 3.1
	 */
	public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
		return new MultiValueMapAdapter<K, V>(map);
	}

	/**
	 * Return an unmodifiable view of the specified multi-value map.
	 * @param  map the map for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified multi-value map.
	 * @since 3.1
	 */
	public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
		Assert.notNull(map, "'map' must not be null");
		Map<K, List<V>> result = new LinkedHashMap<K, List<V>>(map.size());
		for (Map.Entry<? extends K, ? extends List<? extends V>> entry : map.entrySet()) {
			List<? extends V> values = Collections.unmodifiableList(entry.getValue());
			result.put(entry.getKey(), (List<V>) values);
		}
		Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
		return toMultiValueMap(unmodifiableMap);
	}

	
	/**
	 * 
	 * <p>方法说明：去除List中所有重复的值<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月7日下午3:11:09<p>
	 * @param list
	 * @return 去重后的List
	 */
	public static <T> List<T> removeDupValue(final List<T> list) {
		Set<T> set = new HashSet<T>();
		set.addAll(list);
		List<T> retList = new ArrayList<T>();
		retList.addAll(set);
		return retList;
	}
	
	
	/**
	 * 
	 * <p>方法说明：将一个大的List分割成小的List<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月7日下午5:45:52<p>
	 * @param list 要分割的List
	 * @param size 每个小List的大小
	 * @return 元素为小List的List
	 */
	public static <T> List<List<?>> splitList(final List<T> list,final int size) {
		List<List<?>> retList = new ArrayList<List<?>>();
		if (isEmpty(list)) {
			return retList;
		}

		List<T> tmpList = new ArrayList<T>();
		
		for (int i = 0 , j = list.size() ; i < j ; i++){
			
			tmpList.add(list.get(i));
			
			if ((i+1) % size == 0 || i+1 == j){
				retList.add(tmpList);
				tmpList = new ArrayList<T>();
			}
		}
		return retList;
	}
	

	/**
	 * Iterator wrapping an Enumeration.
	 */
	private static class EnumerationIterator<E> implements Iterator<E> {

		private final Enumeration<E> enumeration;

		public EnumerationIterator(Enumeration<E> enumeration) {
			this.enumeration = enumeration;
		}

		@Override
		public boolean hasNext() {
			return this.enumeration.hasMoreElements();
		}

		@Override
		public E next() {
			return this.enumeration.nextElement();
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported");
		}
	}


	/**
	 * Adapts a Map to the MultiValueMap contract.
	 */
	@SuppressWarnings("serial")
	private static class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {

		private final Map<K, List<V>> map;

		public MultiValueMapAdapter(Map<K, List<V>> map) {
			Assert.notNull(map, "'map' must not be null");
			this.map = map;
		}

		@Override
		public void add(K key, V value) {
			List<V> values = this.map.get(key);
			if (values == null) {
				values = new LinkedList<V>();
				this.map.put(key, values);
			}
			values.add(value);
		}

		@Override
		public V getFirst(K key) {
			List<V> values = this.map.get(key);
			return (values != null ? values.get(0) : null);
		}

		@Override
		public void set(K key, V value) {
			List<V> values = new LinkedList<V>();
			values.add(value);
			this.map.put(key, values);
		}

		@Override
		public void setAll(Map<K, V> values) {
			for (Entry<K, V> entry : values.entrySet()) {
				set(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public Map<K, V> toSingleValueMap() {
			LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K,V>(this.map.size());
			for (Entry<K, List<V>> entry : map.entrySet()) {
				singleValueMap.put(entry.getKey(), entry.getValue().get(0));
			}
			return singleValueMap;
		}

		@Override
		public int size() {
			return this.map.size();
		}

		@Override
		public boolean isEmpty() {
			return this.map.isEmpty();
		}

		@Override
		public boolean containsKey(Object key) {
			return this.map.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return this.map.containsValue(value);
		}

		@Override
		public List<V> get(Object key) {
			return this.map.get(key);
		}

		@Override
		public List<V> put(K key, List<V> value) {
			return this.map.put(key, value);
		}

		@Override
		public List<V> remove(Object key) {
			return this.map.remove(key);
		}

		@Override
		public void putAll(Map<? extends K, ? extends List<V>> map) {
			this.map.putAll(map);
		}

		@Override
		public void clear() {
			this.map.clear();
		}

		@Override
		public Set<K> keySet() {
			return this.map.keySet();
		}

		@Override
		public Collection<List<V>> values() {
			return this.map.values();
		}

		@Override
		public Set<Entry<K, List<V>>> entrySet() {
			return this.map.entrySet();
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			return map.equals(other);
		}

		@Override
		public int hashCode() {
			return this.map.hashCode();
		}

		@Override
		public String toString() {
			return this.map.toString();
		}
		
	}
}