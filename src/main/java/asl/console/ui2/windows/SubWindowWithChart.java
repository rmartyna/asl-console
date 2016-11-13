package asl.console.ui2.windows;

import pl.edu.agh.beans.Service;
import asl.console.ui2.MyUI;
import asl.console.ui2.charts.BaseChart;

import com.vaadin.ui.Component;

public class SubWindowWithChart extends SubWindow{
	
	// name of parameter clicked by client 
	private String paramName;
	private Service service;
	
	public SubWindowWithChart(Service service, String paramName) 
	{
		super();
		this.paramName = paramName;
		this.service = service;
		
		content.removeAllComponents();
				
		createChart();
	}
	
	private void createChart()
	{
		content.addComponent(new BaseChart(service, paramName));
		setContent(content);
	}
}
