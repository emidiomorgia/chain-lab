package com.chainlab.backend.model;

import org.bson.types.ObjectId;

import java.time.Instant;

public class Block {

    public ObjectId id;
    public Instant timestamp;
    public long index;
    public Payload payload;
    public String previousHash;
    public String hash;

    public Block() {
    }

    public Block(Instant timestamp, long index, Payload payload, String previousHash, String hash) {
        this.timestamp = timestamp;
        this.index = index;
        this.payload = payload;
        this.previousHash = previousHash;
        this.hash = hash;
    }
}
