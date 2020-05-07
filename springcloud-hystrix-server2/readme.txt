到这里问题又来了，restTemplate.getForEntity("http://hello-service/hi",String.class).getBody();
这是阻塞式的，因为这是阻塞式的，如果后面还有代码，必须等到网络请求
	restTemplate.getForEntity("http://hello-service/hi",String.class).getBody();返回结果后，你后面的代码才会执行。

如果此刻，有一个请求过来，通过Ribbon客户端进来了，Ribbon客户端调用了三个服务，每一服务执行的时间都是2秒钟，那么这三个服务都是用阻塞IO来执行的话，
那么耗时是2+2+2=6，一共就花了6秒钟。那么如果我们使用异步来执行的话，花费的时间就是这三个服务中哪一个耗时长就是总耗时时间，
比如，此时耗时最多的一个服务是3秒钟，那么总共耗时就花了3秒钟。那么异步IO是什么意思呢？就是请求发出去以后，主线程不会在原地等着，
会继续往下执行我的主线程，什么时候返回结果，我就什么时候过去取出来。等着三个服务执行完了我就一次性把结果取出来。

非阻塞式IO有两个分别是：Future将来式，Callable回调式
	1.Future将来式：就是说你用Future将来式去请求一个网络IO之类的任务，它会一多线程的形式去实现，主线程不必卡死在哪里等待，
		等什么时候需要结果就通过Future的get()方法去取，不用阻塞。
	2.Callable回调式：预定义一个回调任务，Callable发出去的请求，主线程继续往下执行，等你请求返回结果执行完了，会自动调用你哪个回调任务。

 好了，那么代码如何修改呢？其实HelloServiceCommand类几乎不用变，只需要改变一下在Controller层的command的调用方式即可，
 command的叫用方式如下：
 	Future<String> queue = command.queue();
	return queue.get();