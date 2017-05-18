/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
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
package de.greenrobot.daogenerator.gentest;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p/>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.neo2.telebang.greendao");

        addRecentKeyword(schema);
        addWishListVideo(schema);
        addDownloadList(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addKeywordSearch(Schema schema) {
        Entity note = schema.addEntity("DBKeywordSearch");
        note.addIdProperty();
        note.addStringProperty("keyword").notNull();
    }

    private static void addWishListVideo(Schema schema) {
        Entity note = schema.addEntity("DBWishListVideo");
        note.addIdProperty();
        note.addIntProperty("VideoID").notNull();
        note.addStringProperty("VideoCategory").notNull();
        note.addStringProperty("VideoThumbnail").notNull();
        note.addStringProperty("VideoName").notNull();
        note.addStringProperty("VideoCreateAt").notNull();
        note.addIntProperty("UserID").notNull();
    }

    private static void addDownloadList(Schema schema) {
        Entity note = schema.addEntity("DBVideoDownload");
        note.addIdProperty();
        note.addIntProperty("VideoID").notNull();
        note.addStringProperty("VideoName").notNull();
        note.addStringProperty("VideoCategory").notNull();
        note.addStringProperty("VideoCreateAt").notNull();
        note.addStringProperty("VideoPath").notNull();
    }

    private static void addRecentKeyword(Schema schema) {
        Entity note = schema.addEntity("DBKeyword");
        note.addIdProperty();
        note.addStringProperty("keyword").notNull();
    }

}
