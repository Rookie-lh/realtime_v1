## Hbase

```
Hbase(Bigtable)是一种分布式、可扩展，支持海量数据存储的NoSQL数据库  负责计算，存储，几十亿数据中秒级查询,基于hdfs，可以在hdfs上做随机写操作，实现hdfs随机写操作，在hdfs上进行增删改查

hbase是稀疏的，分布式的，持久的多维排序map
```

### 1.数据模型和存储结构

```
Hbase的逻辑存储结构：
列式存储
RowKey：为行键，在一张表中必须唯一(按字典序排序，)
列族：一个列族可以包含多个列，动态添加列
region：是一张表的切片，把表横向切分的切片

物理存储结构：(时间很重要，时间戳是实现我们在hbase操作的核心)
存储当时操作数据的各种信息，RowKey，列族，列名，操作时间，操作类型，对应的值
如果有新数据插入或者修改，操作类型为put，会保留之前的数据，如果取值，返回操作时间大的那条数据的值
如果删除，操作类型为delete，会保留之前的数据，如果delete的时间戳比put的时间戳大就不返回数据


nameSpace：
相当于mysql中的数据库，默认有两个命名空间hbase和default，hbase存放的hbase的一些元数据，default是用户默认使用的
补充：kafka的元数据存放在__cusumer_offset下的系统自带的topic,DDL操作表，DML操作数据的
region：
类似于关系型数据库的表概念。不同的是，HBase定义表时只需要声明列族即可，不需
要声明具体的列。这意味着，往HBase写入数据时，字段可以动态、按需指定。因此，和关
系型数据库相比，HBase能够轻松应对字段变更的场景。
Row:
HBase 表中的每行数据都由一个RowKey和多个Column(列)组成，数据是按照 RowKey的字典顺序存储的，并且查询数据时只能根据RowKey进行检索，所以RowKey的设计十分重要。
Column
HBase 中的每个列都由 Column Family(列族)和 Column Qualifier(列限定符)进行限定,info:nameinfo:age。建表时，只需指明列族，而列限定符无需预先定义。
Time Stamp
用于标识数据的不同版本(version)，每条数据写入时，如果不指定时间，系统会自动为其加上该字段，其值为写入HBase的时间
Cell
由{rowkey, column Family:column Qualifier, time Stamp}唯一确定的单元。cell 中的数据是没有类的，全部是字节码形式存贮(底层是字节数组存储)

```

![Hbase逻辑存储结构](F:/knowledge_database/Hbase/Hbase逻辑存储结构.png)

![Hbase数据模型](F:/knowledge_database/Hbase/Hbase数据模型.png)

![Hbase物理存储结构](F:/knowledge_database/Hbase/Hbase物理存储结构.png)

### 2.Hbase架构

```
1.客户端： 客户端是 HBase 的用户接口，用于与 HBase 集群进行交互。客户端可以通过 HBase 的 Java API 或 Thrift、REST 等接口来访问 HBase 集群，进行数据的读写操作。

2.ZooKeeper： ZooKeeper 是 HBase 的协调服务，用于协调 HBase 集群的状态和元数据信息。HBase使用ZooKeeper 来进行主节点的选举、RegionServer 的注册和状态监控等操作。

3.HMaster： HMaster 是 HBase 的主节点，负责管理 HBase 集群的元数据信息，包括表的创建、删除、分割、合并等操作。HMaster 还负责监控 RegionServer 的状态，并负责 RegionServer 的负载均衡和故障转移。

4.RegionServer： RegionServer 是 HBase 的工作节点，负责存储和处理数据。每个 RegionServer 可以管理多个 Region，每个 Region 对应一个 HBase 表的一个分区。RegionServer 负责处理客户端的读写请求，并负责数据的存储和检索。

5.HLog： HLog 是 HBase 的 Write-Ahead Log（WAL）文件，用于记录数据的变更操作。HLog 用于保证数据的一致性和持久性，以防止数据丢失。

6.HRegion： HRegion 是 HBase 的数据存储单元，对应一个 HBase 表的一个分区。每个 HRegion 包含多个 Store，每个 Store 对应一个列族。HRegion 负责存储和管理数据，并负责处理客户端的读写请求。

7.Store： Store 是 HBase 的数据存储单元，对应一个 HBase 表的一个列族。每个 Store 包含多个 StoreFile，每个 StoreFile 对应一个 HFile。Store 负责存储和管理数据，并负责处理客户端的读写请求。

8.MemStore： MemStore 是 HBase 的内存存储单元，用于缓存数据的变更操作。MemStore 用于缓存数据的变更操作，以提高数据的写入性能。当 MemStore 的数据量达到一定阈值时，会将数据刷写到磁盘上的 StoreFile 中。

9.HFile： HFile 是 HBase 的数据文件，用于存储数据。HFile 是基于 HDFS 的数据文件，用于存储 HBase 表的数据。HFile 采用了块压缩和块索引等技术，以提高数据的存储效率和读取性能。

10.HDFS： HDFS 是 HBase 的底层存储引擎，用于存储 HBase 表的数据。HBase 使用 HDFS 来存储 HFile 和 HLog 文件，以实现数据的持久化和高可靠性。

11.DataNode： DataNode 是 HDFS 的数据节点，用于存储 HBase 表的数据。HBase 使用 DataNode 来存储 HFile 和 HLog 文件，以实现数据的持久化和高可靠性。

Master的作用：(自带高可用，可以直接在多台机器中起Master)
Table：create，delete，alter
RegionServer：分配regions到每个RegionServer，监控每个RegionServer的状态
RegionServer的作用：
Data：get，put，delete
Region：splitRegion，compactRegion

主要用于高可用
WAL
WAL全称为Write Ahead Log，它最大的作用就是 故障恢复
WAL是HBase中提供的一种高并发、持久化的日志保存与回放机制
每个业务数据的写入操作（PUT/DELETE/INCR），都会保存在WAL中
一旦服务器崩溃，通过回放WAL，就可以实现恢复崩溃之前的数据
物理上存储是Hadoop的Sequence File
AL全称为Write Ahead Log，它最大的作用就是 故障恢复

WAL是HBase中提供的一种高并发、持久化的日志保存与回放机制
每个业务数据的写入操作（PUT/DELETE/INCR），都会保存在WAL中
一旦服务器崩溃，通过回放WAL，就可以实现恢复崩溃之前的数据
物理上存储是Hadoop的Sequence File

```

