package amey.clock;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class TimeFragment extends Fragment {

    RadioGroup analogDigitalButton_RadioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.time_fragment, container, false);

        view.findViewById(R.id.clockView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textClock).setVisibility(View.GONE);

        analogDigitalButton_RadioGroup = view.findViewById(R.id.analogDigitalButton_RadioGroup);
        view.findViewById(R.id.analogClock_RadioButton);
        view.findViewById(R.id.digitalClock_RadioButton);

        analogDigitalButton_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                /*
                switch ((RadioButton)view.findViewById( radioGroup.getCheckedRadioButtonId())) {
                    case R.id.analogClock_RadioButton:

                        view.findViewById(R.id.clockView).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.textClock).setVisibility(View.GONE);
                        break;
                    case R.id.textClock:
                        view.findViewById(R.id.textClock).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.clockView).setVisibility(View.GONE);
                        break;
                }   // 'switch (i)' closed.
                */

                if (view.findViewById(R.id.analogClock_RadioButton) == view.findViewById(radioGroup.getCheckedRadioButtonId())) {
                    view.findViewById(R.id.clockView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.textClock).setVisibility(View.GONE);
                } else if (view.findViewById(R.id.digitalClock_RadioButton) == view.findViewById(radioGroup.getCheckedRadioButtonId())) {
                    view.findViewById(R.id.textClock).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.clockView).setVisibility(View.GONE);
                }
            }   // 'onCheckedChanged(RadioGroup radioGroup, int i)' closed.
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
