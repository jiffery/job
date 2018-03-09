import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


/**
 * @author 子羽
 * @date 2018年3月6日
 *
 */
public class JobServiceStart {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "classpath:spring-provider.xml", "classpath:spring-mybatis.xml",  "classpath:spring-quartz.xml"
        });
        context.start();

        System.in.read(); // 按任意键退出
    }


}