package com.chainlab.backend.repository;

import com.chainlab.backend.model.Block;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class BlockRepository implements PanacheMongoRepository<Block> {

    public Optional<Block> findLastBlock() {
        return findAll(Sort.descending("index")).firstResultOptional();
    }

    public void insert(Block block) {
        persist(block);
    }
}
