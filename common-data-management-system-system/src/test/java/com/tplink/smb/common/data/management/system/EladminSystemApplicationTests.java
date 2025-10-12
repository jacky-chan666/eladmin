package com.tplink.smb.common.data.management.system;

import com.tplink.smb.common.data.management.system.gen.service.DataInfoService;
import com.tplink.smb.common.data.management.system.gen.service.DeviceInfoService;
import com.tplink.smb.common.data.management.system.gen.service.GatewayInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EladminSystemApplicationTests {
  @Autowired DeviceInfoService deviceInfoService;
  @Autowired GatewayInfoService gatewayInfoService;

  public void contextLoads(DataInfoService dataInfoService) {
    dataInfoService.createFromJson("{\"model\":\"test\",\"type\":\"test\",\"status\":\"1\"}");
  }

  @Test
  public void test() {
    contextLoads(deviceInfoService);
    contextLoads(gatewayInfoService);
  }
}
