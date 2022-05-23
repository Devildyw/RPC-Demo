package com.dyw.rpc.framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Devil
 * @date 2022-05-23-23:16
 */
@SuppressWarnings("all")
public class RpcFramework {
    public static void export(Object service, int port) throws Exception {
        //创建一个套接字监听端口
        ServerSocket server = new ServerSocket(port);
        while (true){
            //循环监听
            Socket socket = server.accept();
            new Thread(() -> {
                try{
                    //获得输入流
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    String methodName = input.readUTF(); //获取方法名
                    Class<?>[] parameterTypes = (Class<?>[]) input.readObject();//获取参数类型
                    Object[] arguments = (Object[]) input.readObject();//获取参数
                    Method method = service.getClass().getMethod(methodName, parameterTypes);//获取方法对象
                    Object result = method.invoke(service, arguments);//调用方法
                    //返回结果
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());//输出流
                    output.writeObject(result);//输出结果
                    output.flush();//刷新流
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();//启动一个线程 将监听到的socket都开启一个线程
        }
    }

    public static <T> T refer(Class<T> interfaceClass, String host, int port){
        //返回代理类
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //开启一个socket连接服务
                        Socket socket = new Socket(host, port);
                        //输出流
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeUTF(method.getName());//传入方法名
                        output.writeObject(method.getParameterTypes());//传入参数类型
                        output.writeObject(args);//传入方法参数
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());//读取结果
                        Object result = input.readObject();
                        return result;//返回结果
                    }
                });
    }
}
