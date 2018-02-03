package afei.stdemonow;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import afei.api.LogX;


public class Fragment04 extends android.support.v4.app.Fragment {

    private static final String TAG = "STdownnow:Fragment04";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment04, container, false);
        LogX.d(TAG, "onCreateView()");
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogX.d(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogX.d(TAG, "onCreate()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogX.d(TAG, "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogX.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogX.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogX.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogX.d(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogX.d(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogX.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogX.d(TAG, "onDetach()");
    }

}
