package asl.console.ui2;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import asl.console.ui2.windows.SubWindow;
import asl.console.ui2.windows.SubWindowWithChart;
import pl.edu.agh.beans.*;
import pl.edu.agh.dao.*;

@SuppressWarnings("serial")
public class ServersStateView extends ServersStateLayout {

	public static final String BUTTON_WIDTH = "150px";
	private DecimalFormat df = new DecimalFormat("#.#");

	private SelectServersView selectServersView;
	private SelectParametersView selectParametersView;

	public ServersStateView() {
		pageTitleLabel.setValue("Servers states");
		setSizeFull();
		setResponsive(true);

		selectServersView = new SelectServersView();
		selectServersWrapperLayout.addComponent(selectServersView);
		selectParametersView = new SelectParametersView();
		selectParametersWrapperLayout.addComponent(selectParametersView);

		for (String paramName : Constants.getAllParameterNames()) {
			Label label = new Label(paramName);
			label.setStyleName("serverDesc");
			label.setWidth(BUTTON_WIDTH);
			label.setStyleName("centered");
			labelsLayout.addComponent(label);
			labelsLayout.setComponentAlignment(label, Alignment.TOP_RIGHT);
		}
		
		try {
			List<Service> services = MyUI.context.getBean(ServiceDAO.class).listAll();
			for(Service service : services) {
				try {
					System.out.println("Adding row service: " + service);
					serversLayout.addComponent(createServerStateRow(service));
				} catch(Exception e) {
					System.err.println("Could not add server: " + service.getDescription());
					e.printStackTrace();
				}
			}

		} catch(Exception e) {
			System.err.println("Could not add servers");
			e.printStackTrace();
		}

		// Marked checkeboxes on startup
		selectParametersView.setAllCheckBoxesChecked();
		selectServersView.setAllCheckBoxesChecked();

		// Must be last call in this method
		setClickListenersForCheckBoxes();
	}

	ServerStateRowLayout createServerStateRow(Service service) throws SQLException {
		ServerStateRowLayout row = new ServerStateRowView();
		row.serverName.setValue(service.getDescription());

		ConsoleConfiguration consoleConfiguration = MyUI.context.getBean(ConsoleConfigurationDAO.class).getByServiceId((int) service.getId());

		CpuUsage cpuUsage = null;
		try {
			cpuUsage = MyUI.context.getBean(CpuDAO.class).getNewestUsageByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not find cpu usage: " + e.getMessage());
		}
		CpuTemp cpuTemp = null;
		try {
			cpuTemp = MyUI.context.getBean(CpuDAO.class).getNewestTempByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not find cpu temp: " + e.getMessage());
		}

		Network network = null;
		try {
			network = MyUI.context.getBean(NetworkDAO.class).getNewestByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not find network: " + e.getMessage());
		}

		DiskUsage diskUsage = null;
		try {
			diskUsage = MyUI.context.getBean(DiskDAO.class).getNewestUsageByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not find disk usage: " + e.getMessage());
		}

		Partition partition = null;
		try {
			partition = MyUI.context.getBean(DiskDAO.class).getNewestPartitionByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not find partition: " + e.getMessage());
		}

		Memory memory = null;
		try {
			memory =  MyUI.context.getBean(MemoryDAO.class).getNewestByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not find memory: " + e.getMessage());
		}




		ArrayList<String> parameterNames = Constants.getAllParameterNames();
		for (String paramName : parameterNames) {
			String color = "GREEN";
			String valueString = "NOT AVAILABLE";
			Double value = 0.0;
			Double lower = 1.0;
			Double upper = 2.0;
			int divisor = 1048576;

			if(paramName.equals(Constants.CPU_TEMP_LABEL) && cpuTemp != null) {
				value = cpuTemp.getValue();
				lower = consoleConfiguration.getCpuTempLowerBound();
				upper = consoleConfiguration.getCpuTempUpperBound();
				valueString = value + " C";
			} else if(paramName.equals(Constants.CPU_USAGE_LABEL) && cpuUsage != null) {
				value = cpuUsage.getUser() + cpuUsage.getSystem() + cpuUsage.getIowait();
				lower = consoleConfiguration.getCpuLoadLowerBound();
				upper = consoleConfiguration.getCpuLoadUpperBound();
				valueString = df.format(value) + " %";
			} else if(paramName.equals(Constants.MEMORY_USAGE_LABEL) && memory != null) {
				value = memory.getCurrent() / divisor;
				lower = consoleConfiguration.getRamLowerBound();
				upper = consoleConfiguration.getRamUpperBound();
				valueString = df.format(value / divisor) + " / " + df.format(memory.getMax() / divisor) + " GB";
			} else if(paramName.equals(Constants.NETWORK_DOWNLOAD_LABEL) && network != null) {
				value = network.getDownload();
				lower = consoleConfiguration.getNetworkDlLowerBound();
				upper = consoleConfiguration.getNetworkUlUpperBound();
				valueString = value + " kb/s";
			} else if(paramName.equals(Constants.NETWORK_UPLOAD_LABEL) && network != null) {
				value = network.getUpload();
				lower = consoleConfiguration.getNetworkUlLowerBound();
				upper = consoleConfiguration.getNetworkUlUpperBound();
				valueString = value + " kb/s";
			} else if(paramName.equals(Constants.DISK_READ_LABEL) && diskUsage != null) {
				value = diskUsage.getRead();
				lower = consoleConfiguration.getDiskReadLowerBound();
				upper = consoleConfiguration.getDiskReadUpperBound();
				valueString = value + " kb/s";
			} else if(paramName.equals(Constants.DISK_WRITE_LABEL) && diskUsage != null) {
				value = diskUsage.getWrite();
				lower = consoleConfiguration.getDiskWriteLowerBound();
				upper = consoleConfiguration.getDiskWriteUpperBound();
				valueString = value + " kb/s";
			} else if(paramName.equals(Constants.DISK_USAGE_LABEL) && partition != null) {
				value = partition.getCurrent() / divisor;
				lower = consoleConfiguration.getDiskUsageLowerBound();
				upper = consoleConfiguration.getDiskUsageUpperBound();
				valueString = df.format(partition.getCurrent() / divisor) + " / " + df.format(partition.getMax() / divisor) + " GB";
			}

			if(value < lower)
				color = "GREEN";
			else if(value < upper)
				color = "YELLOW";
			else
				color = "RED";

			OpenSubWindowButton button = new OpenSubWindowButton(valueString, paramName, new SubWindowWithChart(service, paramName), ServersStateView.class);
			
			button.setWidth(BUTTON_WIDTH);
			button.setStyleName(color);

			row.serverParametersLayout.addComponent(button);
		}
		return row;
	}

	public void setClickListenersForCheckBoxes() {
		
		// Select servers checkboxes
		for(CheckBox checkBox : selectServersView.getCheckBoxes()) {
			checkBox.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if(checkBox.getValue()) {
						Iterator<Component> iterator = serversLayout.iterator();
						while(iterator.hasNext()) {
							ServerStateRowView row = (ServerStateRowView) iterator.next();
							if(row.serverName.getValue().equals(checkBox.getCaption())) {
								row.setVisible(true);
							}
						}
					} if(!checkBox.getValue()) {
						Iterator<Component> iterator = serversLayout.iterator();
						while(iterator.hasNext()) {
							ServerStateRowView row = (ServerStateRowView) iterator.next();
							if(row.serverName.getValue().equals(checkBox.getCaption())) {
								row.setVisible(false);
							}
						}
					}
				}
			});
		}
		
		// Select parameters checkboxes
		for (CheckBox checkBox : selectParametersView.getCheckBoxes()) {
			checkBox.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (checkBox.getValue()) {
						Iterator<Component> labelsIterator = labelsLayout.iterator();
						while (labelsIterator.hasNext()) {
							Label label = (Label) labelsIterator.next();
							if(label.getValue().equals(checkBox.getCaption())) {
								label.setVisible(true);
							}
						}
						Iterator<Component> iterator = serversLayout.iterator();
						while (iterator.hasNext()) {
							ServerStateRowView row = (ServerStateRowView) iterator.next();
							row.getStateParameterButton(checkBox.getCaption()).setVisible(true);
						}
					} else {
						Iterator<Component> labelsIterator = labelsLayout.iterator();
						while (labelsIterator.hasNext()) {
							Label label = (Label) labelsIterator.next();
							if(label.getValue().equals(checkBox.getCaption())) {
								label.setVisible(false);
							}
						}
						
						Iterator<Component> iterator = serversLayout.iterator();
						while (iterator.hasNext()) {
							ServerStateRowView row = (ServerStateRowView) iterator.next();
							row.getStateParameterButton(checkBox.getCaption()).setVisible(false);
						}
					}

				}
			});			
		}
	}
	
	/*	
	private void createServersTable() {

		// Set column names
		serversStateTable.addContainerProperty(Constants.SERVER_NAME_LABEL, com.vaadin.ui.Button.class, null);
		serversStateTable.addContainerProperty(Constants.TEMPERATURE_LABEL, com.vaadin.ui.Button.class, null);
		serversStateTable.addContainerProperty(Constants.CPU_LOAD_LABEL, com.vaadin.ui.Button.class, null);
		serversStateTable.addContainerProperty(Constants.BANDWIDTH_LABEL, com.vaadin.ui.Button.class, null);
		serversStateTable.addContainerProperty(Constants.IO_LABEL, com.vaadin.ui.Button.class, null);
		serversStateTable.addContainerProperty(Constants.RAM_LOAD_LABEL, com.vaadin.ui.Button.class, null);


		int serverCounter = 0;
		for(Service service : serviceList) {
			try {
				

				// Set caption and subwindow for buttons
				OpenSubWindowButton serverNameBtn = new OpenSubWindowButton(service.getDescription(), new SubWindow());
				CpuTemp cputemp = MyUI.context.getBean(CpuDAO.class).getNewestTempByServiceId((int) service.getId());
				OpenSubWindowButton tempBtn = new OpenSubWindowButton(df.format(cputemp.getValue()) + "\u00B0" + "C", new SubWindow());

				CpuUsage cpuUsage = MyUI.context.getBean(CpuDAO.class).getNewestUsageByServiceId((int) service.getId());
				OpenSubWindowButton cpuBtn = new OpenSubWindowButton(cpuUsage.getUser() + "%", new SubWindow());

				Network network = MyUI.context.getBean(NetworkDAO.class).getNewestByServiceId((int) service.getId());
				OpenSubWindowButton bandwidthBtn = new OpenSubWindowButton(network.getDownload() + " kB/s", new SubWindow());

				DiskUsage diskUsage = MyUI.context.getBean(DiskDAO.class).getNewestUsageByServiceId((int) service.getId());
				OpenSubWindowButton ioBtn = new OpenSubWindowButton(String.valueOf(diskUsage.getWrite() + " kB/s"), new SubWindow());

				Memory memory = MyUI.context.getBean(MemoryDAO.class).getNewestByServiceId((int) service.getId());
				OpenSubWindowButton ramBtn = new OpenSubWindowButton(df.format(memory.getCurrent() / 1024) + " MB / " + df.format(memory.getMax() / 1024) + " MB", new SubWindow());



				ConsoleConfiguration consoleConfiguration = MyUI.context.getBean(ConsoleConfigurationDAO.class).getByServiceId(2);
				// Set colors for buttons

				if(cputemp.getValue() < consoleConfiguration.getCpuTempLowerBound())
					tempBtn.addStyleName("GREEN");
				else if(cputemp.getValue() > consoleConfiguration.getCpuTempUpperBound())
					tempBtn.addStyleName("RED");
				else
					tempBtn.addStyleName("YELLOW");

				if(cpuUsage.getUser() < consoleConfiguration.getCpuLoadLowerBound())
					cpuBtn.addStyleName("GREEN");
				else if(cpuUsage.getUser() > consoleConfiguration.getCpuLoadUpperBound())
					cpuBtn.addStyleName("RED");
				else
					cpuBtn.addStyleName("YELLOW");

				if(network.getDownload() < consoleConfiguration.getBandwidthLowerBound())
					bandwidthBtn.addStyleName("GREEN");
				else if(network.getDownload() > consoleConfiguration.getBandwidthUpperBound())
					bandwidthBtn.addStyleName("RED");
				else
					bandwidthBtn.addStyleName("YELLOW");

				if(diskUsage.getWrite() < consoleConfiguration.getIoLowerBound())
					ioBtn.addStyleName("GREEN");
				else if(diskUsage.getWrite() > consoleConfiguration.getIoUpperBound())
					ioBtn.addStyleName("RED");
				else
					ioBtn.addStyleName("YELLOW");

				if((memory.getCurrent() / 1024) < consoleConfiguration.getRamLowerBound())
					ramBtn.addStyleName("GREEN");
				else if((memory.getCurrent() / 1024) > consoleConfiguration.getRamUpperBound())
					ramBtn.addStyleName("RED");
				else
					ramBtn.addStyleName("YELLOW");

				serversStateTable.addItem(new Object[] { serverNameBtn, tempBtn, cpuBtn, bandwidthBtn, ioBtn, ramBtn },
						serverCounter + 2);
				serverCounter++;
			} catch(Exception e) {
				e.printStackTrace();
			}



		}
		
	}
	*/
}
