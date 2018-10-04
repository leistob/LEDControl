package tobi.com.ledcontrol.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;


import tobi.com.ledcontrol.R;

public class Mode1Fragment extends Fragment {

    private SeekBar mSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mode1, container, false);

        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar_speed);

        return view;
    }

    public int getSeekBarProgress() {
        return mSeekBar.getProgress();
    }
}
