package asl.console.ui2;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

import asl.console.ui2.windows.AddNewServerResponseWindow;
import pl.edu.agh.beans.Service;
import pl.edu.agh.dao.ServiceDAO;

import java.util.List;

@SuppressWarnings("serial")
public class RemoveServerView extends RemoveServerLayout {

	public RemoveServerView() {
		loadValues();
		createServersList();
		pageTitleLabel.setValue("Remove server");
		setSizeFull();
		setResponsive(true);
		setImmediate(true);
	}

	private void createServersList() {


		removeServerButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String serverName = (String) serversComboBox.getValue();

				try {
					List<Service> services = MyUI.context.getBean(ServiceDAO.class).listAll();
					for(Service service : services) {
						if(service.getDescription().equals(serversComboBox.getValue())) {
							MyUI.context.getBean(ServiceDAO.class).delete(service);
							break;
						}
					}
					UI.getCurrent().addWindow(new AddNewServerResponseWindow("Server removed"));
				} catch(Exception e) {
					UI.getCurrent().addWindow(new AddNewServerResponseWindow("Could n"));
					System.err.println("Could not load services");
					e.printStackTrace();
				}



			}
		});
	}

	private void loadValues() {

		try {
			List<Service> services = MyUI.context.getBean(ServiceDAO.class).listAll();
			for(Service service : services)
				serversComboBox.addItem(service.getDescription());
		} catch(Exception e) {
			System.err.println("Could not load services");
			e.printStackTrace();
		}

	}
}
