package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.util.MetadataRegistryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "metadata-registry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataRegistry extends BaseEntity {
        private String name;
        private MetadataRegistryType type;
}
