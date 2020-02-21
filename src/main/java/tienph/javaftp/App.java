package tienph.javaftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class App {

    public static void main(String[] args) {

        String server = "172.16.211.24";
        int port = 21;
        String user = "tienph";
        String pass = "It12345!";

        FTPClient ftpClient = new FTPClient();

        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String remoteDirPath = "/Test";
            String localParentDir = "D:\\Project\\AIC-BocBanh\\TichHopToolExport_VPQH\\TV14_42_20200211S";
            String remoteParentDir = "";

            FTPUtils.uploadDirectory(ftpClient, remoteDirPath, localParentDir, remoteParentDir);

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
