# Mybatis

## 基本mybatis

依赖引入

```xml
<!--mybatis引入-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version> <!-- 替换为最新版本 -->
</dependency>
<!--数据库连接器引入-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.23</version> <!-- 替换为最新版本 -->
</dependency>
```

数据源配置

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

配置mybatis的配置文件

```xml
<!-- mybatis-config.xml -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>

```

创建实体类

```java
// User.java
public class User {
    private Long id;
    private String username;
    private String email;

    // getters and setters
}

```

创建mapper接口

```java
// UserMapper.java
public interface UserMapper {
    User getUserById(Long id);
}

```

创建mapper接口的映射xml文件

```xml
<!-- UserMapper.xml -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.UserMapper">
    <select id="getUserById" resultType="com.example.model.User">
        SELECT * FROM user WHERE id = #{id}
    </select>
</mapper>

```

使用myabtis

```java
// UserService.java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }
}

```

配置接口所在位置

```java
@SpringBootApplication
@MapperScan("com.example.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

```

测试

```java
// UserServiceTest.java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = userService.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }
}
```

## MyBatis Generator

依赖引入

```xml
<build>
        
    <!-- 构建过程中用到的插件 -->
    <plugins>
        
        <!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.0</version>
    
            <!-- 插件的依赖 -->
            <dependencies>
                
                <!-- 逆向工程的核心依赖 -->
                <dependency>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-core</artifactId>
                    <version>1.3.2</version>
                </dependency>
                    
                <!-- 数据库连接池 -->
                <dependency>
                    <groupId>com.mchange</groupId>
                    <artifactId>c3p0</artifactId>
                    <version>0.9.2</version>
                </dependency>
                    
                <!-- MySQL驱动 -->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.8</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
 </build>
```

创建配置文件generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
 <!DOCTYPE generatorConfiguration
PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
 <generatorConfiguration>
    <!--
            targetRuntime: 执行生成的逆向工程的版本
                    MyBatis3Simple: 生成基本的CRUD（清新简洁版）
                    MyBatis3: 生成带条件的CRUD（奢华尊享版）
     -->
    <context id="DB2Tables" targetRuntime="MyBatis3Simple">
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/mybatis"
                        userId="root"
                        password="123456">
        </jdbcConnection>
        <!-- javaBean的生成策略-->
        <javaModelGenerator targetPackage="com.atguigu.mybatis.bean" 
targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="com.atguigu.mybatis.mapper" 
 targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER" 
targetPackage="com.atguigu.mybatis.mapper"  targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_dept" domainObjectName="Dept"/>
    </context>
 </generatorConfiguration>
```

## 多数据源配置

创建配置类

```java
@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSource1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.local")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.remote")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManager1")
    @Primary
    public DataSourceTransactionManager transactionManager1(@Qualifier("dataSource1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionManager2")
    public DataSourceTransactionManager transactionManager2(@Qualifier("dataSource2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactory1")
    @Primary
    public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSource1") DataSource dataSource,
                                               ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper1/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource,
                                               ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper2/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate1")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlSessionTemplate2")
    public SqlSessionTemplate sqlSessionTemplate2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```

定义接口

```java
public interface UserMapper {

    List<User> getAllUsers();

    // other methods...
}
```

定义数据源

```properties
# 数据源1
spring.datasource.local.jdbc-url=jdbc:mysql://192.168.1.199:3306/example
spring.datasource.local.username=root
spring.datasource.local.password=gengronglin
spring.datasource.local.driver-class-name=com.mysql.cj.jdbc.Driver
# 数据源2
spring.datasource.remote.jdbc-url=jdbc:mysql://192.168.1.199:3306/example
spring.datasource.remote.username=root
spring.datasource.remote.password=gengronglin
spring.datasource.remote.driver-class-name=com.mysql.cj.jdbc.Driver
```

获取Mapper接口

```java
@Service
public class UserService {

    @Autowired
    @Qualifier("sqlSessionTemplate1")
    private SqlSessionTemplate sqlSessionTemplate1;

    @Autowired
    @Qualifier("sqlSessionTemplate2")
    private SqlSessionTemplate sqlSessionTemplate2;

    public List<User> getAllUsersFromDataSource1() {
        UserMapper userMapper = sqlSessionTemplate1.getMapper(UserMapper.class);
        return userMapper.getAllUsers();
    }

    public List<User> getAllUsersFromDataSource2() {
        UserMapper userMapper = sqlSessionTemplate2.getMapper(UserMapper.class);
        return userMapper.getAllUsers();
    }
}
```

## 事务

```java
//配置事务
@Bean(name = "transactionManager1")
@Primary
public DataSourceTransactionManager transactionManager1(@Qualifier("dataSource1") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}

@Bean(name = "transactionManager2")
public DataSourceTransactionManager transactionManager2(@Qualifier("dataSource2") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}
```

使用事务

```java
@Transactional
public void moveLocalToRemote(){
    LPersonExample lPersonExample=new LPersonExample();
    List<LPerson> lPeople = lPersonMapper.selectByExample(lPersonExample);
    log.info("获取到的用户:{}",lPeople);

    for (LPerson lPerson : lPeople) {
        moveToRemote(lPerson);
    }
    throw new RuntimeException();
}
@Transactional
public void moveToRemote(LPerson lPerson) {
    LPersonExample lPersonExamplex=new LPersonExample();
    lPersonExamplex.createCriteria().andPersonidEqualTo(lPerson.getPersonid());

    delete(lPersonExamplex);
    insert(lPerson);
}
@Transactional(value = "transactionManager2")//配置事务管理器
public void insert(LPerson lPerson) {
    rPersonMapper.insert(new RPerson(lPerson));
}
@Transactional(value = "transactionManager1")
public void delete(LPersonExample lPersonExamplex) {
    lPersonMapper.deleteByExample(lPersonExamplex);
}
```

```java
@Transactional(rollbackFor = Exception.class)
//当出现Exception异常时才进行回滚
```

