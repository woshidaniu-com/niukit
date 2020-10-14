数据库元数据 : http://git.oschina.net/free/DBMetadata/tree/master

本工具可用于数据库表和字段的查询，以及数据库元数据的进一步使用。

目前支持以下数据库(都能正确获取注释信息):

    Oracle
    Mysql
    MariaDB
    SQLite
    Hsqldb
    PostgreSQL
    DB2
    SqlServer(2005+) - 必须使用jtds驱动

本工具目前分为三部分
DBMetadata-core

数据库元数据核心部分，该部分完全独立，不依赖任何第三方，获取元数据部分的代码参考了MyBatis Generator。

如果你要连接数据库，需要有该数据库的JDBC驱动。
使用方法：
1. 引入jar包或者Maven依赖：

<dependency>
    <groupId>com.github.abel533</groupId>
    <artifactId>DBMetadata-core</artifactId>
    <version>0.1.1</version>
</dependency>

下载Jar包:DBMetadata-core-x.x.x.jar
2. 使用方法

首先创建一个SimpleDataSource:

SimpleDataSource dataSource = new SimpleDataSource(
        Dialect.MYSQL,
        "jdbc:mysql://localhost:3306/test",
        "root",
        ""
);

除了上面这种方式外，还可以使用SimpleDataSource(Dialect dialect, DataSource dataSource)这个构造方法，直接使用其他的DataSource。

然后就是用创建好的dataSource去创建DBMetadataUtils:

DBMetadataUtils dbMetadataUtils = new DBMetadataUtils(dataSource);

创建一个DatabaseConfig,调用introspectTables(config)方法获取数据库表的元数据：

DatabaseConfig config = new DatabaseConfig("mydb", null);
List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);

这里需要注意DatabaseConfig，他有下面三个构造方法：

    DatabaseConfig()

    DatabaseConfig(String catalog, String schemaPattern)

    DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern)

一般情况下我们需要设置catalog和schemaPatter，还可以设置tableNamePattern来限定要获取的表。

其中schemaPatter和tableNamePattern都支持sql的%和_匹配。

获取数据库表的元数据后，我们就可以利用这些数据了。

下面代码是简单的将这些信息输出到控制台：

for (IntrospectedTable table : list) {
    System.out.println(table.getName() + ":");
    for (IntrospectedColumn column : table.getAllColumns()) {
        System.out.println(column.getName() + " - " +
                column.getJdbcTypeName() + " - " +
                column.getJavaProperty() + " - " +
                column.getJavaProperty() + " - " +
                column.getFullyQualifiedJavaType().getFullyQualifiedName() + " - " +
                column.getRemarks());
    }
}

DBMetadata-generator

利用数据库元数据，根据模板生成一些内容。

该项目目前只提供了一个BeetlTemplate，只有两个静态方法，这只是一个简单的例子。

使用方法如下：

public static void main(String[] args) throws IOException, SQLException {
    DBMetadataUtils dbUtils = new DBMetadataUtils(
            new SimpleDataSource(Dialect.ORACLE, "jdbc:oracle:thin:@//localhost/orcl", "user", ""));

    List<IntrospectedTable> tables = dbUtils.introspectTables(dbUtils.getDefaultConfig());

    DBMetadataUtils.sortTables(tables);

    BeetlTemplate.exportDatabaseHtml(tables, "d:/test", "db");

    for (IntrospectedTable table : tables) {
        BeetlTemplate.exportTableHtml(table, "d:/test/tables", table.getName());
    }
}

DBMetadata-swing

这个子项目也算是一个对**DBMetadata-core**的使用，通过上述工具获取元数据后，使用swing界面展示数据，并且可以通过查询来筛选符合要求的数据。

这个项目除了实现基本的表和字段查询外，还算是一个基于界面使用该工具的基础，你可以在该项目基础上增加其他功能。
启动

运行com.github.abel533.Launch即可启动本项目。

本项目提供打包好的程序可供直接使用。

下载地址:http://pan.baidu.com/s/1poGI6

程序为绿色版，需要jre1.7+支持

Windows

根据你jre是32位还是64位来选择dbs_32.exe或者dbs_64.exe来运行。

linux

首先给run.sh增加执行权限，然后运行run.sh