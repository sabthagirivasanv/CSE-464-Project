package com.svellin3.impl;

import com.svellin3.GraphManager;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class GraphManagerImpl implements GraphManager{

    Graph graph;
    public void parseGraph(String fileName) throws IOException {
        File initialFile = new File(fileName);
        InputStream dot = new DataInputStream(Files.newInputStream(initialFile.toPath()));
        MutableGraph mutableGraph = new Parser().read(dot);
        graph = new Graph(mutableGraph);
        toString();
    }

    public int nodeSize(){
        return graph.nodeSize();
    }

    public int edgeSize(){
        return graph.edgeSize();
    }

    @Override
    public boolean containsEdge(String src, String dst) {
        return graph.containsEdge(src, dst);
    }

    public boolean containsNode(String nodeName) {
        return graph.containsNode(nodeName);
    }

    public String toString(){
        String output;
        System.out.println("Number of Nodes: "+nodeSize());
        System.out.println("Label of Nodes:"+ graph.getAllNodes());
        System.out.println("Number of Edges: "+edgeSize());
        output = graph.convertToGraphViz().toString();
        System.out.println(output);
        return output;
    }

    public void addNode(String label){
        graph.addNode(label);
    }

    public void addNodes(String... label){
        graph.addNodes(label);
    }

    public void removeNode(String label){
        graph.removeNode(label);
    }

    public void removeNodes(String... label){
        graph.removeNodes(label);
    }


    @Override
    public void addEdge(String srcLabel, String dstLabel) {
        graph.addEdge(srcLabel, dstLabel);
    }

    @Override
    public void removeEdge(String srcLabel, String dstLabel) {
        graph.removeEdge(srcLabel, dstLabel);
    }

    @Override
    public void outputDOTGraph(String path) throws IOException {
        MutableGraph mutGraph = graph.convertToGraphViz();
        Graphviz.fromGraph(mutGraph).width(900).render(Format.DOT).toFile(new File(path));
    }

    @Override
    public void outputGraphics(String path, String format) throws IOException {
        MutableGraph mutGraph = graph.convertToGraphViz();

        if(Objects.equals(format, "png") || Objects.equals(format, "PNG")){
            Graphviz.fromGraph(mutGraph).width(900).render(Format.PNG).toFile(new File(path));
        }
        if(Objects.equals(format, "svg") || Objects.equals(format, "SVG")){
            Graphviz.fromGraph(mutGraph).width(900).render(Format.SVG).toFile(new File(path));
        }
        if(Objects.equals(format, "dot") || Objects.equals(format, "DOT")){
            Graphviz.fromGraph(mutGraph).width(900).render(Format.DOT).toFile(new File(path));
        }
    }

    @Override
    public void outputGraph(String filePath) throws IOException {
        // Defining the file name of the file
        if(!getExtension(filePath).isPresent()){
            filePath += ".txt";
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("\nNumber of Nodes: "+nodeSize());
        writer.write("\nLabel of Nodes:"+ graph.getAllNodes());
        writer.write("\nNumber of Edges: "+edgeSize());
        String output = graph.convertToGraphViz().toString();
        writer.write('\n'+output);
        writer.close();
    }


    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}