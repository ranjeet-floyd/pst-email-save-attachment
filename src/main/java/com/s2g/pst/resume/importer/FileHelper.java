/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2g.pst.resume.importer;

import java.io.File;

/**
 *This class contain file utility method. eg. get unique file name
 * 
 * @author ranjeetkumar
 */
public class FileHelper {

    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    private String getFileName(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(0, lastIndexOf);
    }

    public  String getUniqueFileName(String dirPath, String filename) {
        String fullFileName = dirPath + filename;
        if (!new File(fullFileName).exists()) {
            return filename;
        }
        filename = getFileName(filename) + " - Copy" + getFileExtension(filename);
        return getUniqueFileName(dirPath, filename);
    }

}
