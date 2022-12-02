package app.machines.view.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import app.machines.model.Machine;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class DataReportFill {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DataReportFill(Machine machine) {
		
		
		JasperDesign desenho;
		try {
			desenho = JRXmlLoader.load(getClass().getResourceAsStream("/report/Data.jrxml"));

			JasperReport relatorio = JasperCompileManager.compileReport( desenho );
			
			Map parameters = new HashMap();
			parameters.put("Machine_Id_Receved", machine.getId());
			
			Connection  conexao = DriverManager.getConnection("jdbc:mysql://localhost/machine", "root", "");
	    	JasperPrint impressao  = JasperFillManager.fillReport(relatorio, parameters, conexao);
	    	
	    	impressao.setName("DataReport");
	    	
	    	JasperExportManager.exportReportToPdfFile(impressao, "C://Users/leand/Downloads");
	    	
		} catch (JRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
