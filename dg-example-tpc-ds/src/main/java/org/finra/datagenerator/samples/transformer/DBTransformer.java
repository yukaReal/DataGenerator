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

import org.apache.commons.scxml.model.ModelException;
import org.apache.log4j.Logger;
import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.EquivalenceClassTransformer;

import java.util.Map;
import java.util.Random;

/**
 * DB data transformer.
 */
public class DBTransformer extends EquivalenceClassTransformer {
    private static final Logger log = Logger.getLogger(DBTransformer.class);
    private final Random random = new Random(System.currentTimeMillis());

    /**
     * The transform method for this DataTransformer
     * 
     * @param cr
     *            a reference to DataPipe from which to read the current map
     */
    public void transform(DataPipe cr) {
         super.transform(cr);

        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();

            // if (value.equals("#{customplaceholder}")) {
            // // Generate a random number
            // int ran = random.nextInt();
            // entry.setValue(String.valueOf(ran));
            // }

            if (value.startsWith("%")) {
                String macro;
                String expr;

                int param = value.indexOf("(");
                if (param != -1) {
                    macro = value.substring(1, param);
                    expr = value.substring(param + 1, value.length() - 1);
                } else {
                    macro = value.substring(1);
                    expr = "";
                }

                StringBuilder result = new StringBuilder();

                switch (macro) {
                case "sequance":
                    try {
                        result.append(getSequanceValue(expr));
                    } catch (ModelException e) {
                        log.fatal("Oops! ModelException: " + e);
                    }
                    break;
                default:
                    result.append(value);
                    break;
                }

                entry.setValue(result.toString());
            }
        }
    }

    private String getSequanceValue(String expr) throws ModelException {
        String[] exprSplitted = expr.split(",");
        if (3 == exprSplitted.length) {
            return SequenceManager.getNextValue(exprSplitted[0], Integer.valueOf(exprSplitted[1]), Integer.valueOf(exprSplitted[2]));
        } else {
            throw new ModelException("Can't find all 3 parameters for sequance. All sequances have to have: name, start value and increment value.");
        }
    }

}
