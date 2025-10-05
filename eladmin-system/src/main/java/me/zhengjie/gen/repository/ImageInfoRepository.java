// ImageInfoRepository.java
package me.zhengjie.gen.repository;

import me.zhengjie.gen.domain.ImageInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImageInfoRepository extends JpaRepository<ImageInfoPO, Integer>, JpaSpecificationExecutor<ImageInfoPO> {
    ImageInfoPO findByModelAndModelVersion(String model, String modelVersion);
    ImageInfoPO findByCompoundModel(String compoundModel);
}
