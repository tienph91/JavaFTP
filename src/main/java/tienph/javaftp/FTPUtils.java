package tienph.javaftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

public class FTPUtils {

    /**
     * upload directory to FTP server
     * 
     * @throws IOException
     * 
     */
    public static boolean uploadDirectory(FTPClient ftpClient, String remoteDirPath, String localParentDir,
            String remoteParentDir) throws IOException {

        System.out.println("Listing directory: " + localParentDir);

        File localDir = new File(localParentDir);
        File[] subfiles = localDir.listFiles();

        if (subfiles == null || subfiles.length == 0) {
            return true;
        }

        // iterator subfiles to determine file is directory or single files
        for (File item : subfiles) {

            // get path of remote file
            String remoteFilePath = remoteDirPath + "/" + remoteParentDir + "/" + item.getName();
            if (remoteParentDir.equals("")) {
                remoteFilePath = remoteDirPath + "/" + item.getName();
            }

            if (item.isFile()) {

                // upload the files
                String localFilePath = item.getAbsolutePath();
                boolean uploaded = uploadSingleFile(ftpClient, localFilePath, remoteFilePath);
                if (!uploaded) {
                    return false;
                }
            } else {

                // create directory on the server
                if (!directoryIsExists(ftpClient, remoteFilePath)) {
                    boolean created = ftpClient.makeDirectory(remoteFilePath);

                    if (!created) {
                        return false;
                    }

                    // upload sub directory
                    String parent = remoteParentDir + "/" + item.getName();
                    if (remoteParentDir.equals("")) {
                        parent = item.getName();
                    }

                    localParentDir = item.getAbsolutePath();
                    uploadDirectory(ftpClient, remoteDirPath, localParentDir, parent);
                }
            }
        }

        return false;

    }

    /**
     * upload single file to FTP server
     * 
     * @return
     * @throws IOException
     */
    public static boolean uploadSingleFile(FTPClient ftpClient, String localFilePath, String remoteFilePath)
            throws IOException {

        File localFile = new File(localFilePath);
        InputStream inputStream = new FileInputStream(localFile);

        try {
            if (fileIsExists(ftpClient, remoteFilePath)) {
                ftpClient.rename(remoteFilePath, remoteFilePath + "_bak");
            }

            return ftpClient.storeFile(remoteFilePath, inputStream);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Check files is exists or not in the server
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    private static boolean fileIsExists(FTPClient ftpClient, String fileName) throws IOException {

        InputStream inputStream = ftpClient.retrieveFileStream(fileName);
        boolean isExists = false;

        try {
            isExists = inputStream != null && ftpClient.getReplyCode() != 550;
        } finally {
            if (inputStream != null) {
                inputStream.close();
                ftpClient.completePendingCommand();
            }
        }

        return isExists;
    }

    private static boolean directoryIsExists(FTPClient ftpClient, String dirPath) throws IOException {
        ftpClient.changeWorkingDirectory(dirPath);
        int returnCode = ftpClient.getReplyCode();
        if (returnCode == 550) {
            return false;
        }
        return true;
    }

}
