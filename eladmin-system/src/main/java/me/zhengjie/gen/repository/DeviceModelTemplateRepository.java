// DeviceModelTemplateRepository.java
package me.zhengjie.gen.repository;

import me.zhengjie.gen.domain.DeviceModelTemplatePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceModelTemplateRepository extends JpaRepository<DeviceModelTemplatePO, Integer>, JpaSpecificationExecutor<DeviceModelTemplatePO> {
    DeviceModelTemplatePO findByModelAndModelVersion(String model, String modelVersion);
}
