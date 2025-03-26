package com.bulish.melnikov.converter.repository;

import com.bulish.melnikov.converter.model.ConvertRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConverterRequestRepository extends CrudRepository<ConvertRequest, String> {
}
