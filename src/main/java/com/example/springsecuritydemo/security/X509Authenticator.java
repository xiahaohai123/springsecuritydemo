package com.example.springsecuritydemo.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class X509Authenticator {
    private static final String CMS_SIGNED_DATA_HEADER = "-----BEGIN CMS-----";

    private static final String CMS_SIGNED_DATA_TAIL = "-----END CMS-----";

    private final Log log = LogFactory.getLog(this.getClass());

    private List<X509Certificate> signingCertList = new ArrayList<>();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public boolean loadSigningCert() throws IOException, CertificateException, URISyntaxException {
        URL resourceUrl = getClass().getResource("/x509/sign cert.txt");
        String certPath = resourceUrl == null ? null : resourceUrl.toURI().getPath();

        File file;
        InputStreamReader fileReader;
        PemReader pemReader;
        FileInputStream fileInputStream;

        file = new File(certPath);
        fileInputStream = new FileInputStream(file);

        fileReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        pemReader = new PemReader(fileReader);

        return loadSigningCert(pemReader);
    }

    private boolean loadSigningCert(PemReader pemReader) throws IOException, CertificateException {
        ByteArrayInputStream byteArrayInputStream;

        ArrayList<X509Certificate> certs = new ArrayList<>();
        PemObject pemObject = pemReader.readPemObject();

        while (pemObject != null) {
            if (!pemObject.getType().endsWith("CERTIFICATE")) {
                continue;
            }

            CertificateFactory cf = CertificateFactory.getInstance("X509");

            byteArrayInputStream = new ByteArrayInputStream(pemObject.getContent());

            X509Certificate cert = (X509Certificate) cf.generateCertificate(byteArrayInputStream);
            certs.add(cert);

            pemObject = pemReader.readPemObject();
        }
        if (certs.isEmpty()) {
            return false;
        }
        setSigningCertList(certs);
        return true;
    }

    private void setSigningCertList(Collection<X509Certificate> certs) {
        this.signingCertList.addAll(certs);
    }

    private X509Certificate getSignerCertificate(SignerId sid, List<X509Certificate> certList) {
        for (X509Certificate tmpSignerCert : certList) {
            X500Name issuer = X500Name.getInstance(tmpSignerCert.getIssuerX500Principal().getEncoded());
            if (sid.equals(new SignerId(issuer, tmpSignerCert.getSerialNumber()))) {
                return tmpSignerCert;
            }
        }
        return null;
    }

    public String decryptToken(String tokenCiphertext) throws CMSException, CertificateNotYetValidException,
            CertificateExpiredException, OperatorCreationException {

        if (StringUtils.isBlank(tokenCiphertext)) {
            log.error("tokenCiphertext is blank!");
            return null;
        }

        List<X509Certificate> certList = signingCertList;
        if (certList.isEmpty()) {
            return null;
        }

        tokenCiphertext = tokenCiphertext.replace(CMS_SIGNED_DATA_HEADER, "");
        tokenCiphertext = tokenCiphertext.replace(CMS_SIGNED_DATA_TAIL, "");
        tokenCiphertext = tokenCiphertext.replaceAll("\\n", "");
        tokenCiphertext = tokenCiphertext.replaceAll("-", "/");

        byte[] tokenBin = Base64.getDecoder().decode(tokenCiphertext);

        CMSSignedData cms = new CMSSignedData(tokenBin);
        SignerInformationStore signerInfos = cms.getSignerInfos();

        if (signerInfos.size() <= 0) {
            log.error("IamClient can't find signature in token.");
            return null;
        }

        // verify all signatures in token
        for (SignerInformation signer : signerInfos) {
            X509Certificate x509Cert;
            SignerId sid = signer.getSID();

            x509Cert = getSignerCertificate(sid, certList);

            if (x509Cert == null) {
                if (!updateSigningCert()) {
                    return null;
                }

                certList = this.signingCertList;
                if (certList.isEmpty()) {
                    return null;
                }

                x509Cert = getSignerCertificate(sid, certList);

                // if signer certificate is still not found, return error
                if (x509Cert == null) {
                    return null;
                }
            }

            x509Cert.checkValidity(new Date());

            SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(
                    x509Cert);

            if (!signer.verify(verifier)) {
                log.error("Verify failed!");
                return null;
            }
        }

        Object object = cms.getSignedContent().getContent();
        return new String((byte[]) object, StandardCharsets.UTF_8);
    }

    private boolean updateSigningCert() {
        return false;
    }
}
