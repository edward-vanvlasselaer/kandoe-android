package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.ThemeActivity;
import be.kdg.kandoe.kandoe.dom.Organisation;

import static android.view.LayoutInflater.from;


public class OrganisationAdapter extends BaseAdapter {
    private static OrganisationAdapter instance = null;
    private final Context context;
    private List<Organisation> organisations;
    private Organisation selectedOrganisation;

    public OrganisationAdapter(Context context) {
        this.context = context;
        instance = this;
        this.organisations = new ArrayList<>();
    }

    public static OrganisationAdapter getInstance() {
        return instance;
    }

    public void setOrganisations(List<Organisation> organisations) {
        this.organisations = organisations;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return organisations.size();
    }

    @Override
    public Organisation getItem(int position) {
        return organisations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Organisation organisation = getItem(position);

        final ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.organisation_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(organisation.getOrganisationName());
        //viewHolder.image.setImageBitmap();
        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOrganisation = new Organisation();
                selectedOrganisation.setOrganisationId(organisation.getOrganisationId());
                selectedOrganisation.setOrganisationName(organisation.getOrganisationName());
                selectedOrganisation.setImageUrl(organisation.getImageUrl());
                selectedOrganisation.setCreator(organisation.getCreator());
                selectedOrganisation.setMembers(organisation.getMembers());
                selectedOrganisation.setThemes(organisation.getThemes());

                //Uri uri=Uri.parse("organisationIntent");
                Intent organisationIntent = new Intent(context, ThemeActivity.class);
                //organisationIntent.putExtra("uri",uri.toString());
                organisationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                organisationIntent.putExtra("organisation", selectedOrganisation);
                context.startActivity(organisationIntent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        Button join;
        ImageView image;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.organisation_txt_title);
            join = (Button) view.findViewById(R.id.organisation_btn_join);
            image = (ImageView) view.findViewById(R.id.organisation_imgview);
        }
    }
}
