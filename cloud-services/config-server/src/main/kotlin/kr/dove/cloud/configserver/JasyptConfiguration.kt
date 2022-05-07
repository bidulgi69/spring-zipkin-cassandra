package kr.dove.cloud.configserver

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.Security

@Configuration
class JasyptConfiguration {

    @Bean
    fun jasyptEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()

        Security.addProvider(BouncyCastleProvider())

        config.password = System.getProperty("JASYPT_ENCRYPT_PASSWORD")
        config.algorithm = "PBEWITHSHA256AND128BITAES-CBC-BC"
        config.keyObtentionIterations = 100
        config.poolSize = 1
        config.providerName = "BC"
        config.saltGenerator = org.jasypt.salt.RandomSaltGenerator()
        config.ivGenerator = org.jasypt.iv.RandomIvGenerator()
        config.stringOutputType = "base64"

        encryptor.setConfig(config)
        return encryptor
    }
}