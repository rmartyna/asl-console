package asl.console.ui2.windows;

import com.vaadin.addon.charts.examples.timeline.SingleLineSeries;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

//Define a sub-window by inheritance
@SuppressWarnings("serial")
public class SubWindow extends Window {
	protected VerticalLayout content = new VerticalLayout();
	
	public SubWindow() {
		center();
		setWidth("80%");
		setHeight("80%");
		
		// Some basic content for the window
		content.setMargin(true);
		setContent(content);

		content.addComponent(new SingleLineSeries());
	}


}