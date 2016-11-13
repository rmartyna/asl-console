package asl.console.ui2;

import java.util.Iterator;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import pl.edu.agh.beans.ConsoleConfiguration;
import pl.edu.agh.beans.Service;
import pl.edu.agh.dao.ConsoleConfigurationDAO;

@SuppressWarnings("serial")
public class ConsoleConfigurationPanelView extends ConsoleConfigurationPanelLayout {

	ConsoleConfiguration configuration;

	public ConsoleConfigurationPanelView(Service service) {

		serverDescription.setValue(service.getDescription());
		try {
			configuration = MyUI.context.getBean(ConsoleConfigurationDAO.class).getByServiceId((int) service.getId());
		} catch(Exception e) {
			System.err.println("Could not load configuration: " + e.getMessage());
		}

		for(String paramName : Constants.getAllParameterNames()) {
			ParameterEditPanelLayout editPanelLayout = new ParameterEditPanelLayout();
			editPanelLayout.label.setValue(paramName);
			editPanelLayout.lowerValueTextField.setValue(Constants.mapLower(configuration, paramName).toString());
			editPanelLayout.upperValueTextField.setValue(Constants.mapUpper(configuration, paramName).toString());
			parametersLayout.addComponent(editPanelLayout);
		}

		saveChangesButton.addClickListener(new Button.ClickListener() {
		   public void buttonClick(Button.ClickEvent event) {
			   Iterator<Component> parametersIterator = parametersLayout.iterator();
			   while (parametersIterator.hasNext()) {
				   ParameterEditPanelLayout layout = (ParameterEditPanelLayout) parametersIterator.next();
				   setValues(layout.label.getValue(), Double.parseDouble(layout.lowerValueTextField.getValue()), Double.parseDouble(layout.upperValueTextField.getValue()));
			   }
			   try {
				   MyUI.context.getBean(ConsoleConfigurationDAO.class).update(configuration);
			   } catch(Exception e) {
				   System.err.println("Could not update configuration");
				   e.printStackTrace();
			   }
		   }
	   });

	}


	public void setValues(String paramName, Double lower, Double upper) {
		if(paramName.equals(Constants.CPU_TEMP_LABEL)) {
			configuration.setCpuTempLowerBound(lower);
			configuration.setCpuTempUpperBound(upper);
		} else if(paramName.equals(Constants.CPU_USAGE_LABEL)) {
			configuration.setCpuLoadLowerBound(lower);
			configuration.setCpuLoadUpperBound(upper);
		} else if(paramName.equals(Constants.DISK_READ_LABEL)) {
			configuration.setDiskReadLowerBound(lower);
			configuration.setDiskReadUpperBound(upper);
		} else if(paramName.equals(Constants.DISK_WRITE_LABEL)) {
			configuration.setDiskWriteLowerBound(lower);
			configuration.setDiskWriteUpperBound(upper);
		} else if(paramName.equals(Constants.DISK_USAGE_LABEL)) {
			configuration.setDiskUsageLowerBound(lower);
			configuration.setDiskUsageUpperBound(upper);
		} else if(paramName.equals(Constants.MEMORY_USAGE_LABEL)) {
			configuration.setRamLowerBound(lower);
			configuration.setRamUpperBound(upper);
		} else if(paramName.equals(Constants.NETWORK_DOWNLOAD_LABEL)) {
			configuration.setNetworkDlLowerBound(lower);
			configuration.setNetworkDlUpperBound(upper);
		} else if(paramName.equals(Constants.NETWORK_UPLOAD_LABEL)) {
			configuration.setNetworkUlLowerBound(lower);
			configuration.setNetworkUlUpperBound(upper);
		} else {
			System.err.println("Invalid parameter: " + paramName);
		}
	}

	
	public ParameterEditPanelLayout getParameterEditPanel(String paramName) {
		Iterator<Component> iterator = parametersLayout.iterator();
		ParameterEditPanelLayout editPanelLayoutTemp;
		while (iterator.hasNext()) {
			editPanelLayoutTemp = (ParameterEditPanelLayout) iterator.next();
			if (editPanelLayoutTemp.label.getValue().equals(paramName)) {
				return editPanelLayoutTemp;
			}
		}
		return null;
	}

}
