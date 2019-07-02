package yuan.tools_independ.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：数据库操作工具类
 *
 * @author yuanye
 * @date 2019/4/26 13:56
 */
public class DBUtil {

    private static final String TAG = "DBUtil";
    /**
     * 默认自增字段名
     */
    private static final String PRIMARYKEY = "id";
    /**
     * 单例
     */
    private static DBUtil manager;
    /**
     * 数据库名
     */
    private static String dbName;
    /**
     * 数据库版本
     */
    private static int dbVersion;
    /**
     * 写操作
     */
    private SQLiteDatabase writDB;
    /**
     * Android 原生数据库操作对象
     */
    private SQLHelper helper;

    /**
     * 获取实例
     * 当前数据版本号为1
     * 操作数据库名称为当前项目名数据库
     *
     * @param context
     * @return
     */
    public static DBUtil getDB(Context context) {
        dbVersion = 1;
        return getDB(context, dbVersion);
    }

    /**
     * 获取实例
     * 操作数据库名称为当前项目名数据库
     *
     * @param context
     * @return
     */
    public static DBUtil getDB(Context context, int version) {
        PackageManager pm = context.getPackageManager();
        dbName = context.getApplicationInfo().loadLabel(pm).toString();
        dbVersion = version;
        return getDB(context, dbName, dbVersion);
    }

    /**
     * 获取实例
     *
     * @return
     */
    /**
     * @param context
     * @param name    数据库名称
     * @param version 数据库版本
     * @return
     */
    public static DBUtil getDB(Context context, String name, int version) {
        if (manager == null) {
            synchronized (DBUtil.class) {
                if (manager == null || name != dbName || version != dbVersion) {
                    manager = new DBUtil(context, name, version);
                }
            }
        }
        return manager;
    }

    /**
     * 初始化
     *
     * @param context
     * @param name
     * @param version
     */
    private DBUtil(Context context, String name, int version) {
        if (context == null) throw new NullPointerException("Context 不能为空，请先调用init初始化");
        if (TextUtils.isEmpty(name)) throw new NullPointerException("请先指定操作数据库名称");
        //获取数据库操作对象
        helper = new SQLHelper(context, name, null, version);
        writDB = helper.getWritableDatabase();
        dbName = name;
        dbVersion = version;
    }

    /**
     * 获取数据库版本
     */
    public int getVersion() {
        checkWritDB();
        return writDB.getVersion();
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (helper != null && writDB != null) {
            writDB.close();
        }
    }

    /*
     *****************************创建表***********************************************************
     */

    /**
     * 创建数据表
     * <p>
     * 根据数据类型反射创建数据表
     *
     * @param clazz
     */
    public void createTable(Class clazz) {
        checkWritDB();
        Field[] fields = clazz.getDeclaredFields();//获取该类所有的属性
        HashMap<String, String> map = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                map.put(field.getName(), "text");
            } else if (field.getType() == Integer.class || field.getType() == int.class) {
                map.put(field.getName(), "integer");
            } else if (field.getType() == Float.class || field.getType() == float.class) {
                map.put(field.getName(), "real");
            } else if (field.getType() == Double.class || field.getType() == double.class) {
                map.put(field.getName(), "real");
            } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                map.put(field.getName(), "bit");
            } else if (field.getType() == Long.class || field.getType() == long.class) {
                map.put(field.getName(), "integer");
            } else if (field.getType() == char.class) {
                map.put(field.getName(), "blob");
            } else if (field.getType() == byte.class || field.getType() == Byte.class) {
                map.put(field.getName(), "blob");
            } else if (field.getType() == short.class || field.getType() == Short.class) {
                map.put(field.getName(), "integer");
            } else {

            }
            field.setAccessible(false);
        }
        _createTable(getClazzName(clazz), PRIMARYKEY, map);
        if (writDB != null) writDB.close();
    }

    /**
     * 创建表，默认主键名_id
     *
     * @param tableName            表名
     * @param map<String1,String2> 字段名集合 String1--字段名，String2--字段类型
     *                             .默认主键为_id,自增
     */
    public void createTable(String tableName, Map<String, String> map) {
        _createTable(tableName, PRIMARYKEY, map);
        if (writDB != null) writDB.close();
    }

    /**
     * 创建表
     *
     * @param tableName      表名
     * @param primaryKeyName 自定义主键名称
     * @param map            表字段<字段名，字段类型>
     */
    public void createTable(String tableName, String primaryKeyName, Map<String, String> map) {
        _createTable(tableName, primaryKeyName, map);
        if (writDB != null) writDB.close();
    }

    /**
     * 创建表
     *
     * @param tableName      表名
     * @param primaryKeyName 自定义主键名称
     * @param map            表字段<字段名，字段类型>
     */
    private void _createTable(String tableName, String
            primaryKeyName, Map<String, String> map) {
        checkWritDB();
        String sql = "create table if not exists " + tableName + "( '";//创建表
        sql = sql + primaryKeyName + "' integer" + " not null primary key autoincrement,";
        int i = 0;
        //默认添加主键
        for (Map.Entry<String, String> entry : map.entrySet()) {
            i++;
            if (entry.getKey().equals(primaryKeyName)) continue;
            if (i != map.size()) {
                sql = sql + "'" + entry.getKey() + "' " + entry.getValue() + ",";
            } else {
                sql = sql + "'" + entry.getKey() + "' " + entry.getValue();
            }
        }
        sql = sql + ")";
        try {
            writDB.execSQL(sql);
        } catch (SQLiteException e) {
            Log.e(TAG, "DBUtil createTable execSQL error:  \r\nsql=" + sql + "\r\n" + e.getMessage());
        }
    }


    /*
     * ************************************插入数据**************************************************
     */

    /**
     * 插入多条数据
     *
     * @param object
     */
    public void insertList(List object) {
        checkWritDB();
        writDB.beginTransaction(); // 手动设置开始事务
        List list = (List) object;
        for (Object obj : list) {
            insertData(obj);
        }
        writDB.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        writDB.endTransaction(); // 处理完成
        writDB.close();
    }

    /**
     * 插入数据
     * <p>
     * 如果传入List,则批量插入
     */
    public void insert(Object object) {
        if (helper == null || writDB == null) {
            //创建表
            createTable(object.getClass());
        }

        checkWritDB();
        insertData(object);
        writDB.close();
    }

    /**
     * 通过反射插入一条数据
     *
     * @param object
     */
    private void insertData(Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取该类所有的属性
        ContentValues value = new ContentValues();
        for (Field field : fields) {
            if (PRIMARYKEY.equals(field.getName())) {
                //跳过自增字段
                continue;
            }
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
                } else if (field.getType() == byte.class || field.getType() == Byte.class) {
                    value.put(field.getName(), (Byte) content);
                } else if (field.getType() == short.class || field.getType() == Short.class) {
                    value.put(field.getName(), (Short) content);
                } else {

                }
                field.setAccessible(false);//恢复对age属性的修饰符的检查访问
            } catch (IllegalAccessException e) {
                Log.e(TAG, "DBUtil insert Error:" + e.getMessage());
            }
        }
        writDB.insert(getClazzName(object.getClass()), null, value);
    }


    /*
     * ************************************查询数据**************************************************
     */

    /**
     * 查询整张表
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> query(Class<T> clazz) {
        return query(null, null, null, null, clazz);
    }

    /**
     * 条件查询
     *
     * @param columns       要想显示的列，例如 new String[]{"id","body"}
     * @param selection     where子句，例如 id=?
     * @param selectionArgs where子句对应的条件值    new String[]{"1"}
     * @param orderBy       排序字段名
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> query(String[] columns, String selection,
                                  String[] selectionArgs, String orderBy, Class<T> clazz) {
        checkWritDB();
        String orderby = null;
        ArrayList<T> list = null;
        if (!TextUtils.isEmpty(orderBy)) orderby = orderBy + " desc";
        //查询获得游标
        try {
            Cursor cursor = writDB.query(getClazzName(clazz), columns, selection, selectionArgs, null, null, orderby);
            list = handleCursor(cursor, clazz);
            cursor.close();
            writDB.close();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "DBUtil query异常 Error:" + e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, "DBUtil query异常 Error:" + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "DBUtil query异常 Error:" + e.getMessage());
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
        try {
            Cursor cursor = writDB.rawQuery(sql, selectionArgs);
            list = handleCursor(cursor, clazz);
            cursor.close();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "DBUtil querySQL异常 Error:" + e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, "DBUtil querySQL异常 Error:" + e.getMessage());
        }
        if (writDB != null) writDB.close();
        return list;
    }

    /**
     * 查询最新插入的一条数据的id
     * <p>
     * 注意，只在当前进程插入有效,需要在插入后调用,否则查询到的数据不匹配
     */
    public int queryLastId(Class clazz) {
        checkWritDB();
        String sql = "select last_insert_rowid() from " + getClazzName(clazz);
        Cursor cursor = writDB.rawQuery(sql, null);
        int a = -1;
        if (cursor.moveToFirst()) {
            a = cursor.getInt(0);
        }
        if (writDB != null) writDB.close();
        return a;
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
    private <T> ArrayList<T> handleCursor(Cursor cursor, Class<T> clazz) throws
            IllegalAccessException, InstantiationException {
        ArrayList<T> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T t = clazz.newInstance();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String content = cursor.getString(i);//获得获取的数据记录第i条字段的内容
                String columnName = cursor.getColumnName(i);// 获取数据记录第i条字段名的
                Field field = null;
                try {
                    field = clazz.getDeclaredField(columnName);//获取该字段名的Field对象。
                } catch (NoSuchFieldException e) {
                    //如果不存在该字段，跳过
                    continue;
                }
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
     * ************************************删除数据**************************************************
     */

    /**
     * 按照表名删除，删除整张表
     *
     * @param tableName
     */
    public void delete(String tableName) {
        try {
            checkWritDB();
            writDB.execSQL("delete from " + getClazzName(tableName));
            writDB.close();
        } catch (Exception e) {
            Log.e(TAG, "DBUtil delete Error:" + e.getMessage());
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
    public void update(String table, ContentValues values, String whereClause, String[]
            whereArgs) {
        checkWritDB();
        writDB.update(table, values, whereClause, whereArgs);
    }

    /**
     * 更新数据
     *
     * @param object      更新数据实体
     * @param whereClause 更新条件
     * @param whereArgs
     */
    public void update(Object object, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取该类所有的属性
        for (Field field : fields) {
            try {
                field.setAccessible(true); //取消对age属性的修饰符的检查访问，以便为属性赋值
                Object content = field.get(object);//获取该属性的内容
                if (content != null) {
                    if (content.getClass() == String.class || content.getClass() == char.class) {
                        values.put(field.getName(), (String) content);
                    } else if (content.getClass() == Integer.class || content.getClass() == int.class) {
                        values.put(field.getName(), (int) content);
                    } else if (content.getClass() == Float.class || content.getClass() == float.class) {
                        values.put(field.getName(), (float) content);
                    } else if (content.getClass() == Double.class || content.getClass() == double.class) {
                        values.put(field.getName(), (double) content);
                    } else if (content.getClass() == Boolean.class || content.getClass() == boolean.class) {
                        values.put(field.getName(), (boolean) content);
                    } else if (content.getClass() == Long.class || content.getClass() == long.class) {
                        values.put(field.getName(), (long) content);
                    } else if (content.getClass() == byte.class || content.getClass() == Byte.class) {
                        values.put(field.getName(), (byte[]) content);
                    } else if (content.getClass() == short.class || content.getClass() == Short.class) {
                        values.put(field.getName(), (short) content);
                    } else {
                        update(content, whereClause, whereArgs);
                    }
                }
                field.setAccessible(false);//恢复对age属性的修饰符的检查访问
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        checkWritDB();
        writDB.update(getClazzName(object.getClass().getName()), values, whereClause, whereArgs);
        writDB.close();
    }

    /**
     * 检验Db是否开启
     */
    private void checkWritDB() {
        if (helper == null || writDB == null) throw new NullPointerException("请先打开数据库");
        if (!writDB.isOpen()) {
            writDB = helper.getWritableDatabase();
        }
    }

    /**
     * 解析名字
     */
    private static String getClazzName(Class clazz) {
        return getClazzName(clazz.getName());
    }

    private static String getClazzName(String clazz) {
        String[] names = clazz.split("\\.");
        if (names.length > 0) {
            return names[names.length - 1];
        }
        return "";
    }
}
