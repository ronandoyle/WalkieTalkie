package doyle.ronan.walkietalkie;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import doyle.ronan.walkietalkie.settings.Preferences;


public class MainActivity extends Activity {

    public static SharedPreferences iSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        iSettings = PreferenceManager.getDefaultSharedPreferences(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *  When the settings option is selected from the options menu, the preferences screen will be opened up.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                goToPrefs();
        }
        return true;
    }

    /**
     *  Creates the preferences screen, from which the user can enter the IP address/ports of the Robot
     */
    private void goToPrefs() {
        startActivity(new Intent(MainActivity.this, Preferences.class));
        finish();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private SendMic mSendMic;
        private Button mTalkButton;
        private Button mStopButton;
        private Button mListenButton;
        private Thread mStreamOutAudio;
        private Thread mStreamInAudio;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            setupButton();
        }

        @Override
        public void onPause() {
            super.onPause();
            mStreamOutAudio = null;
        }

        private void setupButton() {
            if (getView() == null) {
                return;
            }
            mTalkButton = (Button) getView().findViewById(R.id.talk_button);
            mStopButton = (Button) getView().findViewById(R.id.stop_button);
            mListenButton = (Button) getView().findViewById(R.id.listen_button);

            mTalkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStreamOutAudio == null) {
                        mStreamOutAudio = new Thread(new Runnable(){

                            @Override
                            public void run() {
                                mSendMic = new SendMic();
                                mSendMic.start();
                            }
                        });
                    }


                    if (!mStreamOutAudio.isAlive()) {
                        mStreamOutAudio.start();
                    }
                }
            });

            mStopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSendMic.stop();
                    mStreamOutAudio = null;
                }
            });

            mListenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mStreamInAudio == null) {
                        mStreamInAudio = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Listen listen = new Listen();
                                listen.playStream();
                            }
                        });
                    }
                }
            });
        }
    }
}
