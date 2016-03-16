package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.MainActivity;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.fragment.ThemeCardFragment;

import static android.view.LayoutInflater.from;

/**
 * Created by claudiu on 15/03/16.
 */
public class ThemeAdapter extends BaseAdapter {
    private final Context context;
    private List<Theme> themes;

    private static ThemeAdapter instance=null;

    public static ThemeAdapter getInstance(){return instance;}

    public ThemeAdapter(Context context){
        this.context=context;
        instance=this;
        this.themes=new ArrayList<>();
    }

    public void setThemes(List<Theme> themes){
        this.themes=themes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Theme getItem(int position) {
        return themes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Theme theme = getItem(position);

        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.theme_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(theme.getName());
        viewHolder.description.setText(theme.getDescription());
        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle=new Bundle();
                bundle.putInt("themeId",theme.getThemeId());
                ThemeCardFragment themeCardFragment=new ThemeCardFragment();
                themeCardFragment.setArguments(bundle);*/
                ThemeCardFragment.setCurrentTheme(theme);

                Intent intent=new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView description;
        Button join;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.theme_txt_title);
            description = (TextView) view.findViewById(R.id.theme_txt_desc);
            join = (Button) view.findViewById(R.id.theme_btn_join);
        }
    }
}
