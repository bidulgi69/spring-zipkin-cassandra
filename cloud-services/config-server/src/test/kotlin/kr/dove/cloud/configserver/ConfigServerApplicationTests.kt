package kr.dove.cloud.configserver

import org.springframework.boot.test.context.SpringBootTest
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.Security

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ConfigServerApplicationTests{

	/**
	 * 	Use below method when you want to encrypt properties in yaml files.
	 * 	@author bidulgi69
	 */
	/*
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	@Test
	@DisplayName("Generates username and password for spring-security")
	fun generate_inMemoryUserDetails_byEncryptedValues() {
		val jasyptEncryptor = getJasyptEncryptor()

		//	username:password@config-server:8888
		var username = "GitHub id(username)"
		var password = "GitHub personal access token"

		username = jasyptEncryptor.encrypt(username)
		password = jasyptEncryptor.encrypt(password)

		logger.info("UserDetails: {}\t{}", username, password)
	}

	private fun getJasyptEncryptor(): StringEncryptor {
		val jasyptEncryptor = PooledPBEStringEncryptor()
		val config = SimpleStringPBEConfig()

		Security.addProvider(BouncyCastleProvider())

		config.password = "Jasypt Secret Key value"
		config.algorithm = "PBEWITHSHA256AND128BITAES-CBC-BC"
		config.keyObtentionIterations = 100
		config.poolSize = 1
		config.providerName = "BC"
		config.saltGenerator = org.jasypt.salt.RandomSaltGenerator()
		config.ivGenerator = org.jasypt.iv.RandomIvGenerator()
		config.stringOutputType = "base64"

		jasyptEncryptor.setConfig(config)

		return jasyptEncryptor
	}
	 */
}
