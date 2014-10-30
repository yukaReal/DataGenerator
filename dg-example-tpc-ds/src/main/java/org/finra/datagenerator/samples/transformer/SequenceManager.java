/*
 * Copyright 2014 DataGenerator Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.finra.datagenerator.samples.transformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Sequance Manger for DB data Transformer.
 */
public final class SequenceManager {
    
    private static Map<String, Sequence> sequences = new HashMap<String, Sequence>();
    
    private SequenceManager() {
     // prevents instantiation from external entities
    }
    
    /**
     * Skeleton like method for finding out corresponding Sequence and get next value
     * @param name - name of sequence
     * @param start - start value
     * @param increment - increment value
     * @return next sequence value
     */
    public static String getNextValue(String name, int start, int increment) {
        String sequenceIdentifier = name + start + increment;
        if (sequences.containsKey(sequenceIdentifier)) {
            return sequences.get(sequenceIdentifier).getNextValue();
        } else {
            Sequence sequence = new Sequence(sequenceIdentifier, name, start, increment);
            sequences.put(sequenceIdentifier, sequence);
            return String.valueOf(start);
        }
    }

}
