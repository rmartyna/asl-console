package asl.console.ui2.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

//Define a sub-window by inheritance
@SuppressWarnings("serial")
public class AddNewServerResponseWindow extends Window {
	
	public AddNewServerResponseWindow(String msg) {
		super("Add new server response"); // Set window caption
		center();

		// Some basic content for the window
		VerticalLayout content = new VerticalLayout();
		content.addComponent(new Label(msg));
		content.setMargin(true);
		setContent(content);

		// Disable the close button
		//setClosable(false);

		// Trivial logic for closing the sub-window
		Button ok = new Button("OK");
		ok.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				close(); // Close the sub-window
			}
		});
		content.addComponent(ok);
	}
}