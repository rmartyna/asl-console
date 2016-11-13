package asl.console.ui2.windows;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class LogBrowserWindow extends Window {
	public LogBrowserWindow(String logFile) {
		
		setWidth("80%");
		setHeight("80%");
		center();
		
		VerticalLayout content = new VerticalLayout();

		Label logsLabel = new Label(logFile, ContentMode.PREFORMATTED);
		logsLabel.setStyleName("logs");
		content.addComponent(logsLabel);
		content.setMargin(true);
		setContent(content);
	}
}
