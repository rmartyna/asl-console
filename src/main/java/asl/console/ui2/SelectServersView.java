package asl.console.ui2;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.CheckBox;
import pl.edu.agh.beans.Service;
import pl.edu.agh.dao.ServiceDAO;

@SuppressWarnings("serial")
public class SelectServersView extends SelectServersLayout {

	ArrayList<CheckBox> checkBoxes;
	
	public SelectServersView() {
		
		checkBoxes = new ArrayList<>();

		try {
			List<Service> serviceList = MyUI.context.getBean(ServiceDAO.class).listAll();

			for (Service service : serviceList) {
				System.out.println("Adding checkbox service: " + service);
				CheckBox checkBox = new CheckBox();
				checkBox.setCaption(service.getDescription());
				addComponent(checkBox);
				checkBoxes.add(checkBox);
			}
		} catch(Exception e) {
			System.err.println("Error showing servers");
			e.printStackTrace();
		}



	}

	public ArrayList<CheckBox> getCheckBoxes() {
		return checkBoxes;
	}

	public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
		this.checkBoxes = checkBoxes;
	}
	
	void setAllCheckBoxesChecked() {
		for(CheckBox checkBox : checkBoxes) {
			checkBox.setValue(true);
		}
	}

}
