package com.tencent.tmgp.sgame.tube.helper;

import android.app.Activity;
import android.util.Log;

import com.copy.mytebe.R;
import com.tencent.tmgp.sgame.tube.StubApplication;
import com.tencent.tmgp.sgame.tube.utils.FileUtils;
import com.tencent.tmgp.sgame.tube.utils.RandomUtils;
import com.tencent.tmgp.sgame.tube.utils.ShellUtils;
import com.wind.meditor.core.FileProcesser;
import com.wind.meditor.property.AttributeItem;
import com.wind.meditor.property.ModificationProperty;
import com.wind.meditor.utils.NodeValue;

import net.fornwall.apksigner.KeyStoreFileManager;
import net.fornwall.apksigner.ZipSigner;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/*
**注意事项
* 1、Exception from call site #0 bootstrap method
* 配置java1.8
*
* */
public class ApkHelper {

    private final String packageName;
    private final String appName;
    private final String workDir;
    private final String oldApk;
    private final String newApk;
    private final String signedApk;
    private final String oldXml;
    private final String newXml;


    public ApkHelper() {
        packageName = RandomUtils.getRandomPkgName();
        appName = RandomUtils.getRandomAppName();
        workDir = FileUtils.getTempDir().getAbsolutePath();
        oldApk = workDir + "/old.apk";
        newApk = workDir + "/new.apk";
        signedApk = workDir + "/signed.apk";
        oldXml = workDir + "/old.xml";
        newXml = workDir + "/new.xml";
    }

    public void installSignedApk(Activity context) {
        //准备xml、apk、keystore
        FileUtils.copyFile(FileUtils.getSelfApk().getAbsolutePath(),oldApk);
//        FileUtils.copyAssertFile("keystore",workDir);
        FileUtils.copyFilesFromRaw(StubApplication.getContext(), R.raw.keystore,"keystore",workDir);
        FileUtils.ExtractZipFile(oldApk,"AndroidManifest.xml", oldXml);

        //配置xml属性
        ModificationProperty property = new ModificationProperty();
        property
                .addApplicationAttribute(new AttributeItem(NodeValue.LABEL, appName))
                .addManifestAttribute(new AttributeItem(NodeValue.Manifest.PACKAGE, packageName).setNamespace(null));

        // 处理manifest文件方法
        FileProcesser.processManifestFile(oldXml, newXml, property);

        // 处理得到的未签名的apk
        FileProcesser.processApkFile(oldApk, newApk, property);

        //签名apk
        signApk(newApk,signedApk);

        //安装apk
        installApk(context, signedApk);
    }

    public void installApk(Activity context, String apkPath) {
            ShellUtils.CommandResult result = ShellUtils.execCommand("pm install " + signedApk, true);
    }

    public void signApk(String src,String dest) {
        String keystorePath = workDir+"/keystore";
        String keyPassword = "cpc7125880";
        try {
            KeyStore keyStore = KeyStoreFileManager.loadKeyStore(keystorePath, keyPassword.toCharArray());
            String alias = keyStore.aliases().nextElement();
            X509Certificate publicKey = (X509Certificate)keyStore.getCertificate(alias);
            PrivateKey privateKey = (PrivateKey)keyStore.getKey(alias, keyPassword.toCharArray());
            ZipSigner.signZip(publicKey, privateKey, "SHA1withRSA", src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
