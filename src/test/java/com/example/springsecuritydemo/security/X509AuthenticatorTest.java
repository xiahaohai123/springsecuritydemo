package com.example.springsecuritydemo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class X509AuthenticatorTest {

    private X509Authenticator x509Authenticator;

    private String token = "MIIa7wYJKoZIhvcNAQcCoIIa4DCCGtwCAQExDTALBglghkgBZQMEAgEwghj-BgkqhkiG9w0BBwGgghjwBIIY7HsidG9" +
            "rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjEtMTItMDhUMDI6Mjc6NDYuODMwMDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZ" +
            "yI6W10sInJvbGVzIjpbeyJuYW1lIjoidGVfYWRtaW4iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kY3NfbXNfcndzIiwiaWQiOiI" +
            "wIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2NlX3R1cmJvX2VuaGFuY2VkIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdjJ4LWNvbnNvb" +
            "GUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF8yMDIxMDEyMjAyNTQyNiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3dzZC1" +
            "0ZXN0MDQyOCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkXzIwMjEwMTI3MDI1NTU1IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0Z" +
            "WRfYmV0YV92cGNfbGRkIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2JyX2ZpbGUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXR" +
            "lZF9tZWV0aW5nX2VuZHBvaW50X2J1eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NmdzA4MDUxIiwiaWQiOiIwIn0seyJuYW1lI" +
            "joib3BfZ2F0ZWRfc2lzX3Nhc3JfZW4iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jZnciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9" +
            "nYXRlZF9vcF9nYXRlZF9pcnRjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYmV0YV9lbGJfZ2dnMjAyMTA2MTUiLCJpZCI6IjAif" +
            "Sx7Im5hbWUiOiJvcF9nYXRlZF9ldnNfdm9sdW1lX3JlY3ljbGVfYmluIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGNzX2RjczI" +
            "tZW50ZXJwcmlzZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3pxdGVzdCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkXzIwM" +
            "jEwMTIzMDI1OTI1IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY3ZyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWFzIiw" +
            "iaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbXVsdGlfYmluZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2JldGFfZWNzX2xkY" +
            "SIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2JldGFfZWNzX2xkYiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llc2JldGE" +
            "iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yb21hZXhjaGFuZ2UiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jY2lfYXJtI" +
            "iwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2VyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfWGlhb2p1bkFQSU5vdGlmaWN" +
            "hdGlvblRlc3QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jZXNfcmVzb3VyY2Vncm91cF90YWciLCJpZCI6IjAifSx7Im5hbWUiO" +
            "iJvcF9nYXRlZF9lNWduYWFzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZ29uZ2NlMDgxMjAyIiwiaWQiOiIwIn0seyJuYW1lIjo" +
            "ib3BfZ2F0ZWRfd2hpdGVsaXN0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX3hzZXJ2ZXIiLCJpZCI6IjAifSx7Im5hbWUiO" +
            "iJvcF9nYXRlZF9saWZlMDkyNDAyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdWRzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F" +
            "0ZWRfY2llIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdnBuX3Znd19pbnRsIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfe" +
            "m9lVGVzdF9zZXJ2aWNlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYmV0YV9nYzEwMjciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9" +
            "nYXRlZF96b2VUZXN0X3NlcnZpY2U1MjUwMSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2RheXVfZGxtX2NsdXN0ZXIiLCJpZCI6I" +
            "jAifSx7Im5hbWUiOiJvcF9nYXRlZF92cGNfdnBjZXB0b29icyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NvbXBhc3MiLCJpZCI" +
            "6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ubHBfbGdfdGciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9nYzA3MDlicSIsImlkIjoiM" +
            "CJ9LHsibmFtZSI6Im9wX2dhdGVkX3NlcnZpY2VzdGFnZV9tZ3JfZHRtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbGlmZTA5MjQ" +
            "iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zZXJ2aWNlc3RhZ2VfaGx0X2R0bSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX" +
            "3Vnb19wdWJsaWN0ZXN0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW90ZWRnZV9iYXNpYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9" +
            "wX2dhdGVkXzIwMjEwMTI0MDI1NTM4IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMDUxMDAwMTExIiwiaWQiOiIwIn0seyJuYW1lI" +
            "joib3BfZ2F0ZWRfc2VydmljZXN0YWdlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW90c3RhZ2UiLCJpZCI6IjAifSx7Im5hbWU" +
            "iOiJvcF9nYXRlZF96b2VUZXN0X3NlcnZpY2UyMTExNSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkXzIwMjEwMTI3MDI1NjI4Iiwia" +
            "WQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMjAyMTAxMjEwMjU2MTMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yaV9kd3MiLCJ" +
            "pZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF94aWFvanVudGVzdG9idCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3JlZiIsImlkI" +
            "joiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Bvc3RwYWlkX3h3eDgyNjY2OSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2dlbWluaWR" +
            "iX2FybSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2F0YyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkXzAzMDQiLCJpZCI6I" +
            "jAifSx7Im5hbWUiOiJvcF9nYXRlZF9ibGFja2xpc3QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zaXNfYXNzZXNzX2F1ZGlvIiw" +
            "iaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfem9lVGVzdF9zZXJ2aWNlNzEyMDEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF8yM" +
            "DIxMDEyMzAyNTk1NyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2djZ2owNjI0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWR" +
            "fb3NjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcXdlcXdlcXdlcXciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9vcF9nY" +
            "XRlZF9kZHNfYXJtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMjAyMTAyMDMwMjU2MTciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9" +
            "nYXRlZF9jc2MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kY3NfZGNzMi1yZWRpczYtZ2VuZXJpYyIsImlkIjoiMCJ9LHsibmFtZ" +
            "SI6Im9wX2dhdGVkX2djMTAyNyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2dyZWVuIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F" +
            "0ZWRfb2NlYW5ib29zdGVyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZ2pnYzA3MDIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nY" +
            "XRlZF9kbHZfb3Blbl9iZXRhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaWVzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWR" +
            "fMjAyMTAxMjIwMjU0NTgiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jc2JzX3Jlc3RvcmUiLCJpZCI6IjAifSx7Im5hbWUiOiJvc" +
            "F9nYXRlZF96eGMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0Z2owODIxbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGV" +
            "kX3ZvbHVtZV9ob3N0X3ByaW9yaXR5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX2M2YSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3pvZVRlc3Rfc2VydmljZTUzMTAyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMDkwMzFxcXFxIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfem9lVGVzdF9zZXJ2aWNlMzI0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfem9lVGVzdF9zZXJ2aWNlNTMxMDEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92cG5fdmd3IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYmV0YV9nY2hkYWJjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfc2VjZGV2X3NlY3NvbGFyX3B0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfc21uX2NhbGxub3RpZnkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9nYzAxMDkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9saWhlMDkwMyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3pvZVRlc3Rfc2VydmljZTUxMjAxIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZ2MwNjIyYm0iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9sZmFnYzA2MjMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9iZXRhX2Vjc19sZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Rtc19yZWxpYWJpbGl0eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29wX3Rlc3Rnb25nY2UiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9idXNpbmVzc2NhcmQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jYWUtYmV0YSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NvbmZpZ3VyYXRpb24iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9sZWdhY3kiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF96b2VUZXN0X3NlcnZpY2U3MTUwMSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Nmc3R1cmJvYmV0YSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2JjZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Rlc3RfejAwNTgzOTMyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWVzc2FnZW92ZXI1ZyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2JldGFfem9ldGVzdF9zZXJ2aWNlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW50bF9jb21wYXNzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfeGlhb2p1bjIwMjEwNTA2MDEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9iZXRhX3ZwY19sZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2djMDkxNCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VwcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3hpYW9qdW4yMDIxMDQzMDAyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMjAyMTAxMjEwMjU1NDEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hc2RzYWRmZGYiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kZDA3MDQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tZXNzYWdlNWciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDcyMiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX1dlTGlua19lbmRwb2ludF9idXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDMyMiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FiczEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pb3RhbmFseXRpY3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF96b2VUZXN0X3NlcnZpY2UwMSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FiczMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hYnMyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMjAyMTAxMjQwMjU2MTAiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3Nfc3Jpb3YiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9xd2UxMTEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF96b2VUZXN0X3NlcnZpY2U2MjUwMiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2JldGFfaV9hIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcmVsaWFibGVtZXNzYWdlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcHVibGljLWJldGEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tYXBfdmlzaW9uIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMjAyMTAyMDMwMjU2NDkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF93aWZpLWJldGEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9nYzA1MjAiLCJpZCI6IjAifV0sInByb2plY3QiOnsiZG9tYWluIjp7Im5hbWUiOiJTQ0NfQ0JIX3MwMDM1NzM1MyIsImlkIjoiNjEzOTZkMjE2NDJiNDUxZmI0N2M0ODEwZjQ3ZWVhYTQifSwibmFtZSI6ImNuLW5vcnRoLTciLCJpZCI6ImI2MDg5MTI4Y2IwMDRjZWZhZjkzZWYxYTIwNTAzNDczIn0sImlzc3VlZF9hdCI6IjIwMjEtMTItMDdUMDI6Mjc6NDYuODMwMDAwWiIsInVzZXIiOnsiZG9tYWluIjp7Im5hbWUiOiJTQ0NfQ0JIX3MwMDM1NzM1MyIsImlkIjoiNjEzOTZkMjE2NDJiNDUxZmI0N2M0ODEwZjQ3ZWVhYTQifSwibmFtZSI6IlNDQ19DQkhfczAwMzU3MzUzIiwicGFzc3dvcmRfZXhwaXJlc19hdCI6IiIsImlkIjoiODM4YzQ3YzRhMzc1NDJmZGE3ZmI2ZWMxMjIyMGIzM2UifX19MYIBwzCCAb8CAQEwgZkwgYsxCzAJBgNVBAYTAkNOMRIwEAYDVQQIDAlHdWFuZ0RvbmcxETAPBgNVBAcMCFNoZW5aaGVuMS4wLAYDVQQKDCVIdWF3ZWkgU29mdHdhcmUgVGVjaG5vbG9naWVzIENvLiwgTHRkMQ4wDAYDVQQLDAVDbG91ZDEVMBMGA1UEAwwMY2E2Ni5wa2kuaWFtAgkA+GWAA0mEWfAwCwYJYIZIAWUDBAIBMA0GCSqGSIb3DQEBAQUABIIBAEMK6NEu-NHxFb9JlNh2gBpuo-BauYjzfoN1r-wOJg7+AwtvHew0k9OQSQtzfbFubjZP5PldZNCABmktBwQURpr5h2g4InSjynlZeP8xYdMn2Kf10Z7ARZ9gvHXhyUpP3e5PKnt1sAOM8qepk5ZHeEUmZqCQwU+c0DC1uTgBlOO8gHhNTVuxxMWN-0XyvvVq-CbmIzEf9V9lXNA7uk55xCyzKsEJ+8lS7Dd0It990LrbqJKRz5SvEZv7NuxcHQDVH2JPsfr7vN1xFcIk7Ze06AFR+5HPdQNepqA1sSNnL95cmc7an47qytIZVZU8nxRRhNkS-QrUDS8kM62s1K-z55M=";

    @BeforeEach
    void setUp() throws CertificateException, IOException, URISyntaxException {
        x509Authenticator = new X509Authenticator();
        x509Authenticator.loadSigningCert();
    }

    @Test
    void testLoadCertificate() {
        X509Authenticator x509Authenticator = new X509Authenticator();
        assertDoesNotThrow(x509Authenticator::loadSigningCert);
    }


    @Test
    void testSwitch() {
        Integer a = null;
        switch (a) {
            case 0:
                System.out.println(0);
            default:
                System.out.println("default");
        }
    }
}