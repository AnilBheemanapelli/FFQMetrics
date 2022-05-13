package com.ffq.metrics.FFQMetrics.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ffq.metrics.FFQMetrics.Model.Metricvalues;


@Controller
public class MetricsController {
	/*
	 * @RequestMapping("/") public String welcome(Map<String, Metricvalues> map) {
	 * //ModelAndView model = new ModelAndView("/metrics"); Metricvalues values =
	 * new Metricvalues(); map.put("metrics", values); return "metrics"; }
	 */
	@RequestMapping(value = { "/showmetrics" })
	public ModelAndView showMetricsPage(Map<String, Metricvalues> map) {
		System.out.println("showMetricsPage");
		ModelAndView model = new ModelAndView("/metrics");

		List<String> stateList = new ArrayList<String>();
		stateList.add("All States");
		stateList.add("PLA States");
		stateList.add("GWPC States");
		stateList.add("EE States");
		//stateList.add("Mississippi(MS)");
		//stateList.add("Arizona(AZ)");

		model.addObject("stateList", stateList);

		Metricvalues values = new Metricvalues();
		map.put("metrics", values);
		return model;

	}

	@RequestMapping(value = { "/addmetrics" })
	public ModelAndView AddMetrics(@ModelAttribute(value = "metrics") Metricvalues values, HttpServletRequest req) {
		System.out.println("Addmetrics ContactPage");
		ModelAndView model = new ModelAndView("/metrics");
		String FromDate = values.getFromDateAndTime();
		String ToDate = values.getToDateAndTime();
		String GetState = values.getStates();
		String AutoLob = "%A%";
		String HomeLobCond = "%H%";
		String RenterLobCond = "%R%";
		String CondoLobCond = "%C%";
		String BusinessLobCond = "%B%";
		String LifeLobCond = "%L%' OR LOB like '%G%' OR LOB like '%T%' OR LOB like '%S%' OR LOB like '%P%' OR LOB like '%Z%";
		String BindLOBAuto = " and b.lob like '%A%'";
		String BindLOBRenter = " and b.lob like '%R%'";
		String IID = " and IID_KO_AUTO IS NULL";
		String IIDKO = " and IID_KO_AUTO IS not NULL";
		String updateState = null;
		String Status = null;
		String HQMState = null;
		String HomeLegacy = null;
		if (GetState.equals("PLA States")) {
			updateState=" not in ('AZ','FL','IL','IA','OH','OK','PA','WA','KY','TX','MS','CO','IN','KS','MD','MN','MO','NV','TX','MA','LA','UT')";	
			Status="PLA";
			HQMState=" in ('AL','AR','CT','MI','MT','ND','CA','SD','VA')";
			HomeLegacy=" in ('GA','ID','NE','NJ','NM','NY','OR','WI','WY','TN')";

		}

		else if (GetState.equals("GWPC States")) {

			updateState=" in ('AZ','FL','IL','IA','OH','OK','PA','WA','KY','TX','MS','CO','IN','KS','MD','MN','MO','NV','TX','MA','LA','UT')";
			Status="GWPC";
			HQMState=" in ('KY','MS','AZ','LA','MA','CO','IN','KS','WA','TX','OK','OH','MO','MN','MD')";
			HomeLegacy=" in ('FL','UT','IL','IA')";
		}

		else if (GetState.equals("EE States")) {
			
			updateState=" in ('KY','MS','LA','MA','NC')";
			Status="Estern Expansion";
			HQMState=" in ('KY','MS','LA','MA','NC')";
			HomeLegacy=" in ('KY','MS','LA','MA','NC')";
			
		} else if (GetState.equals("All States")) {

			updateState = " in ('AL','AR','AZ','CA','CO','CT','FL','GA','IA','ID','IL','IN','KS','KY','MD','MI','MN','MO','MS','MT','ND','NE','NJ','NM','NV','NY','OH','OK','OR','PA','SD','TN','TX','UT','VA','WA','WI','WY','LA','MA','NC')";
			Status = "All";
			HQMState = " in ('AL','AR','CT','MI','MT','ND','CA','SD','VA','KY','MS','AZ','LA','MA','CO','IN','KS','WA','TX','OK','OH','MO','MN','MD')";
			HomeLegacy = " not in ('AL','AR','CT','MI','MT','ND','CA','SD','VA','KY','MS','AZ','LA','MA','CO','IN','KS','WA','TX','OK','OH','MO','MN','MD')";

		}

		/*
		 * else if (GetState.equals("Vlad adhoc")) {
		 * 
		 * updateState = " in ('MO','IN','CO','MD','NV','MN','KS')"; Status =
		 * "(Vlad Adhoc)"; HQMState = " in ('MO','IN','CO','MD','NV','MN','KS')";
		 * HomeLegacy = " in ('MO','IN','CO','MD','NV','MN','KS')";
		 * 
		 * } else if (GetState.equals("Mississippi(MS)")) {
		 * 
		 * updateState = " in ('MS')"; Status = "(MS State)"; HQMState = " in ('MS')";
		 * HomeLegacy = " in ('MS')";
		 * 
		 * } else if (GetState.equals("Arizona(AZ)")) {
		 * 
		 * updateState = " in ('AZ')"; Status = "(AZ State)"; HQMState = " in ('AZ')";
		 * HomeLegacy = " in ('AZ')";
		 * 
		 * }
		 * 
		 * else if (GetState.equals("FL")) {
		 * 
		 * updateState = " in ('FL')"; Status = "FL"; HQMState = " in ('FL')";
		 * HomeLegacy = " in ('FL')"; } else if (GetState.equals("WA")) {
		 * 
		 * updateState = " in ('WA')"; Status = "WA"; HQMState = " in ('WA')";
		 * HomeLegacy = " in ('WA')"; } else if (GetState.equals("NJ")) {
		 * 
		 * updateState = " in ('NJ')"; Status = "NJ"; HQMState = " in ('NJ')";
		 * HomeLegacy = " in ('NJ')"; }
		 */
		System.out.println(updateState);

		// New query
		String CommonCondLife = "select count(*) FROM TQUOTE where (lob like '";
		// Common
		String CommonCond = "select count(*) FROM TQUOTE where lob like ('";
		String AutoLobCond = "%A%";
		String TimeCond = "') and QUOTE_INIT_TMSTMP between '" + FromDate + "'" + " and '" + ToDate + "'";
		String StateCond = " and state" + updateState;
		String HQMStateCond = " and state" + HQMState;
		String LegacyHome = " and state" + HomeLegacy;
		// String GWHome=" and state" +HomeGWLegacy;

		String EmailElimination = " and ( (LOWER(EMAIL) NOT LIKE '%autobindoptout%' AND LOWER(EMAIL) NOT LIKE '%autobind%' AND LOWER(EMAIL) NOT LIKE '%asdf%' AND LOWER(EMAIL) NOT LIKE '%paul.hoogveld%' AND LOWER(EMAIL) <> 'abc123@gmail.com' AND LOWER(EMAIL) <> 'ffq.testing@yahoo.com') OR EMAIL IS NULL OR email = '')";

		// Auto Cond
		String FarmersCompanyCode = " and (AUTO_COMPANY_CODE is NULL or AUTO_COMPANY_CODE = 'F')";
		String BWCompanyCode = " and AUTO_COMPANY_CODE = 'B'";

		// Both-Auto & BW Cond
		String AutoCompletedCond = " and AUTO_PREMIUM_AMT>0 and (PSEUDO_QUOTE_AUTO IS NULL OR PSEUDO_QUOTE_AUTO <> 'Y')";

		// Home completed cond
		String HomeCompletedCond = " AND (PSEUDO_QUOTE_FIRE IS NULL OR PSEUDO_QUOTE_FIRE <> 'Y') and HOME_PREMIUM_AMT>0";

		// Business completed Cond
		String BusinessCompletedCond = " and PAGE_DROPOFF = 'Boss_ThankYou'";

		// Life Completed Cond
		String LifeCompleteCond = " and (LIFE_PREMIUM_AMT > 0 OR STL_PREMIUM_AMT > 0 OR SWL_PREMIUM_AMT > 0 OR GDB_PREMIUM_AMT > 0)";

		// HQMCompletedCond
		String HQMCompletedCond = " and QUOTE_DATA like '%true%'";

		// OnlineBind
		String CommonBindCond = "select count(*)from ffq3prd.ttk_rpt_exec_summary a, ffq3prd.tquote b where";
		String TimeCondBind = " a.bind_tmstmp between '" + FromDate + "'" + " and '" + ToDate + "'";
		String OnlineBindCond = "and a.quote_number = b.quote_number AND A.REMARK_CD = 'FO' and A.LINE_OF_BUS_CD <> 'R' and  b.";
		String StateCondBind = "state" + updateState;
		// add emailElimination
		String FarmersCompanyCodeBind = " and (b.AUTO_COMPANY_CODE is NULL or b.AUTO_COMPANY_CODE = 'F')";
		String KOAuto = " and PSEUDO_QUOTE_AUTO = 'Y' and AUTO_COMPANY_CODE='F'";
		String KOBW = " and PSEUDO_QUOTE_AUTO = 'Y' and AUTO_COMPANY_CODE='B'";
		String KOHome = " and PSEUDO_QUOTE_FIRE = 'Y'";
		String BWCompanyCodeBind = " and b.AUTO_COMPANY_CODE = 'B'";
		String OnlineBindCondRenter = " and a.quote_number = b.quote_number and A.LINE_OF_BUS_CD='R' and A.QUOTE_NUMBER IS NOT NULL and b.";

		// OfflineBind
		String CommonBindOffCond = "select count(*) from ffq3prd.tpcn_quote a, ffq3prd.tquote b where";
		String TimeCondOffBind = " a.quote_init_tmstmp between '" + FromDate + "'" + " and '" + ToDate + "'";
		String OfflineBindCond = " AND a.quote_number = b.quote_number";
		String OffAuto = " AND b.lob LIKE '%A%' and b.";
		String OffRenter = " AND b.lob LIKE '%R%' and b.";
		// use StateCondBind
		String OffAutoCompanyCode = " AND b.AUTO_COMPANY_CODE = 'F'";
		String OffBWCompanyCode = " AND b.AUTO_COMPANY_CODE = 'B'";
		// add emailelimination

		// Verified quote

		String AutoVerify = " and VERIFIED_OAB_TMSTP IS NOT NULL";
		String HomeVerify = " and VERIFIED_OFB_TMSTMP IS NOT NULL";

		// Acxiom

		String CommAcx = " AND ACX_TMSTMP IS NOT NULL";

		// New Query end

		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection con = DriverManager.getConnection("jdbc:db2://LAAPDB18:50018/FFQ3PRD", "ffq3prd", "ffq3prd$");
			Statement myst = con.createStatement();
			// String finalQuery1 = sqlPart1 + "'" + FromDate + "'" + " and '" + ToDate
			// +"'"+a1+a2+a3+a4+a5+a6+a7+AutoCond;

			// Auto query started, completed

			String NewQuery1 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + FarmersCompanyCode
					+ IID;
			System.out.println(NewQuery1);
			String cust1 = null;
			ResultSet myrs1 = myst.executeQuery(NewQuery1);
			while (myrs1.next()) {
				cust1 = myrs1.getString(1);
				System.out.println("Auto Quote Started : " + cust1);
			}
			String AutoStarted = cust1;

			String NewQuery2 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + FarmersCompanyCode
					+ AutoCompletedCond;
			System.out.println(NewQuery2);
			String cust2 = null;
			ResultSet myrs2 = myst.executeQuery(NewQuery2);
			while (myrs2.next()) {
				cust2 = myrs2.getString(1);
				System.out.println("Auto Quote Completed : " + cust2);
			}

			String AutoCompleted = cust2;

			String NewQuery15 = CommonBindCond + TimeCondBind + OnlineBindCond + StateCondBind + EmailElimination
					+ FarmersCompanyCodeBind + BindLOBAuto;
			System.out.println(NewQuery15);
			String cust15 = null;
			ResultSet myrs15 = myst.executeQuery(NewQuery15);
			while (myrs15.next()) {
				cust15 = myrs15.getString(1);
				System.out.println(" Auto Quote Online Bind : " + cust15);
			}
			String AutoOnlineBind = cust15;

			String NewQuery35 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + FarmersCompanyCode
					+ IID + AutoVerify;
			System.out.println(NewQuery35);
			String cust35 = null;
			ResultSet myrs35 = myst.executeQuery(NewQuery35);
			while (myrs35.next()) {
				cust35 = myrs35.getString(1);
				System.out.println("Auto Quote Verified : " + cust35);
			}
			String AutoVerified = cust35;

			/*
			 * //OfflineBind String
			 * CommonBindOffCond="select count(*) from ffq3prd.tpcn_quote a, ffq3prd.tquote b where"
			 * ; String TimeCondOffBind=" a.quote_init_tmstmp between '"+FromDate+ "'" +
			 * " and '" + ToDate +"'"; String
			 * OfflineBindCond=" AND a.quote_number = b.quote_number" String
			 * OffAuto;=" AND b.lob LIKE '%A%' and b."; String
			 * OffRenter=" AND b.lob LIKE '%R%' and b."; //use StateCondBind String
			 * OffAutoCompanyCode=" AND b.AUTO_COMPANY_CODE = 'F'"; String
			 * OffBWCompanyCode=" AND b.AUTO_COMPANY_CODE = 'B'"; //add emailelimination
			 */

			String NewQuery18 = CommonBindOffCond + TimeCondOffBind + OfflineBindCond + OffAuto + StateCondBind
					+ OffAutoCompanyCode + EmailElimination;
			System.out.println(NewQuery18);
			String cust18 = null;
			ResultSet myrs18 = myst.executeQuery(NewQuery18);
			while (myrs18.next()) {
				cust18 = myrs18.getString(1);
				System.out.println(" Auto Quote Offline Bind : " + cust18);
			}

			String AutoOfflineBind = cust18;

			String NewQuery21 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + KOAuto + IID;
			System.out.println(NewQuery21);
			String cust21 = null;
			ResultSet myrs21 = myst.executeQuery(NewQuery21);
			while (myrs21.next()) {
				cust21 = myrs21.getString(1);
				System.out.println("Auto Quote KO : " + cust21);
			}
			String AutoKO = cust21;

			String NewQuery26 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + IIDKO;
			System.out.println(NewQuery26);
			String cust26 = null;
			ResultSet myrs26 = myst.executeQuery(NewQuery26);
			while (myrs26.next()) {
				cust26 = myrs26.getString(1);
				System.out.println("Auto Quote KO : " + cust26);
			}
			String AutoIIDKO = cust26;

			String NewQuery38 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + FarmersCompanyCode
					+ IID + CommAcx;
			System.out.println(NewQuery38);
			String cust38 = null;
			ResultSet myrs38 = myst.executeQuery(NewQuery38);
			while (myrs38.next()) {
				cust38 = myrs38.getString(1);
				System.out.println("Auto Quote Acxiom : " + cust38);
			}
			String AutoAcx = cust38;

			// BW query started, completed, online bind, offline bind

			String NewQuery3 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + BWCompanyCode;
			System.out.println(NewQuery3);
			String cust3 = null;
			ResultSet myrs3 = myst.executeQuery(NewQuery3);
			while (myrs3.next()) {
				cust3 = myrs3.getString(1);
				System.out.println("BW Auto Quote Started : " + cust3);
			}
			String BWAutoStarted = cust3;

			String NewQuery4 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + BWCompanyCode
					+ AutoCompletedCond;
			System.out.println(NewQuery4);
			String cust4 = null;
			ResultSet myrs4 = myst.executeQuery(NewQuery4);
			while (myrs4.next()) {
				cust4 = myrs4.getString(1);
				System.out.println("BW Auto Quote Completed : " + cust4);
			}

			String BWAutoCompleted = cust4;

			String NewQuery16 = CommonBindCond + TimeCondBind + OnlineBindCond + StateCondBind + EmailElimination
					+ BWCompanyCodeBind + BindLOBAuto;
			System.out.println(NewQuery16);
			String cust16 = null;
			ResultSet myrs16 = myst.executeQuery(NewQuery16);
			while (myrs16.next()) {
				cust16 = myrs16.getString(1);
				System.out.println("BW Auto Online Bind : " + cust16);
			}

			String BWOnlineBind = cust16;

			String NewQuery19 = CommonBindOffCond + TimeCondOffBind + OfflineBindCond + OffAuto + StateCondBind
					+ OffBWCompanyCode + EmailElimination;
			System.out.println(NewQuery19);
			String cust19 = null;
			ResultSet myrs19 = myst.executeQuery(NewQuery19);
			while (myrs19.next()) {
				cust19 = myrs19.getString(1);
				System.out.println("BW Auto Offline Bind : " + cust19);
			}

			String BWOfflineBind = cust19;

			String NewQuery22 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + KOBW;
			System.out.println(NewQuery22);
			String cust22 = null;
			ResultSet myrs22 = myst.executeQuery(NewQuery22);
			while (myrs22.next()) {
				cust22 = myrs22.getString(1);
				System.out.println("BW Auto Quote KO : " + cust22);
			}
			String BWKO = cust22;

			String NewQuery36 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + BWCompanyCode
					+ AutoVerify;
			System.out.println(NewQuery36);
			String cust36 = null;
			ResultSet myrs36 = myst.executeQuery(NewQuery36);
			while (myrs36.next()) {
				cust36 = myrs36.getString(1);
				System.out.println("BW Auto Quote Verified : " + cust36);
			}
			String BWAutoVerified = cust36;

			String NewQuery39 = CommonCond + AutoLobCond + TimeCond + StateCond + EmailElimination + BWCompanyCode
					+ CommAcx;
			System.out.println(NewQuery39);
			String cust39 = null;
			ResultSet myrs39 = myst.executeQuery(NewQuery39);
			while (myrs39.next()) {
				cust39 = myrs39.getString(1);
				System.out.println("BW Auto Quote ACXIOM : " + cust39);
			}
			String BWAutoAcx = cust39;

			// Renter query started, completed

			String NewQuery5 = CommonCond + RenterLobCond + TimeCond + StateCond + EmailElimination;
			System.out.println(NewQuery5);
			String cust5 = null;
			ResultSet myrs5 = myst.executeQuery(NewQuery5);
			while (myrs5.next()) {
				cust5 = myrs5.getString(1);
				System.out.println("Renter Quote Started : " + cust5);
			}
			String RenterStarted = cust5;

			String NewQuery6 = CommonCond + RenterLobCond + TimeCond + StateCond + EmailElimination + HomeCompletedCond;
			System.out.println(NewQuery6);
			String cust6 = null;
			ResultSet myrs6 = myst.executeQuery(NewQuery6);
			while (myrs6.next()) {
				cust6 = myrs6.getString(1);
				System.out.println("Renter Quote Completed : " + cust6);
			}
			String RenterCompleted = cust6;

			String NewQuery17 = CommonBindCond + TimeCondBind + OnlineBindCondRenter + StateCondBind + EmailElimination
					+ BindLOBRenter;
			System.out.println(NewQuery17);
			String cust17 = null;
			ResultSet myrs17 = myst.executeQuery(NewQuery17);
			while (myrs17.next()) {
				cust17 = myrs17.getString(1);
				System.out.println("Renter Online Bind : " + cust17);
			}

			String RenterOnlineBind = cust17;

			String NewQuery20 = CommonBindOffCond + TimeCondOffBind + OfflineBindCond + OffRenter + StateCondBind
					+ EmailElimination;
			System.out.println(NewQuery20);
			String cust20 = null;
			ResultSet myrs20 = myst.executeQuery(NewQuery20);
			while (myrs20.next()) {
				cust20 = myrs20.getString(1);
				System.out.println("Renter Offline Bind : " + cust20);
			}

			String RenterOfflineBind = cust20;

			String NewQuery23 = CommonCond + RenterLobCond + TimeCond + StateCond + EmailElimination + KOHome;
			System.out.println(NewQuery23);
			String cust23 = null;
			ResultSet myrs23 = myst.executeQuery(NewQuery23);
			while (myrs23.next()) {
				cust23 = myrs23.getString(1);
				System.out.println("Renter Quote KO : " + cust23);
			}
			String RenterKO = cust23;

			String NewQuery37 = CommonCond + RenterLobCond + TimeCond + StateCond + EmailElimination + HomeVerify;
			System.out.println(NewQuery37);
			String cust37 = null;
			ResultSet myrs37 = myst.executeQuery(NewQuery37);
			while (myrs37.next()) {
				cust37 = myrs37.getString(1);
				System.out.println("Renter Quote Verified : " + cust37);
			}
			String RenterVerified = cust37;

			String NewQuery40 = CommonCond + RenterLobCond + TimeCond + StateCond + EmailElimination + CommAcx;
			System.out.println(NewQuery40);
			String cust40 = null;
			ResultSet myrs40 = myst.executeQuery(NewQuery40);
			while (myrs40.next()) {
				cust40 = myrs40.getString(1);
				System.out.println("Renter Quote Acxiom : " + cust40);
			}
			String RenterAcx = cust40;

			// Home query started, completed

			String NewQuery7 = CommonCond + HomeLobCond + TimeCond + LegacyHome + EmailElimination;
			System.out.println(NewQuery7);
			String cust7 = null;
			ResultSet myrs7 = myst.executeQuery(NewQuery7);
			while (myrs7.next()) {
				cust7 = myrs7.getString(1);
				System.out.println("Home Quote Started : " + cust7);
			}
			String HomeStarted = cust7;

			String NewQuery8 = CommonCond + HomeLobCond + TimeCond + LegacyHome + EmailElimination + HomeCompletedCond;
			System.out.println(NewQuery8);
			String cust8 = null;
			ResultSet myrs8 = myst.executeQuery(NewQuery8);
			while (myrs8.next()) {
				cust8 = myrs8.getString(1);
				System.out.println("Home Quote Completed : " + cust8);
			}
			String HomeCompleted = cust8;

			String NewQuery24 = CommonCond + HomeLobCond + TimeCond + LegacyHome + EmailElimination + KOHome;
			System.out.println(NewQuery24);
			String cust24 = null;
			ResultSet myrs24 = myst.executeQuery(NewQuery24);
			while (myrs24.next()) {
				cust24 = myrs24.getString(1);
				System.out.println("Home Quote KO : " + cust24);
			}
			String HomeKO = cust24;

			String NewQuery41 = CommonCond + HomeLobCond + TimeCond + LegacyHome + EmailElimination + CommAcx;
			System.out.println(NewQuery41);
			String cust41 = null;
			ResultSet myrs41 = myst.executeQuery(NewQuery41);
			while (myrs41.next()) {
				cust41 = myrs41.getString(1);
				System.out.println("Home Quote Acxiom : " + cust41);
			}
			String HomeAcx = cust41;

			// Condo query started, completed

			String NewQuery9 = CommonCond + CondoLobCond + TimeCond + StateCond + EmailElimination;
			System.out.println(NewQuery9);
			String cust9 = null;
			ResultSet myrs9 = myst.executeQuery(NewQuery9);
			while (myrs9.next()) {
				cust9 = myrs9.getString(1);
				System.out.println("Condo Quote Started : " + cust9);
			}
			String CondoStarted = cust9;

			String NewQuery10 = CommonCond + CondoLobCond + TimeCond + StateCond + EmailElimination + HomeCompletedCond;
			System.out.println(NewQuery10);
			String cust10 = null;
			ResultSet myrs10 = myst.executeQuery(NewQuery10);
			while (myrs10.next()) {
				cust10 = myrs10.getString(1);
				System.out.println("Condo Quote Completed : " + cust10);
			}
			String CondoCompleted = cust10;

			String NewQuery25 = CommonCond + CondoLobCond + TimeCond + StateCond + EmailElimination + KOHome;
			System.out.println(NewQuery25);
			String cust25 = null;
			ResultSet myrs25 = myst.executeQuery(NewQuery25);
			while (myrs25.next()) {
				cust25 = myrs25.getString(1);
				System.out.println("Condo Quote KO : " + cust25);
			}
			String CondoKO = cust25;

			String NewQuery42 = CommonCond + CondoLobCond + TimeCond + StateCond + EmailElimination + CommAcx;
			System.out.println(NewQuery42);
			String cust42 = null;
			ResultSet myrs42 = myst.executeQuery(NewQuery42);
			while (myrs42.next()) {
				cust42 = myrs42.getString(1);
				System.out.println("Condo Quote Acxiom : " + cust42);
			}
			String CondoAcx = cust42;

			// Business query started,completed

			String NewQuery11 = CommonCond + BusinessLobCond + TimeCond + StateCond + EmailElimination;
			System.out.println(NewQuery11);
			String cust11 = null;
			ResultSet myrs11 = myst.executeQuery(NewQuery11);
			while (myrs11.next()) {
				cust11 = myrs11.getString(1);
				System.out.println("Business Quote Started : " + cust11);
			}
			String BusinessStarted = cust11;

			String NewQuery12 = CommonCond + BusinessLobCond + TimeCond + StateCond + EmailElimination
					+ BusinessCompletedCond;
			System.out.println(NewQuery12);
			String cust12 = null;
			ResultSet myrs12 = myst.executeQuery(NewQuery12);
			while (myrs12.next()) {
				cust12 = myrs12.getString(1);
				System.out.println("Business Quote Completed : " + cust12);
			}
			String BusinessCompleted = cust12;

			String NewQuery421 = CommonCond + BusinessLobCond + TimeCond + StateCond + EmailElimination + CommAcx;
			System.out.println(NewQuery421);
			String cust421 = null;
			ResultSet myrs421 = myst.executeQuery(NewQuery421);
			while (myrs421.next()) {
				cust421 = myrs421.getString(1);
				System.out.println("Business Quote Acxiom : " + cust421);
			}
			String BusinessAcx = cust421;

			// Life query started,completed

			String NewQuery13 = CommonCondLife + LifeLobCond + TimeCond + StateCond + EmailElimination;
			System.out.println(NewQuery13);
			String cust13 = null;
			ResultSet myrs13 = myst.executeQuery(NewQuery13);
			while (myrs13.next()) {
				cust13 = myrs13.getString(1);
				System.out.println("Life Quote Started : " + cust13);
			}
			String LifeStarted = cust13;

			String NewQuery14 = CommonCondLife + LifeLobCond + TimeCond + StateCond + EmailElimination
					+ LifeCompleteCond;
			System.out.println(NewQuery14);
			String cust14 = null;
			ResultSet myrs14 = myst.executeQuery(NewQuery14);
			while (myrs14.next()) {
				cust14 = myrs14.getString(1);
				System.out.println("Life Quote Completed : " + cust14);
			}
			String LifeCompleted = cust14;

			String NewQuery43 = CommonCondLife + LifeLobCond + TimeCond + StateCond + EmailElimination + CommAcx;
			System.out.println(NewQuery43);
			String cust43 = null;
			ResultSet myrs43 = myst.executeQuery(NewQuery43);
			while (myrs43.next()) {
				cust43 = myrs43.getString(1);
				System.out.println("Life Quote Acxiom : " + cust43);
			}
			String LifeAcx = cust43;

			// HQM query started, completed

			String NewQuery30 = CommonCond + HomeLobCond + TimeCond + HQMStateCond + EmailElimination;
			System.out.println(NewQuery30);
			String cust30 = null;
			ResultSet myrs30 = myst.executeQuery(NewQuery30);
			while (myrs30.next()) {
				cust30 = myrs30.getString(1);
				System.out.println("Home Quote Started : " + cust30);
			}
			String HQMStarted = cust30;

			String NewQuery31 = CommonCond + HomeLobCond + TimeCond + HQMStateCond + EmailElimination
					+ HomeCompletedCond;
			System.out.println(NewQuery31);
			String cust31 = null;
			ResultSet myrs31 = myst.executeQuery(NewQuery31);
			while (myrs31.next()) {
				cust31 = myrs31.getString(1);
				System.out.println("HQM Quote Completed : " + cust31);
			}
			String HQMCompleted = cust31;

			String NewQuery32 = CommonCond + HomeLobCond + TimeCond + HQMStateCond + EmailElimination + KOHome;
			System.out.println(NewQuery32);
			String cust32 = null;
			ResultSet myrs32 = myst.executeQuery(NewQuery32);
			while (myrs32.next()) {
				cust32 = myrs32.getString(1);
				System.out.println("HQM Quote KO : " + cust32);
			}
			String HQMKO = cust32;

			String NewQuery46 = CommonCond + HomeLobCond + TimeCond + HQMStateCond + EmailElimination + CommAcx;
			System.out.println(NewQuery46);
			String cust46 = null;
			ResultSet myrs46 = myst.executeQuery(NewQuery46);
			while (myrs46.next()) {
				cust46 = myrs46.getString(1);
				System.out.println("Home Quote Acxiom : " + cust46);
			}
			String HQMAcx = cust46;

			List<String> formersList = new ArrayList<String>();
			formersList.add("Farmers Auto");
			formersList.add(AutoStarted);
			formersList.add(AutoCompleted);
			formersList.add(AutoIIDKO);
			formersList.add(AutoKO);
			formersList.add(AutoOnlineBind);
			formersList.add(AutoOfflineBind);
			formersList.add(AutoVerified);
			formersList.add(AutoAcx);

			List<String> bristolList = new ArrayList<String>();
			bristolList.add("Bristol West");
			bristolList.add(BWAutoStarted);
			bristolList.add(BWAutoCompleted);
			bristolList.add("NA");
			bristolList.add(BWKO);
			bristolList.add(BWOnlineBind);
			bristolList.add(BWOfflineBind);
			bristolList.add(BWAutoVerified);
			bristolList.add(BWAutoAcx);

			List<String> rentersList = new ArrayList<String>();
			rentersList.add("Renter");
			rentersList.add(RenterStarted);
			rentersList.add(RenterCompleted);
			rentersList.add("NA");
			rentersList.add(RenterKO);
			rentersList.add(RenterOnlineBind);
			rentersList.add(RenterOfflineBind);
			rentersList.add(RenterVerified);
			rentersList.add(RenterAcx);

			System.out.println("Home Quote formersList : " + formersList.toString());

			List<String> homeList = new ArrayList<String>();
			if (Status.equals("(AZ State)")) {

				homeList.add("Home Legacy");
				homeList.add("Moved to HQM");
				homeList.add("Moved to HQM");
				homeList.add("NA");
				homeList.add("Moved to HQM");
				homeList.add("NA");
				homeList.add("NA");
				homeList.add("NA");
				homeList.add("Moved to HQM");

			} else {

				homeList.add("Home");
				homeList.add(HomeStarted);
				homeList.add(HomeCompleted);
				homeList.add("NA");
				homeList.add(HomeKO);
				homeList.add("NA");
				homeList.add("NA");
				homeList.add("NA");
				homeList.add(HomeAcx);

			}
			List<String> condoList = new ArrayList<String>();

			condoList.add("Condo");
			condoList.add(CondoStarted);
			condoList.add(CondoCompleted);
			condoList.add("NA");
			condoList.add(CondoKO);
			condoList.add("NA");
			condoList.add("NA");
			condoList.add("NA");
			condoList.add(CondoAcx);

			List<String> businessList = new ArrayList<String>();

			businessList.add("Business");
			businessList.add(BusinessStarted);
			businessList.add(BusinessCompleted);
			businessList.add("NA");
			businessList.add("NA");
			businessList.add("NA");
			businessList.add("NA");
			businessList.add("NA");
			businessList.add("NA");

			List<String> lifeList = new ArrayList<String>();
			lifeList.add("Life");
			lifeList.add(LifeStarted);
			lifeList.add(LifeCompleted);
			lifeList.add("NA");
			lifeList.add("NA");
			lifeList.add("NA");
			lifeList.add("NA");
			lifeList.add("NA");
			lifeList.add(LifeAcx);

			List<String> hqmList = new ArrayList<String>();
			hqmList.add("HQM");
			hqmList.add(HQMStarted);
			hqmList.add(HQMCompleted);
			hqmList.add("NA");
			hqmList.add(HQMKO);
			hqmList.add("NA");
			hqmList.add("NA");
			hqmList.add("NA");
			hqmList.add(HQMAcx);

//--------------------------------------------------------------------------------
			List<String> stateList = new ArrayList<String>();
			stateList.add("All States");
			stateList.add("PLA States");
			stateList.add("GWPC States");
			stateList.add("EE States");
			//stateList.add("Mississippi(MS)");
			//stateList.add("Arizona(AZ)");
			model.addObject("stateList", stateList);

			model.addObject("formersList", formersList);
			model.addObject("bristolList", bristolList);
			model.addObject("rentersList", rentersList);
			model.addObject("homeList", homeList);
			model.addObject("condoList", condoList);
			model.addObject("businessList", businessList);
			model.addObject("lifeList", lifeList);
			model.addObject("hqmList", hqmList);

			// return model;

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return model;

	}

}
