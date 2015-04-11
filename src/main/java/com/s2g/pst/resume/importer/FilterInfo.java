/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2g.pst.resume.importer;

import java.util.Date;

/**
 * This class contain filters that can apply on pst emails.
 * @author ranjeetkumar
 */
public class FilterInfo {

    public final String emailpstFromPath;
    public final String emailAttachmentSavePath;
    public final Date fromCreatedDate;
    public final Date toCreatedDate;
    public final String emailCc;
    public final String emailTo;
    public final String emailBcc;
    public final String emailFrom;
    public final String emailHeader;
    public final int emailAttachFileSizeFrom;
    public final int emailAttachFileSizeTo;

    public FilterInfo(FilterBuilder filterBuilder) {
        this.emailpstFromPath = filterBuilder.emailpstFromPath;
        this.emailAttachmentSavePath = filterBuilder.emailAttachmentSavePath;
        this.fromCreatedDate = filterBuilder.fromCreatedDate;
        this.toCreatedDate = filterBuilder.toCreatedDate;
        this.emailCc = filterBuilder.emailCc;
        this.emailBcc = filterBuilder.emailBcc;
        this.emailFrom = filterBuilder.emailFrom;
        this.emailTo = filterBuilder.emailTo;
        this.emailHeader = filterBuilder.emailHeader;
        this.emailAttachFileSizeFrom = filterBuilder.emailAttachFileSizeFrom;
        this.emailAttachFileSizeTo = filterBuilder.emailAttachFileSizeTo;

    }

    /**
     * *
     *
     *
     */
    public void run() {
        PSTFileHelper pstFileHelper;
        try {
            pstFileHelper = new PSTFileHelper(this.emailpstFromPath, this.emailAttachmentSavePath);
            //run
            pstFileHelper.processEmail(pstFileHelper.getPSTFolder(), this);
        } catch (Exception ex) {
            System.err.println("Exception : " + ex.getMessage());
        }

    }

    /**
     * *
     * Filter builder class for Filter info
     */
    public static class FilterBuilder {

        private final String emailpstFromPath;
        private final String emailAttachmentSavePath;
        private Date fromCreatedDate;
        private Date toCreatedDate;
        private String emailCc;
        private String emailTo;
        private String emailBcc;
        private String emailFrom;
        private String emailHeader;
        private int emailAttachFileSizeFrom;
        private int emailAttachFileSizeTo;
        //private String isAttachmentTypeZIP;

        public FilterBuilder(String emailpstFromPath, String emailAttachmentSavePath) {
            this.emailpstFromPath = emailpstFromPath;
            this.emailAttachmentSavePath = emailAttachmentSavePath;
        }

        public FilterBuilder haveCreatedDate(Date fromCreatedDate, Date toCreatedDate) {
            this.fromCreatedDate = fromCreatedDate;
            this.toCreatedDate = toCreatedDate;
            return this;
        }

        public FilterBuilder haveEmailCc(String emailCc) {
            this.emailCc = emailCc;
            return this;
        }

        public FilterBuilder haveEmailTo(String emailTo) {
            this.emailTo = emailTo;
            return this;
        }

        public FilterBuilder haveEmailBcc(String emailBcc) {
            this.emailBcc = emailBcc;
            return this;
        }

        public FilterBuilder haveEmailFrom(String emailFrom) {
            this.emailFrom = emailFrom;
            return this;
        }

        public FilterBuilder haveEmailHeader(String emailHeader) {
            this.emailHeader = emailHeader;
            return this;

        }

        public FilterBuilder haveEmailAttachFileSize(int emailAttachFileSizeFrom, int emailAttachFileSizeTo) {
            this.emailAttachFileSizeFrom = emailAttachFileSizeFrom;
            this.emailAttachFileSizeTo = emailAttachFileSizeTo;
            return this;

        }

        public FilterInfo build() {
            FilterInfo filterInfo = new FilterInfo(this);
            return filterInfo;

        }

    }

}
