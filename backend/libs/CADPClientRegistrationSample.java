import com.centralmanagement.CentralManagementProvider;
import com.centralmanagement.RegisterClientParameters;
import com.centralmanagement.CipherTextData;
import com.centralmanagement.policy.CryptoManager;

public class CADPClientRegistrationSample {

    public static void main(String[] args) {
        try {
            // Register client with CipherTrust Manager
            RegisterClientParameters registerClientParams =
                    new RegisterClientParameters.Builder(
                            "172.16.31.237",
                            "N41zpk6n8ezRZWsnQeUZP3nmqUdmwzmY3gBNVNoCeQShRsih34BzbOMbogReUQzs".toCharArray()
                    ).build();

            CentralManagementProvider centralManagementProvider =
                    new CentralManagementProvider(registerClientParams);

            centralManagementProvider.addProvider();
            System.out.println("CADP client registered successfully.");

            // 7-byte tweak (required for FPE/FF3-1/UNICODE with tweak algo NONE)
            byte[] tweakValue = new byte[]{0x4A, 0x3F, 0x7C, 0x12, (byte)0xB8, 0x55, (byte)0xE1};

            // Encrypt
           CipherTextData cipherTextDataObject =
                    CryptoManager.protect(
                            "1234567890ABCDEF".getBytes(),
                            "Encrypt_Policy"
                    );
            System.out.println("Protected Data: "
                    + new String(cipherTextDataObject.getCipherText()));

            // Decrypt
            byte[] revealedData =
                    CryptoManager.reveal(
                            cipherTextDataObject,
                            "Encrypt_Policy",
                            "administrators"
                    );

            System.out.println("Revealed Data: " + new String(revealedData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}