package ca.sfu.greenfoodchallenge.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutFragment extends Fragment{
    final static String TEXT_MESSAGE = "If you need more information, please visit here.";
    final static String LINKED_TEXT = "here";
    final static String WEBPAGE_URL = "http://www.eatlowcarbon.org/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView tvLinkify = (TextView) view.findViewById(R.id.tvLinkify);
        String text = TEXT_MESSAGE;
        tvLinkify.setText(text);

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Pattern pattern1 = Pattern.compile(LINKED_TEXT);
        Linkify.addLinks(tvLinkify, pattern1, WEBPAGE_URL,null,mTransform);

        return view;
    }

}
