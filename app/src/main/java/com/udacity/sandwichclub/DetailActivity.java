package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv)
    ImageView mSandwichImageView;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.also_known_layout)
    LinearLayout mAlsoKnownLayout;
    @BindView(R.id.also_known_tv)
    TextView mAlsoKnownTextView;

    @BindView(R.id.origin_layout)
    LinearLayout mOriginLayout;
    @BindView(R.id.origin_tv)
    TextView mOriginTextView;

    @BindView(R.id.ingredients_layout)
    LinearLayout mIngredientsLayout;
    @BindView(R.id.ingredients_tv)
    TextView mIngredientsTextView;

    @BindView(R.id.description_layout)
    LinearLayout mDescriptionLayout;
    @BindView(R.id.description_tv)
    TextView mDescriptionTextView;

    @BindView(R.id.sandwich_view)
    ScrollView mSandwichView;
    @BindView(R.id.error_message)
    TextView mErrorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        boolean isValidData;

        isValidData = setImage(sandwich.getImage());
        isValidData |= setAlsoKnownAs(sandwich.getAlsoKnownAs());
        isValidData |= setPlaceOfOrigin(sandwich.getPlaceOfOrigin());
        isValidData |= setDescription(sandwich.getDescription());
        isValidData |= setIngredients(sandwich.getIngredients());

        if (isValidData) {
            mSandwichView.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setVisibility(View.GONE);
        } else {
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mSandwichView.setVisibility(View.GONE);
        }
    }

    private boolean setImage(String imageUrl) {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(imageUrl)
                .into(mSandwichImageView, new ImageLoadedCallback(mLoadingIndicator) {
                    @Override
                    public void onSuccess() {
                        if (mLoadingIndicator != null) {
                            mLoadingIndicator.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        return mLoadingIndicator.getVisibility() == View.INVISIBLE;
    }

    private class ImageLoadedCallback implements Callback {

        ProgressBar mLoadingIndicator;

        ImageLoadedCallback(ProgressBar loadingIndicator) {
            mLoadingIndicator = loadingIndicator;
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onError() {

        }
    }

    private boolean setAlsoKnownAs(List<String> alsoKnownAsList) {
        if (alsoKnownAsList.isEmpty()) {
            mAlsoKnownLayout.setVisibility(View.GONE);
            return false;
        }

        mAlsoKnownLayout.setVisibility(View.VISIBLE);
        mAlsoKnownTextView.setText(joinString(alsoKnownAsList));
        return true;
    }

    private boolean setPlaceOfOrigin(String placeOfOrigin) {
        if (TextUtils.isEmpty(placeOfOrigin)) {
            mOriginLayout.setVisibility(View.GONE);
            return false;
        }

        mOriginLayout.setVisibility(View.VISIBLE);
        mOriginTextView.setText(placeOfOrigin);
        return true;
    }

    private boolean setIngredients(List<String> ingredients) {
        if (ingredients.isEmpty()) {
            mIngredientsLayout.setVisibility(View.GONE);
            return false;
        }

        mIngredientsLayout.setVisibility(View.VISIBLE);
        mIngredientsTextView.setText(joinString(ingredients));
        return true;
    }

    private boolean setDescription(String description) {
        if (TextUtils.isEmpty(description)) {
            mDescriptionLayout.setVisibility(View.GONE);
            return false;
        }

        mDescriptionLayout.setVisibility(View.VISIBLE);
        mDescriptionTextView.setText(description);
        return true;
    }

    private String joinString(List<String> stringList) {
        String joinedString = "";
        for (String string : stringList) {
            joinedString = joinedString.concat(string + ", ");
        }
        joinedString = joinedString.substring(0, joinedString.length() - 2);
        return joinedString;
    }
}
