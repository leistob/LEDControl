package tobi.com.ledcontrol.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

import tobi.com.ledcontrol.R;

public class Mode2Fragment extends Fragment {

    private ColorPickerView mPickerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mode2, container, false);

        mPickerView = (ColorPickerView) view.findViewById(R.id.color_picker_view);

        mPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                System.out.println("test");
            }
        });


        return view;
    }

    public int getColorHex() {
        return mPickerView.getSelectedColor();
    }
}
