package me.zhengjie;

import me.zhengjie.gen.service.DataInfoService;
import me.zhengjie.gen.service.DeviceInfoService;
import me.zhengjie.gen.service.GatewayInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
public class EladminSystemApplicationTests {
    @Autowired
    DeviceInfoService deviceInfoService;
    @Autowired
    GatewayInfoService gatewayInfoService;


    public void contextLoads(DataInfoService dataInfoService) {
        dataInfoService.createFromJson("{\"model\":\"test\",\"type\":\"test\",\"status\":\"1\"}");
    }
    @Test
    public void test() {
        contextLoads(deviceInfoService);
        contextLoads(gatewayInfoService);

    }

}

