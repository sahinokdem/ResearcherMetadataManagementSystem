package com.sahinokdem.researcher_metadata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "metadata_value")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataValue extends BaseEntity {
    private String userId;
    @ManyToOne(fetch = FetchType.LAZY)
    private MetadataRegistry metadataRegistry;
    private String value;
}

