package com.sangfor.sdkdemo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.sangfor.sdk.utils.SFLogN;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignatureUtils {
    private static final String TAG = "SignatureUtils";

    /**
     * 获取应用中的公钥信息
     * @param appInfo
     * @return 应用中公钥的字符串形式
     */
    public static String getPubKeyInfo(ApplicationInfo appInfo, Context context) {
        PackageManager mPackageManager = context.getPackageManager();
        if (mPackageManager != null){
            try {
                PackageInfo packageInfo = mPackageManager.getPackageInfo(appInfo.packageName, mPackageManager.GET_SIGNATURES);
                Signature[] signatures = packageInfo.signatures;
                Signature signature = signatures[0];
                byte[] signatureBytes = signature.toByteArray();
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signatureBytes));
                String pubKeyStr = cert.getPublicKey().toString();
                return parsePubKey(pubKeyStr);
            } catch (PackageManager.NameNotFoundException e) {
                SFLogN.error(TAG, "failed to get packageInfo ", e);
            } catch (CertificateException e) {
                SFLogN.error(TAG, "failed to get cert", e);
            }
        }
        return null;
    }

    /**
     * 根据解析安装apk的信息得到公钥信息
     * OpenSSLRSAPublicKey{modulus=98cfceafd123bc2d480af0bf31d41ca542736f8353031b01efad25dcef2dd566491635f23c0fbc950c93624dff4436c0910b0cdfe85bf6a86bcd7e42d790110ef4eec3eb8d3fb279734e71c6afd715f33712207101fa40ed0d196ad7c499bbc55d096de61f94117efaea996eed3a3a7c5bb875164bcb890439f6ccd2c0b2acb5,publicExponent=10001}";
     * @param info
     * @return
     */
    private static String parsePubKey(String info) {
        if (TextUtils.isEmpty(info)) {
            return null;
        }
        String indexStr = "modulus=";
        int index = info.indexOf(indexStr);
        if (index != -1) {
            int startIndex = index + indexStr.length();
            indexStr = info.substring(startIndex);
            String pubkey = indexStr.split(",")[0];
            //因为解析出来的公钥信息中可能含有空格,换行等干扰信息,因此需要对其进行去杂处理,方便后续的字符串比较
            if (pubkey != null) {
                Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
                Matcher matcher = pattern.matcher(pubkey);
                pubkey = matcher.replaceAll("");
            }
            return pubkey;
        }
        return null;
    }
}
