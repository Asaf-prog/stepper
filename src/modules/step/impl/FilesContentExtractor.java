package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.file.FileData;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesContentExtractor extends AbstractStepDefinition {
    public FilesContentExtractor() {

        super("Files Content Extractor", true);

        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.LIST));//full path
        addInput(new DataDefinitionDeclarationImpl("LINE", DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER));//full path

        addOutput(new DataDefinitionDeclarationImpl("DATA", DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.RELATION));

    }
    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        List<FileData> FileList = context.getDataValue("FILES_LIST", List.class);
        int lineNumber = context.getDataValue("LINE", Integer.class);
        List<String> colums = new ArrayList<String>();

      if (FileList == null) {
           context.addSummaryLine("Files Content Extractor ","Their is no files in this folder");
           return StepResult.SUCCESS;
       }
       else {
          List<String>cols= Arrays.asList(new String[]{"Serial Number", "Name Of File","Data in specific line"});
          RelationData table= new RelationData(cols);
          BufferedReader reader = null;
           int index=1;
           for (FileData specificFile : FileList) {

               context.setLogsForStep("Files Content Extractor ","About to start work on file "+specificFile.getName());
               boolean check = false;
               reader = new BufferedReader(new FileReader(specificFile.getFile()));
               String line = null;
               for (int i=0; i <lineNumber;i++){
                   line = reader.readLine();
                   if (i == lineNumber-1){
                       List<String> row = new ArrayList<String>();
                       row.add(Integer.toString(index));
                       row.add(specificFile.getName());
                       row.add(line);
                       index++;
                       table.addRow(row);

                       check= true;
                   }
               }
               if (check == false){
                   context.addSummaryLine("Files Content Extractor ","Not such line");
               }
           }
           context.storeDataValue("DATA",table);
       }
        context.addSummaryLine("Files Content Extractor ","End with Success");

        return StepResult.SUCCESS;

    }
}
