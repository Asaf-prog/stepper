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

        String path = context.getDataValue("SOURCE", String.class);
        //check if path round with " or not
        if(path.charAt(0)=='\"'){
            path=path.substring(1,path.length()-1);
        }
        Enumerator zipOrUnzip = context.getDataValue("OPERATION", Enumerator.class);
        //String action = zipOrUnzip.isFirstContainVal("ZIP") ? "Zip" : "Unzip";
       // Enumerator zipOrUnzip=new Enumerator(action);
        if(zipOrUnzip.isFirstContainVal("ZIP")){

            zipFile(path,path);
            context.setLogsForStep("Zipper", "End with Success ,Zip operation was done");
            context.addSummaryLine("Zipper", "End with Success ,Zip operation was done");
            String result = "Success";
            context.storeDataValue("RESULT", result);
            return StepResult.SUCCESS;
        }else if(zipOrUnzip.isFirstContainVal("UNZIP")) {
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
            unzip(path);
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
        if((!endOfFile.equals("zip"))&&(!endOfFile.equals("zip\""))){
           return false;
        }
        return true;
    }

    private void zipFile(String sourceFilePath, String zipFilePath) throws IOException {
        zipFilePath += ".zip";

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
    private static void unzip(String source) throws IOException {

        File zipFile = new File(source);
        String destinationFolder = getDestinationFolder(source);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String entryName = zipEntry.getName();
            File newFile = new File(destinationFolder, entryName);

            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                FileOutputStream fos = new FileOutputStream(newFile);
                int length;
                while ((length = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
            }

            zis.closeEntry();
            zipEntry = zis.getNextEntry();
        }
        zis.close();
    }
    private static String getDestinationFolder(String source) {
        File zipFile = new File(source);
        String parentFolder = zipFile.getParent();
        return parentFolder != null ? parentFolder : "";
    }
    private static String getBaseName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
}
