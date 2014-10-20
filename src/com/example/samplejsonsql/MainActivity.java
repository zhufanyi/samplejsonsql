package com.example.samplejsonsql;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static final String mockJsonString = "{\n" + "		\"product\" : {\n"
			+ "		\"name\":\"test_name\",\n"
			+ "		\"description\":\"sample description\",\n"
			+ "		\"regular_price\":\"0.00\",\n"
			+ "		\"sale_price\":\"0.00\",\n" + "		\"product_photo\":\"0\",\n"
			+ "		\"colors\" : {\n" + "		},\n" + "		\"stores\": \" \"\n"
			+ "		}\n" + "}";

	private SampleSQLHelper dbHelper;

	private ProductFragment fragment;
	private EditText editSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbHelper = new SampleSQLHelper(getBaseContext());

		// reads mock json string then inserts it into database
		OnClickListener insertClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();

				try {
					JSONObject jsonMain = new JSONObject(mockJsonString);
					JSONObject json = jsonMain.getJSONObject("product");
					values.put("name", json.getString("name"));
					values.put("description", json.getString("description"));
					values.put("regular_price", json.getString("regular_price"));
					values.put("sale_price", json.getString("sale_price"));
				} catch (JSONException e) {
					// Json failed for whatever reason
					Log.w("MainActivity", "json failure:" + e);
				}
				long id = db.insert("product", "name", values);
			}
		};
		Button createButton = (Button) findViewById(R.id.button_create);
		createButton.setOnClickListener(insertClickListener);

		Button createButtonAlt = (Button) findViewById(R.id.button_create_alt);
		createButtonAlt.setOnClickListener(insertClickListener);

		editSelect = (EditText) findViewById(R.id.edit_select);

		Button selectbutton = (Button) findViewById(R.id.button_select);
		// starts new fragment with current database
		selectbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fragment = new ProductFragment();
				fragment.setSQLDatabase(dbHelper);
				fragment.setSelectTable(editSelect.getText().toString());
				getFragmentManager().beginTransaction()
						.add(R.id.main_layout, fragment).commit();

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			// easy kill for fragment
			if (fragment != null) {
				getFragmentManager().beginTransaction().detach(fragment)
						.commit();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class SampleSQLHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE = "create table product (id integer primary key autoincrement, "
				+ "name text, description text, regular_price text, sale_price text);";

		public SampleSQLHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		public SampleSQLHelper(Context context) {
			super(context, "name", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onCreate(db);

		}

	}
}