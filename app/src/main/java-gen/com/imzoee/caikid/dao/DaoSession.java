package com.imzoee.caikid.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.dao.Comment;
import com.imzoee.caikid.dao.Order;

import com.imzoee.caikid.dao.UserDao;
import com.imzoee.caikid.dao.RecipeDao;
import com.imzoee.caikid.dao.CommentDao;
import com.imzoee.caikid.dao.OrderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig recipeDaoConfig;
    private final DaoConfig commentDaoConfig;
    private final DaoConfig orderDaoConfig;

    private final UserDao userDao;
    private final RecipeDao recipeDao;
    private final CommentDao commentDao;
    private final OrderDao orderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        recipeDaoConfig = daoConfigMap.get(RecipeDao.class).clone();
        recipeDaoConfig.initIdentityScope(type);

        commentDaoConfig = daoConfigMap.get(CommentDao.class).clone();
        commentDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        recipeDao = new RecipeDao(recipeDaoConfig, this);
        commentDao = new CommentDao(commentDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Recipe.class, recipeDao);
        registerDao(Comment.class, commentDao);
        registerDao(Order.class, orderDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        recipeDaoConfig.getIdentityScope().clear();
        commentDaoConfig.getIdentityScope().clear();
        orderDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public RecipeDao getRecipeDao() {
        return recipeDao;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

}
