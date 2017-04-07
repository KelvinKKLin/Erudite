package ca.mcmaster.plan6.erudite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terrance on 2017-04-02.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>  {

    public static AdapterListener onClickListener;

    /**
     * interface for button clicks to be read by the content activity
     */
    public interface AdapterListener {
        void viewButtonOnClick(View v, int position);
        void submitButtonOnClick(View v, int position);
    }

    /**
     * Used to hold the different views that will be within the Recycler View
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button viewButton;
        public Button submitButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        /**
         * Constructor for ViewHolder that accepts the entire itrm row and does the view lookups for each subview
         */
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            this.nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            this.submitButton = (Button) itemView.findViewById(R.id.submit_button);
            this.viewButton = (Button) itemView.findViewById(R.id.view_button);

            viewButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onClickListener.viewButtonOnClick(v,getAdapterPosition());
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onClickListener.submitButtonOnClick(v,getAdapterPosition());
                }
            });

        }
    }

    // Store a member variable for the contacts
    private List<Content> mContacts;
    // Store the context for easy access
    private Context mContext;

    /**
     * Pass in the contact array into the constructor
     * @param context
     * @param contacts this is the list of content that will be displayed
     */
    // Pass in the contact array into the constructor
    public ContentAdapter(Context context, List<Content> contacts) {
        mContacts = contacts;
        mContext = context;
    }

    /**
     * Used to pass the onclicklister up to ContentActivity
     * @param contacts list of content that will be displayed
     * @param listener the listener that is used for the button presses in the ContentActivity
     */
    public ContentAdapter(List<Content> contacts, AdapterListener listener) {
        mContacts = contacts;
        onClickListener = listener;
    }

    /**
     * Easy access to the context object in the recyclerview
     */

    private Context getContext() {
        return mContext;
    }

    /**
     * Inflating a layout form XML file
     * @param parent This is the parent view group that holds all of the other views
     * @param viewType
     */
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_content, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * Involves populating data into the item through holder
     * @param position the row where the content is displayed
     * @param viewHolder Viewholder from above that holds all of the different subviews
     */
    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Content contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getName());
        Button vbutton = viewHolder.viewButton;
        vbutton.setText("View");
        Button sbutton = viewHolder.submitButton;
        sbutton.setText("Submit");
    }

       /**
     * Returns the total count of items in the list
     */
    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}
