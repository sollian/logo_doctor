package com.sollian.ld.views.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sollian.ld.R;
import com.sollian.ld.views.BaseFragment;
import com.sollian.ld.views.activity.LookAroundActivity;

/**
 * Created by sollian on 2015/11/17.
 */
public class ObtainLogoSelectFragment extends BaseFragment {
    public interface SelectDataChangeListener {
        void selectDataChange(String id);
    }

    public static final int REQUEST_CODE_PICK_LOGO = 0x1113;
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    private Button vSelect;
    private EditText vName;

    private String id;
    private SelectDataChangeListener selectDataChangeListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_obtain_logo_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        vSelect = (Button) view.findViewById(R.id.select_name);
        vSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LookAroundActivity.class);
                intent.putExtra(LookAroundActivity.KEY_PICK_MODE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_LOGO);
            }
        });
        vName = (EditText) view.findViewById(R.id.name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && (requestCode & 0xffff) == REQUEST_CODE_PICK_LOGO) {
            String name = data.getStringExtra(KEY_NAME);
            id = data.getStringExtra(KEY_ID);
            vName.setText(name);
            if (selectDataChangeListener != null) {
                selectDataChangeListener.selectDataChange(id);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setSelectDataChangeListener(SelectDataChangeListener listener) {
        selectDataChangeListener = listener;
    }

}
