package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementMetaRepo extends MongoRepository<StatementMeta, String> {
   // Page<DailyComItem> findAllByDailyCom(DailyCom dailyCom, Pageable pageable);
}
