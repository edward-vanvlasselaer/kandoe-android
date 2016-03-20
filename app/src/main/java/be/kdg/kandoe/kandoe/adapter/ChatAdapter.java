package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.dom.Chat;

import static android.view.LayoutInflater.from;


public class ChatAdapter extends BaseAdapter {
    private static ChatAdapter instance = null;
    private final Context context;
    //public Card selectedCard;
    private List<Chat> chatMessages;

    public ChatAdapter(Context context) {
        this.context = context;
        instance = this;
        this.chatMessages = new ArrayList<>();
    }

    public static ChatAdapter getInstance() {
        return instance;
    }

    public void setChatMessages(List<Chat> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Chat getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Chat chat = getItem(position);

        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.chatmessage_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.username.setText(chat.getUser().getFirstName());
        viewHolder.message.setText(chat.getMessage());
        notifyDataSetChanged();

        return convertView;
    }

    private class ViewHolder {
        TextView username;
        TextView message;

        public ViewHolder(View view) {
            username = (TextView) view.findViewById(R.id.chat_txt_name);
            message = (TextView) view.findViewById(R.id.chat_txt_msg);
        }
    }
}
