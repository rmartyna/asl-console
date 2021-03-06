package asl.console.ui2;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class MainWindow extends HorizontalLayout {
	protected CssLayout menuCssLayout;
	protected CssLayout mainMenu;
	protected Button aslHomeButton;
	protected Button serversStateButton;
	protected Button addServerButton;
	protected Button removeServerButton;
	protected Button consoleConfigurationButton;
	protected Button systemLogsButton;
	protected Button daemonsConfigurationButton;
	protected Panel contentPanel;
	protected VerticalLayout contentVerticalLayout;

	public MainWindow() {
		Design.read(this);
	}
}
