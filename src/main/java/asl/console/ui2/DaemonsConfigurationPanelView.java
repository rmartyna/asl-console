package asl.console.ui2;

import java.util.Iterator;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class DaemonsConfigurationPanelView extends DaemonsConfigurationPanelLayout{

	public DaemonsConfigurationPanelView() {
		for(String paramName : Constants.getAllParameterNames()) {
			DaemonParameterEditPanelView editPanelView = new DaemonParameterEditPanelView();
			editPanelView.label.setValue(paramName);
			
			// Add daemon parameters here...
			TextField param1 = new TextField();
			param1.setWidth("100px");
			param1.setCaption("Pram1:");
			param1.setValue("Value1");
			
			TextField param2 = new TextField();
			param2.setWidth("100px");
			param2.setCaption("Pram2:");
			param2.setValue("Value2");
			
			editPanelView.addComponent(param1);
			editPanelView.addComponent(param2);
			
			daemonsLayout.addComponent(editPanelView);
			
		}

		//saveChangesButton.addClickListener
		
	}

	
	public DaemonParameterEditPanelView getDaemonParameterEditPanel(String paramName) {
		Iterator<Component> iterator = daemonsLayout.iterator();
		DaemonParameterEditPanelView editPanelLayoutTemp;
		while (iterator.hasNext()) {
			editPanelLayoutTemp = (DaemonParameterEditPanelView) iterator.next();
			if (editPanelLayoutTemp.label.getValue().equals(paramName)) {
				return editPanelLayoutTemp;
			}
		}
		return null;
	}
}
