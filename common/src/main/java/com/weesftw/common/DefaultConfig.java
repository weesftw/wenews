package com.weesftw.common;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.weesftw.common.context.Context;
import com.weesftw.common.context.Environment;
import com.weesftw.common.exception.ConfigLoaderException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Context
public class DefaultConfig implements Config {

    private final Environment environment;
    private final Map<String, String> keys = new HashMap<>();

    public DefaultConfig(Environment environment) throws VaultException {
        this.environment = environment;
        loadVaultCredentials();
    }

    private String getFile(String path) {
        try(var loader = this.getClass().getResourceAsStream(path)) {
            assert loader != null;
            return new String(loader.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ConfigLoaderException(e);
        }
    }

    @Override
    public String get(String key) {
        if(key.startsWith("/"))
            return getFile(key);

        if(keys.containsKey(key))
            return keys.get(key);

        if(System.getenv(key) != null)
            return System.getenv(key);

        if(System.getProperty(key) != null)
            return System.getProperty(key);

        return environment.get(key);
    }

    public void loadVaultCredentials() throws VaultException {
        var token = get("VAULT_TOKEN");
        if(token != null) {
            var vault = new Vault(new VaultConfig()
                    .token(token)
                    .address(get("VAULT_SERVER"))
                    .engineVersion(2)
                    .build());
            keys.putAll(vault.logical().read(get("vault.path")).getData());
        }
    }
}