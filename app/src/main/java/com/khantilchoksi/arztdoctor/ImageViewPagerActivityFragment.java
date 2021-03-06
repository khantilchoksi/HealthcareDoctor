package com.khantilchoksi.arztdoctor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageViewPagerActivityFragment extends Fragment {

    private ImagePagerAdapter imagePagerAdapter;
    private View progressView;
    private ViewPager viewPager;

    private View mRootView;

    public ImageViewPagerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_image_view_pager, container, false);

        progressView = mRootView.findViewById(R.id.imagePagerProgress);
        viewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);

        Intent receiveIntent = getActivity().getIntent();

        if(receiveIntent != null){
            Bundle bundle = receiveIntent.getExtras();
            String[] paths = bundle.getStringArray("imagePaths");
            int size = bundle.getInt("size");
            int currentPosition = bundle.getInt("currentPosition");


            Log.d("Log","Received Intent & issue image adapter: \n Paths: "+paths);
            imagePagerAdapter = new ImagePagerAdapter(getContext(),paths,size,this);

            viewPager.setAdapter(imagePagerAdapter);
            viewPager.setCurrentItem(currentPosition);

        }
        return mRootView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            viewPager.setVisibility(show ? View.GONE : View.VISIBLE);
            viewPager.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewPager.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            viewPager.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
