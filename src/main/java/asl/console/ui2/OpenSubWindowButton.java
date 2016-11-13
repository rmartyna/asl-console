package asl.console.ui2;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class OpenSubWindowButton extends Button{

	private String stateParameterName;
	
	public OpenSubWindowButton(String caption, String stateParameterName, Window subWindow, Class viewClass){

		this.stateParameterName = stateParameterName;
		setCaption(caption);
		
		addClickListener(new ClickListener() {
		    public void buttonClick(ClickEvent event) {
				MyUI.mainWindow.contentVerticalLayout.removeAllComponents();				
				try {
					MyUI.mainWindow.contentVerticalLayout.addComponent((Component) viewClass.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
		    	// check if this window is already opened
		    	for (Window window : UI.getCurrent().getWindows())
		    	{
		    		if (window == subWindow)
		    			return;
		    	}
		        // Add it to the root component if window doesn't exist
		    	UI.getCurrent().addWindow(subWindow);
		    }
		});
		
		
	}
		
	public String getStateParameterName() {
		return stateParameterName;
	}

	public void setStateParameterName(String stateParameterName) {
		this.stateParameterName = stateParameterName;
	}
	
	
}
