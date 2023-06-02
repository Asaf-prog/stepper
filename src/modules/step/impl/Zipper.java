package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.enumerator.Enumerator;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.*;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper extends AbstractStepDefinition {
    public Zipper() {
        super("Zipper", false);

       addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source ", DataDefinitionRegistry.STRING));
       addInput(new DataDefinitionDeclarationImpl("OPERATION", DataNecessity.MANDATORY, "Operation type ", DataDefinitionRegistry.ENUMERATION));

        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Zip operation result", DataDefinitionRegistry.STRING));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {

        //the get data enumeration from the context tell us if we need to do zip or unzip
        // todo if in the enumerator was written unzip we need to check that the end of the file is: .zip

        String path = context.getDataValue("SOURCE", String.class);
        Enumerator zipOrUnzip = context.getDataValue("OPERATION", Enumerator.class);
        if(zipOrUnzip.containVal("ZIP")){
            zipFile(path,path);
            context.setLogsForStep("Zipper", "End with Success ,Zip operation was done");
            context.addSummaryLine("Zipper", "End with Success ,Zip operation was done");
            String result = "Success";
            context.storeDataValue("RESULT", result);
            return StepResult.SUCCESS;
        }else if(zipOrUnzip.containVal("UNZIP")) {
            boolean check = checkIfFileValid(path);
            if (!check)
            {
                
                //mean that the file is not file.zip
                context.setLogsForStep("Zipper", "Error: File is not unzippable");
                context.addSummaryLine("Zipper", "End with Failure ,File is not valid");
                String result = "Error: File is not valid";
                context.storeDataValue("RESULT", result);
                return StepResult.FAILURE;
            }
            unzipFile(path, path);
            context.setLogsForStep("Zipper", "End with Success ,Unzip operation was done");
            context.addSummaryLine("Zipper", "End with Success ,Unzip operation was done");
            String result = "Success";
            context.storeDataValue("RESULT", result);
            return StepResult.SUCCESS;
        }else {
            context.setLogsForStep("Zipper", "Error: Operation type is not valid");
            context.addSummaryLine("Zipper", "End with Failure ,Operation type is not valid");
            String result = "Error: Operation type is not valid";
            context.storeDataValue("RESULT", result);
            return StepResult.FAILURE;
        }
    }

    private boolean checkIfFileValid(String path) {
        //check if file path end with .zip\
        String[] split = path.split("\\.");
        String endOfFile = split[split.length - 1];
        if(!endOfFile.equals("zip")){
           return false;
        }
        return true;
    }

    private void zipFile(String sourceFilePath, String zipFilePath) throws IOException {

        File fileToZip = new File(sourceFilePath);

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zipOut = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(fileToZip)) {

            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }

    }

    private  void unzipFile(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();

                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[1024];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
