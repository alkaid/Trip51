package com.alkaid.base.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alkaid.base.common.LogUtil;


/**
 * SQL类，数据库相关的通用类
 * @author cater
 *
 */
public abstract class BaseSQL{
	protected Context context;
	protected SQLiteOpenHelper helper;
		
	public BaseSQL(Context context) {
		this.context = context;
		this.helper = createDBHelper();
	}
	
	abstract protected SQLiteOpenHelper createDBHelper() ;
	
	public void init(){
		helper.getWritableDatabase();
	}
	
	/**
	 * 执行SQL脚本  {@link SQLiteOpenHelper #onCreate(SQLiteDatabase)}里请勿调用此方法
	 * @param is
	 */
	public void execSQLScript(InputStream is){
		SQLiteDatabase db=helper.getWritableDatabase();
		execSQLScript(db, is);
	}
	/**
	 * 执行SQL脚本  {@link SQLiteOpenHelper #onCreate(SQLiteDatabase)}里可以调用此方法
	 * @param db
	 * @param is
	 */
	public void execSQLScript(SQLiteDatabase db,InputStream is){
//		InputStream is = context.getResources().openRawResource(R.raw.olympic);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = null;
			StringBuffer sql = new StringBuffer();
			while((str = br.readLine())!=null){
				if(str.trim().length()>0&&
						str.substring(str.length()-1).equals(";")){
					sql.append(str.replaceAll(";", ""));
					db.execSQL(sql.toString());
					sql.delete(0, sql.length());
					continue;
				}
				sql.append(str+"\n");
				
			}
		} catch (SQLException e) {
			LogUtil.e("脚本执行出错");
			LogUtil.e(e);
		} catch (IOException e) {
			LogUtil.e("IO操作出错");
			LogUtil.e(e);
		}finally{
			try {
//				db.close();
				is.close();
			} catch (IOException e) {
				LogUtil.e("输入的流不存在，为空");
				LogUtil.e(e);
			}
		}
	}
	/**
	 * 数据库建表的方法，并提供返回SQLiteDatabase,进行数据库后续操作
	 * create table(_id INTEGER PRIMARY KEY AUTOINCREMENT,time TEXT);
	 * @param table 创建的表的名称
	 * @param columns 是每列的集合,String的格式_id INTEGER PRIMARY KEY AUTOINCREMENT
	 * @return SQLiteDatabase对象，用于做关闭操作
	 */
	public  void createTable(SQLiteDatabase db,String table,List<String> columns) {
		try {
			StringBuffer str = new StringBuffer();
			for(String column:columns){
				str.append(column+",");
			}
			str.replace(str.length()-1, str.length(), ";");
			String sql = "CREATE TABLE "+table+" ("+ str + ")";
			db.execSQL(sql);
			db.close();
		} catch (SQLException e) {
			LogUtil.e(e);
			LogUtil.e("建表的sql语句有错误.");
		}
	} 
	/**
	 * 数据库建表的方法，并提供返回SQLiteDatabase,进行数据库后续操作
	 * create table(_id INTEGER PRIMARY KEY AUTOINCREMENT,time TEXT);
	 * @param table 创建的表的名称
	 * @param columns 是每列的集合,String的格式_id INTEGER PRIMARY KEY AUTOINCREMENT
	 * @return SQLiteDatabase对象，用于做关闭操作
	 */
	public  void createTable(String table,List<String> columns) {
		SQLiteDatabase db=helper.getWritableDatabase();
		createTable(db, table, columns);
	} 
	
	/**
	 * 删除数据库的表名为table的表
	 * @param table 删除的表的名称
	 */
	public  void dropTable(SQLiteDatabase db,String table){
		try {
			String sql = "DROP TABLE "+table;
			db.execSQL(sql);
			db.close();
		} catch (SQLException e) {
			LogUtil.e(e);
			LogUtil.e("删除数据库的sql有错误");
		}
	}
	/**
	 * 删除数据库的表名为table的表
	 * @param table 删除的表的名称
	 */
	public  void dropTable(String table){
		SQLiteDatabase db=helper.getWritableDatabase();
		dropTable(db, table);
	}
	/**
	 * 用于DB做分页操作的。查询出的结果是所有字段的
	 * @param table从第几条数据开始查询
	 * @param firstResult跳过几行
	 * @param maxResult取几行
	 * @return
	 */
	public Cursor queryPaging(String table,List<String> columns,int firstResult,int maxResult){
		String sql = null;
		if(columns!=null){
			StringBuffer columnStr = new StringBuffer();
			//拼接需要查询的列
			for(String update:columns){
				columnStr.append(update+",");
			}
			columnStr.deleteCharAt(columnStr.length()-1);
			 sql = "SELECT "+columnStr.toString()+" FROM "+table+" LIMIT "+maxResult+" OFFSET "+firstResult;
		}else{
			 sql = "SELECT * FROM "+table+" LIMIT "+maxResult+" OFFSET "+firstResult;
		}
		return processCurosr(this.rawQuery(sql, null));
	}
	
	/**
	 * 用于判断查询出的Cursor是否为null
	 * @param cursor
	 * @return
	 */
	private Cursor processCurosr(Cursor cursor){
		if(cursor!=null){
			if(cursor.moveToFirst()){
				return cursor;
			}else{
				cursor.close();
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 用于对某些sql出现转义字符的进行处理
	 * @param str需要转义的字符
	 * @return
	 */
	public String formatSQL(String str){
		return str.replaceAll("'","''");
	}
	
	/**
	 * 查询数据
	 * 
	 * @param table
	 *            查询的 table name
	 * @param fieldNames
	 *            查询的数据的字段名称
	 * @param selection
	 *            查询条件字符串，如：field1 = ? and field2 = ?
	 * @param selectionArgs
	 *            查询的条件的值，如：["a","b"]
	 * @param groupBy
	 *            groupBy后面的字符串，如：field1,field2
	 * @param having
	 *            having后面的字符串
	 * @param orderBy
	 *            orderBy后面的字符串
	 * @return Cursor 包含了取得数据的值
	 */
	public Cursor select(String table, String[] fieldNames, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy)
	{
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.query(table, fieldNames, selection, selectionArgs, groupBy, having, orderBy);
	}

	/**
	 * 更新数据
	 * 
	 * @param tableName
	 *            更新数据的table name
	 * @param cv
	 *            更新数据的字段键值对
	 * @param where
	 *            更新数据的条件
	 * @param whereValues
	 *            更新数据的条件值
	 * @return int 更新的笔数
	 */
	public int update(String tableName, ContentValues cv, String where, String[] whereValues)
	{
		try
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			int count = db.update(tableName, cv, where, whereValues);
			db.close();

			return count;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param tableName
	 *            添加数据的table name
	 * @param cv
	 *            添加数据的键值对
	 * @return long id 添加成功后的id
	 */
	public long insert(String tableName, ContentValues cv)
	{
		try
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			long id = db.insert(tableName, null, cv);
			db.close();
			return id;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * <b>description :</b> 根据Key和Value自动判断插入或者更新
	 * 
	 * @param table
	 * @param values
	 * @param keyName
	 * @param keyValue
	 * @return
	 */
	public long insertOrUpdate(String table, ContentValues values,
			String keyName, Object keyValue) {
		String[] columns = new String[] { keyName };
		String selection = keyName + "=?";
		String[] selectionArgs = toArgs(keyValue);
		boolean isExist =select(table, columns, selection, selectionArgs, null, null, null)
				.getCount() > 0;
		if (isExist) {
			values.remove(keyName);
			return update(table, values, selection, selectionArgs);
		} else {
			return insert(table, values);
		}
	}
	
	/**
	 * <b>description :</b> 转换为字符数组
	 * 
	 * @param values
	 * @return
	 */
	private String[] toArgs(Object... values) {
		String[] args = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			args[i] = String.valueOf(values[i]);
		}
		return args;
	}

	/**
	 * 删除数据
	 * 
	 * @param tableName
	 *            删除数据的table name
	 * @param where
	 *            删除数据的条件
	 * @param whereValues
	 *            删除数据的条件值
	 * @return int 删除的笔数
	 */
	public int delete(String tableName, String where, String[] whereValues)
	{
		try
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			int count = db.delete(tableName, where, whereValues);
			db.close();
			return count;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 执行数据库的SQL语句
	 * 
	 * @param sql
	 *            语句
	 */
	public boolean execSQL(String sql)
	{
		try
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			db.execSQL(sql);
			db.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 执行数据库的SQL语句
	 * 
	 * @param sql
	 *            语句
	 * @param bindArgs
	 *            sql语句中的占位符参数；支持byte[], String, Long, Double类型数据
	 * @return 执行是否成功，true：成功、false：失败
	 */
	public boolean execSQL(String sql, Object[] bindArgs)
	{
		try
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			db.execSQL(sql, bindArgs);

			db.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 执行数据库的SQL语句
	 * 
	 * @param sql
	 *            语句
	 * @param bindArgs
	 *            sql语句中的占位符参数；支持byte[], String, Long, Double类型数据
	 * @return 执行是否成功，true：成功、false：失败
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs)
	{
		try
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			return db.rawQuery(sql, selectionArgs);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * </br><b>title : </b> 开启事务 </br><b>description :</b> 开启事务
	 */
	public void beginTransaction() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
	}

	/**
	 * </br><b>title : </b> 设置事务标志为成功，当结束事务时就会提交事务 </br><b>description
	 * :</b>设置事务标志为成功，当结束事务时就会提交事务
	 */
	public void setTransactionSuccessful() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.setTransactionSuccessful();
	}
	/**
	 * 结束事务
	 */
	public void endTransaction() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.endTransaction();
	}
	
	/**
	 * 拼insert语句
	 * @param sb
	 * @param tableName
	 * @param columns
	 * @param values
	 * @return
	 */
	public StringBuilder appendInsertSql(StringBuilder sb,String tableName,String[] columns,String[] values){
		sb.append("INSERT INTO ").append(tableName).append("(");
		for(String colume:columns){
			sb.append(colume).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") VALUES (");
		for(String value:values){
			sb.append("'").append(value).append("',");
		}
		sb.deleteCharAt(sb.length()-1);	
		sb.append(");");
		return sb;
	}
	
	/**
	 * 用于关闭SqliteDatabase的操作
	 */
	public void close(){
		helper.close();
	}
	

}