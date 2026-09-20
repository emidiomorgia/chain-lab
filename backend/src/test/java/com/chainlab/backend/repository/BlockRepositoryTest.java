package com.chainlab.backend.repository;

import com.chainlab.backend.model.Block;
import com.chainlab.backend.model.Payload;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Repository basata su Panache: dipende da un Mongo reale, non mockabile in isolamento
// (eccezione alla policy "unit test senza contesto Quarkus" — vedi piano/note del task).
@QuarkusTest
class BlockRepositoryTest {

    @Inject
    BlockRepository blockRepository;

    @BeforeEach
    void cleanCollection() {
        blockRepository.deleteAll();
    }

    @Test
    void countReturnsZeroWhenCollectionIsEmpty() {
        assertEquals(0, blockRepository.count());
    }

    @Test
    void findLastBlockReturnsEmptyWhenCollectionIsEmpty() {
        assertTrue(blockRepository.findLastBlock().isEmpty());
    }

    @Test
    void findLastBlockReturnsBlockWithHighestIndex() {
        blockRepository.insert(new Block(Instant.now(), 0, null, "0".repeat(64), "hash-0"));
        blockRepository.insert(new Block(Instant.now(), 1, new Payload("a.txt", 3, "hash-a"), "hash-0", "hash-1"));

        Optional<Block> last = blockRepository.findLastBlock();

        assertTrue(last.isPresent());
        assertEquals(1, last.get().index);
        assertEquals("hash-1", last.get().hash);
    }

    @Test
    void insertPersistsBlockWithExpectedFields() {
        Payload payload = new Payload("doc.pdf", 42, "payload-hash");
        Block block = new Block(Instant.now(), 0, payload, "0".repeat(64), "block-hash");

        blockRepository.insert(block);

        assertEquals(1, blockRepository.count());
        Block saved = blockRepository.findLastBlock().orElseThrow();
        assertEquals("doc.pdf", saved.payload.filename());
        assertEquals(42, saved.payload.size());
        assertEquals("payload-hash", saved.payload.hash());
        assertEquals("block-hash", saved.hash);
    }
}
