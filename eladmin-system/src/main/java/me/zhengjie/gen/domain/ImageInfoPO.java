// ImageInfoPO.java
/*
 * Copyright (c) 2024, TP-Link Corporation Limited. All rights reserved.
 */

package me.zhengjie.gen.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * @author Zhou Zhuoran
 * @version 1.0
 * @since 2024/12/9
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image_info")
public class ImageInfoPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "compound_model", unique = true)
    private String compoundModel;

    @Column(name = "model")
    private String model;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "type")
    private String type;

    @Column(name = "controller_version")
    private String controllerVersion;

    @Column(name = "min_controller_version")
    private String minControllerVersion;

    @ElementCollection
    @CollectionTable(name = "image_not_support_controller_versions", joinColumns = @JoinColumn(name = "image_info_id"))
    @Column(name = "not_support_controller_version")
    private List<String> notSupportControllerVersion;

    @Column(name = "image_name")
    private String imageName;

    @ElementCollection
    @CollectionTable(name = "image_bucket_paths", joinColumns = @JoinColumn(name = "image_info_id"))
    @MapKeyColumn(name = "bucket_key")
    @Column(name = "path_value")
    private Map<String, String> imgBucketPathMap;

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
