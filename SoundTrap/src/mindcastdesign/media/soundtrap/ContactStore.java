package mindcastdesign.media.soundtrap;




import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactStore extends Activity {
	private Intent resultIntent;
	private String telefon;
	
	private ListView mContactList;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_store_view);
		
		Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		startManagingCursor(c);
		
		 String[] columns = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
         //  View that should display column
         int to[] = new int[] {android.R.id.text1};
         SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,c ,columns ,to);
         mContactList = (ListView) findViewById(R.id.listView1_frd_add_new);
        
         populateContactList();         
         mContactList.setClickable(true);
         mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

       	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
       		 
       		  
       	    Object o = mContactList.getItemAtPosition(position);
       	    
       	    Cursor mycursor = (Cursor) mContactList.getItemAtPosition(position);;
       	    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
       	   ContentResolver cr = getContentResolver();    
   	    
			Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
   	    		"DISPLAY_NAME = '" + mycursor.getString(1) + "'", null, null);    
   	    if (cursor.moveToFirst()) {        
   	    	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
   	    	//        
   	    	//  Get all phone numbers.        
   	    	//        
   	    	phones = cr.query(Phone.CONTENT_URI, null,            
   	    			Phone.CONTACT_ID + " = " + contactId, null, null);        
   	    	while (phones.moveToNext()) {            
   	    		String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));            
   	    		int type = phones.getInt(phones.getColumnIndex(Phone.TYPE)); 
   	    		//Toast.makeText(getApplicationContext(),number,
   	    		//		Toast.LENGTH_SHORT).show();
   	    		
   	    		//EditText name_mEdit   = (EditText)findViewById(R.id.txt_frd_add_new_name);
   	    		//name_mEdit.setText(mycursor.getString(1));		
   	    		//EditText phone_mEdit   = (EditText)findViewById(R.id.txt_frd_add_new_phone);
   	    		//phone_mEdit.setText(number);
   	    		telefon = number;
   	    		switch (type) {                
   	    			case Phone.TYPE_HOME:                    // do something with the Home number here...                    
   	    				break;                
   	    			case Phone.TYPE_MOBILE:                    // do something with the Mobile number here...                    
   	    				break;                
   	    			case Phone.TYPE_WORK:                    // do something with the Work number here...                    break;                }        }        phones.close();        	    
   	    		}
   	    	}
   	    }
       	    resultIntent = new Intent();
       	    resultIntent.putExtra("Telefon", telefon);
       	    setResult(Activity.RESULT_OK, resultIntent);
       	    finish();
       	  }
         });
         
	}
	public ContactStore() {
		// TODO Auto-generated constructor stub
	}

	
	   private void populateContactList() {
	        // Build adapter with contact entries
	        Cursor cursor = getContacts();
	        int c = cursor.getCount();
	        String[] fields = new String[] {
	                ContactsContract.Data.DISPLAY_NAME
	        };
	        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_row_view, cursor,
	                fields, new int[] {R.id.txt_firend_contact_name});
	        mContactList.setAdapter(adapter);
	    }

	    private Cursor getContacts()
	    {
	        // Run query
	        Uri uri = ContactsContract.Contacts.CONTENT_URI;
	        String[] projection = new String[] {
	                ContactsContract.Contacts._ID,
	                ContactsContract.Contacts.DISPLAY_NAME
	        };
	        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
	                "1" + "'";
	        String[] selectionArgs = null;
	        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

	        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
	    }
}
