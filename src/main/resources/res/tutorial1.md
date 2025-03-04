Markdown 是一种轻量级标记语言，它允许人们使用易读易写的纯文本格式编写文档。

Markdown 语言在 2004 由约翰·格鲁伯（英语：John Gruber）创建。

Markdown 编写的文档可以导出 HTML 、Word、图像、PDF、Epub 等多种格式的文档。

Markdown 编写的文档后缀为 .md, .markdown


# 这是标题1
```markdown
# 这是标题1
```

## 这是标题2
```markdown
## 这是标题2
```

### 这是标题3
```markdown
### 这是标题3
```

#### 这是标题4
```markdown
#### 这是标题4
```

##### 这是标题5
```markdown
##### 这是标题5
```

<a style="color:red;font-size:25px">注意 '#' 和 标题之间需要有空格</a>


这是**加粗**
```markdown
这是**加粗**
```

这是*倾斜*
```markdown
这是*倾斜*
```

这是<u>下划线</u>
```markdown
这是<u>下划线</u>
```

这是~~删除线~~
```markdown
这是~~删除线~~
```

52. 这是标号
```markdown
52. 这是标号
```

- 这是列表
```markdown
- 这是列表
```

- [ ] 这是复选框1
- [x] 这是复选框2

```markdown
- [ ] 这是复选框1
- [x] 这是复选框2
```

这是[链接](https://www.example.com)
```markdown
这是[链接](https://www.example.com)
```

```javascript
console.log("这是代码块")
```

```markdown
<!-- 应当使用 ` 反单引号,此处为避免代码混淆,使用 ' 单引号代替 -->
'''javascript<!-- 注明代码的类型,以获得不同的语法高亮比如javascript,java,c,poperties,shell,markdown........ -->
console.log("这是代码块")
'''
```

使用一对反单引号来表示`引用`或者`关键字`,如:`SELECT`
```markdown
使用一对反单引号来表示`引用`或者`关键字`,如:`SELECT`
```

这是图片
```markdown
<img src="http://localhost:8080/web_note_war_exploded/image/getImage?imageId=1a4e4e359a5e4b3e9f4094734a72cea0" alt="示例图片" style="max-height: 400px; width: auto;">
```

这是表格

|表头|表头|表头|
|----|----|----|
|单元|单元|单元|
|单元|单格|单格|

```markdown
|表头|表头|表头|
|----|----|----|
|单元|单元|单元|
|单元|单格|单格|
```

<a style="color:red;font-size:25px">注意!!!</a>

代码的换行不会导致实际的换行,如下

#### 代码:
```markdown
测试1
测试2
```
#### 显示:
测试1
测试2

#### 如果需要换行,需要在代码行末加两个空格,或者多一个空行

#### 代码:

```markdown
测试1  <!--注意两个空格-->
测试2<!--没有空格-->
```
#### 或者:

```markdown
测试1

测试2
```

#### 显示:

测试1  
测试2

这是分割线

---

```markdown
---
```

或者(三个下划线)

```markdown
___
```

使用反斜杠转义 `\` 来显示特殊内容，如 '\*\*加粗\*\*' , '\#' , '\)'

```markdown
使用反斜杠转义 `\` 来显示特殊内容，如 '\*\*加粗\*\*' , '\#' , '\)'
```