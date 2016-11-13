package asl.console.ui2;


import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

import asl.console.ui2.windows.AddNewServerResponseWindow;
import pl.edu.agh.beans.ConsoleConfiguration;
import pl.edu.agh.beans.Service;
import pl.edu.agh.dao.ConsoleConfigurationDAO;
import pl.edu.agh.dao.ServiceDAO;

@SuppressWarnings("serial")
public class AddServerView extends AddServerLayout {
	public AddServerView() {
		addServerButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {

					Service service = new Service(ipTextField.getValue(), Integer.parseInt(portTextField.getValue()), serverNameTextField.getValue());

					MyUI.context.getBean(ServiceDAO.class).insert(service);

					service = MyUI.context.getBean(ServiceDAO.class).getByHostAndPort(ipTextField.getValue(), Integer.parseInt(portTextField.getValue()));

					ConsoleConfiguration consoleConfiguration = new ConsoleConfiguration();
					consoleConfiguration.setServiceId((int) service.getId());
					consoleConfiguration.setCpuLoadLowerBound(100.0);
					consoleConfiguration.setCpuLoadUpperBound(100.0);
					consoleConfiguration.setCpuTempLowerBound(100.0);
					consoleConfiguration.setCpuTempUpperBound(100.0);
					consoleConfiguration.setRamLowerBound(10.0);
					consoleConfiguration.setRamUpperBound(10.0);
					consoleConfiguration.setNetworkDlLowerBound(1000.0);
					consoleConfiguration.setNetworkDlUpperBound(1000.0);
					consoleConfiguration.setNetworkUlLowerBound(1000.0);
					consoleConfiguration.setNetworkUlUpperBound(1000.0);
					consoleConfiguration.setDiskReadLowerBound(1000.0);
					consoleConfiguration.setDiskReadUpperBound(1000.0);
					consoleConfiguration.setDiskWriteLowerBound(1000.0);
					consoleConfiguration.setDiskWriteUpperBound(1000.0);
					consoleConfiguration.setDiskUsageLowerBound(1000.0);
					consoleConfiguration.setDiskUsageUpperBound(1000.0);

					MyUI.context.getBean(ConsoleConfigurationDAO.class).insert(consoleConfiguration);

					UI.getCurrent().addWindow(new AddNewServerResponseWindow(service.toString()));;

				} catch (Exception e) {
					UI.getCurrent().addWindow(new AddNewServerResponseWindow("Could not add server"));
					System.err.println("Error adding server");
					e.printStackTrace();
				}
			}
		});

		pageTitleLabel.setValue("Add new server");
		setSizeFull();
		setResponsive(true);
	}

}
