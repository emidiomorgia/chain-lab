package com.chainlab.backend.service;

import com.chainlab.backend.model.Block;
import com.chainlab.backend.repository.BlockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    private static final String ZEROS = "0".repeat(64);

    @Mock
    BlockRepository blockRepository;

    @InjectMocks
    BlockService blockService;

    @Test
    void createBlockCreatesGenesisBlockWhenRepositoryIsEmpty() throws Exception {
        List<Block> inserted = new ArrayList<>();
        when(blockRepository.count()).thenAnswer(invocation -> (long) inserted.size());
        when(blockRepository.findLastBlock()).thenAnswer(invocation ->
                inserted.isEmpty() ? Optional.empty() : Optional.of(inserted.get(inserted.size() - 1)));
        doAnswer(invocation -> {
            inserted.add(invocation.getArgument(0));
            return null;
        }).when(blockRepository).insert(any());

        Path file = Files.createTempFile("block-service-test", ".txt");
        Files.writeString(file, "hello world");

        long index = blockService.createBlock("hello.txt", Files.size(file), file);

        assertEquals(1, index);
        assertEquals(2, inserted.size());

        Block genesis = inserted.get(0);
        assertEquals(0, genesis.index);
        assertNull(genesis.payload);
        assertEquals(ZEROS, genesis.previousHash);

        Block uploaded = inserted.get(1);
        assertEquals(1, uploaded.index);
        assertNotNull(uploaded.payload);
        assertEquals("hello.txt", uploaded.payload.filename());
        assertEquals(genesis.hash, uploaded.previousHash);
    }

    @Test
    void createBlockDoesNotCreateGenesisWhenRepositoryAlreadyHasBlocks() throws Exception {
        Block existingLast = new Block(java.time.Instant.now(), 3, null, ZEROS, "existing-hash");
        when(blockRepository.count()).thenReturn(4L);
        when(blockRepository.findLastBlock()).thenReturn(Optional.of(existingLast));

        ArgumentCaptor<Block> captor = ArgumentCaptor.forClass(Block.class);

        Path file = Files.createTempFile("block-service-test", ".txt");
        Files.writeString(file, "content");

        long index = blockService.createBlock("data.bin", Files.size(file), file);

        assertEquals(4, index);
        org.mockito.Mockito.verify(blockRepository, org.mockito.Mockito.times(1)).insert(captor.capture());

        Block created = captor.getValue();
        assertEquals(4, created.index);
        assertEquals("existing-hash", created.previousHash);
        assertEquals("data.bin", created.payload.filename());
    }
}
