package fr.cmoatoto.quiz.lib.utils;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

public class SecurityUtils {

    private static enum Mode {
        NOT_SET, DEBUG, PROD
    }

    private static Mode isInDebugMode = Mode.NOT_SET;

    public static boolean isInDebugMode(Context c) {
        if (isInDebugMode  != Mode.NOT_SET) {
            return isInDebugMode == Mode.DEBUG;
        }
        final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
        Signature raw;
        try {
            raw = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0];
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(raw.toByteArray()));
            if (cert.getSubjectX500Principal().equals(DEBUG_DN)) {
                isInDebugMode = Mode.DEBUG;
                return true;
            }
        } catch (NameNotFoundException | CertificateException e) {
            e.printStackTrace();
        }
        isInDebugMode = Mode.PROD;
        return false;
    }

}
