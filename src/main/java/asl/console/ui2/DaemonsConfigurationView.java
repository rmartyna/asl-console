package asl.console.ui2;

import java.util.Iterator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class DaemonsConfigurationView extends DaemonsConfigurationLayout {

	private SelectParametersView selectParametersView;
	private SelectServersView selectServersView;
	
	public DaemonsConfigurationView() {
		
		pageTitleLabel.setValue("Daemon Configuration for Servers");
		selectParametersView = new SelectParametersView();
		selectParametersWrapperLayout.addComponent(selectParametersView);
		//selectServersView = new SelectServersView();
		//selectServersWrapperLayout.addComponent(selectServersView);
		
		setWidth(null);
		setHeight(null);

		// Marked checkeboxes on startup
		selectParametersView.setAllCheckBoxesChecked();
		
		// Add panels here
		DaemonsConfigurationPanelView panel1 = new DaemonsConfigurationPanelView();
		DaemonsConfigurationPanelView panel2 = new DaemonsConfigurationPanelView();

		panel1.serverDescription.setValue("Server 1");
		panel2.serverDescription.setValue("Server 2");


		daemonsConfigurationPanels.addComponent(panel1);
		daemonsConfigurationPanels.addComponent(panel2);
		
		setClickListenersForCheckBoxes();
	}

	public void setClickListenersForCheckBoxes() {
		for (CheckBox checkBox : selectParametersView.getCheckBoxes()) {
			checkBox.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (checkBox.getValue()) {
						Iterator<Component> iterator = daemonsConfigurationPanels.iterator();
						DaemonsConfigurationPanelView confPanel;
						while (iterator.hasNext()) {
							confPanel = (DaemonsConfigurationPanelView) iterator.next();
							confPanel.getDaemonParameterEditPanel(checkBox.getCaption()).setVisible(true);
						}
					} else {
						Iterator<Component> iterator = daemonsConfigurationPanels.iterator();
						DaemonsConfigurationPanelView confPanel;
						while (iterator.hasNext()) {
							confPanel = (DaemonsConfigurationPanelView) iterator.next();
							confPanel.getDaemonParameterEditPanel(checkBox.getCaption()).setVisible(false);
						}
					}

				}
			});
		}
	}
}
