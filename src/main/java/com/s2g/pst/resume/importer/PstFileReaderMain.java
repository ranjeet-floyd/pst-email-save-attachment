package com.s2g.pst.resume.importer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ranjeetkumar
 */
public class PstFileReaderMain {

    public static void main(String[] args) {
//java -D.prop="my value" MyApp
        String inputPSTPath = System.getProperty("pstPath", "");
        String outSaveDir = System.getProperty("outDir", "");
        String fromDate = System.getProperty("fromDate", "");
        String toDate = System.getProperty("toDate", "");
        String minSize = System.getProperty("minSize", "0");
        String maxSize = System.getProperty("maxSize", "0");

        Date fDate = null;
        Date tDate = null;
        int fromFileSize = 0;
        int toFileSize = 0;
        //   String filename = "/home/ranjeetkumar/NetBeansProjects/SamplePSTData/s2.pst";//args[0];//
        //  String saveTo = "/home/ranjeetkumar/NetBeansProjects/pstSavefiles/";//args[1];//

        if (!inputPSTPath.isEmpty() && !outSaveDir.isEmpty()) {
            //System.out.println("inputPSTPath : " + inputPSTPath);
            //System.out.println("outSaveDir : " + outSaveDir);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                if (!fromDate.isEmpty()) {
                    fDate = formatter.parse(fromDate);
                }
                if (!toDate.isEmpty()) {
                    tDate = formatter.parse(toDate);
                }
                if (Integer.parseInt(minSize) > 0) {
                    fromFileSize = Integer.parseInt(minSize);
                }
                if (Integer.parseInt(maxSize) > 0) {
                    toFileSize = Integer.parseInt(maxSize);
                }
                //Add filter criteria
                FilterInfo filterInfo = new FilterInfo.FilterBuilder(inputPSTPath, outSaveDir)
                        .haveCreatedDate(fDate, tDate)
                        .haveEmailAttachFileSize(fromFileSize, toFileSize)
                        .haveEmailBcc(null)
                        .haveEmailHeader(null)
                        .build();

                //Finally run        
                filterInfo.run();

            } catch (Exception ex) {
                System.err.println("Exception : " + ex.getMessage());
            }
        } else {
            System.err.println("Please provide input .pst file and output dir path as  ");
            System.err.println(" java -DpstPath=<full .pst file path>  -DoutDir=<full out put dir>   -jar <full jar file path>");
        }

    }
}
