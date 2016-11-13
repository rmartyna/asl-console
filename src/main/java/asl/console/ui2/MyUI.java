package asl.console.ui2;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mytheme")
@Widgetset("asl.console.ui2.MyAppWidgetset")
public class MyUI extends UI {

	final static MainWindow mainWindow = new MainWindow();

	public static ApplicationContext context;

	@Override 
	protected void init(VaadinRequest vaadinRequest) {

		if(context == null)
			context = new ClassPathXmlApplicationContext(
				"classpath*:**/asl-console-backend-application-context.xml");

		mainWindow.serversStateButton.addClickListener(createClickListenerForView(ServersStateView.class));	
		mainWindow.addServerButton.addClickListener(createClickListenerForView(AddServerView.class));
		mainWindow.removeServerButton.addClickListener(createClickListenerForView(RemoveServerView.class));
		mainWindow.systemLogsButton.addClickListener(createClickListenerForView(SystemLogsView.class));
		mainWindow.consoleConfigurationButton.addClickListener(createClickListenerForView(ConsoleConfigurationView.class));
		mainWindow.daemonsConfigurationButton.addClickListener(createClickListenerForView(DaemonsConfigurationView.class));
		
		mainWindow.contentPanel.setSizeFull();
		mainWindow.contentVerticalLayout.addComponent(new ServersStateView());
		setContent(mainWindow);
	}
	
	@SuppressWarnings("rawtypes")
	private Button.ClickListener createClickListenerForView(Class c) {
		return new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.contentVerticalLayout.removeAllComponents();				
				try {
					mainWindow.contentVerticalLayout.addComponent((Component) c.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

}
