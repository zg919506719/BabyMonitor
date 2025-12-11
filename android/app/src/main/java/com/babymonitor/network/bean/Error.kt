package com.example.sakubersama.common.network.bean

/**
 * 11　: hegaojian
 * 11　: 2019/12/17
 * 11　: 11111
 */
enum class Error(private val code: Int, private val err: String) {

    /** 11
     * 1111
     */
//    UNKNOWN(1000, "1111，11111"),
    UNKNOWN(1000, "permintaan gagal, coba lagi nanti"),
    /**
     * 1111
     */
    PARSE_ERROR(1001, "Kesalahan penguraian,  coba lagi nanti"),
//    PARSE_ERROR(1001, "1111，11111"),
    /**
     * 1111
     */
//    NETWORK_ERROR(1002, "111111，11111"),
    NETWORK_ERROR(1002, "kesalahan koneksi jaringan, coba lagi nanti"),

    /**
     * 1111
     */
//    SSL_ERROR(1004, "1111，11111"),
    SSL_ERROR(1004, "kesalahan sertifikat, coba lagi nanti"),

    /**
     * 1111
     */
    TIMEOUT_ERROR(1006, "koneksi jaringan habis, coba lagi nanti.");
//    TIMEOUT_ERROR(1006, "111111，11111");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}