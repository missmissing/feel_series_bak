首先看一下web.xml，通过查看log4j的日志可以发现在服务器启动时applicationContext.xml
是先于spring3-servlet.xml加载，因此applicationContext.xml中已经注册到BeanFactory的bean在spring3-servlet.xml
也是可以使用的，
比如在applicationContext.xml中通过context:component-scan扫描了service和dao包，
而在spring3-servlet.xml只扫描了web包，但web包中的Controller依然被注入了Service。


applicationContext 为父容器，servlet 为子容器
