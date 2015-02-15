package ca.uwaterloo.deltahacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


public class CategoryViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<String> categories = Arrays.asList("General", "Community", "Education", "Sports");

    public CategoryViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new Adapter(categories);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private List<String> categories;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
          View mView;
            public ViewHolder(View v) {
                super(v);
                mView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public Adapter(List<String> categories) {
            this.categories = categories;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_category, parent, false);
            // set the view's size, margins, paddings and layout parameters

            Adapter.ViewHolder vh = new Adapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            TextView category_name = (TextView) holder.mView.findViewById(R.id.category_name);
            ImageView img = (ImageView) holder.mView.findViewById(R.id.image);

            String category = categories.get(position);

            category_name.setText(category);

            switch (category) {
                case "General":
                    img.setImageResource(R.drawable.general);
                    break;

                case "Community":
                    img.setImageResource(R.drawable.community);
                    break;

                case "Education":
                    img.setImageResource(R.drawable.education);
                    break;

                case "Sports":
                    img.setImageResource(R.drawable.sports);
                    break;
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return categories.size();
        }
    }


}
