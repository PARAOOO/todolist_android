package com.paraooo.local.util

import android.content.Context
import android.util.Log
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.io.IOException
import java.security.GeneralSecurityException

class CryptoManager {

    private lateinit var aead: Aead

    fun initialize(context: Context) {
        AeadConfig.register()

        try {
            val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
                .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
                .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILENAME)
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle

            aead = keysetHandle.getPrimitive(Aead::class.java)
        } catch (e: GeneralSecurityException) {
            throw IOException("Failed to initialize CryptoManager", e)
        }
    }

    fun encrypt(data: String): String {
        Log.d("PARAOOO", "encrypt: ${data}")
        val associatedData = byteArrayOf()
        val encryptedBytes = aead.encrypt(data.toByteArray(), associatedData)
        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String): String {
        Log.d("PARAOOO", "decrypt: ${encryptedData}")
        val associatedData = byteArrayOf()
        val encryptedBytes = android.util.Base64.decode(encryptedData, android.util.Base64.DEFAULT)
        val decryptedBytes = aead.decrypt(encryptedBytes, associatedData)
        return String(decryptedBytes)
    }

    companion object {
        private const val KEYSET_NAME = "master_keyset"
        private const val PREFERENCE_FILENAME = "secure_user_prefs_keyset"
        private const val MASTER_KEY_URI = "android-keystore://master_key_alias"
    }
}