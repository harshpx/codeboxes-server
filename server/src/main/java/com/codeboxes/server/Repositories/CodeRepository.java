package com.codeboxes.server.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codeboxes.server.Collections.Code;

@Repository
public interface CodeRepository extends MongoRepository<Code, String> {
  List<Code> findByCreatedBy(String createdBy);
}
