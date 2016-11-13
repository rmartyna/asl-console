package asl.console.ui2;

import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import asl.console.ui2.windows.LogBrowserWindow;
import pl.edu.agh.beans.Service;
import pl.edu.agh.beans.ServiceConfiguration;
import pl.edu.agh.beans.SystemLog;
import pl.edu.agh.dao.ServiceConfigurationDAO;
import pl.edu.agh.dao.ServiceDAO;
import pl.edu.agh.dao.SystemLogsDAO;

import java.util.List;

@SuppressWarnings("serial")
public class SystemLogsView extends SystemLogsLayout {


	private List<Service> services;
	private Integer currentServiceId;

	public SystemLogsView() {
		pageTitleLabel.setValue("System Logs");
		setSizeFull();
		setResponsive(true);


		try {
			services = MyUI.context.getBean(ServiceDAO.class).listAll();
			for(Service service : services)
				selectServerComboBox.addItem(service.getDescription());
		} catch(Exception e) {
			System.err.println("Could not load services");
			e.printStackTrace();
			return;
		}


		selectServerComboBox.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
				String value = (String) selectServerComboBox.getValue();
				if(value == null || value.length() == 0)
					return;

				try {
					for(Service service : services)
						if(service.getDescription().equals(value)) {
							currentServiceId = (int) service.getId();
							ServiceConfiguration configuration = MyUI.context.getBean(ServiceConfigurationDAO.class).getByServiceId((int) service.getId());
							String[] logs = configuration.getSyslogList().split(",");
							selectLogFileComboBox.removeAllItems();
							for(String log : logs)
								selectLogFileComboBox.addItem(log);
						}
				} catch(Exception e) {
					System.err.println("Could not load logs");
					e.printStackTrace();
					return;
				}


			}
		});

		showLogFileButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

				if(currentServiceId != null) {
					String value = (String) selectLogFileComboBox.getValue();
					if(value == null || value.length() == 0)
						return;

					try {
						SystemLogsDAO systemLogsDAO = MyUI.context.getBean(SystemLogsDAO.class);
						int num = systemLogsDAO.getMaxFileNumber(currentServiceId, (String) selectLogFileComboBox.getValue());
						List<SystemLog> logs = systemLogsDAO.getSystemLogList(currentServiceId, (String) selectLogFileComboBox.getValue(), num);
						String text = "";
						for(SystemLog systemLog : logs)
							text += systemLog.getLog();

						UI.getCurrent().addWindow(new LogBrowserWindow(text));
					} catch(Exception e) {
						System.err.println("Could not load system log");
						e.printStackTrace();
						UI.getCurrent().addWindow(new LogBrowserWindow("Could not load system log"));
					}

				}
			}
		});
	}
}
