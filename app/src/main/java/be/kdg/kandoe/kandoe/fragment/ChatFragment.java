package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.adapter.ChatAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Chat;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;


public class ChatFragment extends Fragment {
    private ChatAdapter chatAdapter;
    private TextView chatMessage;
    private Button sendBtn;
    private int circleId = 0;
    private Runnable backgroundService;
    private Handler handler;
    private Fragment fragment;
    private int interval = 2000;

    public ChatFragment() {
        fragment = this;
        createBackgroundService();
    }

    private void createBackgroundService() {
        handler = new Handler();
        backgroundService = new Runnable() {
            @Override
            public void run() {
                getChatMessages();
                handler.postDelayed(backgroundService, interval);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (fragment.getContext() != null) {
            if (menuVisible) {
                handler.postDelayed(backgroundService, interval);
            } else {
                handler.removeCallbacks(backgroundService);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.chat_list);
        chatMessage = (TextView) rootView.findViewById(R.id.chat_txt_message);
        sendBtn = (Button) rootView.findViewById(R.id.chat_btn_send);


        chatAdapter = new ChatAdapter(rootView.getContext());
        listView.setAdapter(chatAdapter);

        try {
            circleId = ThemeCardActivity.getCurrentTheme().getCircle().getCircleId();
        } catch (Exception e) {
            e.printStackTrace();
        }


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat();
                chat.setCircle(circleId);
                chat.setMessage(String.valueOf(chatMessage.getText()));

                if (!chat.getMessage().equals("")) {
                    Call<Object> call = KandoeApplication.getCircleApi().addChatMessage(circleId, chat);
                    call.enqueue(new AbstractExceptionCallback<Object>() {
                        @Override
                        public void onResponse(Response<Object> response, Retrofit retrofit) {
                            chatMessage.setText("");
                            getChatMessages();
                        }
                    });
                }
            }
        });
        listView.setSelection(getChatAdapter().getCount() - 1);
        getChatMessages();

        return rootView;
    }


    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    private void getChatMessages() {
        Call<List<Chat>> call = KandoeApplication.getCircleApi().getChatMessages(circleId);
        call.enqueue(new AbstractExceptionCallback<List<Chat>>() {
            @Override
            public void onResponse(Response<List<Chat>> response, Retrofit retrofit) {
                List<Chat> newList = new ArrayList<Chat>();
                if (response.body() != null) {
                    for (Chat chat : response.body()) {
                        newList.add(chat);
                    }
                    getChatAdapter().setChatMessages(newList);

                }
            }
        });
    }
}

