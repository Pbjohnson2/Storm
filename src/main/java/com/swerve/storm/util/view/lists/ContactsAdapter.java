package com.swerve.storm.util.view.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;
import com.swerve.storm.R;
import com.swerve.storm.model.StormContact;
import com.swerve.storm.util.view.transformer.CircleTransform;

import java.util.List;

import lombok.AllArgsConstructor;

public class ContactsAdapter extends BaseAdapter {
    private List<StormContact> stormContacts;
    private Context context;
    private int lastPosition = -1;

    public ContactsAdapter(final List<StormContact> stormContacts, final Context context) {
        this.stormContacts = stormContacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stormContacts.size();
    }

    @Override
    public StormContact getItem(int position) {
        return stormContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_view_contacts_item, parent, false);
            // Locate the TextViews in listview_item.xml
            holder.profileImage = (ImageButton) convertView.findViewById(R.id.button_profile_image);
            holder.profileName = (Button) convertView.findViewById(R.id.button_profile_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StormContact contact = stormContacts.get(position);
        final ContactClickListener contactClickListener = new ContactClickListener(context);

        setProfileImageTraits(holder.profileImage, contact, contactClickListener);
        setProfileNameTraits(holder.profileName, contact, contactClickListener);
        animateListItem(position, convertView);

        return convertView;
    }

    private void setProfileImageTraits(final ImageButton profileImage,
                                       final StormContact contact,
                                       final ContactClickListener contactClickListener) {
        profileImage.setOnClickListener(contactClickListener);
        Picasso.with(context)
                .load(contact.getAvatarLink())
                .transform(new CircleTransform())
                .into(profileImage);
    }

    private void setProfileNameTraits(final Button profileName,
                                       final StormContact contact,
                                       final ContactClickListener contactClickListener) {
        profileName.setOnClickListener(contactClickListener);
        profileName.setText(contact.getFullName());
    }

    private void animateListItem(final int position, final View view){
        final Animation animation = AnimationUtils.loadAnimation(
                context,
                (position > lastPosition) ? R.anim.list_up_from_bottom: R.anim.list_down_from_top);
        view.startAnimation(animation);
        lastPosition = position;
    }

    private class ViewHolder {
        ImageButton profileImage;
        Button profileName;
    }

    @AllArgsConstructor(suppressConstructorProperties = true)
    private static class ContactClickListener implements View.OnClickListener{
        private final Context context;

        @Override
        public void onClick(final View v) {
            //Start original activity here
        }
    }
}
