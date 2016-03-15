package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.dom.Organisation;

import static android.view.LayoutInflater.from;

/**
 * Created by claudiu on 15/03/16.
 */
public class OrganisationAdapter extends BaseAdapter{
    private final Context context;
    private List<Organisation> organisations;

    private static OrganisationAdapter instance=null;

    public static OrganisationAdapter getInstance(){return instance;}

    public OrganisationAdapter(Context context){
        this.context=context;
        instance=this;
        this.organisations=new ArrayList<>();
    }

    public void setOrganisations(List<Organisation> organisations){
        this.organisations=organisations;
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
        final Organisation organisation=getItem(position);

        final ViewHolder viewHolder;

        if(convertView!=null){
            viewHolder=(ViewHolder) convertView.getTag();
        }else {
            convertView = from(context).inflate(R.layout.organisation_item, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(organisation.getOrganisationName());
        //viewHolder.image.setImageBitmap();
        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView title;
        Button join;
        ImageView image;

        public ViewHolder(View view){
            title=(TextView) view.findViewById(R.id.organisation_txt_title);
            join=(Button) view.findViewById(R.id.organisation_btn_join);
            image=(ImageView) view.findViewById(R.id.organisation_imgview);
        }
    }
}
