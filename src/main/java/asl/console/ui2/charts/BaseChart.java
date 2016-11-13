package asl.console.ui2.charts;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.beans.Cpu;
import pl.edu.agh.beans.CpuTemp;
import pl.edu.agh.beans.CpuUsage;
import pl.edu.agh.beans.Disk;
import pl.edu.agh.beans.DiskUsage;
import pl.edu.agh.beans.Memory;
import pl.edu.agh.beans.Network;
import pl.edu.agh.beans.Partition;
import pl.edu.agh.beans.Service;
import pl.edu.agh.dao.CpuDAO;
import pl.edu.agh.dao.DiskDAO;
import pl.edu.agh.dao.MemoryDAO;
import pl.edu.agh.dao.NetworkDAO;
import asl.console.ui2.Constants;
import asl.console.ui2.MyUI;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.examples.AbstractVaadinChartExample;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.RangeSelector;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.ui.Component;

public class BaseChart extends AbstractVaadinChartExample{

	// name of parameter presented on chart
	private String presentedParameterName; 
	
	// name of service for which datas are drawn
	private Service service;
	
	// chart configuration variable
	private Configuration configuration;
	
	
	public BaseChart(Service service, String paramName) {
		presentedParameterName = paramName;
		this.service = service;
	}
	
	
	
    @Override
    public String getDescription() {
        return "Base chart for " + presentedParameterName;
    }
	
	@Override
	protected Component getChart() {
		final Chart chart = new Chart();
        chart.setHeight("450px");
        chart.setWidth("100%");
        chart.setTimeline(true);
        
        configuration = chart.getConfiguration();
        configuration.getTitle().setText(presentedParameterName + " details");
        
        
        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(1);
        configuration.setRangeSelector(rangeSelector);
        
        XAxis xaxis = new XAxis();
        xaxis.setTitle("Time");
        YAxis yaxis = new YAxis();
        yaxis.setTitle("Values");
        configuration.addxAxis(xaxis);
        configuration.addyAxis(yaxis);
        
        configuration.addSeries(getDetailsData());
        
//        PlotOptionsLine plotOptions = new PlotOptionsLine();
//        plotOptions.getDataLabels().setEnabled(true);
//        configuration.setPlotOptions(plotOptions);
        
// 	   Legend legend = new Legend();
// 	   legend.getTitle().setText("Legend");
// 	   legend.setLayout(LayoutDirection.VERTICAL);
// 	   legend.setAlign(HorizontalAlign.LEFT);
// 	   legend.setVerticalAlign(VerticalAlign.TOP);
// 	   legend.setBorderWidth(3);
//        legend.setX(100);
//        legend.setY(70);
//        legend.setFloating(true);
//        legend.setShadow(true);
//         configuration.setLegend(legend);
        
        chart.drawChart(configuration);
		return chart;
	}
	
	
	/**
	 * returns appropriate data from server's db
	 * @return
	 */
	private DataSeries getDetailsData()
	{
		if(presentedParameterName.equals(Constants.CPU_TEMP_LABEL)) {
			return getTemperatureDetails();
		} else if(presentedParameterName.equals(Constants.CPU_USAGE_LABEL)) {
			return getCpuUsageDetails();
		} else if(presentedParameterName.equals(Constants.MEMORY_USAGE_LABEL)) {
			return getMemoryUsageDetails();
		} else if(presentedParameterName.equals(Constants.NETWORK_DOWNLOAD_LABEL)) {
			return getNetworkDownloadDetails();
		} else if(presentedParameterName.equals(Constants.NETWORK_UPLOAD_LABEL)) {
			return getNetworkUploadDetails();
		} else if(presentedParameterName.equals(Constants.DISK_READ_LABEL)) {
			return getDiskReadDetails();
		} else if(presentedParameterName.equals(Constants.DISK_WRITE_LABEL)) {
			return getDiskWriteDetails();
		} else if(presentedParameterName.equals(Constants.DISK_USAGE_LABEL)) {
			return getDiskUsageDetails();
		} else { 
			return null;
		}
	}



	private DataSeries getDiskUsageDetails()
	{
		List<Partition> diskDetailsList = new LinkedList<>();
		int maxDiskUsage = 0;
		try {
			Disk disk = MyUI.context.getBean(DiskDAO.class).getByServiceId((int)service.getId());
			diskDetailsList = MyUI.context.getBean(DiskDAO.class).getPartitionListByDiskId((int)disk.getId());
			maxDiskUsage = (int) MyUI.context.getBean(DiskDAO.class).getNewestPartitionByServiceId((int)service.getId()).getMax();
		} catch(Exception e) {
			System.err.println("Could not find disk details: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		dataSeries.setName(presentedParameterName);
		if (maxDiskUsage > 0)
		{
			PlotOptionsArea plotOptions = new PlotOptionsArea();
	        plotOptions.setStacking(Stacking.PERCENT);
	        plotOptions.setLineWidth(1);
	        configuration.setPlotOptions(plotOptions);
	        YAxis yaxis = configuration.getyAxis();
	        yaxis.setTitle("Percentage value");
	        
	        
			for (Partition diskDetails : diskDetailsList)
			{
	            DataSeriesItem item = new DataSeriesItem();
	            item.setX(diskDetails.getDate());
	            item.setY(100 * diskDetails.getCurrent()/maxDiskUsage);
	            dataSeries.add(item);
			}
		}
		else
		{
			for (Partition diskDetails : diskDetailsList)
			{
	            DataSeriesItem item = new DataSeriesItem();
	            item.setX(diskDetails.getDate());
	            item.setY(diskDetails.getCurrent());
	            dataSeries.add(item);
			}
		}
		
		return dataSeries;
	}
	
	
	private DataSeries getDiskWriteDetails()
	{
		List<DiskUsage> diskDetailsList = new LinkedList<>();
		
		try {
			Disk disk = MyUI.context.getBean(DiskDAO.class).getByServiceId((int)service.getId());
			diskDetailsList = MyUI.context.getBean(DiskDAO.class).getUsageListByDiskId((int)disk.getId());
		} catch(Exception e) {
			System.err.println("Could not find disk details: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		for (DiskUsage diskDetails : diskDetailsList)
		{
            DataSeriesItem item = new DataSeriesItem();
            item.setX(diskDetails.getDate());
            item.setY(diskDetails.getWrite());
            dataSeries.add(item);
		}
		
		return dataSeries;
	}
	
	
	private DataSeries getDiskReadDetails()
	{
		List<DiskUsage> diskDetailsList = new LinkedList<>();
		
		try {
			Disk disk = MyUI.context.getBean(DiskDAO.class).getByServiceId((int)service.getId());
			diskDetailsList = MyUI.context.getBean(DiskDAO.class).getUsageListByDiskId((int)disk.getId());
		} catch(Exception e) {
			System.err.println("Could not find disk details: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		for (DiskUsage diskDetails : diskDetailsList)
		{
            DataSeriesItem item = new DataSeriesItem();
            item.setX(diskDetails.getDate());
            item.setY(diskDetails.getRead());
            dataSeries.add(item);
		}
		
		return dataSeries;
	}
	
	
	private DataSeries getNetworkUploadDetails()
	{
		List<Network> networkDetailsList = new LinkedList<>();
		
		try {
			networkDetailsList = MyUI.context.getBean(NetworkDAO.class).getByServiceId((int)service.getId());
		} catch(Exception e) {
			System.err.println("Could not find network: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		for (Network networkDetails : networkDetailsList)
		{
            DataSeriesItem item = new DataSeriesItem();
            item.setX(networkDetails.getDate());
            item.setY(networkDetails.getUpload());
            dataSeries.add(item);
		}
		
		return dataSeries;
	}
	
	private DataSeries getNetworkDownloadDetails()
	{
		List<Network> networkDetailsList = new LinkedList<>();
		
		try {
			networkDetailsList = MyUI.context.getBean(NetworkDAO.class).getByServiceId((int)service.getId());
		} catch(Exception e) {
			System.err.println("Could not find network: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		for (Network networkDetails : networkDetailsList)
		{
            DataSeriesItem item = new DataSeriesItem();
            item.setX(networkDetails.getDate());
            item.setY(networkDetails.getDownload());
            dataSeries.add(item);
		}
		
		return dataSeries;
	}
	
	
	private DataSeries getMemoryUsageDetails()
	{
		List<Memory> memoryDetailsList = new LinkedList<>();
		int maxMemoryUsage = 0;
		
		try {
			memoryDetailsList = MyUI.context.getBean(MemoryDAO.class).getByServiceId((int)service.getId());
			maxMemoryUsage = (int)MyUI.context.getBean(MemoryDAO.class).getNewestByServiceId((int)service.getId()).getMax();
		} catch(Exception e) {
			System.err.println("Could not find memory: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		
		if (maxMemoryUsage > 0)
		{
			PlotOptionsArea plotOptions = new PlotOptionsArea();
	        plotOptions.setStacking(Stacking.PERCENT);
	        plotOptions.setLineWidth(1);
	        configuration.setPlotOptions(plotOptions);
	        YAxis yaxis = configuration.getyAxis();
	        yaxis.setTitle("Percentage value");
	        
			for (Memory memoryDetails : memoryDetailsList)
			{
	            DataSeriesItem item = new DataSeriesItem();
	            item.setX(memoryDetails.getDate());
	            item.setY(100 * memoryDetails.getCurrent()/maxMemoryUsage);
	            dataSeries.add(item);
			}
		}
		else
		{
			for (Memory memoryDetails : memoryDetailsList)
			{
	            DataSeriesItem item = new DataSeriesItem();
	            item.setX(memoryDetails.getDate());
	            item.setY(memoryDetails.getCurrent());
	            dataSeries.add(item);
			}
		}
		
		return dataSeries;
	}
	
	
	private DataSeries getTemperatureDetails()
	{
		List<CpuTemp> cpuTempList = new LinkedList<>();
		
		try {
			Cpu cpu = MyUI.context.getBean(CpuDAO.class).getByServiceId((int)service.getId());
			cpuTempList = MyUI.context.getBean(CpuDAO.class).getTempListByCpuId((int)cpu.getId());
		} catch(Exception e) {
			System.err.println("Could not find cpu temp: " + e.getMessage());
		}
		
		int numberOfCores = 1;
		DataSeries dataSeries = new DataSeries();
		dataSeries.setPlotOptions(new PlotOptionsSpline());
		dataSeries.setName("1. core");
		for (CpuTemp cpuTemp : cpuTempList)
		{
			if (cpuTemp.getCore() == 1)
			{
	            DataSeriesItem item = new DataSeriesItem();
	            item.setX(cpuTemp.getDate());
	            item.setY(cpuTemp.getValue());
	            dataSeries.add(item);
			}
			else {
				if (cpuTemp.getCore() >= numberOfCores)
					numberOfCores = cpuTemp.getCore();
			}
		}
		
		// if there are more than one core add datas to chart 
		if (numberOfCores > 1){
			for (int i=2; i<=numberOfCores; i++){
				addCoreTemperatureToChart(i);
			}
		}
		
		return dataSeries;
	}
	
	private void addCoreTemperatureToChart(int core) {
		configuration.addSeries(getTemperatureDetails(core));
	}



	private DataSeries getTemperatureDetails(int core)
	{
		List<CpuTemp> cpuTempList = new LinkedList<>();
		
		try {
			Cpu cpu = MyUI.context.getBean(CpuDAO.class).getByServiceId((int)service.getId());
			cpuTempList = MyUI.context.getBean(CpuDAO.class).getTempListByCpuId((int)cpu.getId());
		} catch(Exception e) {
			System.err.println("Could not find cpu temp: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
		dataSeries.setPlotOptions(new PlotOptionsSpline());
		dataSeries.setName(core + ". core");
		for (CpuTemp cpuTemp : cpuTempList)
		{
			if (cpuTemp.getCore() == core)
			{
	            DataSeriesItem item = new DataSeriesItem();
	            item.setX(cpuTemp.getDate());
	            item.setY(cpuTemp.getValue());
	            dataSeries.add(item);
			}
		}
		
		return dataSeries;
	}
	
	private DataSeries getCpuUsageDetails()
	{
		List<CpuUsage> cpuUsageList = new LinkedList<>();
		
		try {
			Cpu cpu = MyUI.context.getBean(CpuDAO.class).getByServiceId((int)service.getId());
			cpuUsageList = MyUI.context.getBean(CpuDAO.class).getUsageListByCpuId((int)cpu.getId());
		} catch(Exception e) {
			System.err.println("Could not find cpu usage: " + e.getMessage());
		}
		
		DataSeries dataSeries = new DataSeries();
        YAxis yaxis = configuration.getyAxis();
        yaxis.setTitle("Percentage value");
        
		for (CpuUsage cpuUsage : cpuUsageList)
		{
            DataSeriesItem item = new DataSeriesItem();
            item.setX(cpuUsage.getDate());
            item.setY(cpuUsage.getUser());		// user's usage
            dataSeries.add(item);
		}
		
		return dataSeries;
	}
		


	

}
