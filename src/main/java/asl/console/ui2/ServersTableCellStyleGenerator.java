package asl.console.ui2;

import com.vaadin.ui.Table;

public class ServersTableCellStyleGenerator implements Table.CellStyleGenerator {

	private static final long serialVersionUID = 1L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		
		if (propertyId == null)
	            return "";
		
		int row = ((Integer) itemId).intValue();
		Object cellValue = source.getItem(row).getItemProperty(propertyId).getValue();
		
		// add cell formatting rules here
		/*
		if (propertyId.equals(MyUI.TEMPERATURE)) {
			if((Double) cellValue > 70) {
				return "red";
			} else if ((Double) cellValue < 30) {
				return "green";
			}
		}
		*/
		return "";
	}

}
