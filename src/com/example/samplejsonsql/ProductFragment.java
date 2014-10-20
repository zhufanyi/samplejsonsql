package com.example.samplejsonsql;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ProductFragment extends Fragment {

	private static final String KEY_SALE_PRICE = "sale_price";
	private static final String KEY_REGULAR_PRICE = "regular_price";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_NAME = "name";
	private static final String PRODUCT_TABLE = "product";
	private SQLiteOpenHelper dbHelper;

	public void setSQLDatabase(SQLiteOpenHelper helper) {
		dbHelper = helper;
	}

	public void setSelectTable(String table) {
		selectName = table;
	}

	EditText editName;
	EditText editDescription;
	EditText editPrice;
	EditText editSale;
	SQLiteDatabase db;
	String selectName = " ";
	Cursor c;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View fragmentView = inflater.inflate(R.layout.product_view, container,
				false);

		editName = (EditText) fragmentView.findViewById(R.id.product_name);
		editName.setText("TEST");
		editDescription = (EditText) fragmentView
				.findViewById(R.id.product_description);
		editPrice = (EditText) fragmentView.findViewById(R.id.product_price);
		editSale = (EditText) fragmentView
				.findViewById(R.id.product_sale_price);

		Button buttonUpdate = (Button) fragmentView
				.findViewById(R.id.button_update);
		buttonUpdate.setOnClickListener(updateClicker);

		Button buttonDelete = (Button) fragmentView
				.findViewById(R.id.button_delete);
		buttonDelete.setOnClickListener(deleteClicker);

		if (dbHelper != null) {
			db = dbHelper.getReadableDatabase();

			// not ideal but simple

			try {
				c = db.query(false, selectName, null, null, null, null, null,
						null, null);
			} catch (Exception e) {
				Log.e("ProductFragment", "error with selection: " + e);
			}
			try {
				if (c == null) {
					c = db.query(false, PRODUCT_TABLE, null, null, null, null,
							null, null, null);
				}
				c.moveToFirst();
				int indexName = c.getColumnIndex(KEY_NAME);
				int indexDescription = c.getColumnIndex(KEY_DESCRIPTION);
				int indexPrice = c.getColumnIndex(KEY_REGULAR_PRICE);
				int indexSalePrice = c.getColumnIndex(KEY_SALE_PRICE);

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

	private OnClickListener updateClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String name = editName.getText().toString();
			String description = editDescription.getText().toString();
			String price = editPrice.getText().toString();
			String sale = editSale.getText().toString(); // test for null

			// update code
			ContentValues values = new ContentValues();

			values.put(KEY_NAME, name);
			values.put(KEY_DESCRIPTION, description);
			values.put(KEY_REGULAR_PRICE, price);
			values.put(KEY_SALE_PRICE, sale);
			db.update(PRODUCT_TABLE, values, null, null);
		}
	};

	private OnClickListener deleteClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (c == null) {
				Cursor c = db.query(false, PRODUCT_TABLE, null, null, null,
						null, null, null, null);
			}
			c.moveToFirst();
			String selection = KEY_NAME + " LIKE ?";
			String[] selectionArgs = { editName.getText().toString() };

			db.delete(PRODUCT_TABLE, selection, selectionArgs);

		}
	};

}
