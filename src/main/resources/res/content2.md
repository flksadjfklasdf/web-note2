# 数据绑定

插值

```html
<div id="dv">
    <span>{{ i }}</span>
    <span>{{ getValue() }}</span>
</div>
<script>
    new Vue({
        el:"#dv",
        data:{
            i:1
        },
        methods:{
            getValue(){
                return Math.random()
            }
        }
    })
</script>
```

绑定属性

```html
<div id="dv">
    <a :href="href">{{ href }}</a><br>
    <input type="text" :value="href"><!-- 单向 -->
    <input type="text" v-model:value="href"><!-- 双向 -->

</div>
<script>
    new Vue({
        el:"#dv",
        data:{
            href:"https://www.baidu.com"
        }
    })
</script>
```

# 事件

点击事件

```html
<div id="dv">
    <input @keydown="tal">fasdfasdfas</input><!--按下按钮时-->
    <input @keyup="tal">fasfasdf</input><!--抬起按钮时-->
    <input @keydown.enter="tal">fsdfsadfasdf</input><!--enter按下时-->
    <input @keydown.down="tal">fsdfsadfasdf</input><!--down-->
</div>
<script>
    new Vue({
        el:"#dv",
        data:{

        },
        methods:{
            tal(ax){
                alert("HELLO"+ax);
            }
        }
    })
</script>
```

# 计算属性

```html
<div id="dv">
    <span>{{ name }}</span>
</div>
<script>
    new Vue({
        el:"#dv",
        data:{
            a:"a",
            b:"b"
        },
        methods:{

        },
        computed:{
            name:{
                get(){
                    return this.a+"---"+this.b
                }
            }
        }
    })
</script>
```

# 监视

```html
<div id="dv">
    <input type="text" v-model:value="a"></input>
</div>
<script>
    new Vue({
        el:"#dv",
        data:{
            a:"a",
        },
        watch:{
            a:{
                handler(n,o){
                    alert('值发生了变化'+n+o)
                }
            }
        }
    })
</script>
```

绑定样式

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="application/javascript" src="vue.js"></script>
    <style>
        .b{
            background-color:BLUE;
        }
        .r{
            background-color:RED;
        }
    </style>
</head>
<body>
<div id="dv">
    <div style="width: 50px;height: 50px" :class="c" @click="change"></div>
</div>
<script>
    new Vue({
        el:"#dv",
        data:{
            c:'b',
        },
        methods:{
            change(){
                if(this.c==='b'){
                    this.c='r'
                }else{
                    this.c='b'
                }
            }

        }
    })
</script>
</body>
</html>
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="application/javascript" src="vue.js"></script>
    <style>
        .b {
            background-color: BLUE;
        }
        .r {
            background-color: RED;
        }
    </style>
</head>
<body>
<div id="dv">
    <div style="cursor: pointer" :style="{marginLeft: ml+'px',marginTop: mt+'px',width: w+'px' , height: h+'px'}"
         class="r" @click="change"></div>
</div>
<script>
    new Vue({
        el: "#dv",
        data: {
            w: 55,
            h: 55,
            ml: 66,
            mt: 66
        },
        methods: {
            change() {
                this.w = Math.random() * 100
                this.h = Math.random() * 100
                this.ml = Math.random() * 1000
                this.mt = Math.random() * 1000
            }
        }
    })
</script>
</body>
</html>
```