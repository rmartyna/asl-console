package asl.console.ui2;

import java.util.ArrayList;
import java.util.Iterator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class SelectParametersView extends SelectParametersLayout {

	private ArrayList<CheckBox> checkBoxes;

	public SelectParametersView() {
		ArrayList<String> parameterNames = Constants.getAllParameterNames();
		checkBoxes = new ArrayList<>();
		for (String paramName : parameterNames) {
			CheckBox checkBox = new CheckBox(paramName);
			checkBoxes.add(checkBox);
			selectParametersLayout.addComponent((Component) checkBox);
		}
	}

	public CheckBox getCheckBoxByCaption(String caption) {
		for (CheckBox checkBox : checkBoxes) {
			if (checkBox.getCaption().equals(caption)) {
				return checkBox;
			}
		}
		return null;
	}

	void setAllCheckBoxesChecked() {
		for (String paramName : Constants.getAllParameterNames()) {
			getCheckBoxByCaption(paramName).setValue(true);
		}
	}

	public ArrayList<CheckBox> getCheckBoxes() {
		return checkBoxes;
	}

	public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
		this.checkBoxes = checkBoxes;
	}

}
