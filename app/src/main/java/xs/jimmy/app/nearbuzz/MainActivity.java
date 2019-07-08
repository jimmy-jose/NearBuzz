package xs.jimmy.app.nearbuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xs.jimmy.app.nearbuzz.models.User;
import xs.jimmy.app.nearbuzz.network.UserInterface;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private TextView notificationData;
    private TextView idTextView;
    private TextView userId;
    private TextView title;
    private TextView completed;
    private CardView cardView;
    private ProgressBar progressBar;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private UserInterface service = retrofit.create(UserInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationData = findViewById(R.id.notification_update);
        cardView = findViewById(R.id.card);
        userId = findViewById(R.id.user_id_val);
        idTextView = findViewById(R.id.id_val);
        title = findViewById(R.id.title_val);
        completed = findViewById(R.id.completed_val);
        progressBar = findViewById(R.id.progress);
    }
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notificationData.setText(getString(R.string.text,intent.getStringExtra("title"),intent.getStringExtra("body")));
            int id = -1;
            try {
                id = Integer.parseInt(intent.getStringExtra("body"));
            } catch (NumberFormatException e){
                Toast.makeText(getApplicationContext(),"Wrong id received!",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onReceive: idTextView is not a number" );
            }

            if (id > 0) {
                progressBar.setVisibility(View.VISIBLE);
                service.getUserData(id).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        User user = response.body();
                        if(user != null) {
                            cardView.setVisibility(View.VISIBLE);
                            userId.setText(String.valueOf(user.getUserId()));
                            idTextView.setText(String.valueOf(user.getId()));
                            title.setText(String.valueOf(user.getTitle()));
                            completed.setText(String.valueOf(user.isCompleted()));
                        }else {
                            Toast.makeText(getApplicationContext(),"Empty User returned",Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        cardView.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"API Call failed!",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    };

}
