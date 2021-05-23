/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.spring.boot.fc;

import java.io.Serializable;
import java.util.Objects;

abstract class Pair<K,V> implements Serializable{

   /**
    * Key of this <code>Pair</code>.
    */
   private K key;

   /**
    * Gets the key for this pair.
    * @return key for this pair
    */
   public K getKey() { return key; }

   /**
    * Value of this this <code>Pair</code>.
    */
   private V value;

   /**
    * Gets the value for this pair.
    * @return value for this pair
    */
   public V getValue() { return value; }

   /**
    * Creates a new pair
    * @param key The key for this pair
    * @param value The value to use for this pair
    */
   protected Pair( K key, V value) {
       this.key = key;
       this.value = value;
   }

   @Override
   public String toString() {
       return key + "=" + value;
   }

   @Override
   public int hashCode() {
       return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (!Objects.equals(key, pair.key)) return false;
            if (!Objects.equals(value, pair.value)) return false;
            return true;
        }
        return false;
    }
}

