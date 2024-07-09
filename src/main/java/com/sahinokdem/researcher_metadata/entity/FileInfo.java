package com.sahinokdem.researcher_metadata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo extends BaseEntity {
    private String originalName;
    private long size;
    private String storagePath;
}

