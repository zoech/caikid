package com.imzoee.caikid.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.imzoee.caikid.dao.Recipe;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RECIPE.
*/
public class RecipeDao extends AbstractDao<Recipe, Void> {

    public static final String TABLENAME = "RECIPE";

    /**
     * Properties of entity Recipe.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Integer.class, "id", false, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Info = new Property(2, String.class, "info", false, "INFO");
        public final static Property Img_path = new Property(3, String.class, "img_path", false, "IMG_PATH");
        public final static Property Price = new Property(4, Double.class, "price", false, "PRICE");
        public final static Property OriginPrice = new Property(5, Double.class, "originPrice", false, "ORIGIN_PRICE");
        public final static Property Stock = new Property(6, Integer.class, "stock", false, "STOCK");
        public final static Property Sales = new Property(7, Integer.class, "sales", false, "SALES");
        public final static Property Status = new Property(8, Integer.class, "status", false, "STATUS");
        public final static Property Desc = new Property(9, String.class, "desc", false, "DESC");
        public final static Property Number_comment = new Property(10, Integer.class, "number_comment", false, "NUMBER_COMMENT");
    };


    public RecipeDao(DaoConfig config) {
        super(config);
    }
    
    public RecipeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RECIPE' (" + //
                "'ID' INTEGER," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'INFO' TEXT," + // 2: info
                "'IMG_PATH' TEXT," + // 3: img_path
                "'PRICE' REAL," + // 4: price
                "'ORIGIN_PRICE' REAL," + // 5: originPrice
                "'STOCK' INTEGER," + // 6: stock
                "'SALES' INTEGER," + // 7: sales
                "'STATUS' INTEGER," + // 8: status
                "'DESC' TEXT," + // 9: desc
                "'NUMBER_COMMENT' INTEGER);"); // 10: number_comment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RECIPE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Recipe entity) {
        stmt.clearBindings();
 
        Integer id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(3, info);
        }
 
        String img_path = entity.getImg_path();
        if (img_path != null) {
            stmt.bindString(4, img_path);
        }
 
        Double price = entity.getPrice();
        if (price != null) {
            stmt.bindDouble(5, price);
        }
 
        Double originPrice = entity.getOriginPrice();
        if (originPrice != null) {
            stmt.bindDouble(6, originPrice);
        }
 
        Integer stock = entity.getStock();
        if (stock != null) {
            stmt.bindLong(7, stock);
        }
 
        Integer sales = entity.getSales();
        if (sales != null) {
            stmt.bindLong(8, sales);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(9, status);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(10, desc);
        }
 
        Integer number_comment = entity.getNumber_comment();
        if (number_comment != null) {
            stmt.bindLong(11, number_comment);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Recipe readEntity(Cursor cursor, int offset) {
        Recipe entity = new Recipe( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // info
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // img_path
            cursor.isNull(offset + 4) ? null : cursor.getDouble(offset + 4), // price
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // originPrice
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // stock
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // sales
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // status
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // desc
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // number_comment
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Recipe entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setInfo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImg_path(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPrice(cursor.isNull(offset + 4) ? null : cursor.getDouble(offset + 4));
        entity.setOriginPrice(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setStock(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setSales(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setStatus(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setDesc(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setNumber_comment(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Recipe entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Recipe entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
