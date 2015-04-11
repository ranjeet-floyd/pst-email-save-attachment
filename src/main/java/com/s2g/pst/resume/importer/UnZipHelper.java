/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2g.pst.resume.importer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;

/**
 * This file contain zip utility
 *
 * @author ranjeetkumar
 */
public class UnZipHelper {

    /**
     * Unzip it
     *
     * @param fileName
     * @param outputFolder
     *
     * @throws java.io.FileNotFoundException
     */
    public void unZipFolder(String fileName, String outputFolder) throws IOException {

        ZipFile zipFile = new ZipFile(fileName);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(outputFolder, entry.getName());
            entryDestination.getParentFile().mkdirs();
            if (entry.isDirectory()) {
                //FileHelper fileHelper = new FileHelper();
                //String filename = fileHelper.getUniqueFileName(outputFolder, FilenameUtils.removeExtension(entry.getName()));
                //System.out.println("zip :" + filename);
                //new File(filename).mkdirs();

                entryDestination.mkdirs();

            } else {
                InputStream in = zipFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }

        zipFile.close();
        this.deleteZipFile(fileName);
        System.out.println("Done");

    }

    public void deleteZipFile(String fullFilePath) {
        File file = new File(fullFilePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

}
