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
package com.imzoee;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 *
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */

public class AllDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3,"com.imzoee.caikid.dao");

        addUser(schema);
        addRecipe(schema);

        new DaoGenerator().generateAll(schema,args[0]);
    }

    private static void addUser(Schema schema){
        Entity user = schema.addEntity("User");

        user.addIntProperty("id");                     /* user id */
        user.addStringProperty("account");             /* email adress */
        user.addStringProperty("pwd");                 /* password for user */
        user.addStringProperty("name").notNull();      /* name */
        user.addIntProperty("credit");                 /* user credits */
        user.addStringProperty("avatarUrl");           /* user avatar's url */
    }

    private static void addRecipe(Schema schema){
        Entity recipe = schema.addEntity("Recipe");

        recipe.addIntProperty("id");                   /* recipe id */
        recipe.addStringProperty("name");
        recipe.addStringProperty("info");              /* short description of the recipe */
        recipe.addStringProperty("img_path");          /* the  path of the image on the server */
        recipe.addDoubleProperty("price");
        recipe.addDoubleProperty("originPrice");
        recipe.addIntProperty("stock");
        recipe.addIntProperty("sales");
        recipe.addBooleanProperty("status");               /* 上架 1, 下架 0 */
        //recipe.addStringProperty("desc");              /* full descirption of the recipe */
        recipe.addIntProperty("number_comment");       /* number of comments */
        recipe.addDoubleProperty("score");
        /* score, the heat average */
        /* list of the comments' id */
        /* type of this recipe */
    }
}
