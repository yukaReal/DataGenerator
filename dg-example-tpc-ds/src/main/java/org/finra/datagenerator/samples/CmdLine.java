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
package org.finra.datagenerator.samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.finra.datagenerator.distributor.multithreaded.DefaultDistributor;
import org.finra.datagenerator.engine.Engine;
import org.finra.datagenerator.engine.scxml.SCXMLEngine;
import org.finra.datagenerator.samples.consumer.RepeatingDataConsumer;
import org.finra.datagenerator.samples.transformer.DBTransformer;
import org.finra.datagenerator.writer.DefaultWriter;

/**
 * Driver for a data generation for TPC-DS DB.
 */
public final class CmdLine {
    private static final Logger log = Logger.getLogger(CmdLine.class);

    private CmdLine() {
    }

    /**
     * Entry point for the TPC-DS.
     * * @param args Command-line arguments for the this module. Not used not.
     */
    public static void main(String[] args) {
        generateStoreTableData();
    }

    private static void generateStoreTableData() {
        Engine engine = new SCXMLEngine();

        InputStream is = CmdLine.class.getResourceAsStream("/tpc-ds_store.xml");

        engine.setModelByInputFileStream(is);

        engine.setBootstrapMin(1);

        //Prepare the consumer with the proper writer and transformer
        RepeatingDataConsumer consumer = new RepeatingDataConsumer();
        consumer.setRepeatNumber(14);
        consumer.addDataTransformer(new DBTransformer());
        
        createNecessaryFolder();
        File file = new File("results/store.dat");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            consumer.addDataWriter(new DefaultWriter(fileOutputStream, new String[]{"var__s_store_sk", "var__s_store_id"}));

            //Prepare the distributor
            DefaultDistributor defaultDistributor = new DefaultDistributor();
            defaultDistributor.setThreadCount(1);
            defaultDistributor.setDataConsumer(consumer);
            defaultDistributor.setMaxNumberOfLines(10);
            Logger.getLogger("org.apache").setLevel(Level.WARN);

            engine.process(defaultDistributor);
        } catch (FileNotFoundException e) {
            log.fatal("Oops! Can't create output file!.. " + e);
        }
        
    }

    private static void createNecessaryFolder() {
        File resultFolder = new File("results");
        resultFolder.mkdirs();
        log.info("Created / verified existance of '" + resultFolder.getAbsolutePath() + "' folder for results...");
        
    }
}
