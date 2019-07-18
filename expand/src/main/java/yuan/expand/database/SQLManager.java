package yuan.expand.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YuanYe on 2018/1/5.
 * 管理SQLHelper  简化SQL语句操作
 * 1、实现数据库连接
 * 2、创建库
 * 3、创建表
 * 4、更新表字段
 * 5、删除表字段
 * 6、按id查询数据库
 * 7、模糊查询
 */

public class SQLManager {

    private final static String TAG = "SQLManager";

    private SQLiteDatabase writDB; //写操作
    private SQLHelper helper;

    private int errorCode = 0; //错误码

    private static SQLManager manager;

    public static SQLManager getInstance() {
        if (manager == null) manager = new SQLManager();
        return manager;
    }

    private SQLManager() {
    }

    /**
     * 新建并打开数据库，数据库文件保存在项目目录下
     * 进行数据库操作必须先打开数据库才能操作
     */
    public void create(Context context, String name, int version) {
        if (helper == null) {
            helper = new SQLHelper(context, name, null, version);
            writDB = helper.getWritableDatabase();
        } else {
            checkWritDB();
        }
    }

    /*
     *****************************创建表***********************************************************
     */

    /**
     * 创建表
     *
     * @param tableName      表名
     * @param primaryKeyName 自定义主键名称
     * @param map            表字段
     */
    public void createTable(String tableName, String primaryKeyName, Map<String, String> map) {
        checkWritDB();
        String sql = "create table if not exists " + tableName + "(";//创建表
        sql = sql + primaryKeyName + " integer " + " not null primary key autoincrement,";
        int i = 0;
        //默认添加主键
        for (Map.Entry<String, String> entry : map.entrySet()) {
            i++;
            if (entry.getKey().equals(primaryKeyName)) continue;
            if (i != map.size()) {
                sql = sql + entry.getKey() + " " + entry.getValue() + ",";
            } else {
                sql = sql + entry.getKey() + " " + entry.getValue();
            }
        }
        sql = sql + ")";
        writDB.execSQL(sql);
        writDB.close();
    }

    /**
     * 创建表，默认主键名_id
     *
     * @param tableName            表名
     * @param map<String1,String2> 字段名集合 String1--字段名，String2--字段类型
     *                             .默认主键为_id,自增
     */
    public void createTable(String tableName, Map<String, String> map) {
        createTable(tableName, "_id", map);
    }


    /*
     * ************************************插入数据**************************************************
     */

    /**
     * 批量插入数据
     * 采用事务处理，一次提交，提高数据库读写速度
     */
    public void insert(String tableName, String primaryKeyName, List<Object> list) {
        checkWritDB();
        writDB.beginTransaction(); // 手动设置开始事务
        for (int i = 0; i < list.size(); i++) {
            insert(tableName, primaryKeyName, list.get(i));
        }
        writDB.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        writDB.endTransaction(); // 处理完成
        writDB.close();
    }

    /**
     * 插入一条数据
     *
     * @param tableName 表名
     * @param object    插入对象
     */
    public void insert(String tableName, String primaryKeyName, Object object) {
        checkWritDB();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取该类所有的属性
        ContentValues value = new ContentValues();
        for (Field field : fields) {
            if (!primaryKeyName.equals(field.getName())) {
                try {
                    field.setAccessible(true); //取消对age属性的修饰符的检查访问，以便为属性赋值
                    Object content = field.get(object);//获取该属性的内容
                    if (field.getType() == String.class) {
                        value.put(field.getName(), (String) content);
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        value.put(field.getName(), (int) content);
                    } else if (field.getType() == Float.class || field.getType() == float.class) {
                        value.put(field.getName(), (float) content);
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        value.put(field.getName(), (double) content);
                    } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        value.put(field.getName(), (boolean) content);
                    } else if (field.getType() == Long.class || field.getType() == long.class) {
                        value.put(field.getName(), (long) content);
                    }
                    field.setAccessible(false);//恢复对age属性的修饰符的检查访问
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "DBUtil insert Error:" + e.getMessage());
                }
            }
        }
        writDB.insert(tableName, null, value);
    }

    /*
     * ************************************查询数据**************************************************
     */

    /**
     * 查询整张表
     *
     * @param tableName 表名
     */
    public <T> ArrayList<T> queryTableAll(String tableName, Class<T> clazz) {
        return query(tableName, null, null, null, null, clazz);
    }

    /**
     * 条件查询
     *
     * @param tableName     表名
     * @param columns       要想显示的列，例如 new String[]{"id","body"}
     * @param selection     where子句，例如 id=?
     * @param selectionArgs where子句对应的条件值    new String[]{"1"}
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> queryCondition(String tableName, String[] columns, String selection,
                                           String[] selectionArgs, Class<T> clazz) {
        return query(tableName, columns, selection, selectionArgs, null, clazz);
    }

    /**
     * 查询
     *
     * @param tableName 表名
     * @param orderBy   排序字段名
     */
    public <T> ArrayList<T> query(String tableName, String[] columns, String selection,
                                  String[] selectionArgs, String orderBy, Class<T> clazz) {
        checkWritDB();
        String orderby = null;
        ArrayList<T> list = null;
        if (!TextUtils.isEmpty(orderBy)) orderby = orderBy + " desc";
        //查询获得游标
        try {
            Cursor cursor = writDB.query(tableName, columns, selection, selectionArgs, null, null, orderby);
            list = handleCursor(cursor, clazz);
            cursor.close();
            writDB.close();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "DBUtil query Error:" + e.getMessage());
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "DBUtil query Error:" + e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, "DBUtil query Error:" + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "DBUtil query Error:" + e.getMessage());
        }
        return list;
    }

    /**
     * 直接调用SQL语句查询
     *
     * @param sql           sql语句
     * @param selectionArgs 需要替换的参数例如:Cursor c = db.rawQuery("select * from user
     *                      where username=? and password = ?",
     *                      new String[]{"用户名","密码"});
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> querySQL(String sql, String[] selectionArgs, Class<T> clazz) {
        checkWritDB();
        ArrayList<T> list = null;
        Cursor cursor = writDB.rawQuery(sql, selectionArgs);
        try {
            list = handleCursor(cursor, clazz);
            cursor.close();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "DBUtil querySQL Error:" + e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, "DBUtil querySQL Error:" + e.getMessage());
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "DBUtil querySQL Error:" + e.getMessage());
        }
        return list;
    }

    /**
     * 处理游标查询
     *
     * @param cursor
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private <T> ArrayList<T> handleCursor(Cursor cursor, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        ArrayList<T> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T t = clazz.newInstance();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String content = cursor.getString(i);//获得获取的数据记录第i条字段的内容
                String columnName = cursor.getColumnName(i);// 获取数据记录第i条字段名的
                Field field = t.getClass().getDeclaredField(columnName);//获取该字段名的Field对象。
                field.setAccessible(true);//取消对age属性的修饰符的检查访问，以便为属性赋值
                //属性名和字段名相同
                if (columnName.equals(field.getName())) {
                    if (field.getType() == String.class) {
                        field.set(t, content);
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        field.set(t, Integer.parseInt(content));
                    } else if (field.getType() == Float.class || field.getType() == float.class) {
                        field.set(t, Float.parseFloat(content));
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        field.set(t, Double.parseDouble(content));
                    } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        field.set(t, Boolean.parseBoolean(content));
                    } else if (field.getType() == Long.class || field.getType() == long.class) {
                        field.set(t, Long.parseLong(content));
                    }
                }
                field.setAccessible(false);//恢复对age属性的修饰符的检查访问
            }
            list.add(t);
            cursor.moveToNext();
        }
        return list;
    }


    /**
     * 查询最新插入的一条数据的id
     * ***注意，只在当前进程插入有效
     * 需要在插入后调用，否则查询到的数据不匹配
     */
    public int searchLastInsertRowID(String tableName) {
        checkWritDB();
        String sql = "select last_insert_rowid() from " + tableName;
        Cursor cursor = writDB.rawQuery(sql, null);
        int a = -1;
        if (cursor.moveToFirst()) {
            a = cursor.getInt(0);
        }
        return a;
    }

    /**
     * ************************************删除数据**************************************************
     */

    public void deleteTable(String tableName) {
        //清空表
        try {
            checkWritDB();
            writDB.execSQL("delete from " + tableName);
            writDB.close();
        } catch (Exception e) {
            Log.e(TAG, "DBUtil deleteTable Error:" + e.getMessage());
        }
    }

    /**
     * 按条件删除数据
     *
     * @param table       表名
     * @param whereClause where子句，例如 id=?
     * @param whereArgs   where子句对应的条件值    new String[]{"1"}
     */
    public void delete(String table, String whereClause, String[] whereArgs) {
        checkWritDB();
        writDB.delete(table, whereClause, whereArgs);
        writDB.close();
    }

    /*
     * ************************************更新数据**************************************************
     */

    /**
     * 更新数据
     *
     * @param table       表名
     * @param values      需要更新的内容，例如 values.put("price", 10.99);
     * @param whereClause where子句，例如 id=?
     * @param whereArgs   where子句对应的条件值    new String[]{"1"}
     */
    public void update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        checkWritDB();
        writDB.update(table, values, whereClause, whereArgs);
        writDB.close();
    }

    public void update(String table, Object object, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取该类所有的属性
        for (Field field : fields) {
            try {
                field.setAccessible(true); //取消对age属性的修饰符的检查访问，以便为属性赋值
                String content = field.get(object) + "";//获取该属性的内容
                if (!TextUtils.isEmpty(content)) {
                    values.put(field.getName(), content);
                }
                field.setAccessible(false);//恢复对age属性的修饰符的检查访问
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        checkWritDB();
        writDB.update(table, values, whereClause, whereArgs);
        writDB.close();
    }

    /**
     * 检验Db是否开启
     */
    private void checkWritDB() {
        if (helper == null || writDB == null) throw new NullPointerException("请先创建数据库");
        if (!writDB.isOpen()) {
            writDB = helper.getWritableDatabase();
        }
    }
}
