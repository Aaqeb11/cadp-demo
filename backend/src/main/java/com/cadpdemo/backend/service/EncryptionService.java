package com.cadpdemo.backend.service;

import com.centralmanagement.CipherTextData;
import com.centralmanagement.policy.CryptoManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    @Value("${cadp.policy}")
    private String policy;

    @Value("${cadp.user-set}")
    private String userSet;

    private static final int CHUNK_SIZE = 2048;

    // ── Field-level ──────────────────────────────────────────────────

    public byte[] encryptField(String plaintext) throws Exception {
        CipherTextData ct = CryptoManager.protect(plaintext.getBytes(), policy);
        return ct.getCipherText();
    }

    public String decryptField(byte[] storedBytes) throws Exception {
        CipherTextData ct = new CipherTextData();
        ct.setCipherText(storedBytes);
        return new String(CryptoManager.reveal(ct, policy, userSet));
    }

    public static class FileOperationResult {

        private byte[] data;
        private int numChunks;
        private java.util.List<Integer> chunkSizes;

        public FileOperationResult(
            byte[] data,
            int numChunks,
            java.util.List<Integer> chunkSizes
        ) {
            this.data = data;
            this.numChunks = numChunks;
            this.chunkSizes = chunkSizes;
        }

        public byte[] getData() {
            return data;
        }

        public int getNumChunks() {
            return numChunks;
        }

        public java.util.List<Integer> getChunkSizes() {
            return chunkSizes;
        }
    }

    public FileOperationResult encryptFile(InputStream inputStream)
        throws Exception {
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[CHUNK_SIZE];
        int bytesRead;
        int numChunks = 0;
        java.util.List<Integer> chunkSizes = new java.util.ArrayList<>();

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] chunk = Arrays.copyOf(buffer, bytesRead);
            CipherTextData ct = CryptoManager.protect(chunk, policy);
            byte[] encryptedChunk = ct.getCipherText();

            numChunks++;
            chunkSizes.add(encryptedChunk.length);

            encryptedOutput.write(
                ByteBuffer.allocate(4).putInt(encryptedChunk.length).array()
            );
            encryptedOutput.write(encryptedChunk);
        }
        return new FileOperationResult(
            encryptedOutput.toByteArray(),
            numChunks,
            chunkSizes
        );
    }

    public FileOperationResult decryptFile(byte[] encryptedData)
        throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(encryptedData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] lenBytes = new byte[4];
        int numChunks = 0;
        java.util.List<Integer> chunkSizes = new java.util.ArrayList<>();

        while (in.read(lenBytes) == 4) {
            int chunkLen = ByteBuffer.wrap(lenBytes).getInt();
            byte[] encryptedChunk = in.readNBytes(chunkLen);

            numChunks++;
            chunkSizes.add(chunkLen);

            CipherTextData ct = new CipherTextData();
            ct.setCipherText(encryptedChunk);
            out.write(CryptoManager.reveal(ct, policy, userSet));
        }
        return new FileOperationResult(
            out.toByteArray(),
            numChunks,
            chunkSizes
        );
    }
}
