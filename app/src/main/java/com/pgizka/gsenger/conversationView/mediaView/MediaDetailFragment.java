package com.pgizka.gsenger.conversationView.mediaView;


import android.app.job.JobScheduler;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.util.CustomViewPager;
import com.pgizka.gsenger.util.DepthPageTransformer;
import com.pgizka.gsenger.util.ImageUtil;
import com.pgizka.gsenger.util.TouchImageView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class MediaDetailFragment extends Fragment implements MediaDetailContract.View{

    private static final String CURRENT_POSITION_STATE = "currentPositionState";

    @Inject
    MediaDetailContract.Presenter presenter;

    private CustomViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private List<Message> messages;

    private ActionBar actionBar;

    public MediaDetailFragment() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_detail, container, false);

        sectionsPagerAdapter = new SectionsPagerAdapter();
        viewPager = (CustomViewPager) view.findViewById(R.id.media_detail_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer(DepthPageTransformer.TransformType.DEPTH));

        PageChangeListener pageChangeListener = new PageChangeListener();
        viewPager.addOnPageChangeListener(pageChangeListener);

        int messageId = getArguments().getInt(MediaDetailActivity.MESSAGE_ID_ARGUMENT);
        int savedPosition = -1;
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt(CURRENT_POSITION_STATE);
        }
        presenter.onCreate(this, messageId, savedPosition);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        presenter.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION_STATE, viewPager.getCurrentItem());
    }

    public class SectionsPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_media_detail_page, container, false);

            TouchImageView imageView = (TouchImageView) view.findViewById(R.id.media_detail_page_image_view);
            ImageView videoPlayImage = (ImageView) view.findViewById(R.id.media_detail_page_video_image_view);
            TextView infoTextView = (TextView) view.findViewById(R.id.media_detail_page_info_text);
            TextView descriptionTextView = (TextView) view.findViewById(R.id.media_detail_page_description_text);

            Message message = messages.get(position);
            MediaMessage mediaMessage = message.getMediaMessage();

            int type = mediaMessage.getMediaType();
            boolean isImage = type == MediaMessage.Type.PHOTO.code;
            imageView.setZoomable(isImage);
            videoPlayImage.setVisibility(isImage ? View.GONE : View.VISIBLE);

            View.OnClickListener onClickListener = v ->{ if (!isImage) launchPlayVideoActivity(mediaMessage);};
            imageView.setOnClickListener(onClickListener);
            videoPlayImage.setOnClickListener(onClickListener);

            Glide.with(MediaDetailFragment.this)
                    .load(mediaMessage.getPath())
                    .sizeMultiplier(ImageUtil.getImageSizeMultiplier(mediaMessage.getPath()))
                    .into(imageView);

            imageView.setScaleChangeListener(isZoomed -> {
                viewPager.setSwipingEnabled(!isZoomed);

                int visibility = isZoomed ? View.GONE : View.VISIBLE;
                infoTextView.setVisibility(visibility);
                descriptionTextView.setVisibility(visibility);

                if (isZoomed) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                }
            });

            infoTextView.setText(getInfoTextFor(message));
            descriptionTextView.setText(mediaMessage.getDescription());

            container.addView(view);

            return view;
        }

        private void launchPlayVideoActivity(MediaMessage mediaMessage) {
            Intent intent = new Intent(getActivity(), MediaDetailPlayVideoActivity.class);
            intent.putExtra(MediaDetailPlayVideoActivity.VIDEO_PATH_ARGUMENT, mediaMessage.getPath());
            startActivity(intent);
        }

        private String getInfoTextFor(Message message) {
            String infoText = "";

            boolean isFromOwner = message.getSender().getId() == 0;
            if (isFromOwner) {
                infoText += "You, ";
            } else {
                infoText += message.getSender().getUserName() + ", ";
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, HH:mm"); //29 January, 14:51
            infoText += simpleDateFormat.format(new Date(message.getSendDate()));

            return infoText;
        }

        @Override
        public int getCount() {
            return messages == null ? 0 : messages.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            position++;
            actionBar.setTitle(position + " of " + messages.size());
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    }

    @Override
    public void displayMessages(List<Message> messages) {
        this.messages = messages;
        sectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setInitialPosition(int position) {
        viewPager.setCurrentItem(position, false);
        actionBar.setTitle((position + 1) + " of " + messages.size());
    }
}
