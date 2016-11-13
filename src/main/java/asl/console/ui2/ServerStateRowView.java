package asl.console.ui2;

import java.util.Iterator;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class ServerStateRowView extends ServerStateRowLayout {

	public OpenSubWindowButton getStateParameterButton(String paramName) {
		Iterator<Component> iterator = serverParametersLayout.iterator();
		while (iterator.hasNext()) {
			OpenSubWindowButton button = (OpenSubWindowButton) iterator.next();
			if (button.getStateParameterName().equals(paramName)) {
				return button;
			}
		}
		return null;
	}
}
