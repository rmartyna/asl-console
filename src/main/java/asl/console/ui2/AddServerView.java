package asl.console.ui2;


import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

import asl.console.ui2.windows.AddNewServerResponseWindow;
import pl.edu.agh.beans.ConsoleConfiguration;
import pl.edu.agh.beans.Service;
import pl.edu.agh.beans.ServiceConfiguration;
import pl.edu.agh.dao.ConsoleConfigurationDAO;
import pl.edu.agh.dao.ServiceConfigurationDAO;
import pl.edu.agh.dao.ServiceDAO;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Formatter;

@SuppressWarnings("serial")
public class AddServerView extends AddServerLayout {
	public AddServerView() {
		addServerButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {

					Service service = new Service(ipTextField.getValue(), Integer.parseInt(portTextField.getValue()), serverNameTextField.getValue(), getPasswordHash("aqq123"));

					MyUI.context.getBean(ServiceDAO.class).insert(service);

					service = MyUI.context.getBean(ServiceDAO.class).getByHostAndPort(ipTextField.getValue(), Integer.parseInt(portTextField.getValue()));

					addConsoleConfiguration(service.getId());
					addServiceConfiguration(service.getId());


					UI.getCurrent().addWindow(new AddNewServerResponseWindow(service.toString()));

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

	private void addConsoleConfiguration(long serviceId) throws SQLException {
		ConsoleConfiguration consoleConfiguration = new ConsoleConfiguration();
		consoleConfiguration.setServiceId((int) serviceId);
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
	}

	private void addServiceConfiguration(long serviceId) throws SQLException {
		ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
		serviceConfiguration.setServiceId(serviceId);
		serviceConfiguration.setMode("push");
		serviceConfiguration.setPollRate(60000);
		serviceConfiguration.setCpuEnabled(1);
		serviceConfiguration.setCpuFrequency(60000);
		serviceConfiguration.setMemoryEnabled(1);
		serviceConfiguration.setMemoryFrequency(60000);
		serviceConfiguration.setNetworkEnabled(1);
		serviceConfiguration.setNetworkFrequency(60000);
		serviceConfiguration.setDiskEnabled(1);
		serviceConfiguration.setDiskFrequency(60000);
		serviceConfiguration.setSyslogEnabled(1);
		serviceConfiguration.setSyslogFrequency(60000);
		serviceConfiguration.setSyslogList("");

		MyUI.context.getBean(ServiceConfigurationDAO.class).insert(serviceConfiguration);
	}

	private String getPasswordHash(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			return byteToHex(digest.digest(password.getBytes("UTF-8")));
		} catch(Exception e) {
			throw new RuntimeException("Could not generate digest", e);
		}
	}

	private static String byteToHex(byte[] digest) {
		Formatter formatter = new Formatter();
		for(byte b : digest) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
