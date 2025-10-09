// ImageInfoPO.java
package me.zhengjie.gen.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import me.zhengjie.gen.utils.StringListConverter;
import me.zhengjie.gen.utils.StringMapConverter;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    // 👇 修改这里：使用 List<String>
    @Column(name = "not_support_controller_version", columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> notSupportControllerVersion;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "path_value", columnDefinition = "JSON")
    @Convert(converter = StringMapConverter.class)
    private Map<String, String> imgBucketPathMap; // 假设是 key-value 映射

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
