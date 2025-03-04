# Spring Boot

## 基本项目构建

maven依赖引入spring boot父项目

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.4.RELEASE</version>
</parent>
```

引入相应的starter依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

启动框架

```java
//jar包
@SpringBootApplication(scanBasePackages = "org.example")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}
```

```java
//war包
@SpringBootApplication(scanBasePackages = "org.example")
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }
}
```

项目打包

**jar包:**引入依赖

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

```shell
java -jar test.jar
```

如果需要外部配置文件

```shell
java -jar test3-1.0-SNAPSHOT.jar --spring.config.location=file:./config/application.properties
```

打包执行即可

**war包:**

```xml
<packaging>war</packaging>
```

创建webapps目录以及web.xml文件

## 项目配置

常用配置:

```properties
#spring boot基本配置
spring.application.name=my-application
server.port=8080
```

```properties
#mybatis配置
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
```

```properties
#日志配置
logging.level.root=info
logging.level.org.springframework.web=debug
```

```properties
#jpa配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

```properties
#thymeleaf模板引擎配置
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

```properties
#文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

bean管理

@configuration

```java
@Configuration(proxyBeanMethods = true)//单实例:false
public class b1 {

    @Bean(name = "AAA")
    public User getUser(){
        return new User("FSDFSDF","341231");
    }
    @Bean(name = "AAB")
    public User getUser2(){
        return new User("12312312312","BBBBBBB");
    }
}
```

yaml

```yaml
person:
  userName: zhangsan
  boss: true
  birth: 2019/12/9
  age: 18
  interests:
  	- 篮球
  	- 足球
  	- 18
  score: {englist:80,math:90}
  salarys:
    - 9999.98
    - 9999.99
  pet:
    name: AAA
    weight: 123
  allpet:
    sick:
      - {name: A,weight: 9}
      - {name: B,weight: 3}
      - name: C
        weight: 4
    health:
      - {name: A,weight: 9}
      - {name: B,weight: 3}
```



```java
@Controller
public class test {


    @Autowired
    @Qualifier("AAB")
    User u;

    @ResponseBody
    @RequestMapping("/")
    public String index(){
        return "Hello!"+u.toString();
    }
}
```

## spring boot扩展

**引入测试模块**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

示例

```java
@Slf4j
@SpringBootTest
@ContextConfiguration(classes = Main.class)
public class MySpringBootTests {

    @Autowired
    Service service;

    @Test
    public void myTest() {
        System.out.println(service);

    }
}
```

**引入jstl标签库(jsp)**

```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
```

**引入lombok,快速添加setter,getter等方法**

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.22</version>
</dependency>
```

**引入mybatis**

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version>
</dependency>
```

**引入mysql**

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.26</version>
</dependency>
```

**thymeleaf**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

```



## spring mvc

配置yaml

```yaml
#配置默认静态资源位置,默认resource/static
spring:
	resources:
		static-locations: classpath:/haha
```

properties

```properties
spring.mvc.static-path-pattern= /res/**
spring.resources.static-locations= classpath:/ccc/
#访问/res/a.html将被映射到/ccc/a.html
```

**拦截器**

创建拦截器

```java
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
//创建一个实现 HandlerInterceptor 接口的拦截器类。这个接口包括三个方法：preHandle、postHandle 和 afterCompletion，你可以根据需求选择性地实现它们。
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 在请求处理之前进行调用
        System.out.println("preHandle method is called");
        return true; // 如果返回false，请求将被终止
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 在请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
        System.out.println("postHandle method is called");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // 在整个请求完成之后调用，也就是在 DispatcherServlet 渲染了对应的视图之后执行
        System.out.println("afterCompletion method is called");
    }
}
```

配置拦截器

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyInterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/api/**");
    }
}

```



**异常解析器**

```java
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}

```

**文件上传**

```java
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file , HttpServletRequest httpServletRequest,HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    throw new IOException("非法的文件名");
                }

                Path filePath = Paths.get(path).resolve(originalFilename);

                File dest = filePath.toFile();

                file.transferTo(dest);

                modelAndView.addObject("message", "文件上传成功！");
            } catch (IOException e) {
                e.printStackTrace();
                modelAndView.addObject("message", e.getMessage());
            }

        } else {
            modelAndView.addObject("message", "请选择要上传的文件！");
        }
        String requestURL = httpServletRequest.getRequestURL().toString().replace("uploadFile","upload");

        httpSession.setAttribute("originalUrl",requestURL);
        modelAndView.setViewName("message");
        return modelAndView;
    }
```

**文件下载**

```java
        @RequestMapping("/download/{resourceName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String resourceName) {
        Path filePath = Paths.get(path).resolve(resourceName);
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + resourceName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
```

## spring aop

切点表达式

```java
@Pointcut("execution(public com.example.service.MyService.myMethod(String, int))")
//其中修饰符public可以*代表任意修饰符
//其中单层包com,example可以使用*代替
//其中任意层包com.example.service使用**代替
//其中任意方法名myMethod使用*代替
//其中任意参数String, int使用..代替
```

通知类型

```java
@AfterReturning： 在目标方法成功执行后调用，用于处理返回值或修改返回值。

@AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", returning = "result")
public void afterReturningAdvice(Object result) {
    // 在方法成功执行并返回后执行的逻辑
}
@AfterThrowing： 在目标方法抛出异常时调用，用于处理异常情况。

@AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", throwing = "ex")
public void afterThrowingAdvice(Exception ex) {
    // 在方法抛出异常时执行的逻辑
}
@After： 在目标方法执行后调用，无论方法是否成功执行。

@After("execution(* com.example.service.*.*(..))")
public void afterAdvice() {
    // 在方法执行后无论成功还是失败都执行的逻辑
}
@Around： 环绕通知，包围目标方法的执行。
    
@Around("execution(* com.example.service.*.*(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    // 在目标方法执行前后进行处理
    Object result = joinPoint.proceed();
    // 在目标方法执行后进行处理
    return result;
}

@Before("execution(* com.example.service.*.*(..))")
public void beforeServiceMethodExecution() {
    System.out.println("Before advice: Executing the method...");
}
```



示例

```java
//特定类的特定方法名以及特定参数：
@Pointcut("execution(* com.example.service.MyService.myMethod(String, int))")
public void specificMethodWithParameters() {}
这将匹配MyService类中名为myMethod，参数为String和int的方法。

//特定类的特定方法名：
@Pointcut("execution(* com.example.service.MyService.myMethod())")
public void specificMethodName() {}
这将匹配MyService类中名为myMethod的方法，不考虑参数。

//特定类的所有方法：
@Pointcut("execution(* com.example.service.MyService.*(..))")
public void allMethodsInSpecificClass() {}
这将匹配MyService类中的所有方法。

//特定包的所有方法：
@Pointcut("execution(* com.example..*.*(..))")
public void allMethodsInSpecificPackage() {}
这将匹配com.example包及其子包下的所有类的所有方法。

//不定包的特定类：
@Pointcut("execution(* *..MyService.*(..))")
public void specificClassInAnyPackage() {}
```

代理方式

```java
JDK动态代理：

//使用接口的目标类，Spring可以使用Java的动态代理机制来创建代理对象。这要求目标类实现至少一个接口。Spring的Proxy接口是基于JDK动态代理的。

public interface MyService {
    void myMethod();
}

public class MyServiceImpl implements MyService {
    public void myMethod() {
        // 实现方法逻辑
    }
}
///在使用时，Spring会为MyService接口创建一个动态代理对象。

///CGLIB代理：

///如果目标类没有实现任何接口，Spring将使用CGLIB库创建代理对象。CGLIB（Code Generation Library）是一个强大的代码生成库，可以在运行时生成字节码。
public class MyService {
    public void myMethod() {
        // 实现方法逻辑
    }
}
///Spring将使用CGLIB为MyService类生成代理对象。
```

使用代理

创建配置类

```java
@Aspect
@Component
public class TestAop {
    @Before("execution(* org.example.service.PersonService.*(..))")
    public void beforeServiceMethodExecution(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            System.out.println(arg);
        }
    }
}
```

调用方法

```java
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
        PersonService personServiceImpl =run.getBean(PersonService.class);
        personServiceImpl.testAop("fasfdasfa","11111111111111111111111111111111111111111111");

        System.out.println(personServiceImpl.getClass());
    }
```

