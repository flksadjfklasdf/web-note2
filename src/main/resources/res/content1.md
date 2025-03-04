# 引入依赖

```xml
<dependency>
    <groupId>org.luaj</groupId>
    <artifactId>luaj-jse</artifactId>
    <version>3.0.1</version>
</dependency>
```

###示例

```java
package com.lua.test;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public class T1 {

    public static void main(String[] args) {
        // 创建 Lua 解释器
        LuaValue luaGlobals = JsePlatform.standardGlobals();

        try {
            // 加载 Lua 脚本
            luaGlobals.get("dofile").call(LuaValue.valueOf("D:\\desktop\\l1.lua"));

            // 调用 Lua 函数
            LuaValue luaFunction = luaGlobals.get("multiply");
            LuaValue result = luaFunction.call(LuaValue.valueOf(5425345),LuaValue.valueOf(342));

            // 处理 Lua 函数返回的结果
            System.out.println("Lua Function returned: " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

```lua
function multiply(a,b)
    return a*b
end
```

## 使用lua对java进行回调

```java
package com.lua.test;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
public class JavaLuaExample {
    public static void main(String[] args) {
        // 创建一个全局Lua环境，它包含了Java的标准库
        Globals globals = JsePlatform.standardGlobals();

        // 加载Lua代码
        LuaValue chunk = globals.load("function callJavaMethod(input) return javaMethod(input) end");

        // 执行Lua代码
        chunk.call();

        // 获取Lua函数
        LuaValue luaFunction = globals.get("callJavaMethod");

        // 在Java中定义一个方法
        LuaValue javaMethod = new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                System.out.println("Java方法被调用");
                return LuaValue.valueOf("Hello from Java");
            }
        };

        // 将Java方法添加到Lua环境
        globals.set("javaMethod", javaMethod);

        // 调用Lua函数
        LuaValue result = luaFunction.call(LuaValue.valueOf("Hello from Lua"));
        System.out.println(result.tojstring());
    }
}

```

# 生成随机数测试示例

```java
package com.lua.test;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.Random;

public class Example {
    public static void main(String[] args) {
        Globals globals = JsePlatform.standardGlobals();
        globals.get("dofile").call(LuaValue.valueOf("example.lua"));
        LuaValue luaFunction = globals.get("generate");

        final Random r = new Random();

        LuaValue random = new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(r.nextDouble());
            }
        };
        globals.set("random", random);
        LuaValue result = luaFunction.call();
        System.out.println("生成结果:" + result.tojstring());
    }

}

```

```lua
function generate()
    b = random()
    return b*10000
end
```