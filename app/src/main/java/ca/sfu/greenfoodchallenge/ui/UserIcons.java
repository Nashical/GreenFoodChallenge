package ca.sfu.greenfoodchallenge.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

/*Fragment to show and allow users to pick available icon.
 *Sends the new icon via a listener to the parent fragment.
 */
public class UserIcons extends DialogFragment {

    private View view;
    private List<ImageButton> ICON_VIEWS = new ArrayList<>();
    private int lastViewId = R.id.icon1;
    private final int[] SRC_IDS = {R.mipmap.ic_app_round, R.mipmap.ic_launcher_round, R.drawable.icon7,
            R.drawable.icon1, R.drawable.icon2, R.drawable.icon3,
            R.drawable.icon4, R.drawable.icon5, R.drawable.icon6};
    private int selectedIconId = SRC_IDS[0]; //default icon
    private Button select;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_icons, container, false);

        loadIcons();

        select = (Button) view.findViewById(R.id.selectIcon);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sendIcon();
            }
        });

        return view;
    }

    private void loadIcons() {

        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon1));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon2));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon3));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon4));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon5));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon6));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon7));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon8));
        ICON_VIEWS.add((ImageButton) view.findViewById(R.id.icon9));

        int j = -1;
        for(ImageView i : ICON_VIEWS) {
            j++;
            final int index = j;
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set old view background to transparent
                    view.findViewById(lastViewId).setBackgroundColor(Color.TRANSPARENT);
                    lastViewId = v.getId();
                    selectedIconId = SRC_IDS[index];
                    v.setBackgroundColor(Color.LTGRAY);
                }
            });
        }
    }

    //listener interface
    public interface EditIconListener {
        void onFinishDialog(int iconID);
    }

    public void sendIcon() {
        EditIconListener listener = (EditIconListener) getTargetFragment();
        listener.onFinishDialog(selectedIconId);
        getDialog().dismiss();
    }
}
