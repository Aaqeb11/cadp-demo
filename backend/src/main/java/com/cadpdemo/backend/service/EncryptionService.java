package com.cadpdemo.backend.service;

import com.centralmanagement.CipherTextData;
import com.centralmanagement.policy.CryptoManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    // ── Serialization helpers ────────────────────────────────────────

    private byte[] serializeCipherTextData(CipherTextData ct) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(ct);
        }
        return bos.toByteArray();
    }

    private CipherTextData deserializeCipherTextData(byte[] data)
        throws Exception {
        try (
            ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data)
            )
        ) {
            return (CipherTextData) ois.readObject();
        }
    }

    // ── Field-level ──────────────────────────────────────────────────

    public byte[] encryptField(String plaintext) throws Exception {
        CipherTextData ct = CryptoManager.protect(plaintext.getBytes(), policy);
        return serializeCipherTextData(ct);
    }

    public String decryptField(byte[] stored) throws Exception {
        CipherTextData ct = deserializeCipherTextData(stored);
        return new String(CryptoManager.reveal(ct, policy, userSet));
    }

    // ── Chunked file ─────────────────────────────────────────────────

    public byte[] encryptFile(InputStream inputStream) throws Exception {
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[CHUNK_SIZE];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] chunk = Arrays.copyOf(buffer, bytesRead);
            CipherTextData ct = CryptoManager.protect(chunk, policy);
            byte[] serialized = serializeCipherTextData(ct);

            // Prefix with length (4 bytes) for reassembly
            encryptedOutput.write(
                ByteBuffer.allocate(4).putInt(serialized.length).array()
            );
            encryptedOutput.write(serialized);
        }
        return encryptedOutput.toByteArray();
    }

    public byte[] decryptFile(byte[] encryptedData) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(encryptedData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] lenBytes = new byte[4];

        while (in.read(lenBytes) == 4) {
            int chunkLen = ByteBuffer.wrap(lenBytes).getInt();
            byte[] serialized = in.readNBytes(chunkLen);
            CipherTextData ct = deserializeCipherTextData(serialized);
            out.write(CryptoManager.reveal(ct, policy, userSet));
        }
        return out.toByteArray();
    }
}
