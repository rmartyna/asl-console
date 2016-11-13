package asl.console.ui2;

import pl.edu.agh.beans.ConsoleConfiguration;

import java.util.ArrayList;

public class Constants {
	public static final String SERVER_NAME_LABEL = "Server name";
	
	// Parameters
	public static final String CPU_TEMP_LABEL = "CPU Temp.";
	public static final String CPU_USAGE_LABEL = "CPU Usage";
	public static final String MEMORY_USAGE_LABEL = "Memory Usage";
	public static final String NETWORK_DOWNLOAD_LABEL = "Network DL";
	public static final String NETWORK_UPLOAD_LABEL = "Network UL";
	public static final String DISK_READ_LABEL = "Disk Read";
	public static final String DISK_WRITE_LABEL = "Disk Write";
	public static final String DISK_USAGE_LABEL = "Disk Usage";
	
	public static ArrayList<String> getAllParameterNames () {
		ArrayList<String> paramNames = new ArrayList<>();
		paramNames.add(CPU_TEMP_LABEL);
		paramNames.add(CPU_USAGE_LABEL);
		paramNames.add(MEMORY_USAGE_LABEL);
		paramNames.add(NETWORK_DOWNLOAD_LABEL);
		paramNames.add(NETWORK_UPLOAD_LABEL);
		paramNames.add(DISK_READ_LABEL);
		paramNames.add(DISK_WRITE_LABEL);
		paramNames.add(DISK_USAGE_LABEL);
		return paramNames;
	}

	public static Double mapLower(ConsoleConfiguration consoleConfiguration, String paramName) {

		if(paramName.equals(Constants.CPU_TEMP_LABEL))
			return consoleConfiguration.getCpuTempLowerBound();
		else if(paramName.equals(Constants.CPU_USAGE_LABEL))
			return consoleConfiguration.getCpuLoadLowerBound();
		else if(paramName.equals(Constants.MEMORY_USAGE_LABEL))
			return consoleConfiguration.getRamLowerBound();
		else if(paramName.equals(Constants.NETWORK_DOWNLOAD_LABEL))
			return consoleConfiguration.getNetworkDlLowerBound();
		else if(paramName.equals(Constants.NETWORK_UPLOAD_LABEL))
			return consoleConfiguration.getNetworkUlLowerBound();
		else if(paramName.equals(Constants.DISK_READ_LABEL))
			return consoleConfiguration.getDiskReadLowerBound();
		else if(paramName.equals(Constants.DISK_WRITE_LABEL))
			return consoleConfiguration.getDiskWriteLowerBound();
		else if(paramName.equals(Constants.DISK_USAGE_LABEL))
			return consoleConfiguration.getDiskUsageLowerBound();
		else
			return 0.0;
	}

	public static Double mapUpper(ConsoleConfiguration consoleConfiguration, String paramName) {
		if(paramName.equals(Constants.CPU_TEMP_LABEL))
			return consoleConfiguration.getCpuTempUpperBound();
		else if(paramName.equals(Constants.CPU_USAGE_LABEL))
			return consoleConfiguration.getCpuLoadUpperBound();
		else if(paramName.equals(Constants.MEMORY_USAGE_LABEL))
			return consoleConfiguration.getRamUpperBound();
		else if(paramName.equals(Constants.NETWORK_DOWNLOAD_LABEL))
			return consoleConfiguration.getNetworkDlUpperBound();
		else if(paramName.equals(Constants.NETWORK_UPLOAD_LABEL))
			return consoleConfiguration.getNetworkUlUpperBound();
		else if(paramName.equals(Constants.DISK_READ_LABEL))
			return consoleConfiguration.getDiskReadUpperBound();
		else if(paramName.equals(Constants.DISK_WRITE_LABEL))
			return consoleConfiguration.getDiskWriteUpperBound();
		else if(paramName.equals(Constants.DISK_USAGE_LABEL))
			return consoleConfiguration.getDiskUsageUpperBound();
		else
			return 0.0;
	}

}
