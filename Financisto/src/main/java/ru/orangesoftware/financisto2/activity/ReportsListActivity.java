/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 *     Abdsandryk Souza - implementing 2D chart reports
 ******************************************************************************/
package ru.orangesoftware.financisto2.activity;

import ru.orangesoftware.financisto2.R;
import ru.orangesoftware.financisto2.adapter.ReportListAdapter;
import ru.orangesoftware.financisto2.db.DatabaseAdapter;
import ru.orangesoftware.financisto2.graph.Report2DChart;
import ru.orangesoftware.financisto2.model.Currency;
import ru.orangesoftware.financisto2.report.Report;
import ru.orangesoftware.financisto2.report.ReportType;
import ru.orangesoftware.financisto2.utils.PinProtection;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ReportsListActivity extends ListActivity {
	
	public static final String EXTRA_REPORT_TYPE = "reportType";
	
	public final ReportType[] reports = new ReportType[]{
			ReportType.BY_PERIOD,
			ReportType.BY_CATEGORY,
            ReportType.BY_PAYEE,
			ReportType.BY_LOCATION,
			ReportType.BY_PROJECT,
			ReportType.BY_ACCOUNT_BY_PERIOD, 
			ReportType.BY_CATEGORY_BY_PERIOD,
            ReportType.BY_PAYEE_BY_PERIOD,
			ReportType.BY_LOCATION_BY_PERIOD,
			ReportType.BY_PROJECT_BY_PERIOD
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reports_list);		
		setListAdapter(new ReportListAdapter(this, reports));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		PinProtection.lock(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PinProtection.unlock(this);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (reports[position].isConventionalBarReport()) {
			// Conventional Bars reports
			Intent intent = new Intent(this, ReportActivity.class);
			intent.putExtra(EXTRA_REPORT_TYPE, reports[position].name());
			startActivity(intent);			
		} else {
			// 2D Chart reports
			Intent intent = new Intent(this, Report2DChartActivity.class);
			intent.putExtra(Report2DChart.REPORT_TYPE, position);
			startActivity(intent);
		}
	}

	public static Report createReport(Context context, DatabaseAdapter db, Bundle extras) {
		String reportTypeName = extras.getString(EXTRA_REPORT_TYPE);
		ReportType reportType = ReportType.valueOf(reportTypeName);
        Currency c = db.getHomeCurrency();
		return reportType.createReport(context, c);
	}

}
