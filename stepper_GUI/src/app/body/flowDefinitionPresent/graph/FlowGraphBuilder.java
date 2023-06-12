package app.body.flowDefinitionPresent.graph;

import app.management.style.StyleManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.step.api.DataDefinitionDeclaration;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FlowGraphBuilder {
    public static void buildFlowGraph(FlowDefinitionImpl flow) {
        StringBuilder dotCode = new StringBuilder();
        dotCode.append("digraph FlowDiagram {\n");
        dotCode.append("  rankdir=LR;\n");
        dotCode.append("  size=\"400,600\";\n");
      //  dotCode.append("  node [style=filled, color=light blue];\n");
        String color;
        if (StyleManager.getCurrentTheme().equals("dark")) {
            color = "#24292e";
        }else{

            color = "#40ffb4";
        }

        dotCode.append("  bgcolor=\"#24292e\"\n");
        dotCode.append("  user_inputs [label=\"User-Inputs\", shape=circle, color=blue, penwidth=5 ];\n");
        dotCode.append("  user_outputs [label=\"User-Outputs\", shape=circle, color=red, penwidth=5 ];\n");
        dotCode.append("  node [fontcolor=white];\n" +
                "  edge [fontcolor=white];\n");


        List<StepUsageDeclaration> steps = flow.getSteps();

        int stepNumber = 1;
        for (StepUsageDeclaration step : steps) {
            String stepName = step.getFinalStepName();
            dotCode.append("  " + stepName + " [label=\"" + stepName + "\\n(" + stepNumber + ")\", shape=circle, color=green, penwidth=2 ];\n");
            stepNumber++;
        }
        //check for each user input to witch step it is connected
        for (StepUsageDeclaration step : steps) {
            String stepName = step.getFinalStepName();
            List<DataDefinitionDeclaration> inputsDD = step.getStepDefinition().inputs();
            for (DataDefinitionDeclaration inputDD : inputsDD) {
                String inputName = inputDD.getName();
                dotCode.append("  user_inputs -> " + stepName + " [label=\"" + inputName + "\", color=orange, penwidth=2 ];\n");
            }

            List<DataDefinitionDeclaration> outputsDD = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration outputDD : outputsDD) {
                String outputName = outputDD.getName();
                dotCode.append("  " + stepName + " -> user_outputs [label=\"" + outputName + "\", color=red, penwidth=2 ];\n");
            }
        }

        dotCode.append("  user_outputs [label=\"UserOutputs\", shape=circle, color=red , penwidth=2 ];\n");

        dotCode.append("}\n");



        String folderPath = "";
        String dotFilePath = folderPath + "flow.dot";
        String pngFilePath = folderPath + "flow.png";
        try (PrintWriter writer = new PrintWriter(dotFilePath)) {
            writer.print(dotCode.toString());
        } catch (FileNotFoundException e) {
            System.err.println("graphviz not installed");
        }
        // Generate the graph using the Graphviz command-line tool
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "-o", pngFilePath, dotFilePath);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            System.err.println("graphviz not installed");
        }
    }
}
