/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2g.pst.resume.importer;

import com.pff.PSTAttachment;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.FilenameUtils;

/**
 *This file contain pst related processing.
 * @author ranjeetkumar
 */
public class PSTFileHelper {

    private final String emailFilePath;
    private String isAttachmentTypeZIP;
    private final String emailAttachmentSavePath;
    private final List<PSTMessage> emailList;
    private String uniqueFileName;

    /**
     * *
     *
     * @param emailFilePath
     * @param emailAttachmentSavePath
     * @throws PSTException
     * @throws IOException
     */
    public PSTFileHelper(String emailFilePath, String emailAttachmentSavePath) throws PSTException, IOException {
        this.emailFilePath = emailFilePath;
        this.emailAttachmentSavePath = emailAttachmentSavePath;
        this.emailList = new ArrayList<>();

    }

    /**
     * *
     *
     * @param email
     * @throws FileNotFoundException
     * @throws PSTException
     * @throws IOException
     */
    public void saveAttachmentFromEmail(PSTMessage email, FilterInfo filterInfo)
            throws FileNotFoundException, PSTException, IOException, Exception {

        FileOutputStream out = null;
        this.uniqueFileName = null;
        int numberOfAttachments = email.getNumberOfAttachments();
        for (int x = 0; x < numberOfAttachments; x++) {

            PSTAttachment attach = email.getAttachment(x);
            //System.out.println("file size : " + attach.getAttachSize());
            //check file size
            if ((filterInfo.emailAttachFileSizeFrom == 0 || filterInfo.emailAttachFileSizeFrom <= attach.getAttachSize())
                    && (filterInfo.emailAttachFileSizeTo == 0 || attach.getAttachSize() <= filterInfo.emailAttachFileSizeTo)) {
                InputStream attachmentStream = attach.getFileInputStream();
                // both long and short filenames can be used for attachments
                String filename = attach.getLongFilename();
                if (filename.isEmpty()) {
                    filename = attach.getFilename();
                }
                FileHelper fileHelper = new FileHelper();
                filename = fileHelper.getUniqueFileName(this.emailAttachmentSavePath, filename);
                String fullFileName = this.emailAttachmentSavePath + filename;

                out = new FileOutputStream(fullFileName);
                // 8176 is the block size used internally and should give the best performance
                int bufferSize = 8176;
                byte[] buffer = new byte[bufferSize];
                int count = attachmentStream.read(buffer);
                while (count == bufferSize) {
                    out.write(buffer);
                    count = attachmentStream.read(buffer);
                }
                byte[] endBuffer = new byte[count];
                System.arraycopy(buffer, 0, endBuffer, 0, count);
                out.write(endBuffer);
                out.close();
                //System.out.println("saved fullFileName : " + fullFileName);
                attachmentStream.close();
                //extract if zip file
                if ("zip".equals(FilenameUtils.getExtension(filename))) {
                    UnZipHelper unZipHelper = new UnZipHelper();
                    unZipHelper.unZipFolder(fullFileName, this.emailAttachmentSavePath);

                }
            }
        }
    }

    /**
     * *
     * Get Folder structure for provided .pst file path
     *
     * @return @throws PSTException
     * @throws IOException
     * @throws Exception
     */
    public PSTFolder getPSTFolder() throws PSTException, IOException, Exception {
        PSTFile pstFile = new PSTFile(this.emailFilePath);
        if (pstFile == null) {
            throw new Exception("pstFile object is null.");
        }

        return pstFile.getRootFolder();
    }

    /**
     * *
     *
     * @param folder
     * @param filterInfo
     * @return
     * @throws PSTException
     * @throws java.io.IOException
     */
    public void processEmail(PSTFolder folder, FilterInfo filterInfo)
            throws PSTException, java.io.IOException, Exception {

        //  String dirPath = "/home/ranjeetkumar/NetBeansProjects/pstSavefiles/";
        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processEmail(childFolder, filterInfo);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {

            PSTMessage email = (PSTMessage) folder.getNextChild();
            //String fullFileName = "";
            while (email != null) {

                //System.out.println("email.getCreationTime()" + email.getCreationTime());
                // System.out.println("to :" + email.getCreationTime().before(filterInfo.toCreatedDate));
                // System.out.println("from:" + email.getCreationTime().after(filterInfo.fromCreatedDate));
                try {
                    if ((filterInfo.fromCreatedDate == null || email.getCreationTime().after(filterInfo.fromCreatedDate))
                            && (filterInfo.toCreatedDate == null || email.getCreationTime().before(filterInfo.toCreatedDate))) {

                        System.out.println("save Email Subject: " + email.getSubject());
                        this.saveAttachmentFromEmail(email, filterInfo);
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }

                email = (PSTMessage) folder.getNextChild();
            }

        }

    }

    /**
     * *
     *
     * @param basePath
     * @param fileName
     * @param count
     * @return Return unique file name
     */
    public void getUniqueFileName(String basePath, String fileName, int count) {

//        if (this.uniqueFileName != null) {
//            return;
//        }
        File file = null;
        if (count <= 0) {
            file = new File(FilenameUtils.concat(basePath, fileName));
        } else {
            file = new File(basePath + File.separator
                    + FilenameUtils.removeExtension(fileName)
                    + "_" + count + "."
                    + FilenameUtils.getExtension(fileName));
        }
        if (file.exists()) {
            getUniqueFileName(basePath, fileName, count + 1);
        } else {
            this.uniqueFileName = file.getName();
            System.out.println("called : " + this.uniqueFileName);

        }
        // return ;//this.uniqueFileName;
    }

}
