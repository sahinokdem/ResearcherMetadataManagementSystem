package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "metadata_registry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataRegistry extends BaseEntity {
        private String name;
        private MetadataRegistryType type;
        @OneToMany(mappedBy = "metadataRegistry", cascade = CascadeType.ALL)
        private List<MetadataValue> metadataValues;
}
