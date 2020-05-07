Eureka 的主要作用实际上跟Zookeeper是非常类似的，但是在spring cloud中虽然支持有ZooKeeper，但是
从官方的宣传角度来说，并不支持这样的处理，推荐使用的是Eureka，以为速度更快。同时，该服务组件是
以程序的形式出现的。也就是说你只需要编写一个项目，而后就可以启动Eureka的注册服务了。

Eureka服务端的安全认证配置
	在pom.xml文件中引入SpringSecurity的依赖包。一旦导入了这个包，则我们每次启动微服务的时候，都会自动生成一个密码（用户名默认为user），
	而这个密码因为会改变，所以一般都不使用。所以要修改application.yml的配置文件，追加密码的配置项。
	此时用户注册的时候也需要在前面加上账号和密码:http://navy:123456@127.0.0.1:6868/eureka/

发现管理：
	在实际项目运行过程中，需要通过Eureka作为微服务的监控处理程序
		1. 新服务追加的时候应该立即进行注册
		2. 当某一个服务下线或掉线后应该进行清理
	
打包发布：这个时候我们必须考虑项目的各种环境，如果是开发（dev）、测试（beta）、生成（product），那么下面我们也将基于
	这样的方式进行eureka项目的打包。本次的打包处理将直接基于yml的配置文件完成
	
打包：
	项目上右击Run as -> maven build    在弹出框的Goals中输入： clean install package
	打开cmd，输入java -jar eureka-server.jar回车
	再打开一个cmd，输入 java -jar eureka-server.jar --spring.profiles.active=dev-6869 
	