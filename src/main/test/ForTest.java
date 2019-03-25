import org.junit.Test;
import org.springframework.util.Assert;

/**
 * Created by hairui on 2018/12/26.
 */
public class ForTest {
    @Test
    public void test(){
        Assert.notNull(null,"不能为空");
        System.out.println();
    }
}
