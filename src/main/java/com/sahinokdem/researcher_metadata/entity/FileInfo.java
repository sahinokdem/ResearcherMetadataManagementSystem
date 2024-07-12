package com.sahinokdem.researcher_metadata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileInfo extends BaseEntity {
    private String name;
    private String location;
    @Lob
    @Column(name= "size", length = 1000)
    private byte[] size;
}

