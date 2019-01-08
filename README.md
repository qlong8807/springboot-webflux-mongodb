# SpringBoot响应式服务端webflux的使用。(客户端例子在src/test/java中)

原来的**方法**返回User的话，那现在就返回Mono<User>；原来返回List<User>的话，那现在就返回Flux<User>。
对于稍微复杂的业务逻辑或一些必要的异常处理，比如UserService的save方法，请一定采用响应式的编程方式来定义，从而一切都是异步非阻塞的。

## 启动后，用PostMan或其他软件
* 发送post请求http://localhost:8080/user?name=ww&age=25&email=123@qew.com，可以看到新增了用户；
* 用get发送http://localhost:8080/user，可以看到所有的用户；
* 用get发送http://localhost:8080/user/zs，可以看到zs的信息；
* get --- http://localhost:8080/user/findAll1 也不是响应式的
* get --- http://localhost:8080/user/findAll2 使用curl执行，可以看到数据是一条条出现的。

## 让数据双向流动
准备MyEvent.java,MyEventRepository.java,MyEventController.java
启动后会清空MongoDB中的events数据并进行一些设置(com.xa.jans.config.MyEventStreamHandler.initConfig(MongoOperations))
 * 1.运行一次com.xa.jans.MyEventControllerTest.test()方法插入几条数据；
 * 2.命令行执行curl http://localhost:8080/events可以看到刚插入的数据，并且流没有断开；
 * 3.再次执行com.xa.jans.MyEventControllerTest.test()方法插入几条数据；
 * 4.在命令行中就能看到数据一条条的出现了。

# Spring WebFlux

Spring WebFlux是随Spring 5推出的响应式Web框架。

## 服务端技术栈

Spring提供了完整的支持响应式的服务端技术栈。

如webflux1.jpg所示，左侧为基于spring-webmvc的技术栈，右侧为基于spring-webflux的技术栈，

    Spring WebFlux是基于响应式流的，因此可以用来建立异步的、非阻塞的、事件驱动的服务。它采用Reactor作为首选的响应式流的实现库，不过也提供了对RxJava的支持。
    由于响应式编程的特性，Spring WebFlux和Reactor底层需要支持异步的运行环境，比如Netty和Undertow；也可以运行在支持异步I/O的Servlet 3.1的容器之上，比如Tomcat（8.0.23及以上）和Jetty（9.0.4及以上）。
    从图的纵向上看，spring-webflux上层支持两种开发模式：
        类似于Spring WebMVC的基于注解（@Controller、@RequestMapping）的开发模式；
        Java 8 lambda 风格的函数式开发模式。
    Spring WebFlux也支持响应式的Websocket服务端开发。

## 响应式Http客户端

此外，Spring WebFlux也提供了一个响应式的Http客户端API WebClient。它可以用函数式的方式异步非阻塞地发起Http请求并处理响应。其底层也是由Netty提供的异步支持。

我们可以把WebClient看做是响应式的RestTemplate，与后者相比，前者：

    是非阻塞的，可以基于少量的线程处理更高的并发；
    可以使用Java 8 lambda表达式；
    支持异步的同时也可以支持同步的使用方式；
    可以通过数据流的方式与服务端进行双向通信。

-------------------
