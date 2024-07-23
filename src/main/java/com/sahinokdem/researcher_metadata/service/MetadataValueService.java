package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetadataValueService {

    MetadataValueRepository metadataValueRepository;

}