package com.example.samplejsonsql;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ProductFragment extends Fragment {

	private SQLiteOpenHelper dbHelper;

	public void setSQLDatabase(SQLiteOpenHelper helper) {
		dbHelper = helper;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View fragmentView = inflater.inflate(R.layout.product_view, container,
				false);

		EditText editName = (EditText) fragmentView
				.findViewById(R.id.product_name);
		editName.setText("TEST");
		EditText editDescription = (EditText) fragmentView
				.findViewById(R.id.product_description);
		EditText editPrice = (EditText) fragmentView
				.findViewById(R.id.product_price);
		EditText editSale = (EditText) fragmentView
				.findViewById(R.id.product_sale_price);

		if (dbHelper != null) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				Cursor c = db.query(false, "product", null, null, null, null,
						null, null, null);

				c.moveToFirst();
				int indexName = c.getColumnIndex("name");
				int indexDescription = c.getColumnIndex("description");
				int indexPrice = c.getColumnIndex("regular_price");
				int indexSalePrice = c.getColumnIndex("sale_price");

				editName.setText(c.getString(indexName));
				editDescription.setText(c.getString(indexDescription));
				editPrice.setText(c.getString(indexPrice));
				editSale.setText(c.getString(indexSalePrice));

			} catch (Exception e) {
				Log.w("ProductFragment", "error : " + e);
			}

		}
		return fragmentView;
	}

}
