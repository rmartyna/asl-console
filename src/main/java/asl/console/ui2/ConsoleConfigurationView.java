package asl.console.ui2;

import java.util.Iterator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import pl.edu.agh.beans.Service;
import pl.edu.agh.dao.ServiceDAO;

@SuppressWarnings("serial")
public class ConsoleConfigurationView extends ConsoleConfigurationLayout {

	private SelectParametersView selectParametersView;

	public ConsoleConfigurationView() {
		pageTitleLabel.setValue("Console Configuration for Servers");
		selectParametersView = new SelectParametersView();
		selectParametersWrapperLayout.addComponent(selectParametersView);
		setWidth(null);
		setHeight(null);

		// Marked checkeboxes on startup
		selectParametersView.setAllCheckBoxesChecked();
		
		// Add panels here
		try {
			for(Service service : MyUI.context.getBean(ServiceDAO.class).listAll()) {
				ConsoleConfigurationPanelView panel = new ConsoleConfigurationPanelView(service);
				consoleConfigurationPanels.addComponent(panel);
			}
		} catch(Exception e) {
			System.err.println("Could not add rows: " + e.getMessage());
		}

		// Must be last call in this method
		setClickListenersForCheckBoxes();
	}
	
	public void setClickListenersForCheckBoxes() {
		for (CheckBox checkBox : selectParametersView.getCheckBoxes()) {
			checkBox.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (checkBox.getValue()) {
						Iterator<Component> iterator = consoleConfigurationPanels.iterator();
						ConsoleConfigurationPanelView confPanel;
						while (iterator.hasNext()) {
							confPanel = (ConsoleConfigurationPanelView) iterator.next();
							confPanel.getParameterEditPanel(checkBox.getCaption()).setVisible(true);
						}
					} else {
						Iterator<Component> iterator = consoleConfigurationPanels.iterator();
						ConsoleConfigurationPanelView confPanel;
						while (iterator.hasNext()) {
							confPanel = (ConsoleConfigurationPanelView) iterator.next();
							confPanel.getParameterEditPanel(checkBox.getCaption()).setVisible(false);
						}
					}

				}
			});
		}
	}
}
