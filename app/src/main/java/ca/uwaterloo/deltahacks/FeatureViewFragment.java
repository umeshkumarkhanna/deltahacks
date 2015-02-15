package ca.uwaterloo.deltahacks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class FeatureViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SQLiteHelper sqliteHelper;

    List<Listing> listings;

    public FeatureViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feature_view, container, false);

        sqliteHelper = new SQLiteHelper(rootView.getContext());
        sqliteHelper.onUpgrade(sqliteHelper.getWritableDatabase(),1,1);
        listings = sqliteHelper.getAllElements();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new Adapter(listings);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private List<Listing> listings;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
          View mCardView;
            public ViewHolder(View v) {
                super(v);
                mCardView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public Adapter(List<Listing> listings) {
            this.listings = listings;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_listing, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CardView card = (CardView) v.findViewById(R.id.card_view);

            card.setPreventCornerOverlap(false);
            card.setCardElevation(50);

            Adapter.ViewHolder vh = new Adapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            TextView organization_name = (TextView) holder.mCardView.findViewById(R.id.organization_name);
            TextView distance = (TextView) holder.mCardView.findViewById(R.id.distance);
            TextView hours = (TextView) holder.mCardView.findViewById(R.id.hours);
            ImageView img = (ImageView) holder.mCardView.findViewById(R.id.image);

            Listing listing = listings.get(position);

            organization_name.setText(listing.org);
            distance.setText(String.format("%d km", listing.distanceKm));
            hours.setText(listing.startTime.toString() + " - " + listing.endTime.toString());

            switch (listing.cat) {
                case "library":
                    img.setImageResource(R.drawable.library);
                    break;
                case "soup_kitchen":
                    img.setImageResource(R.drawable.soup_kitchen);
                    break;
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return listings.size();
        }
    }

    public class Listing {
        String org;
        String cat;
        String[] tags;
        Time startTime;
        Time endTime;
        int distanceKm;

        public int getStarthour() {
            return startTime.hour;
        }

        public int getStartMinute() {
            return startTime.minute;
        }

        public int getEndhour() {
            return endTime.hour;
        }

        public int getEndminute() {
            return endTime.minute;
        }

        public String getOrg() {
            return org;
        }

        public String getCat() {
            return cat;
        }

        public String[] getTags() {
            return tags;
        }

        public int getDistanceKm() {
            return distanceKm;
        }

        public class Time {
            int hour;
            int minute;

            public Time(int hour, int minute) {
                if( hour > 24 || hour < 1) {
                    throw new IllegalArgumentException("Invalid hour");
                }
                if( minute > 60 || minute < 0) {
                    throw new IllegalArgumentException("Invalid minute");
                }
                this.hour = hour;
                this.minute = minute;
            }


            @Override
            public String toString() {
                return String.format("%d:" + (minute < 10 ? "0" : "") + "%d " + (hour > 12 ? "pm" : "am"),
                        ((hour - 1) % 12) + 1, minute);
            }
        }

        public Listing(String org, String cat, String[] tags, int startHour, int startMinute,
                       int endHour, int endMinute, int distanceKm) {
            this.org = org;
            this.cat = cat;
            this.tags = tags;
            this.startTime = new Time(startHour, startMinute);
            this.endTime = new Time(endHour, endMinute);
            this.distanceKm = distanceKm;
        }
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "ListingDb";
        private static final String TABLE_LISTINGS = "listings";

        private static final String KEY_ORG = "Organization";
        private static final String KEY_CAT = "Category";
        private static final String KEY_TAGS = "Tags";
        private static final String KEY_STARTHOUR = "StartHour";
        private static final String KEY_STARTMINUTE = "StartMinute";
        private static final String KEY_ENDHOUR = "EndHour";
        private static final String KEY_ENDMINUTE = "EndMinute";
        private static final String KEY_DISTANCE = "Distance";

        private static final String CREATE_LISTING_TABLE = "CREATE TABLE " + TABLE_LISTINGS +
                " (Organization TEXT, " +
                "Category TEXT, " +
                "Tags TEXT, " +
                "StartHour TEXT, " +
                "StartMinute TEXT, " +
                "EndHour TEXT, " +
                "EndMinute TEXT, " +
                "Distance TEXT)";

        public SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LISTING_TABLE);
            addElement(new Listing("Hamilton Public Library",
                    "library",
                    new String[]{"community"},
                    9, 0,
                    17, 30,
                    2), db);
            addElement(new Listing("McMaster Soup Kitchen",
                    "soup_kitchen",
                    new String[]{"community"},
                    8, 30,
                    20, 0,
                    1), db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTINGS);

            this.onCreate(db);
        }

        public void addElement(Listing listing, SQLiteDatabase db) {
            ContentValues values = new ContentValues();
            values.put(KEY_ORG, listing.getOrg());
            values.put(KEY_CAT, listing.getCat());
            values.put(KEY_TAGS, listing.getTags()[0]);
            values.put(KEY_STARTHOUR, String.valueOf(listing.getStarthour()));
            values.put(KEY_STARTMINUTE, String.valueOf(listing.getStartMinute()));
            values.put(KEY_ENDHOUR, String.valueOf(listing.getEndhour()));
            values.put(KEY_ENDMINUTE, String.valueOf(listing.getEndminute()));
            values.put(KEY_DISTANCE, String.valueOf(listing.getDistanceKm()));
            db.insert(TABLE_LISTINGS, null, values);
        }

        public List<Listing> getAllElements() {
            List<Listing> listings = new ArrayList<>();
            String query = "SELECT * FROM " + TABLE_LISTINGS;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Listing listing = new Listing(cursor.getString(0), cursor.getString(1),
                            new String[]{cursor.getString(2)},
                            cursor.getInt(3),
                            cursor.getInt(4),
                            cursor.getInt(5),
                            cursor.getInt(6),
                            cursor.getInt(7));

                    listings.add(listing);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return listings;
        }
    }


}
