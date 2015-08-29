package com.csvsim.wrapper.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.csvsim.wrapper.CsvGenerator;
import com.csvsim.wrapper.Decision;
import com.csvsim.wrapper.DiscreteEvent;
import com.csvsim.wrapper.Generator;
import com.csvsim.wrapper.List;
import com.csvsim.wrapper.Normal;
import com.csvsim.wrapper.Setter;

public class CsvGenerator2Graph {
	
	private Integer numGen;	
	
	private HashMap<String,String> labelByVertex;	
	
	private HashMap<DefaultEdge,String> labelByEdge;
	
	@SuppressWarnings("rawtypes")	
	private HashMap<Generator,String> vertexByGenerator;
	
	private DirectedGraph<String, DefaultEdge> graph;
	
	private CsvGenerator csvGenerator;
	
	@SuppressWarnings("rawtypes")
	public CsvGenerator2Graph(CsvGenerator csvGenerator) {
		this.csvGenerator = csvGenerator;
		this.numGen = 0;	
		this.labelByVertex = new HashMap<String,String>();
		this.labelByEdge = new HashMap<DefaultEdge,String>();
		this.vertexByGenerator = new HashMap<Generator,String>();
		this.graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	}

	@SuppressWarnings("rawtypes")
	private String addGeneratorToGraph(String parentName, Generator parent, Generator generator){
		String className = StringUtils.substringAfterLast(generator.getClass().toString(), ".");
		String generatorName = className + ++numGen;			
		
		if(generator.getClass() == Decision.class){
			if( !((Decision) generator).isExact()){
				labelByVertex.put(generatorName, numGen + " "  + className + " by Range\", shape=\"diamond");
			}
			else{
				labelByVertex.put(generatorName, numGen + " "  + className + " Exact\", shape=\"diamond");
			}
		}
		else if(generator.getClass() == List.class){
			labelByVertex.put(generatorName, numGen + " "  + className + "\", shape=\"box");
		}
		else if(generator.getClass() == Setter.class){
			Setter setter = (Setter) generator;
			labelByVertex.put(generatorName, numGen + " "  + className+ " \\nValue: "+ setter.getValue() +"\", shape=\"oval");
		}
		else if(generator.getClass() == Normal.class){
			Normal normal = (Normal) generator;
			labelByVertex.put(generatorName, numGen + " "  + className+ " \\nMean: "+ normal.getMean() + " Std Dev: "+ normal.getSd() + "\", shape=\"oval");
		}	
		else{
			labelByVertex.put(generatorName, numGen + " "  + className+ "\", shape=\"oval");
		}
		
		this.graph.addVertex(generatorName);
		this.vertexByGenerator.put(generator, generatorName);

		return generatorName;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DefaultEdge addToGraph(String parentName, Generator parent, Generator generator) {
		
		DefaultEdge generatorEdge = null;
		if(!vertexByGenerator.keySet().contains(generator)){
			
			String generatorName = addGeneratorToGraph(parentName, parent, generator);			
			if(parentName!=null){
				generatorEdge = this.graph.addEdge(parentName, generatorName);
			}
			if(generator.isContainer()){
				if(generator.getClass() == DiscreteEvent.class){
					DiscreteEvent discreteEvent = (DiscreteEvent) generator;				
					addToGraph(generatorName, discreteEvent, discreteEvent.getTimeDistribution());
					for (Generator subGenerator : discreteEvent.getGenerators()) {
						addToGraph(generatorName,discreteEvent,subGenerator);
					}				
				}
				else if(generator.getClass() == List.class){
					List list = (List) generator;
					for (Generator subGenerator : list.getGenerators()) {
						addToGraph(generatorName,list,subGenerator);
					}
				}
				else{
					Decision decision = (Decision) generator;
					Iterator itr = decision.getGeneratorsByValue().entrySet().iterator();
					while (itr.hasNext()) {
						Entry<Object,Generator> entry = (Entry<Object, Generator>) itr.next();
						DefaultEdge sonEdge = addToGraph(generatorName,decision,entry.getValue());
						if(decision.isExact()){
							labelByEdge.put(sonEdge, "= " + entry.getKey().toString());
						}
						else{
							labelByEdge.put(sonEdge, "<= "+ entry.getKey().toString());
						}
					}
					DefaultEdge inputEdge = this.graph.addEdge(generator.getField().getName(), generatorName);

					if(generator.getField().getConstantValue()==null){
						if(csvGenerator.getHiddenFields().contains(generator.getField())){
							labelByEdge.put(inputEdge, "\", color=\"#FF6600\", arrowtype=\"box\", style=\"bold");
						}
						else{
							labelByEdge.put(inputEdge, "\", color=\"#FF5050\", arrowtype=\"box\", style=\"bold");
						}
					}
					else{
						labelByEdge.put(inputEdge, "\", color=\"#D6EBFF\", arrowtype=\"box\", style=\"bold");
					}
				}				
			}
			else{
				this.graph.addEdge(generatorName, generator.getField().getName());
			}
			return generatorEdge;
		}
		if(parentName!=null){
			generatorEdge = this.graph.addEdge(parentName, vertexByGenerator.get(generator));
		}		
		return generatorEdge;
	}

	private DirectedGraph<String, DefaultEdge> toGraph(CsvGenerator csvGenerator) {
		
		for(com.csvsim.wrapper.Field field : csvGenerator.getFields()){
			this.graph.addVertex(field.getName());			
			if(field.getConstantValue()==null){
				labelByVertex.put(field.getName(), "Field: " + field.getName() + "\\n Tipo: "+ field.getType().toString().toLowerCase() + "\", style=\"filled\", color=\"#CC5252\", fillcolor=\"#FFC2C2");
			}
			else{
				labelByVertex.put(field.getName(), "Constant field: " + field.getName() + "\\n Tipo: "+ field.getType().toString().toLowerCase() + "\", style=\"filled\", color=\"#47A3FF\", fillcolor=\"#D6EBFF");
			}
		}
		for(com.csvsim.wrapper.Field hiddenField : csvGenerator.getHiddenFields()){
			this.graph.addVertex(hiddenField.getName());
			if(hiddenField.getConstantValue()==null){
				labelByVertex.put(hiddenField.getName(), "Hidden Field: " + hiddenField.getName() + "\\n Tipo: "+ hiddenField.getType().toString().toLowerCase() + "\", style=\"filled\", color=\"#FF6600\", fillcolor=\"#FFB280");
			}
			else{
				labelByVertex.put(hiddenField.getName(), "Constant hidden Field: " + hiddenField.getName() + "\\n Tipo: "+ hiddenField.getType().toString().toLowerCase() + "\", style=\"filled\", color=\"#47A3FF\", fillcolor=\"#D6EBFF");
			}
		}		
		this.addToGraph(null, null, csvGenerator.getGenerator());
		return graph;
	}
	
	private HashMap<String, String> getLabelByVertex() {
		return this.labelByVertex;
	}

	private HashMap<DefaultEdge, String> getLabelByEdge() {
		return this.labelByEdge;
	}

	public static void toDot(java.io.OutputStreamWriter writer, CsvGenerator csvGenerator) {
		CsvGenerator2Graph csvGenerator2Graph = new CsvGenerator2Graph(csvGenerator);

		DirectedGraph<String, DefaultEdge> graph = csvGenerator2Graph.toGraph(csvGenerator);

		VertexNameProvider<String> vertexName = new VertexNameProvider<String>() {
			@Override
			public String getVertexName(String vertex) {
				return vertex;
			}
		};

		final HashMap<String, String> labelByVertex = csvGenerator2Graph.getLabelByVertex();
		VertexNameProvider<String> vertexLabel = new VertexNameProvider<String>() {
			@Override
			public String getVertexName(String vertex) {
				return labelByVertex.get(vertex);				
			}
		};

		final HashMap<DefaultEdge, String> labelByEdge = csvGenerator2Graph.getLabelByEdge();
		EdgeNameProvider<DefaultEdge> edgeName = new EdgeNameProvider<DefaultEdge>() {
			@Override
			public String getEdgeName(DefaultEdge edge) {
				String label = labelByEdge.get(edge);
				return (label==null?"":label);
			}

		};
		DOTExporter<String, DefaultEdge> exporter = new DOTExporter<String, DefaultEdge>(vertexName, vertexLabel, edgeName);
		exporter.export(writer, graph);
	}
}
