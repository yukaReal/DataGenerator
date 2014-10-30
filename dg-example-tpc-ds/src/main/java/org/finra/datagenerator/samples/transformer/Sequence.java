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

/**
 * Sequance for DB data Transformer.
 */
public class Sequence {
    private String sequenceIdentifier;
    private String name;
    private int increment;
    private long currentValue;

/**
 * Constructor method for Sequences
 * @param sequenceIdentifier - internal sequence identifier
 * @param name - sequence name
 * @param start - sequence start value
 * @param increment - sequence increment value
 */
    public Sequence(final String sequenceIdentifier, final String name, final int start, final int increment) {
        this.sequenceIdentifier = sequenceIdentifier;
        this.name = name;
        currentValue = start;
        this.increment = increment;
    }

/**
 * get next value method
 * @return next sequence value
 */
    public String getNextValue() {
        currentValue += increment;
        return String.valueOf(currentValue);
    }

}
