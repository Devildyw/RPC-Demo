import com.dyw.rpc.framework.RpcFramework;
import com.dyw.rpc.service.RpcService;
import com.dyw.rpc.service.impl.RpcServiceImpl;
import org.junit.Test;

/**
 * @author Devil
 * @date 2022-05-23-23:36
 */
public class MyTest {
    @Test
    public void doTest(){
        RpcService service = new RpcServiceImpl();
        try {
            RpcFramework.export(service,8080);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void Test01(){
        RpcService refer = RpcFramework.refer(RpcService.class, "127.0.0.1", 8080);
        System.out.println(refer.hello("123456"));
    }

}
