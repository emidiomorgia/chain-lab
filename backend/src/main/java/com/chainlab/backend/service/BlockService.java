package com.chainlab.backend.service;

import com.chainlab.backend.model.Block;
import com.chainlab.backend.model.Payload;
import com.chainlab.backend.repository.BlockRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@ApplicationScoped
public class BlockService {

    private static final String GENESIS_PREVIOUS_HASH = "0".repeat(64);

    @Inject
    BlockRepository blockRepository;

    public long createBlock(String filename, long size, Path filePath) {
        ensureGenesisBlock();

        long index = blockRepository.count();
        Block last = blockRepository.findLastBlock().orElseThrow();

        String payloadHash = sha256(filePath);
        Instant timestamp = Instant.now();
        String hash = computeBlockHash(timestamp, index, payloadHash, last.hash);

        Payload payload = new Payload(filename, size, payloadHash);
        Block block = new Block(timestamp, index, payload, last.hash, hash);
        blockRepository.insert(block);

        return index;
    }

    private void ensureGenesisBlock() {
        if (blockRepository.count() == 0) {
            Instant timestamp = Instant.now();
            long index = 0;
            String hash = computeBlockHash(timestamp, index, "", GENESIS_PREVIOUS_HASH);
            blockRepository.insert(new Block(timestamp, index, null, GENESIS_PREVIOUS_HASH, hash));
        }
    }

    private String computeBlockHash(Instant timestamp, long index, String payloadHash, String previousHash) {
        String data = timestamp.toString() + index + payloadHash + previousHash;
        return sha256(data.getBytes(StandardCharsets.UTF_8));
    }

    private String sha256(Path filePath) {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
