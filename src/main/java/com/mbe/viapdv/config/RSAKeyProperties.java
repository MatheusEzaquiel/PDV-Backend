package com.mbe.viapdv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RSAKeyProperties {

    private final RSAPublicKey publicKey;
    private final RSAPrivateCrtKey privateKey;

    public RSAKeyProperties(
            @Value("${jwt.public.key}") String publicKeyPath,
            @Value("${jwt.private.key}") String privateKeyPath
    ) throws IOException, GeneralSecurityException {

        this.publicKey = loadPublicKey(publicKeyPath);
        this.privateKey = loadPrivateKey(privateKeyPath);
    }

    private RSAPublicKey loadPublicKey(String path) throws IOException, GeneralSecurityException {
        String key = readKeyFromClasspath(path)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private RSAPrivateCrtKey loadPrivateKey(String path) throws IOException, GeneralSecurityException {
        String key = readKeyFromClasspath(path)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateCrtKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private String readKeyFromClasspath(String path) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(path.replace("classpath:", "/"))) {
            if (inputStream == null) throw new IOException("Key file not found: " + path);
            return new String(inputStream.readAllBytes());
        }
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateCrtKey getPrivateKey() {
        return privateKey;
    }
}
