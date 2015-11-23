package com.sollian.ld.views.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sollian.ld.R;
import com.sollian.ld.views.BaseFragment;

/**
 * Created by sollian on 2015/11/17.
 */
public class ObtainLogoNewFragment extends BaseFragment {

    public interface NewDataChangeListener {
        void newDataChange(String name, String extra, String desc);
    }

    private EditText vName, vExtra, vDesc;

    private NewDataChangeListener newDataChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_obtain_logo_new, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        vName = (EditText) view.findViewById(R.id.name);
        vExtra = (EditText) view.findViewById(R.id.extra);
        vDesc = (EditText) view.findViewById(R.id.desc);
    }

    public void setNewDataChangeListener(NewDataChangeListener listener) {
        newDataChangeListener = listener;
    }

    public void triggerDataChangeListener() {
        if (newDataChangeListener != null) {
            newDataChangeListener.newDataChange(
                    vName.getText().toString().trim(),
                    vExtra.getText().toString().trim(),
                    vDesc.getText().toString().trim());
        }
    }
}
