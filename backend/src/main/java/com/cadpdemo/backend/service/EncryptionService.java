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

    public byte[] encryptField(String plaintext) throws Exception {
        CipherTextData ct = CryptoManager.protect(plaintext.getBytes(), policy);
        return ct.getCipherText();
    }

    public String decryptField(byte[] ciphertext) throws Exception {
        CipherTextData ct = new CipherTextData(ciphertext);
        return new String(CryptoManager.reveal(ct, policy, userSet));
    }

    public byte[] encryptFile(InputStream inputStream) throws Exception {
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[CHUNK_SIZE];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] chunk = Arrays.copyOf(buffer, bytesRead);
            CipherTextData ct = CryptoManager.protect(chunk, policy);
            byte[] encryptedChunk = ct.getCipherText();
            encryptedOutput.write(
                ByteBuffer.allocate(4).putInt(encryptedChunk.length).array()
            );
            encryptedOutput.write(encryptedChunk);
        }
        return encryptedOutput.toByteArray();
    }

    public byte[] decryptFile(byte[] encryptedData) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(encryptedData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] lenBytes = new byte[4];

        while (in.read(lenBytes) == 4) {
            int chunkLen = ByteBuffer.wrap(lenBytes).getInt();
            byte[] encryptedChunk = in.readNBytes(chunkLen);
            CipherTextData ct = new CipherTextData(encryptedChunk);
            out.write(CryptoManager.reveal(ct, policy, userSet));
        }
        return out.toByteArray();
    }
}
