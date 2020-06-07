# StoreApp
Android ContentProvider 数据库存储

# 日志存储

数据库格式
--------
create table if not exists logs(uid INTEGER(64) primary key autoincrement, time int(64), txt text)

简单示例
--------
- 添加日志 LogStore.add(getApplicationContext(), "msg");
- 删除日志 LogStore.delete(getApplicationContext(), 1, 10);
- 读取日志 List<LogValue> logs = LogStore.get(getApplicationContext(), 1, 100);
  
# 数据存储
存储键值对，类似Map<String, String>

数据库格式
---------
create table if not exists datas(uid text primary key, value text)

简单示例
-------
- 设置 DataStore.set(getApplicationContext(), "key", "value");
- 获取 String value = DataStore.get(getApplicationContext(), "key", "default");


### 详细使用可以查看app里面的代码

