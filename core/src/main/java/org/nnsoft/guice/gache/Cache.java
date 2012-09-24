package org.nnsoft.guice.gache;

/*
 *  Copyright 2012 The 99 Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.Serializable;

/**
 * Cache facade interface.
 */
public interface Cache
{

    /**
     * Returns <code>true</code> if this cache contains a mapping for the
     * specified key.
     *
     * @param key
     *          key whose presence in this cache is to be tested
     * @return <code>true</code> if this map contains a mapping for the
     *         specified key
     */
    <K> boolean containsKey( K key );

    /**
     * Returns the value to which this cache maps the specified key. Returns
     * <code>null</code> if the cache contains no mapping for this key. A return
     * value of <code>null</code> does not <i>necessarily</i> indicate that the
     * cache contains no mapping for the key; it's also possible that the cache
     * explicitly maps the key to <code>null</code> or the cache entry has
     * expired. The <code>containsKey</code> operation may be used to
     * distinguish these two cases.
     *
     * @param key
     *          key whose associated value is to be returned
     * @return the value to which this cache maps the specified key, or
     *         <code>null</code> if this cache contains no mapping for this key
     *
     * @see #containsKey(Serializable)
     */
    <K, V> V get( K key );

    /**
     * @return <code>true</code> if this cache contains no key-value mappings.
     */
    boolean isEmpty();

    /**
     * Associates the specified value with the specified key in this cache. If the
     * cache previously contained a mapping for this key, the old value is
     * replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return previous value associated with specified key, or <code>null</code>
     *         if there was no mapping for key. A <code>null</code> return can
     *         also indicate that this cache previously associated
     *         <code>null</code> with the specified key
     */
    <K, V> V put( K key, V value );

    /**
     * Associates the specified value with the specified key in this cache. If the
     * cache previously contained a mapping for this key, the old value is
     * replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param timeToLive
     *          the number of milliseconds until the cache entry will expire
     * @return previous value associated with specified key, or <code>null</code>
     *         if there was no mapping for key. A <code>null</code> return can
     *         also indicate that the map previously associated <code>null</code>
     *         with the specified key, if the implementation supports
     *         <code>null</code> values
     */
    <K, V> V put( K key, V value, long timeToLive );

    /**
     * Removes the mapping for this key from this cache if it is present.
     *
     * @param key key whose mapping is to be removed from the cache
     * @return previous value associated with specified key, or <code>null</code>
     *         if there was no mapping for key
     */
    <K, V> V remove( K key );

    /**
     * Returns the number of key-value mappings in this cache. If the cache
     * contains more than <code>Integer.MAX_VALUE</code> elements, returns
     * <code>Integer.MAX_VALUE</code>.
     *
     * @return the number of key-value mappings in this cache
     */
    int size();

}
